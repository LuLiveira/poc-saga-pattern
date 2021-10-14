package dev.lucas.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.config.AmqpConfiguration;
import dev.lucas.producers.StorageProducer;
import dev.lucas.repositories.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class StorageConsumer {

    private final StorageProducer storageProducer;
    private final Logger LOGGER = LoggerFactory.getLogger(StorageConsumer.class);
    private final StorageRepository repository;

    public StorageConsumer(StorageProducer storageProducer, StorageRepository repository) {
        this.storageProducer = storageProducer;
        this.repository = repository;
    }

    @RabbitListener(queues = AmqpConfiguration.QUEUE_PAYMENT_APPROVED)
    public void consume(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        int quantity = repository.findStockById(order.getUuid());
        LOGGER.info("Consumindo order que teve o pagamento aprovado");
        if(quantity > 0) {
            repository.save(quantity-1);
        }else{
            storageProducer.produce(order);
        }
    }
}


