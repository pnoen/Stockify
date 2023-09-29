package com.stockify.invoiceservice.controller;

import com.stockify.invoiceservice.dto.ApiResponse;
import com.stockify.invoiceservice.dto.InvoiceIdRequest;
import com.stockify.invoiceservice.model.Invoice;
import com.stockify.invoiceservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> send(@RequestBody InvoiceIdRequest invoiceIdRequest) throws IOException {

        Invoice invoice = emailService.getInvoiceById(invoiceIdRequest.getInvoiceId());

        if (emailService.sendTextEmail(invoice)) {
            return ResponseEntity.ok(new ApiResponse(200, "Invoice emailed successfully."));
        }
        return ResponseEntity.badRequest().body(new ApiResponse(404, "Invoice could not be emailed."));
    }
}