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
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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
    public void testCreateOrderItem_Success() {
        Order mockOrder = new Order();
        mockOrder.setBusinessCode(1);

        OrderItemRequest mockRequest = new OrderItemRequest();
        mockRequest.setOrderId(1);
        mockRequest.setProductId(1);
        mockRequest.setBusinessCode(1);
        mockRequest.setQuantity(1);
        mockRequest.setLastUpdated(LocalDate.of(2023, 10, 9));
        mockRequest.setPrice(1);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(mockOrder));
        when(orderItemRepository.findByOrderIdAndProductId(anyInt(), anyInt())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order item processed successfully.", response.getBody().getMessage());
    }

    @Test
    public void testCreateOrderItem_OrderDoesNotExist() {
        OrderItemRequest mockRequest = new OrderItemRequest();
        mockRequest.setOrderId(100);
        mockRequest.setBusinessCode(1);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(mockRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Order does not exist.", response.getBody().getMessage());
    }

    @Test
    public void testCreateOrderItem_ZeroQuantity() {
        OrderItemRequest mockRequest = new OrderItemRequest();
        mockRequest.setQuantity(0);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(new Order()));

        ResponseEntity<ApiResponse> response = orderItemController.createOrderItem(mockRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Sorry, Order Quantity cannot be 0.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testDeleteOrderItem_Success() {
        OrderItem mockOrderItem = new OrderItem();

        GetProductResponse mockProductResponse = new GetProductResponse();
        mockProductResponse.setQuantity(10);
        doReturn(mockProductResponse).when(orderItemService).fetchProduct(anyInt());

        doNothing().when(orderItemService).updateOrderTotalCost(any(OrderItem.class));

        doNothing().when(orderItemService).updateProductQuantity(anyInt(), anyInt());

        doNothing().when(orderItemRepository).deleteById(anyInt());

        when(orderItemRepository.findById(anyInt())).thenReturn(Optional.of(mockOrderItem));

        OrderItemIdRequest deleteRequest = new OrderItemIdRequest();
        deleteRequest.setOrderItemId(1);

        ResponseEntity<ApiResponse> response = orderItemController.deleteOrderItem(deleteRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order Item deleted successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testDeleteOrderItem_ItemDoesNotExist() {
        when(orderItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        OrderItemIdRequest deleteRequest = new OrderItemIdRequest();
        deleteRequest.setOrderItemId(999); // Non-existent ID

        ResponseEntity<ApiResponse> response = orderItemController.deleteOrderItem(deleteRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Order Item does not exist.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testDeleteOrderItem_ServerError() {
        when(orderItemRepository.findById(anyInt())).thenAnswer(invocation -> {
            throw new RuntimeException("Database server error"); // Simulate a database/server failure
        });

        OrderItemIdRequest deleteRequest = new OrderItemIdRequest();
        deleteRequest.setOrderItemId(1);

        assertThrows(
                RuntimeException.class,
                () -> orderItemController.deleteOrderItem(deleteRequest),
                "Database server error"
        );
    }

    // Still need to test getAllOrders, want to refactor the code so that RestTemplate is injected and not instantiated in the method.

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
