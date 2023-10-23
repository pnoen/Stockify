package com.stockify.ordermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockify.ordermanagement.model.*;
import com.stockify.ordermanagement.dto.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final WebClient webClient = WebClient.create("http://localhost:8084");

    public int getUserIdByEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/account/getUserIdByEmail?email=" + email;
        return restTemplate.getForObject(url, Integer.class);
    }
    public int getBusinessCodeByEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/account/getBusinessCode?email=" + email;
        return restTemplate.getForObject(url, Integer.class);
    }

    public String getBusinessNameByBusinessCode(int businessCode) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/account/getBusinessName?businessCode=" + businessCode;
        return restTemplate.getForObject(url, String.class);
    }
    public String getUserEmailByCustomerId(int customerId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/account/getUserEmail?userId=" + customerId;
        String jsonResponse = restTemplate.getForObject(url, String.class);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(jsonResponse, Map.class);

            String email = (String) responseMap.get("email");
            return email;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public double addAllOrderItemCosts(List<OrderItem> orderItemList) {
        double total = 0.0;

        for (OrderItem o:orderItemList) {
            total += o.getPrice() * o.getQuantity();
        }

        return Double.parseDouble(String.format("%.2f", total));
    }
}