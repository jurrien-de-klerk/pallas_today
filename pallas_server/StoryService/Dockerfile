FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the pre-built JAR (built locally with Maven)
COPY target/storyservice-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
