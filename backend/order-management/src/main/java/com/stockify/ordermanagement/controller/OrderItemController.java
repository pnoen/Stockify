package com.stockify.ordermanagement.controller;

import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.model.OrderItem;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.repository.OrderItemRepository;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderItemRequest orderItemRequest) {
        int orderId = orderItemRequest.getOrderId();
        int productId = orderItemRequest.getProductId();
        int productVarietyId = orderItemRequest.getProductVarietyId();
        int quantitySuffixId = orderItemRequest.getQuantitySuffixId();
        float quantity = orderItemRequest.getQuantity();
        LocalDate lastUpdated = orderItemRequest.getLastUpdated();

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setOrderId(orderId);
        newOrderItem.setProductId(productId);
        newOrderItem.setProductVarietyId(productVarietyId);
        newOrderItem.setQuantitySuffixId(quantitySuffixId);
        newOrderItem.setQuantity(quantity);
        newOrderItem.setLastUpdated(lastUpdated);

        orderItemRepository.save(newOrderItem);

        return ResponseEntity.ok(new ApiResponse(200, "Order Item created successfully."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody OrderItemIdRequest orderItemIdRequest) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemIdRequest.getOrderItemId());

        if (orderItemOptional.isPresent()) {
            orderItemRepository.deleteById(orderItemIdRequest.getOrderItemId());
            return ResponseEntity.ok(new ApiResponse(200, "Order Item deleted successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(404, "Order Item does not exist."));
        }
    }

    // Get a list of all the order items with the given order id
    @GetMapping("/getAllByOrderId")
    public List<OrderItem> getAllOrders(@RequestBody OrderIdRequest orderIdRequest) {
        return orderItemRepository.findAllByOrderId(orderIdRequest.getOrderId());
    }

    // Get an order item by its ID
    @GetMapping("/getOrderItemById")
    public OrderItem getOrderById(@RequestBody OrderIdRequest orderIdRequest) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderIdRequest.getOrderId());

        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
            return orderItem;
        } else {
            return null;
        }
    }
}