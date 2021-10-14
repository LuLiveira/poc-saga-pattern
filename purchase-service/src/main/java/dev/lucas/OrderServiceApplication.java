package dev.lucas;

import dev.lucas.producers.OrderProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication implements CommandLineRunner {


	private final OrderProducer producer;

	public OrderServiceApplication(OrderProducer producer) {
		this.producer = producer;
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		producer.produce();
	}


}
