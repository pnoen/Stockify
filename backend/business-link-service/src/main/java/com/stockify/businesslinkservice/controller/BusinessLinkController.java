package com.stockify.businesslinkservice.controller;

import com.stockify.businesslinkservice.dto.CreateLinkRequest;
import com.stockify.businesslinkservice.service.BusinessLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.status(HttpStatus.OK).body("");
    }


}
