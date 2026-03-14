# Healthcare Assistant Web Application - Implementation Summary

## Overview
A complete Healthcare Assistant Web Application built with Spring Boot, Vaadin Flow, and OpenAI integration. This application allows users to:
1. Describe symptoms in free text
2. Receive AI-powered illness predictions with certainty percentages
3. Get medical advice tailored to selected illnesses
4. Find nearby medical specialists based on geolocation
5. Share results with others

## Project Structure

### Java Package Layout
```
dev.cyberjar.aicodingbattle/
├── AiCodingBattleApplication.java       # Main Spring Boot application
├── entity/
│   └── Doctor.java                       # JPA entity for doctors
├── repository/
│   └── DoctorRepository.java            # Spring Data JPA repository
├── service/
│   ├── SymptomAnalysisService.java      # OpenAI symptom analysis
│   ├── AdviceService.java               # OpenAI medical advice generation
│   └── SpecialistSearchService.java     # Doctor location search
├── model/
│   ├── Illness.java                     # Illness DTO
│   └── DoctorDistance.java              # Doctor with distance info
├── api/
│   ├── SymptomAnalysisController.java   # REST endpoint for symptom analysis
│   ├── AdviceController.java            # REST endpoint for medical advice
│   └── DoctorController.java            # REST endpoint for doctor search
└── view/
    ├── MainView.java                    # Home view (redirects to symptoms)
    ├── SymptomsView.java                # Symptom input view
    ├── IllnessSelectionView.java        # Illness selection view
    ├── AdviceView.java                  # Medical advice display view
    ├── SpecialistView.java              # Doctor search view with geolocation
    └── ShareView.java                   # Share results view
```

### Key Files
- **Database**: `src/main/resources/db/migration/V1__create_doctor_table.sql`
- **Configuration**: `src/main/resources/application.properties`
- **Docker**: `Dockerfile` (multi-stage build with Maven and Java)
- **Performance Tests**: `performance-tests/healthcare-assistant-load-test.jmx`

## Technology Stack

- **Backend**: Spring Boot 4.0.3
- **Frontend**: Vaadin Flow 25.0.6
- **Database**: PostgreSQL 18 (via Docker Compose)
- **AI**: Spring AI 2.0.0-M2 (OpenAI integration)
- **ORM**: Hibernate with Spring Data JPA
- **Migrations**: Flyway
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Maven with Wrapper
- **Containerization**: Docker & Docker Compose

## Running the Application

### Prerequisites
- Java 25+
- Maven Wrapper (included)
- Docker & Docker Compose
- PostgreSQL database (via compose.yaml) or local PostgreSQL

### 1. Start the PostgreSQL Database
```bash
docker-compose up -d postgres
```

This starts a PostgreSQL container with:
- Database: `telemed`
- Username: `meduser`
- Password: `medpass`
- Port: `5432`

### 2. Configure OpenAI API Key
Set the OpenAI API key as an environment variable:
```bash
export OPENAI_API_KEY="your-api-key-here"
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

The application will:
- Run database migrations via Flyway
- Start on `http://localhost:8080`
- Create the doctor table automatically

### 4. Access the Application
Open your browser and navigate to:
```
http://localhost:8080
```

The app will redirect to `/symptoms` - the main entry point.

## API Endpoints

### POST /api/analyze-symptoms
Analyze symptoms and get illness predictions.

**Request:**
```
POST /api/analyze-symptoms?symptoms=I have a headache, fever, and sore throat
```

**Response:**
```json
[
  {
    "name": "Flu",
    "certainty": 85,
    "specialty": "General Practitioner"
  },
  {
    "name": "Cold",
    "certainty": 70,
    "specialty": "General Practitioner"
  }
]
```

### GET /api/advice/{illness}
Get medical advice for a specific illness.

**Request:**
```
GET /api/advice/Flu
```

**Response:**
```json
[
  "Stay hydrated by drinking water, tea, or soup",
  "Get plenty of rest to help your immune system fight the virus",
  "Use over-the-counter pain relievers for aches and fever",
  ...
]
```

### GET /api/doctors/nearby
Find nearby doctors by specialty.

