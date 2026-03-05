# Multi-stage build for the Healthcare Assistant application

# Stage 1: Build the application with Maven
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Copy the source code
COPY src src

# Build the application
RUN chmod +x mvnw && ./mvnw clean package -B -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/ai-coding-battle-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables
ENV OPENAI_API_KEY=""
ENV DATABASE_URL="postgresql://meduser:medpass@postgres:5432/telemed"
ENV SPRING_DATASOURCE_URL="$DATABASE_URL"
ENV SPRING_DATASOURCE_USERNAME="meduser"
ENV SPRING_DATASOURCE_PASSWORD="medpass"

# Expose the port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
