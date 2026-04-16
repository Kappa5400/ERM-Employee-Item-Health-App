# Stage 1: Build (Mavenイメージを直接使うのが楽です)
FROM maven:3.9.6-eclipse-temurin-21-bookworm AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-bookworm AS runtime
WORKDIR /app

# Docker CLI と curl のインストール
RUN apt-get update && apt-get install -y \
    docker.io \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 前のステージ（builder）から JAR をコピー
# ※ ここを --from=build から --from=builder に合わせる必要があります
COPY --from=builder /app/target/*.jar app.jar

# Codespaces の API バージョン不一致を防ぐための環境変数
ENV DOCKER_API_VERSION=1.43

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
