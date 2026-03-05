# Performance Tests

This folder contains Apache JMeter performance tests for the Telemed API.

## Setup

### Prerequisites

1. **Apache JMeter** (5.6.3 or later)
   - Download from: https://jmeter.apache.org/download_jmeter.html
   - Extract to a location of your choice

2. **Running Application**
   - Ensure the Telemed API is running on `http://localhost:8080`
   - Start with: `./mvnw.cmd spring-boot:run`

### Installation Steps

#### Windows
```bash
# Download JMeter (5.6.3 or later)
# From: https://jmeter.apache.org/download_jmeter.html

# Extract to a location, e.g., C:\tools\apache-jmeter-5.6.3
# Add to PATH: C:\tools\apache-jmeter-5.6.3\bin

# Verify installation
jmeter --version
```

#### Linux/macOS
```bash
# Using Homebrew (macOS)
brew install jmeter

# Or download and extract manually
cd ~/tools
wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.6.3.tgz
tar -xzf apache-jmeter-5.6.3.tgz
export PATH=~/tools/apache-jmeter-5.6.3/bin:$PATH
jmeter --version
```

## Running Performance Tests

### Option 1: GUI Mode (Interactive)

```bash
# Open JMeter GUI
jmeter -t telemed-performance-test.jmx

# In GUI:
# 1. Review test configuration (users, ramp-up time, loops)
# 2. Click "Run" button (green play icon)
# 3. Monitor real-time results in Summary Report
# 4. View detailed results after test completes
```

### Option 2: Command Line Mode (Recommended for CI/CD)

```bash
# Run test and save results
jmeter -n -t telemed-performance-test.jmx -l results.jtl -j jmeter.log

# Generate HTML report (optional)
jmeter -g results.jtl -o report/

# View report
# Windows:
start report\index.html

# Linux/macOS:
open report/index.html
```

### Option 3: Batch Script (Windows)

Run the included batch script:
```bash
run-test.bat
```

This will:
1. Run the test in CLI mode
2. Generate results.jtl file
3. Generate HTML report in `report/` directory
4. Display summary statistics

### Option 4: Shell Script (Linux/macOS)

Run the included shell script:
```bash
chmod +x run-test.sh
./run-test.sh
```

## Test Configuration

The test simulates **50 concurrent users** calling the `POST /api/analyze-symptoms` endpoint.

**Current Settings:**
- **Concurrent Users:** 50
- **Ramp-up Time:** 30 seconds (5 second per user to reach 50)
- **Loops per User:** 10
- **Total Requests:** 500 (50 users × 10 loops)
- **Endpoint:** `POST http://localhost:8080/api/analyze-symptoms`
- **Payload:** JSON with sample symptoms

### Customizing Test Parameters

Edit the test plan in GUI mode or modify variables directly in the JMX file:

```xml
<elementProp name="CONCURRENT_USERS" elementType="Argument">
  <stringProp name="Argument.value">50</stringProp>
</elementProp>

<elementProp name="RAMP_UP_TIME" elementType="Argument">
  <stringProp name="Argument.value">30</stringProp>
</elementProp>

<elementProp name="LOOP_COUNT" elementType="Argument">
  <stringProp name="Argument.value">10</stringProp>
</elementProp>
```

## Metrics Collected

The test measures the following performance metrics:

### Response Time
- **Average Response Time:** Mean time for all requests
- **95th Percentile:** 95% of requests complete within this time
- **Min/Max:** Minimum and maximum response times

### Throughput
- **Samples per Second:** Requests processed per second
- **Sent KB/sec:** Data sent rate
- **Received KB/sec:** Data received rate

### Errors
- **Error Count:** Number of failed requests
- **Error Rate:** Percentage of failed requests
- **Response Codes:** Distribution of HTTP status codes

## Example Output

```
Summary Report
==============
Samples:                500
Average:                245 ms
Min:                    120 ms
Max:                    890 ms
Std. Dev:               145 ms
Error Rate:             0.0%
Throughput:             2.5 req/sec
Received (KB/sec):      1.8
Sent (KB/sec):          0.6
```

## Interpreting Results

### Healthy Performance Indicators
- Average response time < 500ms
- 95th percentile < 1000ms
- Error rate < 1%
- Throughput > 1 req/sec

### Warning Signs
- Average response time > 1000ms
- 95th percentile > 2000ms
- Error rate > 5%
- Increasing response times as test progresses (thread starvation)

## Troubleshooting

### Connection Refused
- Ensure application is running on port 8080
- Check firewall settings
- Verify browser can access http://localhost:8080

### High Error Rate / Timeouts
- Check application logs for errors
- Verify database connectivity
- Check OpenAI API availability (if service makes external calls)
- Increase connection timeout in test plan

### GC Pauses / Performance Spikes
- Monitor JVM heap usage during test
- Consider increasing heap size: `-Xmx1g`
- Check background processes consuming CPU

### JMeter Load Not Reaching Target
- Verify thread group settings
- Check OS limits on concurrent connections
- Run JMeter with more heap: `jmeter -Xmx2g -t test.jmx`

## Saving and Exporting Results

### Results File (results.jtl)
Tab-separated values with detailed results for every request.

```bash
# Convert to CSV for spreadsheet analysis
jmeter -g results.jtl -o report/
```

### HTML Report
Comprehensive dashboard with graphs and statistics:
```bash
jmeter -g results.jtl -o report/
```

### JTL to CSV
```bash
# Use JMeter's report generator
jmeter -g results.jtl -o report-output/
```

## CI/CD Integration

For automated performance testing in CI pipelines:

```bash
#!/bin/bash
# Start app
./mvnw spring-boot:run &
APP_PID=$!
sleep 30  # Wait for startup

# Run performance test
jmeter -n -t performance-tests/telemed-performance-test.jmx \
  -l performance-tests/results.jtl \
  -j performance-tests/jmeter.log

# Generate report
jmeter -g performance-tests/results.jtl \
  -o performance-tests/report/

# Stop app
kill $APP_PID

# Check error rate
ERROR_RATE=$(grep -i "error" performance-tests/results.jtl | wc -l)
if [ $ERROR_RATE -gt 5 ]; then
  echo "Performance test failed!"
  exit 1
fi
```

## Additional Resources

- [Apache JMeter Documentation](https://jmeter.apache.org/usermanual/index.html)
- [JMeter Best Practices](https://jmeter.apache.org/usermanual/best-practices.html)
- [Performance Testing Tips](https://jmeter.apache.org/usermanual/properties-reference.html)
