# Stage 1: Build (Maven + Java 21 Alpine)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# テストをスキップして高速ビルド
RUN mvn clean package -DskipTests

# Stage 2: Runtime (JRE 21 Alpine - 最小サイズ)
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# 必要なのは Docker CLI と curl だけ。
# docker.io ではなく docker-cli を使うのがコツです。
RUN apk add --no-cache docker-cli curl

# ビルドした JAR をコピー
COPY --from=builder /app/target/*.jar app.jar

# Codespaces 互換設定
ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
