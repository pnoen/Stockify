package com.stockify.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import com.stockify.ordermanagement.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findById(int id);
}