package com.stockify.businesslinkservice.controller;

import com.stockify.businesslinkservice.dto.*;
import com.stockify.businesslinkservice.service.BusinessLinkService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/businessLink")
@RequiredArgsConstructor
public class BusinessLinkController {

    private final BusinessLinkService businessLinkService;

    @PostMapping("/createLink")
    public ResponseEntity<String> createLink(@RequestBody LinkRequest linkRequest) {
        String msg = businessLinkService.createLink(linkRequest);
        if (msg != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<GetCustomersResponse> getCustomers(@RequestParam int businessCode) {
        List<CustomerDto> customers = businessLinkService.getCustomers(businessCode);

        if (customers == null || customers.isEmpty()) {
            GetCustomersResponse res = GetCustomersResponse.builder()
                    .customers(new ArrayList<>())
                    .message("No customers found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        GetCustomersResponse res = GetCustomersResponse.builder()
                .customers(customers)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/getBusinesses")
    public ResponseEntity<GetBusinessesResponse> getBusinesses(@RequestParam int customerId) {
        List<BusinessDto> businesses = businessLinkService.getBusinesses(customerId);

        if (businesses == null || businesses.isEmpty()) {
            GetBusinessesResponse res = GetBusinessesResponse.builder()
                    .businesses(new ArrayList<>())
                    .message("No businesses found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        GetBusinessesResponse res = GetBusinessesResponse.builder()
                .businesses(businesses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/removeLink")
    @Transactional
    public ResponseEntity<String> removeLink(@RequestBody LinkRequest linkRequest) {
        String msg = businessLinkService.removeLink(linkRequest);
        if (msg != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }


}
