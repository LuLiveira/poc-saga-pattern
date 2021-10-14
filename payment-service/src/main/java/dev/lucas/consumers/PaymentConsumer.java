package dev.lucas.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.configuration.AmqpConfiguration;
import dev.lucas.producers.ErrorPaymentProducer;
import dev.lucas.producers.SuccessPaymentProducer;
import dev.lucas.repositories.Payment;
import dev.lucas.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentConsumer {

    private final ErrorPaymentProducer errorPaymentProducer;
    private final SuccessPaymentProducer successPaymentProducer;
    private final PaymentRepository repository;

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);

    public PaymentConsumer(RabbitTemplate rabbitTemplate, ErrorPaymentProducer errorPaymentProducer, SuccessPaymentProducer successPaymentProducer, PaymentRepository repository) {
        this.errorPaymentProducer = errorPaymentProducer;
        this.successPaymentProducer = successPaymentProducer;
        this.repository = repository;
    }

    @RabbitListener(queues = AmqpConfiguration.ORDER_RECEIVED)
    public void orderReceivedConsumer(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        LOGGER.info("Consumindo ordem que foi aprovada no primeiro processo: {}", order.getUuid());

        if (new Random().nextInt(10) % 2 == 0) {
            //TODO uma exceção/erro/falha/etc...
            errorPaymentProducer.produce(order);
        } else {
            //TODO sucesso/seguir em frente/bora
            successPaymentProducer.produce(order);
        }
    }

    @RabbitListener(queues = AmqpConfiguration.ORDER_CANCELED)
    public void orderCanceledConsumer(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        LOGGER.info("Consumindo order que estava sem estoque: {}", order.getUuid());
        Payment byOrderId = repository.findByOrderId(order.getUuid());
        repository.delete(byOrderId);
    }
}
