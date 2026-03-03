# Prompt #1: Database + Domain Layer

You are working inside an existing Spring Boot + Vaadin project.

Extend the existing project. Do NOT create a new project.

### Goal

Implement the persistence layer for doctors.

### Tasks

1. Extend Flyway migrations:
   Create a new migration that adds table `doctor` with columns:

   id
   first_name
   last_name
   specialty
   address
   latitude
   longitude

2. Create:

   * JPA entity `Doctor`
   * `DoctorRepository` using Spring Data JPA

3. Implement a simple distance calculation utility class:
   Input:

   * user latitude
   * user longitude
   * doctor latitude
   * doctor longitude

   Output:

   * distance in kilometers

   A simple Euclidean approximation is sufficient.

4. Add repository method:

   * Find doctors by specialty

5. Ensure:

   * Flyway migration runs successfully
   * Entity mapping works
   * Application builds and starts

Do not implement UI or OpenAI logic yet.

# Prompt #2: OpenAI Integration Services

You are working inside the existing project.

Implement OpenAI integration using Spring AI.

### Tasks

Create:

### 1. SymptomAnalysisService

Input:

* symptoms text

Behavior:

* Call OpenAI using Spring AI
* Use classification style prompt
* Limit to maximum 5 illnesses

Return JSON structure:

[
{
"name":"Illness",
"certainty":percentage,
"specialty":"medical specialty"
}
]

Parse JSON into a DTO list.

Do not store in database.

### 2. AdviceService

Input:

* illness name

Behavior:

* Generate short medical advice
* Return bullet list format
* No persistence

### Requirements

* Use clean DTOs
* Proper JSON parsing
* Handle invalid AI responses defensively
* Unit test JSON parsing

Do not implement UI or REST controllers yet.


# Prompt #3: REST API Layer

Extend the existing project.

Implement REST endpoints only.

### Endpoints

POST `/api/analyze-symptoms`

* Request body: symptoms text
* Calls SymptomAnalysisService
* Returns JSON list

GET `/api/advice/{illness}`

* Calls AdviceService
* Returns advice text

GET `/api/doctors/nearby?lat=x&lon=y&specialty=y`

* Uses DoctorRepository
* Filters by specialty
* Sort by distance
* Return closest 5 doctors

### Requirements

* Proper validation
* Clear DTO usage
* No UI logic
* Unit tests for:

  * distance sorting
  * controller layer

Application must start successfully.


# Prompt #4: Vaadin UI Flow

Extend the existing project.

Implement Vaadin Flow views only.

### Create Views

SymptomsView
Route `/symptoms`

* TextArea for symptoms
* Analyze button
* Call backend
* Store predictions in VaadinSession
* Navigate to `/illness-selection`

IllnessSelectionView
Route `/illness-selection`

* Show illness list
* Show certainty percentage
* Allow selection
* Store selected illness
* Navigate to `/advice`

AdviceView
Route `/advice`

* Show selected illness
* Show advice
* Button: find specialist
* Button: generate share text

SpecialistView
Route `/specialists`

* Use browser geolocation API
* Call backend endpoint
* Display:

  * Name
  * Specialty
  * Address
  * Distance

ShareView
Route `/share`

* Show generated share text
* Copy to clipboard button

### Requirements

* Clean navigation
* No business logic inside views
* Use services or REST calls
* Handle empty session state safely

Do not implement performance tests or Docker here.


# Prompt #5: Integration Testing

Extend the project.

Focus only on tests.

### Add

Use Testcontainers with PostgreSQL.

### Integration Tests

* Flyway migration executes
* DoctorRepository CRUD works
* Doctor specialty search works
* Distance sorting logic works

### Service Tests

* SymptomAnalysisService JSON parsing
* AdviceService response handling

### UI Flow Tests

Test main navigation flow.

Do not add performance testing yet.

# Prompt #6: Performance + Docker

Extend the project.

### Performance

Create folder:

performance-tests

Add JMeter test plan:

Simulate 50 concurrent users calling:

POST `/api/analyze-symptoms`

Measure:

* Average response time
* 95th percentile
* Error rate

Provide instructions to run test.

---

### Docker

Create Dockerfile:

* Multi stage build
* Build Spring Boot app
* Produce optimized runtime image

Compatible with existing compose.yaml.

Application must build and run in container.