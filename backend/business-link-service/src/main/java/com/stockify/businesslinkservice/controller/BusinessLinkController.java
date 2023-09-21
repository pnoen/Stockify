package com.stockify.businesslinkservice.controller;

import com.stockify.businesslinkservice.dto.CreateLinkRequest;
import com.stockify.businesslinkservice.dto.GetCustomerResponse;
import com.stockify.businesslinkservice.service.BusinessLinkService;
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
    public ResponseEntity<String> createLink(@RequestBody CreateLinkRequest createLinkRequest) {
        String msg = businessLinkService.createLink(createLinkRequest);
        if (msg != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<GetCustomerResponse> getCustomers(@RequestParam int businessCode) {
        List<Integer> customers = businessLinkService.getCustomers(businessCode);

        if (customers.isEmpty()) {
            GetCustomerResponse res = GetCustomerResponse.builder()
                    .customers(new ArrayList<>())
                    .message("No customers found.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        GetCustomerResponse res = GetCustomerResponse.builder()
                .customers(customers)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


}
