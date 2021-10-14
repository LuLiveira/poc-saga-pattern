FROM rabbitmq:3-management

EXPOSE 5672
EXPOSE 15672

RUN apt-get update && apt-get install -y curl