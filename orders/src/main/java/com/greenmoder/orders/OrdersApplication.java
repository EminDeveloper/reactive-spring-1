package com.greenmoder.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class OrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersApplication.class, args);
	}

}

@Controller
class OrderRSocketController {

	private final Map<Integer, List<Order>> db =
			new ConcurrentHashMap<>();

	OrderRSocketController() {

		for (var customerId = 1; customerId <= 10; customerId++) {

			var listOfOrders = new ArrayList<Order>();
			for (var orderId = 1; orderId <= (Math.random() * 100); orderId++) {
				listOfOrders.add(new Order(orderId, customerId));
			}
			this.db.put(customerId, listOfOrders);

		}
	}

	@MessageMapping("orders.{customerId}")
	Flux<Order> getOrdersBy(@DestinationVariable Integer customerId) {
		var list = this.db.get(customerId);
		return Flux.fromIterable(list);
	}

}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Order {

	private Integer id;
	private Integer customerId;

}
