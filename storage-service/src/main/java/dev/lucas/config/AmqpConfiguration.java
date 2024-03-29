package dev.lucas.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class AmqpConfiguration {

    public static final String TOPIC_EXCHANGE_ORDER = "order";
    public static final String FANOUT_EXCHANGE_STORAGE = "storage";
    public static final String QUEUE_PAYMENT_APPROVED = "payment.approved";
    public static final String ROUTING_KEY = "payment.approved";


    @Bean
    @Primary
    public Exchange exchangeStorage() {
        return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_STORAGE)
                .durable(true).build();
    }

    @Bean
    @Qualifier("exchange")
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE_ORDER)
                .durable(true).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_PAYMENT_APPROVED)
                .build();
    }

    @Bean
    public Binding binding(@Qualifier("exchange") Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY)
                .noargs();
    }

    @Bean
    @Profile("docker")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("rabbitmq");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }
}
