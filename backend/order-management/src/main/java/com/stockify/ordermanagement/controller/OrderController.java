package com.stockify.ordermanagement.controller;

import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.repository.OrderRepository;
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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        int organisation = orderRequest.getOrganisation();
        int customerId = orderRequest.getCustomerId();
        int supplierId = orderRequest.getSupplierId();
        int invoiceId = orderRequest.getInvoiceId();
        LocalDate orderDate = orderRequest.getOrderDate();
        LocalDate completionDate = orderRequest.getCompletionDate();

        Order newOrder = new Order();
        newOrder.setOrganisation(organisation);
        newOrder.setCustomerId(customerId);
        newOrder.setSupplierId(supplierId);
        newOrder.setInvoiceId(invoiceId);
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
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();

        Iterable<Order> orders = orderRepository.findAll();

        for (Order o:orders) {
            orderList.add(o);
        }

        return orderList;
    }

    // Get an order by its ID
    @GetMapping("/getOrderById")
    public Order getOrderById(@RequestBody OrderIdRequest orderIdRequest) {
        Optional<Order> orderOptional = orderRepository.findById(orderIdRequest.getOrderId());

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return order;
        } else {
            return null;
        }
    }
}