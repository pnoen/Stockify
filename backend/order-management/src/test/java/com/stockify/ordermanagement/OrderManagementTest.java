package com.stockify.ordermanagement;

import com.stockify.ordermanagement.constants.OrderStatus;
import com.stockify.ordermanagement.controller.OrderController;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.repository.OrderItemRepository;
import com.stockify.ordermanagement.repository.OrderRepository;
import com.stockify.ordermanagement.service.EmailService;
import com.stockify.ordermanagement.service.OrderService;
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

import java.time.LocalDate;
import java.util.*;

public class OrderManagementTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

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
    void testValidGetAllCurrentCustomerOrders() {
        int customerId = 1;

        Order orderOne = new Order();
        orderOne.setCustomerId(customerId);
        orderOne.setOrderDate(LocalDate.of(2023, 10, 9));
        orderOne.setCompletionDate(LocalDate.of(2023, 10, 9));
        orderOne.setOrderStatus(OrderStatus.COMPLETE);

        Order orderTwo = new Order();
        orderTwo.setCustomerId(customerId);
        orderTwo.setOrderDate(LocalDate.of(2023, 10, 9));
        orderTwo.setOrderStatus(OrderStatus.PACKING);

        Order orderThree = new Order();
        orderThree.setCustomerId(customerId);
        orderThree.setOrderDate(LocalDate.of(2023, 10, 9));
        orderThree.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        Order orderFour = new Order();
        orderFour.setCustomerId(customerId);
        orderFour.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFour.setOrderStatus(OrderStatus.DRAFT);

        Order orderFive = new Order();
        orderFive.setCustomerId(customerId);
        orderFive.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFive.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        List<Order> orders = Arrays.asList(orderOne, orderTwo, orderThree, orderFour, orderFive);
        List<Order> filteredOrders = Arrays.asList(orderTwo, orderThree, orderFour);

        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(orders);

        ResponseEntity<?> response = orderController.getAllCurrentCustomerOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders.get(0).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(0).getId());
        assertEquals(filteredOrders.get(1).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(1).getId());
        assertEquals(filteredOrders.get(2).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(2).getId());
    }

    @Test
    void testInvalidEmailGetAllCurrentCustomerOrders() {
        when(orderService.getUserIdByEmail("invalid@email.com")).thenReturn(0);

        ResponseEntity<?> response = orderController.getAllCurrentCustomerOrders("invalid@email.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email or user not found.", ((ApiResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testNoOrdersGetAllCurrentCustomerOrders() {
        int customerId = 1;

        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = orderController.getAllCurrentCustomerOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList());
    }

    @Test
    void testValidGetAllCompletedCustomerOrders() {
        int customerId = 1;

        Order orderOne = new Order();
        orderOne.setCustomerId(customerId);
        orderOne.setOrderDate(LocalDate.of(2023, 10, 9));
        orderOne.setCompletionDate(LocalDate.of(2023, 10, 9));
        orderOne.setOrderStatus(OrderStatus.COMPLETE);

        Order orderTwo = new Order();
        orderTwo.setCustomerId(customerId);
        orderTwo.setOrderDate(LocalDate.of(2023, 10, 9));
        orderTwo.setOrderStatus(OrderStatus.PACKING);

        Order orderThree = new Order();
        orderThree.setCustomerId(customerId);
        orderThree.setOrderDate(LocalDate.of(2023, 10, 9));
        orderThree.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        Order orderFour = new Order();
        orderFour.setCustomerId(customerId);
        orderFour.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFour.setOrderStatus(OrderStatus.DRAFT);

        Order orderFive = new Order();
        orderFive.setCustomerId(customerId);
        orderFive.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFive.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        List<Order> orders = Arrays.asList(orderOne, orderTwo, orderThree, orderFour, orderFive);
        List<Order> filteredOrders = Arrays.asList(orderOne);

        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(orders);

        ResponseEntity<?> response = orderController.getAllCompletedCustomerOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders.get(0).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(0).getId());
    }

    @Test
    void testInvalidEmailGetAllCompletedCustomerOrders() {
        when(orderService.getUserIdByEmail("invalid@email.com")).thenReturn(0);

        ResponseEntity<?> response = orderController.getAllCompletedCustomerOrders("invalid@email.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email or user not found.", ((ApiResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testNoOrdersGetAllCompletedCustomerOrders() {
        int customerId = 1;

        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = orderController.getAllCompletedCustomerOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList());
    }

    @Test
    void testValidGetAllCurrentBusinessOrders() {
        int businessCode = 11;

        Order orderOne = new Order();
        orderOne.setBusinessCode(businessCode);
        orderOne.setOrderDate(LocalDate.of(2023, 10, 9));
        orderOne.setCompletionDate(LocalDate.of(2023, 10, 9));
        orderOne.setOrderStatus(OrderStatus.COMPLETE);

        Order orderTwo = new Order();
        orderTwo.setBusinessCode(businessCode);
        orderTwo.setOrderDate(LocalDate.of(2023, 10, 9));
        orderTwo.setOrderStatus(OrderStatus.PACKING);

        Order orderThree = new Order();
        orderThree.setBusinessCode(businessCode);
        orderThree.setOrderDate(LocalDate.of(2023, 10, 9));
        orderThree.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        Order orderFour = new Order();
        orderFour.setBusinessCode(businessCode);
        orderFour.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFour.setOrderStatus(OrderStatus.DRAFT);

        Order orderFive = new Order();
        orderFive.setBusinessCode(businessCode);
        orderFive.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFive.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        List<Order> orders = Arrays.asList(orderOne, orderTwo, orderThree, orderFour, orderFive);
        List<Order> filteredOrders = Arrays.asList(orderTwo, orderThree, orderFive);

        when(orderService.getBusinessCodeByEmail("valid@email.com")).thenReturn(businessCode);
        when(orderRepository.findByBusinessCode(businessCode)).thenReturn(orders);

        ResponseEntity<?> response = orderController.getAllCurrentBusinessOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders.get(0).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(0).getId());
        assertEquals(filteredOrders.get(1).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(1).getId());
        assertEquals(filteredOrders.get(2).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(2).getId());
    }

    @Test
    void testInvalidEmailGetAllCurrentBusinessOrders() {
        when(orderService.getBusinessCodeByEmail("invalid@email.com")).thenReturn(0);

        ResponseEntity<?> response = orderController.getAllCurrentBusinessOrders("invalid@email.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email or user not found.", ((ApiResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testNoOrdersGetAllCurrentBusinessOrders() {
        int businessCode = 11;

        when(orderService.getBusinessCodeByEmail("valid@email.com")).thenReturn(businessCode);
        when(orderRepository.findByBusinessCode(businessCode)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = orderController.getAllCurrentBusinessOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList());
    }

    @Test
    void testValidGetAllCompleteBusinessOrders() {
        int businessCode = 11;

        Order orderOne = new Order();
        orderOne.setBusinessCode(businessCode);
        orderOne.setOrderDate(LocalDate.of(2023, 10, 9));
        orderOne.setCompletionDate(LocalDate.of(2023, 10, 9));
        orderOne.setOrderStatus(OrderStatus.COMPLETE);

        Order orderTwo = new Order();
        orderTwo.setBusinessCode(businessCode);
        orderTwo.setOrderDate(LocalDate.of(2023, 10, 9));
        orderTwo.setOrderStatus(OrderStatus.PACKING);

        Order orderThree = new Order();
        orderThree.setBusinessCode(businessCode);
        orderThree.setOrderDate(LocalDate.of(2023, 10, 9));
        orderThree.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        Order orderFour = new Order();
        orderFour.setBusinessCode(businessCode);
        orderFour.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFour.setOrderStatus(OrderStatus.DRAFT);

        Order orderFive = new Order();
        orderFive.setBusinessCode(businessCode);
        orderFive.setOrderDate(LocalDate.of(2023, 10, 9));
        orderFive.setOrderStatus(OrderStatus.AWAITING_SHIPMENT);

        List<Order> orders = Arrays.asList(orderOne, orderTwo, orderThree, orderFour, orderFive);
        List<Order> filteredOrders = Arrays.asList(orderOne);

        when(orderService.getBusinessCodeByEmail("valid@email.com")).thenReturn(businessCode);
        when(orderRepository.findByBusinessCode(businessCode)).thenReturn(orders);

        ResponseEntity<?> response = orderController.getAllCurrentBusinessOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders.get(0).getId(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList().get(0).getId());
    }

    @Test
    void testInvalidEmailGetAllCompletedBusinessOrders() {
        when(orderService.getBusinessCodeByEmail("invalid@email.com")).thenReturn(0);

        ResponseEntity<?> response = orderController.getAllCurrentBusinessOrders("invalid@email.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email or user not found.", ((ApiResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void testNoOrdersGetAllCompletedBusinessOrders() {
        int businessCode = 11;

        when(orderService.getBusinessCodeByEmail("valid@email.com")).thenReturn(businessCode);
        when(orderRepository.findByBusinessCode(businessCode)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = orderController.getAllCurrentBusinessOrders("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), ((OrderListResponse) Objects.requireNonNull(response.getBody())).getOrderList());
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

    @Test
    public void testValidCreateDraftOrder() {
        int customerId = 1;
        Order draftOrder = new Order();
        draftOrder.setOrderStatus(OrderStatus.DRAFT);
        draftOrder.setCustomerId(customerId);

        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByOrderStatusAndCustomerId(OrderStatus.DRAFT, customerId)).thenReturn(Optional.of(draftOrder));

        ResponseEntity<ApiResponse> response = orderController.createDraftOrder("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testInvalidCreateDraftOrder() {
        int customerId = 1;
        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByOrderStatusAndCustomerId(OrderStatus.DRAFT, customerId))
                .thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderController.createDraftOrder("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testValidGetDraftOrder() {
        int customerId = 1;
        Order draftOrder = new Order();
        draftOrder.setOrderStatus(OrderStatus.DRAFT);
        draftOrder.setCustomerId(customerId);

        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByOrderStatusAndCustomerId(OrderStatus.DRAFT, customerId)).thenReturn(Optional.of(draftOrder));

        ResponseEntity<ApiResponse> response = orderController.getDraftOrder("valid@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testInvalidGetDraftOrder() {
        int customerId = 1;
        when(orderService.getUserIdByEmail("valid@email.com")).thenReturn(customerId);
        when(orderRepository.findByOrderStatusAndCustomerId(OrderStatus.DRAFT, customerId)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderController.getDraftOrder("valid@email.com");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Shopping Cart is Empty", response.getBody().getMessage());
    }

    @Test
    void testValidUpdateDraftOrder() {
        UpdateDraftOrderRequest updateDraftOrderRequest = new UpdateDraftOrderRequest();
        updateDraftOrderRequest.setTotalCost(100.35);

        Order draftOrder = new Order();
        draftOrder.setOrderStatus(OrderStatus.DRAFT);
        draftOrder.setBusinessCode(11111);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(draftOrder));
        when(orderService.getBusinessNameByBusinessCode(11111)).thenReturn("Business Name");

        ResponseEntity<ApiResponse> response = orderController.updateDraftOrder(updateDraftOrderRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Order successfully placed.", response.getBody().getMessage());
    }

    @Test
    void testInvalidUpdateDraftOrder() {
        UpdateDraftOrderRequest updateDraftOrderRequest = new UpdateDraftOrderRequest();

        when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderController.updateDraftOrder(updateDraftOrderRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cannot place order with empty shopping cart.", response.getBody().getMessage());
    }

    @Test
    void testInvalidOrderStatus() {
        UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest();
        updateOrderStatusRequest.setOrderId(123);

        when(orderRepository.findById(123)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = orderController.updateOrderStatus(updateOrderStatusRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cannot find order.", response.getBody().getMessage());
    }
}
