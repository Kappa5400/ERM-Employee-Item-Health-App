# Stage 1: Build (Maven + Java 21)
# maven:3.9.6-eclipse-temurin-21 は Debian Bookworm ベースです
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (JRE 21 Debian)
# eclipse-temurin:21-jre は標準で Debian Bookworm ベースになります
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Docker CLI と curl をインストール (Debian 用)
RUN apt-get update && apt-get install -y --no-install-recommends \
    docker.io \
    curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# ビルドした JAR をコピー
COPY --from=builder /app/target/*.jar app.jar

# Codespaces 互換設定
ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
