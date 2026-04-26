FROM eclipse-temurin:21-jre

ARG VERSION=0.0.1-SNAPSHOT
ARG JAR_FILE=target/storyservice-${VERSION}.jar

WORKDIR /app

# Copy the pre-built Spring Boot JAR (built locally with Maven)
# Using explicit version to avoid ambiguity with -sources.jar or -javadoc.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
