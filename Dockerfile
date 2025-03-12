FROM openjdk:23-jdk

WORKDIR /app

COPY FlightApplication.jar /app/FlightApplication.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/FlightApplication.jar"]