package dev.lucas.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.config.AmqpConfiguration;
import dev.lucas.consumers.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ErrorStorageProducer {

    private final RabbitTemplate rabbitTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(ErrorStorageProducer.class);

    public ErrorStorageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produce(Order order) throws JsonProcessingException {
        LOGGER.info("Erro no estoque da Order: {}", order.getUuid());
        rabbitTemplate.convertAndSend(AmqpConfiguration.EXCHANGE_STORAGE, "", new ObjectMapper().writeValueAsString(order));
    }
}
