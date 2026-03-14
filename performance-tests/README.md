# Healthcare Assistant - Performance Tests

This directory contains JMeter test plans for performance benchmarking of the Healthcare Assistant application.

## Test Plan: healthcare-assistant-load-test.jmx

**Description**: Load testing the symptom analysis endpoint with concurrent users.

**Configuration**:
- **Users**: 50 concurrent users
- **Ramp-up time**: 30 seconds (gradually ramp up to 50 users)
- **Iterations per user**: 10
- **Endpoint tested**: `POST /api/analyze-symptoms`

**Test Sample**:
```
Symptoms: "I have a headache, fever, and sore throat. It started 2 days ago and I feel tired."
```

## Running the Test

### Prerequisites
1. JMeter installed (download from https://jmeter.apache.org/download_jmeter.html)
2. Healthcare Assistant application running on http://localhost:8080

### Steps

1. Start the application:
```bash
./mvnw spring-boot:run
```

2. Open JMeter:
```bash
jmeter -t healthcare-assistant-load-test.jmx
```

3. Run the test via GUI or command line:
```bash
jmeter -n -t healthcare-assistant-load-test.jmx -l performance-test-results.jtl -j performance-test.log
```

## Expected Results

- **Average response time**: < 2000ms
- **95th percentile**: < 5000ms
- **Error rate**: < 1%
- **Throughput**: > 5 requests/second

## Analyzing Results

After the test completes, review:
- `performance-test-results.jtl`: Raw test results
- Summary report in GUI shows aggregate statistics
- Graph results show response time trends

## Performance Optimization Notes

If performance is below expected levels:
1. Check OpenAI API latency (typically 1-2 seconds per request)
2. Verify database connection pooling is configured
3. Monitor CPU and memory usage on the server
4. Consider increasing thread pool sizes if applicable
