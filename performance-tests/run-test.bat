@echo off
REM Performance Test Runner for Windows
REM Requires: Apache JMeter installed and in PATH

setlocal enabledelayedexpansion

echo.
echo ====================================
echo  Telemed Performance Test Runner
echo ====================================
echo.

REM Check if JMeter is installed
where jmeter >nul 2>nul
if errorlevel 1 (
    echo ERROR: Apache JMeter not found in PATH
    echo.
    echo Please install JMeter from: https://jmeter.apache.org/download_jmeter.html
    echo Add JMeter/bin directory to your PATH environment variable
    exit /b 1
)

echo [INFO] Found JMeter version:
jmeter --version

echo.
echo [INFO] Checking if API is running on localhost:8080...
powershell -NoP -C "try{$r=iwr -Uri 'http://localhost:8080' -UseBasicParsing -TimeoutSec 3; Write-Host '[OK] API is accessible'} catch{Write-Host '[ERROR] Cannot reach API on localhost:8080'; exit 1}"

if errorlevel 1 (
    echo.
    echo ERROR: API is not responding on localhost:8080
    echo Please start the application with: mvnw spring-boot:run
    exit /b 1
)

echo.
echo [INFO] Starting performance test...
echo [INFO] - 50 concurrent users
echo [INFO] - 30 second ramp-up time
echo [INFO] - 10 loops per user
echo [INFO] - Total 500 requests
echo.

REM Create results directory if it doesn't exist
if not exist "results" mkdir results

REM Run JMeter test
jmeter -n -t telemed-performance-test.jmx -l results.jtl -j jmeter.log

if errorlevel 1 (
    echo.
    echo ERROR: Performance test failed
    exit /b 1
)

echo.
echo [INFO] Test completed successfully!
echo [INFO] Results saved to: results.jtl
echo.

REM Extract and display summary statistics
echo ====================================
echo  Test Summary
echo ====================================
echo.

REM Parse JTL file and display basic stats
python -c "
import csv
import statistics

try:
    with open('results.jtl', 'r') as f:
        reader = csv.DictReader(f)
        responses = []
        errors = 0
        success = 0
        
        for row in reader:
            try:
                elapsed = int(row.get('elapsed', 0))
                responses.append(elapsed)
                
                if row.get('success', 'true').lower() == 'true':
                    success += 1
                else:
                    errors += 1
            except:
                pass
        
        total = success + errors
        if responses:
            avg = statistics.mean(responses)
            p95 = sorted(responses)[int(len(responses) * 0.95)] if len(responses) > 1 else responses[0]
            min_r = min(responses)
            max_r = max(responses)
            
            print('Total Requests:', total)
            print('Successful:', success)
            print('Failed:', errors)
            print('Error Rate: {:.2f}%%'.format((errors / total * 100) if total > 0 else 0))
            print()
            print('Response Times:')
            print('  Average: {:.0f} ms'.format(avg))
            print('  95th Percentile: {:.0f} ms'.format(p95))
            print('  Min: {} ms'.format(min_r))
            print('  Max: {} ms'.format(max_r))
            print()
            print('Throughput: {:.2f} req/sec'.format(total / 30) if total > 0 else 0)
except Exception as e:
    print('Note: Install Python to see detailed statistics')
" 2>nul || (
    echo Note: Install Python to see detailed summary statistics
    echo You can still review results.jtl file manually
)

echo.
echo [INFO] Generating HTML report...
if exist "report" rmdir /s /q report
jmeter -g results.jtl -o report 2>nul

if exist "report\index.html" (
    echo [OK] HTML report generated in: report\index.html
    echo.
    echo Opening report in browser...
    start report\index.html
) else (
    echo [Note] HTML report generation requires newer JMeter version
    echo You can manually view results.jtl
)

echo.
echo ====================================
echo  Performance Test Complete!
echo ====================================
echo.
