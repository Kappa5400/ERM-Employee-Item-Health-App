FROM eclipse-temurin:21-jdk-jammy AS builder
 
WORKDIR /app
 
RUN apt-get update && apt-get install -y --no-install-recommends maven \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
 
# Cache deps layer separate from source
COPY pom.xml .
RUN mvn dependency:go-offline -B
 
COPY src ./src
RUN mvn clean package -DskipTests -B
 
# ─────────────────────────────
# Stage 2: Runtime
# ─────────────────────────────
FROM eclipse-temurin:21-jre-jammy AS runtime
 
WORKDIR /app
 
# git + docker CLI + compose plugin
RUN apt-get update && apt-get install -y --no-install-recommends \
        git \
        docker.io \
        docker-compose-v2 \
        curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
 
# Non-root user — added to docker group for socket access
RUN groupadd --system spring \
    && useradd --system --gid spring spring \
    && usermod -aG docker spring
 
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar
 
# Mount the host Docker socket at runtime:
#   docker run -v /var/run/docker.sock:/var/run/docker.sock ...
# or in docker-compose:
#   volumes:
#     - /var/run/docker.sock:/var/run/docker.sock
VOLUME ["/var/run/docker.sock"]
 
USER spring
 
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
 
EXPOSE 8080
 
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
