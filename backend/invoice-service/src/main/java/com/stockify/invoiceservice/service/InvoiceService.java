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
    private final WebClient webClientOrder = WebClient.create("http://localhost:8081");

    public boolean updateInvoiceIdInOrder(int orderId, int invoiceId) {
        InvoiceIdInOrderRequest invoiceIdInOrderRequest = new InvoiceIdInOrderRequest(invoiceId, orderId);

        ResponseEntity<BooleanResponse> responseEntity = webClientOrder.post()
                .uri("/order/setInvoiceIdToOrder")
                .bodyValue(invoiceIdInOrderRequest)
                .retrieve()
                .toEntity(BooleanResponse.class)
                .block();

        if (responseEntity != null && responseEntity.hasBody()) {
            BooleanResponse booleanResponse = responseEntity.getBody();

            if (booleanResponse != null) {
                return booleanResponse.getSuccess();
            }
        }

        return false;
    }
}