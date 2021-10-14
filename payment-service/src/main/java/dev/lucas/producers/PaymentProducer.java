package dev.lucas.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.configuration.AmqpConfiguration;
import dev.lucas.consumers.Order;
import dev.lucas.repositories.Payment;
import dev.lucas.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PaymentProducer {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentProducer.class);
    private final static String SUCCESS_ROUTING_KEY = "payment.approved";
    private final static String ERROR_ROUTING_KEY = "payment.canceled";

    private final RabbitTemplate rabbitTemplate;
    private final PaymentRepository repository;

    public PaymentProducer(RabbitTemplate rabbitTemplate, PaymentRepository repository) {
        this.rabbitTemplate = rabbitTemplate;
        this.repository = repository;
    }

    public void successPaymentProducer(Order message) throws JsonProcessingException {
        message.setStatus("APPROVED");
        LOGGER.info("Sucesso no pagamento da Order: {}", message.getUuid());
        repository.save(new Payment(message.getUuid(), "APPROVED"));
        rabbitTemplate.convertAndSend(AmqpConfiguration.TOPIC_EXCHANGE_ORDER, this.SUCCESS_ROUTING_KEY, new ObjectMapper().writeValueAsString(message));
    }

    public void errorPaymentProducer(Order message) throws JsonProcessingException {
        message.setStatus("RECUSED");
        LOGGER.info("Erro no pagamento da Order: {}", message.getUuid());
        rabbitTemplate.convertAndSend(AmqpConfiguration.TOPIC_EXCHANGE_ORDER, this.ERROR_ROUTING_KEY, new ObjectMapper().writeValueAsString(message));
    }

}
