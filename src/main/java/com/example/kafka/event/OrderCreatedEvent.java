package com.example.kafka.event;

public class OrderCreatedEvent {

    public OrderCreatedEvent(Long orderId, String userId, Double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }

    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private String userId;
    private Double amount;
}
