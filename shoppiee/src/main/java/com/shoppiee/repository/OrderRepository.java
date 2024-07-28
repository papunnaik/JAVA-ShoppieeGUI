package com.shoppiee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoppiee.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
