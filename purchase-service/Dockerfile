FROM openjdk:8-jdk-alpine

ADD target/order-service*.jar order.jar

ENTRYPOINT ["java", "-jar", "/order.jar"]