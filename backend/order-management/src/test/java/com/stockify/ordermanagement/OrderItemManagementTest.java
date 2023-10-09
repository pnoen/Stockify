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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
    }

    @Test
    public void testValidDeleteOrderItem() {
        OrderItemIdRequest validRequest = new OrderItemIdRequest();
        validRequest.setOrderItemId(1);

        OrderItem orderItem = new OrderItem();
        Optional<OrderItem> optionalOrderItem = Optional.of(orderItem);

        when(orderItemRepository.findById(1)).thenReturn(optionalOrderItem);

        ResponseEntity<ApiResponse> response = orderItemController.deleteOrderItem(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order Item deleted successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testInvalidDeleteOrderItem() {
        OrderItemIdRequest validRequest = new OrderItemIdRequest();
        validRequest.setOrderItemId(1);

        ResponseEntity<ApiResponse> response = orderItemController.deleteOrderItem(validRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Order Item does not exist.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testGetAllOrders() {
        List<OrderItem> mockOrderItems = new ArrayList<>();
        mockOrderItems.add(new OrderItem());
        mockOrderItems.add(new OrderItem());

        when(orderItemRepository.findAllByOrderId(1)).thenReturn(mockOrderItems);

        ResponseEntity<OrderItemListResponse> response = orderItemController.getAllOrders(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrderItems, Objects.requireNonNull(response.getBody()).getOrderItem());
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
