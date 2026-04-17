# Stage 1: Build (Using the Microsoft Java/Maven image you requested)
FROM mcr.microsoft.com/devcontainers/java:21-bookworm AS builder
WORKDIR /app

# Copy pom.xml and source
COPY pom.xml .
COPY src ./src

# Build with memory limits to prevent Codespaces disconnects
RUN mvn clean package -DskipTests -T 1 -Dmaven.compiler.fork=false

# Stage 2: Runtime (Fixed: Using a valid Debian-based JRE)
FROM openjdk:21-slim-bookworm AS runtime
WORKDIR /app

# Install Docker CLI and Git (The Debian way)
RUN apt-get update && apt-get install -y --no-install-recommends \
    docker.io \
    git \
    curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy the JAR from builder
COPY --from=builder /app/target/*.jar app.jar

ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
