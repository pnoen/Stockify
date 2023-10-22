package com.stockify.ordermanagement.repository;

import com.stockify.ordermanagement.constants.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import com.stockify.ordermanagement.model.Order;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findById(int id);

    Optional<Order> findByOrderStatusAndCustomerId(OrderStatus status, int customerId);
    List<Order> findByCustomerId(int customerId);

    List<Order> findByBusinessCode(int businessCode);

    int countByBusinessCodeAndOrderStatus(int businessCode, OrderStatus orderStatus);

    @Query("SELECT SUM(o.totalCost) FROM Order o WHERE o.businessCode = :businessCode")
    int sumTotalCostByBusinessCode(@Param("businessCode") int businessCode);

    int countByBusinessCode(int businessCode);


}