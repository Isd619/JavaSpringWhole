package com.example.kafka.service;

import com.example.kafka.model.OrderEvent;
import com.example.kafka.store.OrderEventStore;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final OrderService orderService;

    public KafkaConsumerService(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "order-topic", groupId = "demo-group")
    public void consume(OrderEvent event) {
        System.out.println("Consumed from Kafka: "+ event);
        orderService.saveOrder(event);
    }
}
