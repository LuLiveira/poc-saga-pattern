FROM openjdk:8-jdk-alpine

ADD target/storage-service*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker" ,"-jar", "/app.jar"]