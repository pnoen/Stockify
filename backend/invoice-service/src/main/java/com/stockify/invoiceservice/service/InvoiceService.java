package com.stockify.invoiceservice.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.stockify.invoiceservice.model.Order;
import com.stockify.invoiceservice.dto.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final WebClient webClient = WebClient.create("http://localhost:8081");

    public String generateInvoiceContent(int orderId) {
        ResponseEntity<OrderResponse> responseEntity = webClient.get()
                .uri("/order/getOrderById?orderId=" + orderId)
                .retrieve()
                .toEntity(OrderResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderResponse orderResponse = responseEntity.getBody();

            if (orderResponse != null && orderResponse.getOrder() != null) {
                Order order = orderResponse.getOrder();

                return generateInvoiceString(order);
            }

            return "Order doesn't exist";
        }

        return "Something went wrong";
    }

    public String generateInvoiceString(Order order) {
        StringBuilder sb = new StringBuilder();

        // Line 1: Order ID
        sb.append("Order ID: ");
        sb.append(order.getId());
        sb.append("\n");

        // Line 2: Empty
        sb.append("\n");

        // Line 3: Customer ID
        sb.append("Customer ID: ");
        sb.append(order.getCustomerId());
        sb.append("\n");

        // Line 4: Supplier ID
        sb.append("Supplier ID: ");
        sb.append(order.getSupplierId());
        sb.append("\n");

        // Line 5: Organisation ID
        sb.append("Organisation ID: ");
        sb.append(order.getOrganisation());
        sb.append("\n");

        // Line 6: Empty
        sb.append("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Line 7: Order date
        sb.append("Order Date: ");
        String orderDate = order.getOrderDate().format(formatter);
        sb.append(orderDate);
        sb.append("\n");

        // Line 8: Order completion date
        sb.append("Completion Date: ");
        String completionDate = order.getCompletionDate().format(formatter);
        sb.append(completionDate);
        sb.append("\n");

        return sb.toString();
    }
}