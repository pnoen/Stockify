package com.stockify.ordermanagement.controller;

import com.stockify.ordermanagement.model.Order;
import com.stockify.ordermanagement.model.OrderItem;
import com.stockify.ordermanagement.model.ProductItem;
import com.stockify.ordermanagement.repository.OrderRepository;
import com.stockify.ordermanagement.service.OrderItemService;
import com.stockify.ordermanagement.dto.*;
import com.stockify.ordermanagement.repository.OrderItemRepository;

import java.util.*;
import java.time.LocalDate;
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

    @Autowired
    private RestTemplate restTemplate;

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
            return ResponseEntity.accepted().body(new ApiResponse(202, "Order does not exist."));
        }

        if (quantity == 0 ){
            return ResponseEntity.accepted().body(new ApiResponse(202, "Sorry, Order Quantity cannot be 0."));
        }
        Order order = orderOptional.get();

        if (order.getBusinessCode() == 0) {
            order.setBusinessCode(productBusinessCode);
            orderRepository.save(order);
        } else if (order.getBusinessCode() != productBusinessCode) {
            return ResponseEntity.accepted().body(new ApiResponse(202, "Sorry, Orders cannot contain items from different businesses."));
        }

        Optional<OrderItem> existingOrderItemOpt = orderItemRepository.findByOrderIdAndProductId(orderId, productId);

        if (existingOrderItemOpt.isPresent()) {
            OrderItem existingOrderItem = existingOrderItemOpt.get();
            existingOrderItem.setQuantity(existingOrderItem.getQuantity() + quantity);
            existingOrderItem.setLastUpdated(lastUpdated);
            existingOrderItem.setPrice(price);

            orderItemService.updateOrderTotalCost(existingOrderItem);
            orderItemRepository.save(existingOrderItem);
        } else {
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrderId(orderId);
            newOrderItem.setProductId(productId);
            newOrderItem.setQuantity(quantity);
            newOrderItem.setLastUpdated(lastUpdated);
            newOrderItem.setPrice(price);

            orderItemService.updateOrderTotalCost(newOrderItem);
            orderItemRepository.save(newOrderItem);
        }

        GetProductResponse getProductResponse = orderItemService.fetchProduct(orderItemRequest.getProductId());

        if (getProductResponse != null) {
            int newQuantity = getProductResponse.getQuantity() - orderItemRequest.getQuantity();
            orderItemService.updateProductQuantity(orderItemRequest.getProductId(), newQuantity);
        }

        return ResponseEntity.ok(new ApiResponse(200, "Order item processed successfully."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteOrderItem(@RequestBody OrderItemIdRequest orderItemIdRequest) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemIdRequest.getOrderItemId());

        if (orderItemOptional.isPresent()) {
            OrderItem orderItemToDelete = orderItemOptional.get();

            GetProductResponse getProductResponse = orderItemService.fetchProduct(orderItemToDelete.getProductId());
            int newQuantity = getProductResponse.getQuantity() + orderItemToDelete.getQuantity();
            orderItemService.updateProductQuantity(orderItemToDelete.getProductId(), newQuantity);
            int associatedOrderId = orderItemToDelete.getOrderId();
            orderItemRepository.deleteById(orderItemIdRequest.getOrderItemId());
            List<OrderItem> remainingOrderItems = orderItemRepository.findAllByOrderId(associatedOrderId);
            if (remainingOrderItems.isEmpty()) {
                orderRepository.deleteById(associatedOrderId);
            }

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

            if (productIds.isEmpty()) {
                ProductItemListResponse finalResponse = new ProductItemListResponse(HttpStatus.OK.value(), new ArrayList<>());
                return ResponseEntity.ok(finalResponse);
            }

            String productUrl = "http://localhost:8083/api/product/getProducts?ids=" + productIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            ProductItemListResponse fetchedProductItemListResponse = restTemplate.getForObject(productUrl, ProductItemListResponse.class);

            if (fetchedProductItemListResponse != null) {
                for (ProductItem product : fetchedProductItemListResponse.getProducts()) {
                    for (OrderItem orderItem : orderItems) {
                        if (orderItem.getProductId() == product.getId()) {
                            product.setQuantity(orderItem.getQuantity());
                            product.setOrderItemId(orderItem.getId());
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
