FROM openjdk:17-jdk
EXPOSE 8089
ADD target/gestion-station-sky-1.0.jar gestion-station-sky-1.0.jar
ENTRYPOINT ["java", "-jar", "gestion-station-sky-1.0.jar"]
