package dev.lucas.configuration;

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

    public static final String EXCHANGE = "order";
    public static final String ROUTING_KEY = "order";
    public static final String QUEUE = "payment.canceled";
    public static final String ORDER_CANCELED = "order.canceled";
    public static final String EXCHANGE_STORAGE = "storage";


    @Bean
    @Primary
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE).durable(true).build();
    }

    @Bean
    @Primary
    public Queue queue() {
        return QueueBuilder.durable(QUEUE)
                .build();
    }

    @Bean
    @Qualifier("exchangeFanout")
    public Exchange exchangeFanout() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_STORAGE)
                .durable(true).build();
    }

    @Bean
    @Qualifier("canceledQueue")
    public Queue canceledQueue() {
        return QueueBuilder.durable(ORDER_CANCELED)
                .build();
    }

    @Bean
    public Binding bindingOrderCanceled(@Qualifier("exchangeFanout") Exchange exchange, @Qualifier("canceledQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("")
                .noargs();
    }

    @Bean
    public Binding binding(Exchange exchange, Queue queue) {
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