**Request:**
```
GET /api/doctors/nearby?lat=40.7128&lon=-74.0060&specialty=Cardiology
```

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "specialty": "Cardiology",
    "address": "123 Main St, New York, NY",
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  ...
]
```

## Vaadin Views (Web UI)

### 1. SymptomsView (/symptoms)
- **Route**: `/symptoms`
- **Features**:
  - Text area for symptom input
  - "Analyze Symptoms" button
  - Calls OpenAI to analyze symptoms
  - Stores results in VaadinSession

### 2. IllnessSelectionView (/illness-selection)
- **Route**: `/illness-selection`
- **Features**:
  - Dropdown selector for illnesses
  - Shows certainty percentages
  - Allows user to select the relevant illness

### 3. AdviceView (/advice)
- **Route**: `/advice`
- **Features**:
  - Displays selected illness name
  - Shows bullet-point medical advice from OpenAI
  - "Find Specialist" button (navigates to SpecialistView)
  - "Generate Share Text" button (navigates to ShareView)

### 4. SpecialistView (/specialists)
- **Route**: `/specialists`
- **Features**:
  - "Use My Location" button requests browser geolocation
  - Displays nearby doctors matching the illness specialty
  - Shows doctor names, specialty, address
  - Uses simple Euclidean distance calculation for simplicity

### 5. ShareView (/share)
- **Route**: `/share`
- **Features**:
  - Displays shareable text about the health recommendation
  - "Copy to Clipboard" button
  - "Start Over" button to restart the flow

## Testing

### Unit Tests (9 tests - All passing)
- `SpecialistSearchServiceTest`: Tests distance calculation and doctor filtering
- `IllnessJsonParsingTest`: Tests JSON serialization/deserialization
- `DoctorControllerTest`: Tests controller initialization and service integration

### Run Tests
```bash
./mvnw test
```

## Building for Production

### Build JAR
```bash
./mvnw clean package
```

Output: `target/ai-coding-battle-0.0.1-SNAPSHOT.jar`

### Build Docker Image
```bash
docker build -t healthcare-assistant:latest .
```

### Run with Docker Compose
```bash
docker-compose up app
```

Ensure PostgreSQL is running:
```bash
docker-compose up -d postgres
docker-compose up app
```

## Performance Testing

### Run JMeter Load Test
1. Ensure the application is running on `http://localhost:8080`

2. Execute the test plan:
```bash
jmeter -n -t performance-tests/healthcare-assistant-load-test.jmx \
       -l performance-tests/results.jtl \
       -j performance-tests/test.log
```

**Test Configuration**:
- 50 concurrent users
- 30-second ramp-up
- 10 iterations per user
- Endpoint: `POST /api/analyze-symptoms`

**Expected Results**:
- Average response time: < 2 seconds
- 95th percentile: < 5 seconds
- Error rate: < 1%
- Throughput: > 5 requests/second

## Database Schema

### Doctor Table
```sql
CREATE TABLE doctor (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialty VARCHAR(150) NOT NULL,
    address TEXT,
    latitude NUMERIC(9,6),
    longitude NUMERIC(9,6)
);

CREATE INDEX idx_doctor_last_name ON doctor(last_name);
CREATE INDEX idx_doctor_specialty ON doctor(specialty);
```

## Configuration

### application.properties
```properties
# Server
vaadin.launch-browser=true
spring.application.name=ai-coding-battle

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/telemed
spring.datasource.username=meduser
spring.datasource.password=medpass
spring.datasource.driver-class-name=org.postgresql.Driver

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# OpenAI
spring.ai.openai.api-key=${OPENAI_API_KEY:your-api-key-here}
spring.ai.openai.chat.options.model=gpt-4o
spring.ai.openai.chat.options.temperature=0.7
spring.ai.openai.chat.options.max-tokens=500
```

## Code Quality

### Quality Checks
Pre-configured Maven plugins:
- **JaCoCo**: Code coverage reporting
- **PMD**: Static code analysis
- **SpotBugs**: Bug detection
- **CycloneDX**: SBOM generation

### Run Code Quality Checks
```bash
./mvnw verify
```

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL container is running: `docker-compose ps`
- Check credentials in application.properties
- Verify port 5432 is accessible

### OpenAI API Errors
- Verify OPENAI_API_KEY environment variable is set
- Check API key is valid in OpenAI dashboard
- Ensure API key has proper permissions

### Geolocation Not Working (SpecialistView)
- Application must be served over HTTPS or localhost
- Browser must grant location permission
- Falls back to New York coordinates (40.7128, -74.0060) if denied

### CORS Issues
- All API endpoints respond on the same origin
- Vaadin handles CORS transparently

## Development Notes

### Adding New Doctors
To populate the database with doctors, connect to PostgreSQL:
```sql
INSERT INTO doctor (first_name, last_name, specialty, address, latitude, longitude)
VALUES ('John', 'Smith', 'Cardiology', '123 Main St', 40.7128, -74.0060);
```

### Extending Services
- Services use Spring AI's ChatModel interface
- Easy to switch providers (e.g., Anthropic, Gemini) via configuration

### Vaadin Component Customization
- All views extend Composite for better encapsulation
- Uses VaadinSession for state management between views
- No authentication currently (add Spring Security if needed)

## Future Enhancements

1. **User Authentication**: Add Spring Security for user accounts
2. **Data Persistence**: Store analysis history in database
3. **Doctor Ratings**: Allow users to rate specialists
4. **Multiple Language Support**: i18n for global accessibility
5. **Mobile App**: Create React Native version of UI
6. **Advanced Analytics**: More detailed performance metrics
7. **Doctor Filtering**: Filter by insurance, availability, reviews
8. **Appointment Booking**: Integration with calendar systems

## License
This project is part of the AI Coding Battle challenge.

## Support
For issues or questions, refer to:
- Spring Boot: https://spring.io/projects/spring-boot
- Vaadin: https://vaadin.com/docs
- Spring AI: https://github.com/spring-projects/spring-ai
