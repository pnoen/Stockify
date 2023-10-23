package com.stockify.ordermanagement;

import com.stockify.ordermanagement.controller.OrderItemController;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.model.OrderItem;
import com.stockify.ordermanagement.repository.OrderItemRepository;
import com.stockify.ordermanagement.repository.OrderRepository;
import com.stockify.ordermanagement.service.OrderItemService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderItemManagementTest {

    @InjectMocks
    private OrderItemController orderItemController;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
    }

    @Test
    public void testValidGetOrderById() {
        OrderItem mockOrderItem = new OrderItem();

        when(orderItemRepository.findById(mockOrderItem.getOrderId())).thenReturn(Optional.of(mockOrderItem));

        ResponseEntity<OrderItemResponse> response = orderItemController.getOrderById(mockOrderItem.getOrderId());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockOrderItem, response.getBody().getOrderItem());
    }

    @Test
    public void testInvalidGetOrderById() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<OrderItemResponse> response = orderItemController.getOrderById(1);

        assertEquals(null, response.getBody().getOrderItem());
    }
}
