# Multi-stage build for Spring Boot application
# Stage 1: Build stage
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /workspace

# Copy Maven wrapper files
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copy POM and source
COPY pom.xml .
COPY src src

# Build the application
RUN chmod +x mvnw && \
    ./mvnw clean package -DskipTests -B

# Rename the JAR for easier reference
RUN mkdir -p build && \
    cp target/*.jar build/application.jar && \
    cd build && \
    jar -xf application.jar && \
    rm application.jar

# Stage 2: Runtime stage
FROM eclipse-temurin:25-jre-alpine

# Create non-root user for security
RUN addgroup -g 1000 spring && \
    adduser -D -u 1000 -G spring spring

WORKDIR /app

# Copy extracted JAR layers from builder
COPY --from=builder --chown=spring:spring /workspace/build/BOOT-INF/lib /app/lib
COPY --from=builder --chown=spring:spring /workspace/build/META-INF /app/META-INF
COPY --from=builder --chown=spring:spring /workspace/build/BOOT-INF/classes /app

# Switch to non-root user
USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Start application
ENTRYPOINT ["java", "-cp", ".:lib/*", "org.springframework.boot.loader.launch.JarLauncher"]
