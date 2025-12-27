package com.example.kafka.controller;


import com.example.kafka.enums.DeleteType;
import com.example.kafka.model.OrderEvent;
import com.example.kafka.service.KafkaProducerService;
import com.example.kafka.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final KafkaProducerService producerService;



     public OrderController(KafkaProducerService producerService, OrderService orderService) {
         this.producerService = producerService;
         this.orderService = orderService;
     }

     @PostMapping
     public OrderEvent createOrder(@RequestBody OrderEvent event) {
         log.info("Received request to create order: {}", event);
         OrderEvent orderEvent = orderService.saveOrder(event);
         producerService.send(orderEvent);
         log.info("Received completed for create order: {}", event);
         return orderEvent;
     }


     @GetMapping
     public Page<OrderEvent> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
     ) {
         return orderService.getOrders(page, size, sortBy, direction);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOrders(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "oldest") String type) {

         DeleteType deleteType;

         try {
             deleteType = DeleteType.valueOf(type.toUpperCase());
         } catch (IllegalArgumentException ex) {
             return ResponseEntity.badRequest().body("Invalid type. Allowed values: oldest, latest");
         }

         int deleted = orderService.deleteOrders(count, deleteType);

         return ResponseEntity.ok("Deleted " + deleted + " " + type + " orders");
    }


}
