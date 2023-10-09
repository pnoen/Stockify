package com.stockify.ordermanagement;

import com.stockify.ordermanagement.controller.OrderController;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.repository.OrderRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderManagementTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
    }

    @Test
    public void testCreateOrder() {
        OrderRequest validRequest = new OrderRequest();
        validRequest.setOrganisation(1);
        validRequest.setCustomerId(1);
        validRequest.setSupplierId(1);
        validRequest.setOrderDate(LocalDate.of(2023, 10, 9));
        validRequest.setCompletionDate(LocalDate.of(2023, 10, 9));

        ResponseEntity<ApiResponse> response = orderController.createOrder(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order created successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testValidDeleteOrder() {
        OrderIdRequest validRequest = new OrderIdRequest();
        validRequest.setOrderId(anyInt());

        Order order = new Order();
        Optional<Order> optionalOrder = Optional.of(order);

        when(orderRepository.findById(validRequest.getOrderId())).thenReturn(optionalOrder);

        ResponseEntity<ApiResponse> response = orderController.deleteOrder(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order deleted successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testInvalidDeleteOrder() {
        OrderIdRequest validRequest = new OrderIdRequest();
        validRequest.setOrderId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderController.deleteOrder(validRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Order does not exist.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testGetAll() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> mockOrderList = new ArrayList<>();
        mockOrderList.add(order1);
        mockOrderList.add(order2);

        when(orderRepository.findAll()).thenReturn(mockOrderList);

        ResponseEntity<OrderListResponse> response = orderController.getAllOrders();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockOrderList, response.getBody().getOrder());
    }

    @Test
    public void getValidOrderById() {
        Order order = new Order();
        Optional<Order> optionalOrder = Optional.of(order);

        when(orderRepository.findById(1)).thenReturn(optionalOrder);

        ResponseEntity<OrderResponse> response = orderController.getOrderById(1);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getInvalidOrderById() {
        when(orderRepository.findById(2)).thenReturn(Optional.empty());

        ResponseEntity<OrderResponse> response = orderController.getOrderById(2);

        assertEquals(null, response.getBody().getOrder());
    }

    @Test
    public void testValidUpdateTotalCost() {
        Order order = new Order();

        OrderCostUpdateRequest orderCostUpdateRequest = new OrderCostUpdateRequest(order.getId(), 1.0);

        Optional<Order> optionalOrder = Optional.of(order);

        when(orderRepository.findById(order.getId())).thenReturn(optionalOrder);

        ResponseEntity<ApiResponse> response = orderController.updateTotalCost(orderCostUpdateRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order total cost edited successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testInvalidUpdateTotalCost() {
        OrderCostUpdateRequest orderCostUpdateRequest = new OrderCostUpdateRequest(1, 1.0);

        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderController.updateTotalCost(orderCostUpdateRequest);

        assertEquals("Order does not exist", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testValidSetInvoiceIdToOrder() {
        Order order = new Order();

        InvoiceIdRequest invoiceIdRequest = new InvoiceIdRequest();
        invoiceIdRequest.setInvoiceId(1);
        invoiceIdRequest.setOrderId(order.getId());

        Optional<Order> optionalOrder = Optional.of(order);

        when(orderRepository.findById(order.getId())).thenReturn(optionalOrder);

        ResponseEntity<BooleanResponse> response = orderController.setInvoiceIdToOrder(invoiceIdRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody().getSuccess());
    }

    @Test
    public void testInvalidSetInvoiceIdToOrder() {
        InvoiceIdRequest invoiceIdRequest = new InvoiceIdRequest();
        invoiceIdRequest.setInvoiceId(1);
        invoiceIdRequest.setOrderId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<BooleanResponse> response = orderController.setInvoiceIdToOrder(invoiceIdRequest);

        assertEquals(false, response.getBody().getSuccess());
    }
}
