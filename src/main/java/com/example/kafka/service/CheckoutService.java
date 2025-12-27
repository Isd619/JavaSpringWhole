package com.example.kafka.service;

import com.example.kafka.event.OrderCreatedEvent;
import com.example.kafka.model.CartItem;
import com.example.kafka.model.Order;
import com.example.kafka.store.OrderEventStoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {

    private final CartService cartService;
    private final OrderEventStoreRepository orderEventStoreRepository;
    private final KafkaProducerService kafkaProducerService;

    public CheckoutService(CartService cartService,
                           OrderEventStoreRepository orderEventStoreRepository,
                           KafkaProducerService kafkaProducerService) {
        this.cartService = cartService;
        this.orderEventStoreRepository = orderEventStoreRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public Order checkout(String userId) {

        List<CartItem> items = cartService.getCart(userId);

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty for user: " + userId);
        }

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setStatus("CREATED");

        kafkaProducerService.sendOrderCreated(
                new OrderCreatedEvent(order.getId(), userId, total)
        );

        cartService.clearCart(userId);

        return order;
    }
}
