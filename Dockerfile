FROM openjdk:11
WORKDIR /app
COPY Devops-Gestion-Sky/target/gestion-station-ski-1.0.jar /app/gestion-station-ski-1.0.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar "]