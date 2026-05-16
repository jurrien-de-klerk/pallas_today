FROM eclipse-temurin:21-jre

ARG VERSION=0.0.1-SNAPSHOT
ARG JAR_FILE=target/memberservice-${VERSION}.jar

WORKDIR /app

RUN groupadd --system --gid 1001 appgroup \
    && useradd --system --uid 1001 --gid 1001 appuser \
    && chown appuser:appgroup /app

# Copy the pre-built Spring Boot JAR (built locally with Maven)
# Using explicit version to avoid ambiguity with -sources.jar or -javadoc.jar
COPY --chown=appuser:appgroup ${JAR_FILE} app.jar

EXPOSE 8081

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]