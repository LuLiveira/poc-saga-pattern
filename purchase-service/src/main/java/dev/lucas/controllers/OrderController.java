package dev.lucas.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.lucas.producers.OrderProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer producer;

    public OrderController(OrderProducer producer) {
        this.producer = producer;
    }

    @GetMapping
    public String generate() throws JsonProcessingException, InterruptedException {
        producer.produce();
        return "OK!";
    }
}
