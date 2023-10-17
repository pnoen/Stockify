package com.stockify.ordermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;
import com.stockify.ordermanagement.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    Optional<OrderItem> findById(int id);

    Optional<OrderItem> findByOrderId(int orderId);

    Optional<OrderItem> findByOrderIdAndProductId(int orderId, int productId);

    List<OrderItem> findAllByOrderId(int orderId);


}