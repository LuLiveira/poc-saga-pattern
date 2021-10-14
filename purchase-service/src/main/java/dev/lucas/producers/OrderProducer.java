package dev.lucas.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.configuration.AmqpConfiguration;
import dev.lucas.repositories.Order;
import dev.lucas.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    private final OrderRepository repository;

    private final RabbitTemplate producer;

    public OrderProducer(OrderRepository repository, RabbitTemplate producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public void produce() throws JsonProcessingException, InterruptedException {
        Order order = repository.save(new Order());
        String json = new ObjectMapper().writeValueAsString(order);
        producer.convertAndSend(AmqpConfiguration.EXCHANGE, "order.received", json);
        LOGGER.info("Sucesso na criação da Order: {}", order.getUuid());
    }
}
