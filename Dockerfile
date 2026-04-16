# Stage 1: ビルド (Maven + Java 21 Alpine)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# メモリ節約のため、テストをスキップしてビルド
RUN mvn clean package -DskipTests

# Stage 2: 実行 (JRE 21 Alpine)
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# git, docker-cli, curl をインストール
# docker.io ではなく docker-cli を使うことで、
# デーモンを含まない軽量なクライアントツールのみを導入します
RUN apk add --no-cache git docker-cli curl

# ビルドした JAR をコピー
COPY --from=builder /app/target/*.jar app.jar

# Codespaces の Docker API バージョンに合わせる (1.43)
ENV DOCKER_API_VERSION=1.43
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
