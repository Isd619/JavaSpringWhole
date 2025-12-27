package com.example.kafka.service;

import com.example.kafka.event.OrderCreatedEvent;
import com.example.kafka.model.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final String TOPIC = "order-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public KafkaProducerService(KafkaTemplate<String,Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderEvent event) {
        kafkaTemplate.send(TOPIC, event.getOrderId(), event);
    }

    public  void sendOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send("order-created", event.getOrderId().toString(), event);
    }

}
