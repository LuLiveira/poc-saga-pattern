package dev.lucas.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.configuration.AmqpConfiguration;
import dev.lucas.repositories.Order;
import dev.lucas.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final RabbitTemplate rabbitTemplate;

    private final OrderRepository repository;

    private final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);


    public OrderConsumer(RabbitTemplate rabbitTemplate, OrderRepository repository) {
        this.rabbitTemplate = rabbitTemplate;
        this.repository = repository;
    }

    @RabbitListener(queues = {AmqpConfiguration.QUEUE, AmqpConfiguration.ORDER_CANCELED})
    public void consume(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        LOGGER.info("Consumindo ordem que teve o pagamento recusado ou sem estoque: {}", order.getUuid());

        //TODO logica de cancelar o pedido
        repository.delete(order);
    }

}
