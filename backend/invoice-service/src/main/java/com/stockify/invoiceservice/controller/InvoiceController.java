package com.stockify.invoiceservice.controller;

import com.stockify.invoiceservice.dto.ApiResponse;
import com.stockify.invoiceservice.dto.InvoiceIdRequest;
import com.stockify.invoiceservice.dto.InvoiceRequest;
import com.stockify.invoiceservice.model.Invoice;
import com.stockify.invoiceservice.repository.InvoiceRepository;
import com.stockify.invoiceservice.service.InvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;
    private InvoiceService invoiceService = new InvoiceService();

     @PostMapping("/create")
     public ResponseEntity<ApiResponse> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
         int orderId = invoiceRequest.getOrderId();
         String invoiceContent = invoiceService.generateInvoiceContent(orderId);

         Invoice newInvoice = new Invoice();
         newInvoice.setOrderId(orderId);
         newInvoice.setInvoiceContent(invoiceContent);

         invoiceRepository.save(newInvoice);

         return ResponseEntity.ok(new ApiResponse(200, "Invoice created successfully."));
     }

     @DeleteMapping("/delete")
     public ResponseEntity<ApiResponse> deleteInvoice(@RequestBody InvoiceIdRequest invoiceIdRequest) {
         Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceIdRequest.getInvoiceId());

         if (invoiceOptional.isPresent()) {
             invoiceRepository.deleteById(invoiceIdRequest.getInvoiceId());
             return ResponseEntity.ok(new ApiResponse(200, "Invoice deleted successfully."));
         } else {
             return ResponseEntity.badRequest().body(new ApiResponse(404, "Invoice does not exist."));
         }
     }
}