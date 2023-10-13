package com.stockify.businesslinkservice.controller;

import com.stockify.businesslinkservice.dto.*;
import com.stockify.businesslinkservice.service.BusinessLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class BusinessLinkControllerTests {
    @Mock
    private BusinessLinkService businessLinkService;

    @InjectMocks
    private BusinessLinkController businessLinkController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void validCreateBusinessLink() {
        when(businessLinkService.createBusinessLink(any(BusinessLinkRequest.class))).thenReturn(null);

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        ResponseEntity<String> result = businessLinkController.createBusinessLink(businessLinkRequest);

        assertEquals("", result.getBody());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    public void invalidUserCreateBusinessLink() {
        when(businessLinkService.createBusinessLink(any(BusinessLinkRequest.class))).thenReturn("Unable to find user.");

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        ResponseEntity<String> result = businessLinkController.createBusinessLink(businessLinkRequest);

        assertEquals("Unable to find user.", result.getBody());
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void existingLinkCreateBusinessLink() {
        when(businessLinkService.createBusinessLink(any(BusinessLinkRequest.class))).thenReturn("Link already exists between business and user.");

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        ResponseEntity<String> result = businessLinkController.createBusinessLink(businessLinkRequest);

        assertEquals("Link already exists between business and user.", result.getBody());
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void validCreateUserLink() {
        when(businessLinkService.createUserLink(any(UserLinkRequest.class))).thenReturn(null);

        UserLinkRequest userLinkRequest = new UserLinkRequest(111111, "john", "smith", "john@mail.com");
        ResponseEntity<String> result = businessLinkController.createUserLink(userLinkRequest);

        assertEquals("", result.getBody());
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    public void invalidUserCreateUserLink() {
        when(businessLinkService.createUserLink(any(UserLinkRequest.class))).thenReturn("Unable to find user.");

        UserLinkRequest userLinkRequest = new UserLinkRequest(111111, "john", "smith", "john@mail.com");
        ResponseEntity<String> result = businessLinkController.createUserLink(userLinkRequest);

        assertEquals("Unable to find user.", result.getBody());
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void existingLinkCreateUserLink() {
        when(businessLinkService.createUserLink(any(UserLinkRequest.class))).thenReturn("Link already exists between business and user.");

        UserLinkRequest userLinkRequest = new UserLinkRequest(111111, "john", "smith", "john@mail.com");
        ResponseEntity<String> result = businessLinkController.createUserLink(userLinkRequest);

        assertEquals("Link already exists between business and user.", result.getBody());
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void validGetUsers() {
        List<UserDto> users = new ArrayList<>();
        users.add(new UserDto(1, "john", "smith", "john@mail.com"));
        users.add(new UserDto(2, "jim", "cable", "jim@mail.com"));
        when(businessLinkService.getUsers("business@mail.com")).thenReturn(users);

        ResponseEntity<GetUsersResponse> result = businessLinkController.getUsers("business@mail.com");
        GetUsersResponse body = result.getBody();

        assertEquals(2, body.getUsers().size());
        assertEquals("john@mail.com", body.getUsers().get(0).getEmail());
        assertEquals("jim@mail.com", body.getUsers().get(1).getEmail());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void nullUsersGetUsers() {
        when(businessLinkService.getUsers("business@mail.com")).thenReturn(null);

        ResponseEntity<GetUsersResponse> result = businessLinkController.getUsers("business@mail.com");
        GetUsersResponse body = result.getBody();

        assertEquals(0, body.getUsers().size());
        assertEquals("No users found.", body.getMessage());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void emptyUsersGetUsers() {
        when(businessLinkService.getUsers("business@mail.com")).thenReturn(new ArrayList<>());

        ResponseEntity<GetUsersResponse> result = businessLinkController.getUsers("business@mail.com");
        GetUsersResponse body = result.getBody();

        assertEquals(0, body.getUsers().size());
        assertEquals("No users found.", body.getMessage());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void validGetBusinesses() {
        List<BusinessDto> businesses = new ArrayList<>();
        businesses.add(new BusinessDto(111111, "Business 1"));
        businesses.add(new BusinessDto(111112, "Business 2"));
        when(businessLinkService.getBusinesses("john@mail.com")).thenReturn(businesses);

        ResponseEntity<GetBusinessesResponse> result = businessLinkController.getBusinesses("john@mail.com");
        GetBusinessesResponse body = result.getBody();

        assertEquals(2, body.getBusinesses().size());
        assertEquals(111111, body.getBusinesses().get(0).getBusinessCode());
        assertEquals(111112, body.getBusinesses().get(1).getBusinessCode());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void nullBusinessesGetBusinesses() {
        when(businessLinkService.getBusinesses("john@mail.com")).thenReturn(null);

        ResponseEntity<GetBusinessesResponse> result = businessLinkController.getBusinesses("john@mail.com");
        GetBusinessesResponse body = result.getBody();

        assertEquals(0, body.getBusinesses().size());
        assertEquals("No businesses found.", body.getMessage());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void emptyBusinessesGetBusinesses() {
        when(businessLinkService.getBusinesses("john@mail.com")).thenReturn(new ArrayList<>());

        ResponseEntity<GetBusinessesResponse> result = businessLinkController.getBusinesses("john@mail.com");
        GetBusinessesResponse body = result.getBody();

        assertEquals(0, body.getBusinesses().size());
        assertEquals("No businesses found.", body.getMessage());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void validRemoveUserLink() {
        when(businessLinkService.removeUserLink(any(RemoveUserLinkRequest.class))).thenReturn(null);

        RemoveUserLinkRequest removeUserLinkRequest = new RemoveUserLinkRequest(111111, 1);
        ResponseEntity<String> result = businessLinkController.removeUserLink(removeUserLinkRequest);

        assertEquals("", result.getBody());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void nonExistingLinkRemoveUserLink() {
        when(businessLinkService.removeUserLink(any(RemoveUserLinkRequest.class))).thenReturn("Link doesn't exist.");

        RemoveUserLinkRequest removeUserLinkRequest = new RemoveUserLinkRequest(111111, 1);
        ResponseEntity<String> result = businessLinkController.removeUserLink(removeUserLinkRequest);

        assertEquals("Link doesn't exist.", result.getBody());
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void validRemoveBusinessLink() {
        when(businessLinkService.removeBusinessLink(any(BusinessLinkRequest.class))).thenReturn(null);

        BusinessLinkRequest removeBusinessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        ResponseEntity<String> result = businessLinkController.removeBusinessLink(removeBusinessLinkRequest);

        assertEquals("", result.getBody());
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void nonNullMessageRemoveBusinessLink() {
        when(businessLinkService.removeBusinessLink(any(BusinessLinkRequest.class))).thenReturn("Unable to find user.");

        BusinessLinkRequest removeBusinessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        ResponseEntity<String> result = businessLinkController.removeBusinessLink(removeBusinessLinkRequest);

        assertEquals("Unable to find user.", result.getBody());
        assertEquals(404, result.getStatusCode().value());
    }

}
