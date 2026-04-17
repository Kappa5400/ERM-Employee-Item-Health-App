# Stage 1: Build (Maven + Java 21 on Alpine)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
# -T 1: Uses a single thread to prevent memory spikes on 2-core machines
RUN mvn clean package -DskipTests -T 1

# Stage 2: Runtime (JRE 21 on Alpine)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install Docker CLI, Compose, Bash, and Git
# Alpine needs bash specifically for VS Code server stability
RUN apk add --no-cache \
    docker-cli \
    docker-cli-compose \
    bash \
    curl \
    git

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Configuration for Docker-outside-of-Docker
ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
