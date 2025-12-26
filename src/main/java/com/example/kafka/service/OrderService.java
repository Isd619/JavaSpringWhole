package com.example.kafka.service;

import com.example.kafka.enums.DeleteType;
import com.example.kafka.model.OrderEvent;
import com.example.kafka.store.OrderEventStore;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderEventStore orderEventStore;

    public OrderService(OrderEventStore orderEventStore) {
        this.orderEventStore = orderEventStore;
    }

    public OrderEvent saveOrder(OrderEvent event) {
        return orderEventStore.save(event);
    }

    public List<OrderEvent> getAllOrders() {
        return orderEventStore.findAll();
    }

    public Page<OrderEvent> getOrders(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderEventStore.findAll(pageable);
    }

    public int deleteOrders(int count, DeleteType type) {

        if(count <= 0) {
            throw new IllegalArgumentException("Count must be greater than zero");
        }

        Pageable pageable = PageRequest.of(0, count);

        List<OrderEvent> orders = switch (type) {
            case LATEST -> orderEventStore.findLatest(pageable);
            case OLDEST -> orderEventStore.findOldest(pageable);
        };

        if(orders .isEmpty()) {
            return 0;
        }

        List<Long> ids = orders.stream()
                .map(OrderEvent::getId)
                .toList();

        orderEventStore.deleteByIdIn(ids);
        return ids.size();
    }



}
