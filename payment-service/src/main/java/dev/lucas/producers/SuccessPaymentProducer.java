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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SuccessPaymentProducer {

    private final Logger LOGGER = LoggerFactory.getLogger(SuccessPaymentProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final PaymentRepository repository;

    public SuccessPaymentProducer(RabbitTemplate rabbitTemplate, PaymentRepository repository) {
        this.rabbitTemplate = rabbitTemplate;
        this.repository = repository;
    }

    public void produce(Order message) throws JsonProcessingException {
        message.setStatus("APPROVED");
        LOGGER.info("Sucesso no pagamento da Order: {}", message.getUuid());
        repository.save(new Payment(message.getUuid(), "APPROVED"));
        rabbitTemplate.convertAndSend(AmqpConfiguration.EXCHANGE, "payment.approved", new ObjectMapper().writeValueAsString(message));
    }

}
