# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Start Database
```bash
docker-compose up -d postgres
```

### Step 2: Set API Key
```bash
export OPENAI_API_KEY="sk-..."  # Your OpenAI API key
```

### Step 3: Run the Application
```bash
./mvnw spring-boot:run
```

### Step 4: Open Browser
Go to: **http://localhost:8080**

## 📝 Using the Application

1. **Describe Your Symptoms**: Type your symptoms in the text area
2. **Get AI Predictions**: Click "Analyze Symptoms"
3. **Select Illness**: Choose the most relevant illness from the list
4. **Read Medical Advice**: Get tailored medical advice
5. **Find Specialist**: Click to find nearby doctors
6. **Share Results**: Copy the recommendation to share

## 🧪 Run Tests
```bash
./mvnw test
```

## 📦 Build for Production
```bash
./mvnw clean package
docker build -t healthcare-assistant:latest .
```

## 📊 Performance Testing
```bash
# Start application first (./mvnw spring-boot:run)
jmeter -n -t performance-tests/healthcare-assistant-load-test.jmx \
       -l performance-tests/results.jtl
```

## 🔧 Available Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/analyze-symptoms?symptoms=...` | Analyze symptoms with OpenAI |
| GET | `/api/advice/{illness}` | Get medical advice |
| GET | `/api/doctors/nearby?lat=X&lon=Y&specialty=...` | Find nearby doctors |

## 📋 Database Credentials

| Property | Value |
|----------|-------|
| Database | telemed |
| Username | meduser |
| Password | medpass |
| Host | localhost:5432 |

## ⚠️ Troubleshooting

**Connection refused?**
- Make sure PostgreSQL is running: `docker-compose ps`

**OpenAI API error?**
- Check `OPENAI_API_KEY` is set: `echo $OPENAI_API_KEY`
- Verify the key is valid in OpenAI dashboard

**Geolocation not working?**
- Browser must grant permission
- Falls back to New York if denied
- Must use HTTPS in production (localhost works for testing)

## 📚 Documentation

- Full implementation: See `IMPLEMENTATION.md`
- Project guidelines: See `AGENTS.md`
- Architecture: See `README.md`
