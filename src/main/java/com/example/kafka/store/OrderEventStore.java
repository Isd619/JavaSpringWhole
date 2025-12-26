package com.example.kafka.store;

import com.example.kafka.model.OrderEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public interface OrderEventStore extends JpaRepository<OrderEvent, Long> {

    Page<OrderEvent> findAll(Pageable pageable);

    Optional<OrderEvent> findByOrderId(String orderId);

    @Query("""
           SELECT o FROM OrderEvent o
           ORDER BY o.createdAt DESC
    """)
    List<OrderEvent> findLatest(Pageable pageable);

    @Query("""
           SELECT o FROM OrderEvent o
           ORDER BY o.createdAt ASC
    """)
    List<OrderEvent> findOldest(Pageable pageable);

    void deleteByIdIn(List<Long> ids);

}



//  In-memory store at Phase-1
//    private final List<OrderEvent> events = new CopyOnWriteArrayList<>();
//
//    public void add(OrderEvent event) {
//        events.add(event);
//    }
//
//    public List<OrderEvent> getAll() {
//        return events;
//    }
