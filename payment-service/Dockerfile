FROM openjdk:8-jdk-alpine

ADD target/payment-service*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker" ,"-jar", "/app.jar"]