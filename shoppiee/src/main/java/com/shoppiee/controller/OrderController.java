package com.shoppiee.controller;

import com.shoppiee.model.Order;
import com.shoppiee.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/addToCart")
    public ResponseEntity<Order> addToCart(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.addToCart(order));
    }

    @PostMapping("/completeOrder")
    public ResponseEntity<Order> completeOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.completeOrder(order));
    }

    @PostMapping("/processOrder")
    public ResponseEntity<Order> processOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.processOrder(order));
    }
}
