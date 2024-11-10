FROM openjdk:8-jdk-alpine
EXPOSE 9096
ADD target/gestion-station-ski-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
