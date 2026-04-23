# Get maven, openjdk, alpine linux.
# cloud instance is low power 1 vCPU 2gb ram,
# so need to use extremely efficient linux build such as alpine.
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
 
WORKDIR /app
 
# download all dependencies from pom file
# to /app
COPY pom.xml .
RUN mvn dependency:go-offline -B -T 1
 
COPY src ./src
# test are run before in github actions
RUN mvn clean package -DskipTests -T 1
 

FROM eclipse-temurin:21-jre-alpine
 
WORKDIR /app
 
# Get bash, curl, git, docker on server
# as our build doesn't come with them baked in
RUN apk add --no-cache \
    bash \
    curl \
    git \
    docker-cli \
    docker-cli-compose
    
# from previous build extrat app.jar file
COPY --from=builder /app/target/*.jar app.jar

# Alpine limited to docker 1.43
ENV DOCKER_API_VERSION=1.43

# To optimize java running in our tiny cloud instance
# we have the inside container flag tirggered and set
# max ram heap size to 75% of container memory.
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
 
EXPOSE 8080
 
# Runs the jar file of the app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
