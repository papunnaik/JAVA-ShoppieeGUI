package com.shoppiee.service;

import com.shoppiee.model.Order;
import com.shoppiee.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order addToCart(Order order) {
        return orderRepository.save(order);
    }

    public Order completeOrder(Order order) {
        order.setProcessed(true);
        return orderRepository.save(order);
    }

    public Order processOrder(Order order) {
        order.setProcessed(true);
        return orderRepository.save(order);
    }
}
