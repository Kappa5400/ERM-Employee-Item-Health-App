# ─────────────────────────────────────────────────────────────
# Stage 1: Build
# eclipse-temurin is the official Adoptium JDK — small, well-maintained
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y --no-install-recommends maven \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy pom.xml FIRST — caches the dependency layer separately from source.
# A code-only change won't re-download Maven dependencies.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ─────────────────────────────────────────────────────────────
# Stage 2: Runtime  (~220MB vs ~600MB for the full JDK image)
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Docker CLI — only keep this if your app shells out to `docker`
# commands at runtime (e.g. spawning containers). If you only
# need Docker for dev/Codespaces, delete this block and add it
# to devcontainer.json instead — keeps your prod image lean.
RUN apt-get update && apt-get install -y --no-install-recommends \
        docker.io \
        curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Non-root user for security
RUN groupadd --system spring && useradd --system --gid spring spring \
    && usermod -aG docker spring   # allows `docker` commands via socket mount

COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

USER spring

# Respects cgroup memory limits set by cloud/k8s schedulers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
