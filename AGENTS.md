# Project Guidelines

## Code Style
- Java package root is `dev.cyberjar.aicodingbattle` (see `src/main/java/dev/cyberjar/aicodingbattle`).
- Keep classes and methods simple and explicit; prefer readability over clever abstractions.
- Follow existing Spring Boot style from `AiCodingBattleApplication` and test classes in `src/test/java/dev/cyberjar/aicodingbattle`.
- Use Flyway SQL migrations in `src/main/resources/db/migration` for schema changes.

## Architecture
- Single Spring Boot app entrypoint: `src/main/java/dev/cyberjar/aicodingbattle/AiCodingBattleApplication.java`.
- Persistence stack: PostgreSQL + Flyway + Spring Data JPA.
- UI stack: Vaadin Flow (`src/main/frontend/index.html`, Vaadin dependencies in `pom.xml`).
- Keep boundaries clean: UI/views, services, repositories, and persistence concerns should remain separated.

## Build and Test
- Use Maven Wrapper, not global Maven.
- Windows: `./mvnw.cmd clean verify -B`
- Linux/macOS: `./mvnw clean verify -B`
- Run focused checks when needed:
	- `./mvnw.cmd test -B`
	- `./mvnw.cmd pmd:check pmd:pmd -B`
	- `./mvnw.cmd spotbugs:check spotbugs:spotbugs -B`
- Start app locally after changes: `./mvnw.cmd spring-boot:run`

## Project Conventions
- Write tests for all implemented features.
- Prefer simple code over elegant code.
- Use the newest reasonable dependency versions when adding/updating dependencies.
- Always verify changes by compiling/running the application and executing tests.
- Keep domain wording consistent with existing config/migrations (`telemed`, `doctor`).

## Integration Points
- OpenAI via Spring AI (`spring-ai-starter-model-openai` in `pom.xml`, properties in `src/main/resources/application.properties`).
- Database config is property-driven in `application.properties`; local infra uses `compose.yaml`.
- Testcontainers PostgreSQL is configured in `TestcontainersConfiguration` with `postgres:18-alpine`.
- CI pipeline in `.github/workflows/ci.yml` runs verify, PMD, SpotBugs, SBOM generation, Trivy, and ZAP baseline.

## Security
- Do not hardcode real secrets; use environment variables (notably `OPENAI_API_KEY`).
- Treat datasource credentials and API keys as sensitive in logs, tests, and sample code.
- Keep Flyway migrations deterministic and forward-only; do not edit existing applied migration files.
- Preserve existing security/quality tooling behavior unless explicitly asked (`Trivy`, `OWASP ZAP`, `PMD`, `SpotBugs`).
