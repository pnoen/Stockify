package com.stockify.ordermanagement.controller;

import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.repository.OrderRepository;
import com.stockify.ordermanagement.service.OrderService;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    private OrderService orderService = new OrderService();

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        int organisation = orderRequest.getOrganisation();
        int customerId = orderRequest.getCustomerId();
        int supplierId = orderRequest.getSupplierId();
        LocalDate orderDate = orderRequest.getOrderDate();
        LocalDate completionDate = orderRequest.getCompletionDate();

        Order newOrder = new Order();
        newOrder.setOrganisation(organisation);
        newOrder.setCustomerId(customerId);
        newOrder.setSupplierId(supplierId);
        newOrder.setOrderDate(orderDate);
        newOrder.setCompletionDate(completionDate);

        orderRepository.save(newOrder);

        return ResponseEntity.ok(new ApiResponse(200, "Order created successfully."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@RequestBody OrderIdRequest orderIdRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderIdRequest.getOrderId());

        if (orderOptional.isPresent()) {
            orderRepository.deleteById(orderIdRequest.getOrderId());
            return ResponseEntity.ok(new ApiResponse(200, "Order deleted successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(404, "Order does not exist."));
        }
    }

    // Get a list of all the orders
    @GetMapping("/getAll")
    public ResponseEntity<OrderListResponse> getAllOrders() {
        List<Order> orderList = new ArrayList<>();

        Iterable<Order> orders = orderRepository.findAll();

        for (Order o:orders) {
            orderList.add(o);
        }

        return ResponseEntity.ok(new OrderListResponse(200, orderList));
    }

    // Get an order by its ID
    @GetMapping("/getOrderById")
    public ResponseEntity<OrderResponse> getOrderById(@RequestParam int orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return ResponseEntity.ok(new OrderResponse(200, order));
        } else {
            return ResponseEntity.ok(new OrderResponse(404, null));
        }
    }

    // Update Total Cost
    @PostMapping("/updateTotalCost")
    public ResponseEntity<ApiResponse> updateTotalCost(@RequestBody OrderCostUpdateRequest orderCostUpdateRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderCostUpdateRequest.getOrderId());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            double newTotalCost = Double.parseDouble(String.format("%.2f", (order.getTotalCost() + orderCostUpdateRequest.getPrice()))); // Replace with the new value you want
            order.setTotalCost(newTotalCost);

            orderRepository.save(order);

            return ResponseEntity.ok(new ApiResponse(200, "Order total cost edited successfully."));
        } else {
            return ResponseEntity.ok(new ApiResponse(404, "Order does not exist"));
        }
    }
    
    @PostMapping("setInvoiceIdToOrder")
    public ResponseEntity<BooleanResponse> setInvoiceIdToOrder(@RequestBody InvoiceIdRequest invoiceIdRequest) {
        Optional<Order> orderOptional = orderRepository.findById(invoiceIdRequest.getOrderId());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            order.setInvoiceId(invoiceIdRequest.getInvoiceId());

            orderRepository.save(order);

            return ResponseEntity.ok(new BooleanResponse(200, true));
        } else {
            return ResponseEntity.ok(new BooleanResponse(404, false));
        }
    }
}