############################################################
# RUNTIME-ONLY DOCKERFILE
# Jenkins already built the JAR using Maven.
# This image ONLY packages and runs the final artifact.
############################################################
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

## needed for secrets
RUN mkdir -p /app/config

# Copy the JAR produced by Jenkins
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
