# syntax=docker/dockerfile:1.4
# Build stage with Docker BuildKit cache for Maven dependencies
FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy POM and resolve dependencies (cached in BuildKit)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the application (caches dependencies and compiled artifacts)
RUN --mount=type=cache,target=/root/.m2 --mount=type=cache,target=/build/target mvn package -q -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/target/storyservice-*.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
