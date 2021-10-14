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

    public static final String TOPIC_EXCHANGE_ORDER = "order";
    public static final String QUEUE_ORDER_RECEIVED = "order.received";
    public static final String FANOUT_EXCHANGE_STORAGE = "storage";
    public static final String QUEUE_ORDER_CANCELED = "order.canceled.payment";
    public static final String ROUTING_KEY = "order.received";


    @Bean
    @Primary
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE_ORDER)
                .durable(true).build();
    }

    @Bean
    @Primary
    public Queue receiveQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_RECEIVED)
                .build();
    }

    @Bean
    public Binding bindingOrderReceived(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY)
                .noargs();
    }

    @Bean
    @Qualifier("exchangeFanout")
    public Exchange exchangeFanout() {
        return ExchangeBuilder.fanoutExchange(FANOUT_EXCHANGE_STORAGE)
                .durable(true).build();
    }

    @Bean
    @Qualifier("canceledQueue")
    public Queue canceledQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_CANCELED)
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
    @Profile("docker")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("rabbitmq");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

}
