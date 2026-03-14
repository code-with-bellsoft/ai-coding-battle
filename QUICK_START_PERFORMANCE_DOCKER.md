# Quick Start Guide - Performance Testing & Docker

## Performance Testing (JMeter)

### 1-Minute Setup
```bash
# Install JMeter (Windows PowerShell)
winget install ApacheJMeter

# Or download: https://jmeter.apache.org/download_jmeter.html
```

### Run Tests
```bash
# Windows (from project root)
cd performance-tests
run-test.bat

# Linux/macOS
cd performance-tests
chmod +x run-test.sh
./run-test.sh
```

**What it does:**
- Simulates 50 concurrent users
- Makes 500 requests to `/api/analyze-symptoms`
- Generates HTML report with metrics
- Shows: avg response time, 95th percentile, error rate

**Output location:** `performance-tests/report/index.html`

---

## Docker Deployment

### Build Image
```bash
docker build -t telemed:latest .
# Result: ~338MB optimized image
```

### Run with Docker Compose
```bash
# Start app + database
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Stop
docker-compose down
```

**Access application:** http://localhost:8080

### Manual Docker Commands
```bash
# Create network
docker network create telemed_net

# Run PostgreSQL
docker run -d --name telemed-postgres --network telemed_net \
  -e POSTGRES_DB=telemed -e POSTGRES_USER=meduser -e POSTGRES_PASSWORD=medpass \
  postgres:18-alpine

# Run app
docker run -d --name telemed-app --network telemed_net \
  -e DATABASE_URL="postgresql://meduser:medpass@telemed-postgres:5432/telemed" \
  -p 8080:8080 telemed:latest
```

---

## Key Features Implemented

### ✅ Performance Testing
- JMeter test plan with 50 concurrent users
- Windows/Linux/macOS helper scripts
- HTML reporting with statistics
- Customizable test parameters
- CI/CD ready

### ✅ Docker Support
- Multi-stage build (JDK → JRE)
- Alpine Linux for minimal size
- Non-root user execution
- Health checks for orchestration
- Compatible with existing compose.yaml

### ✅ Documentation
- `performance-tests/README.md` - Complete testing guide
- `DOCKER.md` - Docker setup & deployment guide
- `PROMPT_6_SUMMARY.md` - Implementation summary

---

## Project Status

| Component | Status |
|-----------|--------|
| Tests | ✅ 40/40 passing |
| Build | ✅ Success |
| Docker Image | ✅ Built (338MB) |
| Performance Tests | ✅ Ready |
| Documentation | ✅ Complete |

---

## Files Created/Modified

**New Files:**
- `Dockerfile` - Multi-stage build configuration
- `.dockerignore` - Build optimization
- `DOCKER.md` - Docker documentation
- `performance-tests/telemed-performance-test.jmx` - JMeter test plan
- `performance-tests/README.md` - Testing guide (800+ lines)
- `performance-tests/run-test.bat` - Windows test runner
- `performance-tests/run-test.sh` - Linux/macOS test runner
- `PROMPT_6_SUMMARY.md` - Implementation summary

**Modified Files:**
- `.gitignore` - Added performance test results patterns

**Unchanged:**
- `compose.yaml` - Already references Dockerfile
- All application source code
- All tests (all 40 still pass)

---

## Next Steps

1. **Try Performance Testing:**
   ```bash
   cd performance-tests && run-test.bat
   ```
   → View report in browser

2. **Build Docker Image:**
   ```bash
   docker build -t telemed:latest .
   ```
   → Image ready for deployment

3. **Deploy with Compose:**
   ```bash
   docker-compose up -d --build
   ```
   → App running at http://localhost:8080

---

## Support

- **Performance Testing Help:** See `performance-tests/README.md`
- **Docker Deployment Help:** See `DOCKER.md`
- **Project Summary:** See `PROMPT_6_SUMMARY.md`

All documentation is **comprehensive** with examples, troubleshooting, and production tips.
