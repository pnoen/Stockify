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
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/businessLink")
@RequiredArgsConstructor
public class BusinessLinkController {

    private final BusinessLinkService businessLinkService;

    @PostMapping("/createBusinessLink")
    public ResponseEntity<String> createBusinessLink(@RequestBody BusinessLinkRequest businessLinkRequest) {
        String msg = businessLinkService.createBusinessLink(businessLinkRequest);
        if (msg != null) {
            if (msg.equals("Unable to find user.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            }
            else if (msg.equals("Unable to find business.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @PostMapping("createUserLink")
    public ResponseEntity<String> createUserLink(@RequestBody UserLinkRequest userLinkRequest) {
        String msg = businessLinkService.createUserLink(userLinkRequest);
        if (msg != null) {
            if (msg.equals("Unable to find user.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @GetMapping("/getUsers")
    public ResponseEntity<GetUsersResponse> getUsers(@RequestParam String email) {
        List<UserDto> users = businessLinkService.getUsers(email);

        if (users == null || users.isEmpty()) {
            GetUsersResponse res = GetUsersResponse.builder()
                    .users(new ArrayList<>())
                    .message("No users found.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        GetUsersResponse res = GetUsersResponse.builder()
                .users(users)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/getBusinesses")
    public ResponseEntity<GetBusinessesResponse> getBusinesses(@RequestParam String email) {
        List<BusinessDto> businesses = businessLinkService.getBusinesses(email);

        if (businesses == null || businesses.isEmpty()) {
            GetBusinessesResponse res = GetBusinessesResponse.builder()
                    .businesses(new ArrayList<>())
                    .message("No businesses found.")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        GetBusinessesResponse res = GetBusinessesResponse.builder()
                .businesses(businesses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/removeUserLink")
    @Transactional
    public ResponseEntity<String> removeUserLink(@RequestBody RemoveUserLinkRequest removeUserLinkRequest) {
        String msg = businessLinkService.removeUserLink(removeUserLinkRequest);
        if (msg != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @DeleteMapping("/removeBusinessLink")
    @Transactional
    public ResponseEntity<String> removeBusinessLink(@RequestBody BusinessLinkRequest removeBusinessLinkRequest) {
        String msg = businessLinkService.removeBusinessLink(removeBusinessLinkRequest);
        if (msg != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }


}
