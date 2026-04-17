# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
 
WORKDIR /app
 
# FIX: copy pom.xml first so Maven deps are cached as a separate layer —
# source changes won't re-download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B -T 1
 
COPY src ./src
RUN mvn clean package -DskipTests -T 1
 
# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
 
WORKDIR /app
 
# bash: required for VS Code server stability on Alpine
# docker-cli + docker-cli-compose: for docker compose up/down inside container
# git: for VS Code source control
RUN apk add --no-cache \
    bash \
    curl \
    git \
    docker-cli \
    docker-cli-compose
 
COPY --from=builder /app/target/*.jar app.jar
 
ENV DOCKER_API_VERSION=1.43
# FIX: container-aware memory — respects the 2G limit set in compose
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
 
EXPOSE 8080
 
# FIX: was missing JAVA_OPTS — the 2G compose limit was ignored
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
