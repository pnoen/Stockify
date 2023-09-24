package com.stockify.ordermanagement.service;

import com.stockify.ordermanagement.model.*;
import com.stockify.ordermanagement.dto.*;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final WebClient webClient = WebClient.create("http://localhost:8081");
    public double calculateTotalCost(int orderId) {
        ResponseEntity<OrderItemListResponse> responseEntity = webClient.get()
                .uri("/order/getAllByOrderId?orderId=" + orderId)
                .retrieve()
                .toEntity(OrderItemListResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderItemListResponse orderItemListResponse = responseEntity.getBody();

            if (orderItemListResponse != null && orderItemListResponse.getOrderItem() != null) {
                List<OrderItem> orderItemList = orderItemListResponse.getOrderItem();

                return addAllOrderItemCosts(orderItemList);
            }

            return -1;
        }

        return -1;
    }

    public double addAllOrderItemCosts(List<OrderItem> orderItemList) {
        double total = 0.0;

        for (OrderItem o:orderItemList) {
            total += o.getPrice();
        }

        return total;
    }
}