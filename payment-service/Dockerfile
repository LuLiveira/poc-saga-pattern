FROM openjdk:8-jdk-alpine

ADD target/payment-service*.jar payment.jar

ENTRYPOINT ["java", "-jar", "/payment.jar"]