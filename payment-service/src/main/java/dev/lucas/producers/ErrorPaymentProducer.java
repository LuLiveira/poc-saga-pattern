package dev.lucas.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.configuration.AmqpConfiguration;
import dev.lucas.consumers.PaymentConsumer;
import dev.lucas.consumers.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ErrorPaymentProducer {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);

    private final RabbitTemplate rabbitTemplate;

    public ErrorPaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produce(Order message) throws JsonProcessingException {
        message.setStatus("RECUSED");
        LOGGER.info("Erro no pagamento da Order: {}", message.getUuid());
        rabbitTemplate.convertAndSend(AmqpConfiguration.EXCHANGE, "payment.canceled", new ObjectMapper().writeValueAsString(message));
    }
}
