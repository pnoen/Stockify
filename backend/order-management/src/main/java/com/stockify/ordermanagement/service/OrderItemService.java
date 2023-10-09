package com.stockify.ordermanagement.service;

import com.stockify.ordermanagement.model.*;
import com.stockify.ordermanagement.dto.*;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    private final WebClient webClient = WebClient.create("http://localhost:8081");
    public void updateOrderTotalCost(OrderItem orderItem) {
        OrderCostUpdateRequest request = new OrderCostUpdateRequest(orderItem.getOrderId(), orderItem.getPrice() * orderItem.getQuantity());

        ResponseEntity<ApiResponse> responseEntity = webClient.post()
                .uri("/order/updateTotalCost")
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
}