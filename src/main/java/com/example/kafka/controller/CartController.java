package com.example.kafka.controller;

import com.example.kafka.model.CartItem;
import com.example.kafka.model.Order;
import com.example.kafka.service.CartService;
import com.example.kafka.service.CheckoutService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    public CartController(CartService cartService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }

    @PostMapping("/{userId}/items")
    public CartItem addItem(
            @PathVariable String userId,
            @RequestBody CartItem item
    ) {
        return cartService.addToCart(userId, item);
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCart(
            @PathVariable String userId
    ) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/item/{itemId}")
    public void removeItem(
            @PathVariable Long itemId
    ) {
        cartService.removeItem(itemId);
    }

    @PostMapping("/{userId}/checkout")
    public Order checkout(
            @PathVariable String userId
    ) {
        return checkoutService.checkout(userId);
    }

}
