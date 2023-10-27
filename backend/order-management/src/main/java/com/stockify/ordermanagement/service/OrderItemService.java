package com.stockify.ordermanagement.service;

import com.stockify.ordermanagement.model.*;
import com.stockify.ordermanagement.dto.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemService {

    private final WebClient webClient = WebClient.create("http://localhost:8084");

    public GetProductResponse fetchProduct(int productId) {
        RestTemplate restTemplate = new RestTemplate();
        String getProductUrl = "http://localhost:8083/api/product/get?id=" + productId;
        return restTemplate.getForObject(getProductUrl, GetProductResponse.class);
    }

    public void updateOrderTotalCost(OrderItem orderItem) {
        OrderCostUpdateRequest request = new OrderCostUpdateRequest(orderItem.getOrderId(), orderItem.getPrice() * orderItem.getQuantity());

        ResponseEntity<ApiResponse> responseEntity = webClient.post()
                .uri("/api/order/updateTotalCost")
                .bodyValue(request)
                .retrieve()
                .toEntity(ApiResponse.class)
                .block();
    }

    public double addAllOrderItemCosts(List<OrderItem> orderItemList) {
        double total = 0.0;

        for (OrderItem o:orderItemList) {
            total += o.getPrice();
        }

        return total;
    }

    // Update product quantity using RestTemplate
    public void updateProductQuantity(int productId, int newQuantity) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id", productId);
        requestMap.put("quantity", newQuantity);
        requestMap.put("name", "");
        requestMap.put("description", "");
        requestMap.put("imageUrl", "");
        requestMap.put("price", 0);

        RestTemplate restTemplate = new RestTemplate();
        String editProductUrl = "http://localhost:8083/api/product/edit";
        restTemplate.postForEntity(editProductUrl, requestMap, ApiResponse.class);
    }
}
