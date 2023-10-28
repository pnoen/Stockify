package com.stockify.ordermanagement;

import com.stockify.ordermanagement.controller.OrderItemController;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.model.OrderItem;
import com.stockify.ordermanagement.model.ProductItem;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

public class OrderItemManagementTest {

    @InjectMocks
    private OrderItemController orderItemController;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private RestTemplate restTemplate;

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

    @Test
    public void testGetAllOrdersSuccess() {
        int orderId = 1;

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(3);

        orderItem1.setProductId(1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(5);

        orderItem2.setProductId(2);

        List<OrderItem> orderItemList = Arrays.asList(orderItem1, orderItem2);

        when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(orderItemList);

        ProductItem product1 = new ProductItem();
        product1.setId(1);

        ProductItem product2 = new ProductItem();
        product2.setId(2);

        ProductItemListResponse productResponse = new ProductItemListResponse();
        productResponse.setStatusCode(200);
        productResponse.setProducts(Arrays.asList(product1, product2));

        when(restTemplate.getForObject(anyString(), eq(ProductItemListResponse.class)))
                .thenReturn(productResponse);

        ResponseEntity<ProductItemListResponse> response = orderItemController.getAllOrders(orderId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        ProductItemListResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(200, responseBody.getStatusCode());
        assertEquals(2, responseBody.getProducts().size());
    }

    @Test
    public void testGetAllOrders_NoOrderItemsFound() {
        int orderId = 1;

        when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(Collections.emptyList());

        ResponseEntity<ProductItemListResponse> response = orderItemController.getAllOrders(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        ProductItemListResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.getProducts().isEmpty());
    }

    @Test
    public void testGetAllOrders_ExternalServiceFailure() {
        int orderId = 1;

        List<OrderItem> mockOrderItems = new ArrayList<>();
        mockOrderItems.add(new OrderItem());
        when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(mockOrderItems);

        // Simulating an external service failure
        when(restTemplate.getForObject(anyString(), eq(ProductItemListResponse.class)))
                .thenThrow(new RestClientException("External service failed"));

        ResponseEntity<ProductItemListResponse> response = orderItemController.getAllOrders(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());

        ProductItemListResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.getStatusCode());
    }

    @Test
    public void testGetAllOrders_ExceptionOccurred() {
        int orderId = 1;

        // Simulating a database/unknown error
        when(orderItemRepository.findAllByOrderId(anyInt())).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ProductItemListResponse> response = orderItemController.getAllOrders(orderId);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());

        ProductItemListResponse responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseBody.getStatusCode());
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