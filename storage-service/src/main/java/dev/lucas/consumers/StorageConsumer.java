package dev.lucas.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lucas.config.AmqpConfiguration;
import dev.lucas.producers.ErrorStorageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StorageConsumer {

    private final ErrorStorageProducer errorStorageProducer;
    private final Logger LOGGER = LoggerFactory.getLogger(StorageConsumer.class);

    public StorageConsumer(ErrorStorageProducer errorStorageProducer) {
        this.errorStorageProducer = errorStorageProducer;
    }

    @RabbitListener(queues = AmqpConfiguration.QUEUE)
    public void consume(@Payload String payload) throws JsonProcessingException {
        Order order = new ObjectMapper().readValue(payload, Order.class);
        int quantity = StorageRepository.findStockById(order.getUuid());
        LOGGER.info("Consumindo order que teve o pagamento aprovado");
        if(false) {
            StorageRepository.save(quantity-1);
            //TODO segue o fluxo de sucesso/finalização da venda
        }else{
            errorStorageProducer.produce(order);
        }
    }


    static class StorageRepository {
        public static int findStockById(String id){
            return new Random().nextInt(2);
        }

        public static void save(int quantity){

        }
    }
}


