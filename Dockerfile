FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q
EXPOSE 8080
