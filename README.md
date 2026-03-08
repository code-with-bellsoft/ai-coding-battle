# AI Coding Battle Demo

## Tooling and Tech

### Prerequisites

- Java 25
- Spring Boot 4.x
- Maven
- GitHub Copilot
- VSCode

### Uses
- OpenAI via Spring AI
- PostgreSQL
- Flyway
- Vaadin
- JPA/Hibernate
- Testcontainers
- CycloneDX SBOM

### Infra
- Docker Compose
- GitHub Actions

## Tools Used for Quality Checks

- OWASP ZAP Baseline
- Apache Maven PMD Plugin
- SpotBugs Maven Plugin
- Trivy
- JaCoCo

## Person A — One-Shot Prompt

Paste once, then sit back.

## Person B — Iterative Prompts

One at a time, review between each.

## Comparison

| | One-Shot | Iterative |
|---|---|---|
| Speed | Fast start, everything at once | Slower, reviews between steps |
| Coherence | Risk of cross-layer inconsistencies | Each piece verified before the next |
| Errors | Cascade — one mistake breaks layers above | Caught early |
| Architecture | AI decides autonomously | Person B steers intentionally |
| Entertainment | Suspense: "will it work?!" | Educational: audience follows the design |

## Results of Live Stream
[YouTube](https://www.youtube.com/live/ukcQcDXhot4) | 
[LinkedIn](https://www.linkedin.com/posts/johannes-rabauer_live-vibe-coding-battle-build-a-java-app-ugcPost-7435399910750961664-2Rzi) | 
[Twitter](https://x.com/i/broadcasts/1dJrPErjXvYKX?s=20)

[Results of **One-Shot Prompt**](https://github.com/code-with-bellsoft/ai-coding-battle/actions/runs/22734664933)

[Results of **Iterative Prompt**](https://github.com/code-with-bellsoft/ai-coding-battle/actions/runs/22736760516)

| Tool / Metric         | One-Shot | Interactive Prompt |
| --------------------- | -------: | -----------------: |
| JaCoCo Coverage       |      11% |                32% |
| PMD Violations        |        1 |                  0 |
| SpotBugs Total Bugs   |       26 |                 17 |
| Trivy Vulnerabilities |        3 |                  3 |
| ZAP Medium            |        2 |                  2 |
| ZAP Low               |        6 |                  6 |
