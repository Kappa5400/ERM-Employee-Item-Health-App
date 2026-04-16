# Stage 1: Build (Maven + Java 21 Alpine)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -T 1 -Dmaven.compiler.fork=false

# Stage 2: Runtime (JRE 21 Alpine)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# git, curl に加え、操作用の docker-cli とスクリプト用の bash をインストール
RUN apk add --no-cache git docker-cli docker-cli-compose curl bash

COPY --from=builder /app/target/*.jar app.jar

ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
