# Stage 1: Build (Maven + Java 21 Bookworm)
FROM maven:3.9.6-eclipse-temurin-21-bookworm AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (JRE 21 Bookworm Slim)
FROM eclipse-temurin:21-jre-bookworm AS runtime
WORKDIR /app

# Debian Slim環境を軽量に保つための設定
# --no-install-recommends を使用して余計なパッケージを省きます
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
