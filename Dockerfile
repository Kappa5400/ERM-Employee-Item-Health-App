# Stage 1: ビルド (Maven + Java 21)
FROM maven:3.9.6-eclipse-temurin-21-jammy AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: 実行 (Codespaces 標準に近い Ubuntu 環境)
FROM mcr.microsoft.com/devcontainers/base:ubuntu-22.04 AS runtime
WORKDIR /app

# Java 21 と Docker CLI のインストール
RUN apt-get update && apt-get install -y \
    openjdk-21-jre-headless \
    docker.io \
    curl \
    && rm -rf /var/lib/apt/lists/*

# ビルドステージから JAR をコピー
COPY --from=builder /app/target/*.jar app.jar

# Codespaces の Docker デーモンに合わせるための環境変数
ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
