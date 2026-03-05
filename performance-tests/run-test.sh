#!/bin/bash
# Performance Test Runner for Linux/macOS
# Requires: Apache JMeter installed and in PATH

set -e

echo ""
echo "===================================="
echo "  Telemed Performance Test Runner"
echo "===================================="
echo ""

# Check if JMeter is installed
if ! command -v jmeter &> /dev/null; then
    echo "ERROR: Apache JMeter not found in PATH"
    echo ""
    echo "Install JMeter:"
    echo "  macOS (Homebrew): brew install jmeter"
    echo "  Or download from: https://jmeter.apache.org/download_jmeter.html"
    exit 1
fi

echo "[INFO] Found JMeter version:"
jmeter --version

echo ""
echo "[INFO] Checking if API is running on localhost:8080..."
if ! curl -s -f http://localhost:8080 > /dev/null 2>&1; then
    echo "[ERROR] Cannot reach API on localhost:8080"
    echo ""
    echo "Please start the application with:"
    echo "  ./mvnw spring-boot:run"
    exit 1
fi
echo "[OK] API is accessible"

echo ""
echo "[INFO] Starting performance test..."
echo "[INFO] - 50 concurrent users"
echo "[INFO] - 30 second ramp-up time"
echo "[INFO] - 10 loops per user"
echo "[INFO] - Total 500 requests"
echo ""

# Create results directory if it doesn't exist
mkdir -p results

# Run JMeter test
jmeter -n -t telemed-performance-test.jmx -l results.jtl -j jmeter.log

echo ""
echo "[INFO] Test completed successfully!"
echo "[INFO] Results saved to: results.jtl"
echo ""

# Extract and display summary statistics
echo "===================================="
echo "  Test Summary"
echo "===================================="
echo ""

# Parse JTL file and display basic stats
python3 -c "
import csv
import statistics
import sys

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
            print('Error Rate: {:.2f}%'.format((errors / total * 100) if total > 0 else 0))
            print()
            print('Response Times:')
            print('  Average: {:.0f} ms'.format(avg))
            print('  95th Percentile: {:.0f} ms'.format(p95))
            print('  Min: {} ms'.format(min_r))
            print('  Max: {} ms'.format(max_r))
            print()
            print('Throughput: {:.2f} req/sec'.format(total / 30) if total > 0 else 0)
except Exception as e:
    print('Note: Python 3 not found or error parsing results')
" 2>/dev/null || echo "[Note] Install Python 3 to see detailed summary statistics"

echo ""
echo "[INFO] Generating HTML report..."
rm -rf report
jmeter -g results.jtl -o report 2>/dev/null || echo "[Note] HTML report generation requires JMeter 5.2+"

if [ -f "report/index.html" ]; then
    echo "[OK] HTML report generated in: report/index.html"
    echo ""
    echo "Opening report in browser..."
    if command -v open &> /dev/null; then
        open report/index.html
    elif command -v xdg-open &> /dev/null; then
        xdg-open report/index.html
    else
        echo "Open file:///$(pwd)/report/index.html in your browser"
    fi
else
    echo "[Note] HTML report generation requires newer JMeter version"
    echo "You can manually review results.jtl"
fi

echo ""
echo "===================================="
echo "  Performance Test Complete!"
echo "===================================="
echo ""
