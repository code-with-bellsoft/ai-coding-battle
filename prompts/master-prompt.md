You are working inside an existing Spring Boot + Vaadin project.

The repository already contains:

- Spring Boot skeleton
- Vaadin setup
- PostgreSQL connection
- Hibernate
- Flyway
- OpenAI configuration
- Agents.md

Extend the existing project. Do NOT create a new project.

## Goal

Build a Healthcare Assistant Web Application.

Features:
1. User enters symptoms as free text
2. OpenAI predicts illnesses with certainty percentages
3. User selects an illness
4. OpenAI generates medical advice
5. Find closest specialist from database
6. Generate shareable text
7. Provide performance benchmarks
8. Dockerfile for the Application that is used in compose.yaml

No authentication required.

## Technologies
Use:
- Spring Boot
- Vaadin Flow
- Spring AI (OpenAI)
- JPA / Hibernate
- PostgreSQL
- Flyway
- Testcontainers
- JMeter

## Views
Create Vaadin views:

### SymptomsView
Route `/symptoms`
- Text input for symptoms
- Analyze button
- Store results in VaadinSession

### IllnessSelectionView
Route `/illness-selection`
- Show illnesses returned by OpenAI
- Show certainty percentages
- Allow selection

### AdviceView
Route `/advice`
- Show selected illness
- Show medical advice from OpenAI
- Button to find specialist
- Button to generate share text

### SpecialistView
Route `/specialists`
- Get user geolocation using browser API
- Show closest doctors from database

Show:
- Name
- Specialty
- Address
- Distance

### ShareView
Route `/share`

- Show generated share text
- Copy button

## Database

Extend Flyway migrations.

### Doctor

id  
first_name  
last_name  
specialty  
address  
latitude  
longitude

Create JPA entity and repository.

## OpenAI Integration

Use Spring AI.

### SymptomAnalysisService

Input:
symptoms text

Output:
list of illnesses with certainty percentage and specialty

Use OpenAI classification.
Return JSON:

[
{
"name":"Illness",
"certainty":percentage,
"specialty":"medical specialty"
}
]

Limit to 5 illnesses.

Store predictions in VaadinSession.

### AdviceService

Generate medical advice using OpenAI.

Prompt:
Provide short medical advice for illness:
{illness}

Return bullet list advice.
No database storage.

## Specialist Search

Find closest doctors using:

- user latitude
- user longitude

Sort by distance.

Return closest 5 doctors matching specialty.

Simple distance calculation is sufficient.

## REST API

Create endpoints:

POST `/api/analyze-symptoms`

GET `/api/advice/{illness}`

GET `/api/doctors/nearby?lat=x&lon=y&specialty=y`

Used for performance tests.

## Testing

### Unit Tests

- Services
- JSON parsing
- Distance calculation

### Integration Tests

Use Testcontainers PostgreSQL.

Test:

- Flyway migrations
- Repository operations
- Doctor search

### UI Tests

Test main flows.

## Performance

Create folder: performance-tests

Add JMeter test plan.

Simulate 50 users calling:

POST `/api/analyze-symptoms`

Measure response time.

## Requirements

- Simple code
- Clear package structure
- No overengineering

Generate all required code and modify existing files if needed.

Ensure application builds and starts successfully.