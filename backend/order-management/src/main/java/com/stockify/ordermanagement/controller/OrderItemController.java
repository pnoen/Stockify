package com.stockify.ordermanagement.controller;

import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.model.OrderItem;
import com.stockify.ordermanagement.model.ProductItem;
import com.stockify.ordermanagement.repository.OrderRepository;
import com.stockify.ordermanagement.service.OrderItemService;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.repository.OrderItemRepository;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orderItem")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    private OrderItemService orderItemService = new OrderItemService();

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        int orderId = orderItemRequest.getOrderId();
        int productId = orderItemRequest.getProductId();
        int productBusinessCode = orderItemRequest.getBusinessCode();
        int quantity = orderItemRequest.getQuantity();
        LocalDate lastUpdated = orderItemRequest.getLastUpdated();
        double price = Double.parseDouble(String.format("%.2f", orderItemRequest.getPrice()));
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Order does not exist."));
        }
        Order order = orderOptional.get();

        // Check businessCode logic
        if (order.getBusinessCode() == 0) {
            order.setBusinessCode(productBusinessCode);
            orderRepository.save(order);
        } else if (order.getBusinessCode() != productBusinessCode) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Sorry, Orders cannot contain items from different businesses."));
        }

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setOrderId(orderId);
        newOrderItem.setProductId(productId);
        newOrderItem.setQuantity(quantity);
        newOrderItem.setLastUpdated(lastUpdated);
        newOrderItem.setPrice(price);

        orderItemService.updateOrderTotalCost(newOrderItem);
        orderItemRepository.save(newOrderItem);

        return ResponseEntity.ok(new ApiResponse(200, "Order Item created successfully."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteOrderItem(@RequestBody OrderItemIdRequest orderItemIdRequest) {
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
    public ResponseEntity<ProductItemListResponse> getAllOrders(@RequestParam int orderId) {
        try {
            List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
            List<Integer> productIds = orderItems.stream()
                    .map(OrderItem::getProductId)
                    .collect(Collectors.toList());

            String productUrl = "http://localhost:8083/api/product/getProducts?ids=" + productIds.stream().map(String::valueOf).collect(Collectors.joining(","));

            ProductItemListResponse fetchedProductItemListResponse = new RestTemplate().getForObject(productUrl, ProductItemListResponse.class);

            if (fetchedProductItemListResponse != null) {
                for (ProductItem product : fetchedProductItemListResponse.getProducts()) {
                    for (OrderItem orderItem : orderItems) {
                        if (orderItem.getProductId() == product.getId()) {
                            product.setQuantity(orderItem.getQuantity());  // set ordered quantity
                            break;
                        }
                    }
                }

                ProductItemListResponse finalResponse = new ProductItemListResponse(HttpStatus.OK.value(), fetchedProductItemListResponse.getProducts());

                return ResponseEntity.ok(finalResponse);
            } else {
                ProductItemListResponse errorResponse = new ProductItemListResponse(HttpStatus.NOT_FOUND.value(), Collections.emptyList());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            ProductItemListResponse errorResponse = new ProductItemListResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    // Get an order item by the orderItem ID
    @GetMapping("/getOrderItemById")
    public ResponseEntity<OrderItemResponse> getOrderById(@RequestParam int orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);

        if (orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
            return ResponseEntity.ok(new OrderItemResponse(200, orderItem));
        } else {
            return ResponseEntity.ok(new OrderItemResponse(404, null));
        }
    }
}