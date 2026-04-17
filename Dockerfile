FROM mcr.microsoft.com/devcontainers/java:21-bookworm AS builder
WORKDIR /app

# Copy only the pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
# Using -T 1 to keep memory usage low on 2-core Codespaces
RUN mvn clean package -DskipTests -T 1

# Stage 2: Runtime (Using a slim Debian JRE)
FROM eclipse-temurin:21-jre-bookworm AS runtime
WORKDIR /app

# Install Docker CLI, Git, and Bash using Debian's package manager (apt)
RUN apt-get update && apt-get install -y --no-install-recommends \
    docker.io \
    git \
    curl \
    bash \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Environment and Port setup
ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
