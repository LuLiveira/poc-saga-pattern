package dev.lucas.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.configuration.AmqpConfiguration;
import dev.lucas.producers.PaymentProducer;
import dev.lucas.repositories.Payment;
import dev.lucas.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentConsumer {

    private final PaymentProducer producer;
    private final PaymentRepository repository;

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);

    public PaymentConsumer(PaymentProducer producer, PaymentRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    @RabbitListener(queues = AmqpConfiguration.QUEUE_ORDER_RECEIVED)
    public void orderReceivedConsumer(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        LOGGER.info("Consumindo ordem que foi aprovada no primeiro processo: {}", order.getUuid());

        if (new Random().nextInt(2) % 2 == 0) {
            producer.errorPaymentProducer(order);
        } else {
            producer.successPaymentProducer(order);
        }
    }

    @RabbitListener(queues = AmqpConfiguration.QUEUE_ORDER_CANCELED)
    public void orderCanceledConsumer(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        LOGGER.info("Consumindo order que estava sem estoque: {}", order.getUuid());
        Payment returnedOrder = repository.findByOrderId(order.getUuid());
        repository.delete(returnedOrder);
    }
}
