package com.stockify.invoiceservice.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.stockify.invoiceservice.model.*;
import com.stockify.invoiceservice.dto.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final WebClient webClient = WebClient.create("http://localhost:8081");

    public String generateInvoiceContent(int orderId) {

        Order order = getOrder(orderId);
        List<OrderItem> orderItemList = getOrderItem(orderId);

        if (order != null) {
            return generateInvoiceString(order, orderItemList);
        }

        return "failed";
    }

    public Order getOrder(int orderId) {
        ResponseEntity<OrderResponse> responseEntity = webClient.get()
                .uri("/order/getOrderById?orderId=" + orderId)
                .retrieve()
                .toEntity(OrderResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderResponse orderResponse = responseEntity.getBody();

            if (orderResponse != null && orderResponse.getOrder() != null) {
                Order order = orderResponse.getOrder();

                return order;
            }
        }

        return null;
    }

    public List<OrderItem> getOrderItem(int orderId) {
        ResponseEntity<OrderItemListResponse> responseEntity = webClient.get()
                .uri("/orderItem/getAllByOrderId?orderId=" + orderId)
                .retrieve()
                .toEntity(OrderItemListResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            OrderItemListResponse orderItemListResponse = responseEntity.getBody();

            if (orderItemListResponse != null && orderItemListResponse.getOrderItem() != null) {
                List<OrderItem> orderItemList = orderItemListResponse.getOrderItem();

                return orderItemList;
            }
        }

        return null;
    }

    public String generateInvoiceString(Order order, List<OrderItem> orderItemList) {
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
        sb.append("Customer ID: ");
        String orderDate = order.getOrderDate().format(formatter);
        sb.append(orderDate);
        sb.append("\n");

        // Line 8: Order completion date
        sb.append("Customer ID: ");
        String completionDate = order.getCompletionDate().format(formatter);
        sb.append(completionDate);
        sb.append("\n");

        // Line 9: Empty
        sb.append("\n");

        // Line 10: Total cost
        sb.append("Total cost: ");
        String totalCost = String.valueOf(order.getTotalCost());
        sb.append(totalCost);
        sb.append("\n");

        // Line 11: Empty
        sb.append("\n");

        // List of all items
        for (OrderItem o:orderItemList) {
            sb.append("Product ID: ");
            String itemId = String.valueOf(o.getId());
            sb.append(itemId);
            sb.append("     Product Price: ");
            String itemPrice = String.valueOf(o.getPrice());
            sb.append("\n");
        }

        sb.append("\n");

        // Final line: Total cost again
        sb.append("Total cost: ");
        sb.append(totalCost);
        sb.append("\n");

        return sb.toString();
    }
}