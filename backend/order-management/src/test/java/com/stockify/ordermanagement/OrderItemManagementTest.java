package com.stockify.ordermanagement;

import com.stockify.ordermanagement.constants.OrderStatus;
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
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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

        Order mockOrder = new Order();
        mockOrder.setOrganisation(1);
        mockOrder.setCustomerId(1);
        mockOrder.setBusinessCode(1); // example valid order for the tests
        mockOrder.setInvoiceId(1);
        mockOrder.setOrderDate(LocalDate.of(2023, 10, 9));
        mockOrder.setCompletionDate(LocalDate.of(2023, 10, 9));
        mockOrder.setTotalCost(1.0);
        mockOrder.setOrderStatus(OrderStatus.COMPLETE);
        mockOrder.setBusinessName("Stockify");
        when(orderRepository.findById(1)).thenReturn(Optional.of(mockOrder));

        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
    }

    @Test
    public void testValidCreateOrderItem() {
        // Create Valid Order Item
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setOrderId(1);
        orderItemRequest.setProductId(1);
        orderItemRequest.setBusinessCode(1);
        orderItemRequest.setQuantity(1);
        orderItemRequest.setLastUpdated(LocalDate.of(2023, 10, 9));
        orderItemRequest.setPrice(1);

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(orderItemRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testInvalidCreateOrderItem() {
        // Create Order Item Without Valid Existing Order
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setOrderId(0);
        orderItemRequest.setProductId(1);
        orderItemRequest.setBusinessCode(1);
        orderItemRequest.setQuantity(1);
        orderItemRequest.setLastUpdated(LocalDate.of(2023, 10, 9));
        orderItemRequest.setPrice(1);

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(orderItemRequest);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testInvalidCreateOrderItemPriceZero() {
        // Create Order Item Without Valid Order Item Quantity
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setOrderId(1);
        orderItemRequest.setProductId(1);
        orderItemRequest.setBusinessCode(1);
        orderItemRequest.setQuantity(0);
        orderItemRequest.setLastUpdated(LocalDate.of(2023, 10, 9));
        orderItemRequest.setPrice(1);

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(orderItemRequest);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testInvalidCreateOrderItemWrongCode() {
        // Create Order Item with Invalid Business Code
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setOrderId(1);
        orderItemRequest.setProductId(1);
        orderItemRequest.setBusinessCode(2);
        orderItemRequest.setQuantity(1);
        orderItemRequest.setLastUpdated(LocalDate.of(2023, 10, 9));
        orderItemRequest.setPrice(1);

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(orderItemRequest);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
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
