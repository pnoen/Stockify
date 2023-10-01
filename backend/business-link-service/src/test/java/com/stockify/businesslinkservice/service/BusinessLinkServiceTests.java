package com.stockify.businesslinkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockify.businesslinkservice.dto.*;
import com.stockify.businesslinkservice.model.BusinessLink;
import com.stockify.businesslinkservice.repository.BusinessLinkRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class BusinessLinkServiceTests {

    @Mock
    private BusinessLinkRepository businessLinkRepository;

    @InjectMocks
    private BusinessLinkService businessLinkService;

    private MockWebServer mockWebServer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        mockWebServer.start(8080);

        businessLinkService = new BusinessLinkService(businessLinkRepository, WebClient.builder().build());
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void stop() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void validCreateBusinessLink() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("1")
        );

        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(new ArrayList<>());

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        String result = businessLinkService.createBusinessLink(businessLinkRequest);

        assertNull(result);
        verify(businessLinkRepository, times(1)).save(any(BusinessLink.class));
    }

    @Test
    public void invalidEmailCreateBusinessLink() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("-1")
        );

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        String result = businessLinkService.createBusinessLink(businessLinkRequest);

        assertEquals("Unable to find user.", result);
        verify(businessLinkRepository, times(0)).save(any(BusinessLink.class));
    }

    @Test
    public void existingLinkCreateBusinessLink() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("1")
        );

        List<BusinessLink> businessLinks = new ArrayList<>();
        businessLinks.add(new BusinessLink(1, 111111, 1));
        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(businessLinks);

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        String result = businessLinkService.createBusinessLink(businessLinkRequest);

        assertEquals("Link already exists between business and user.", result);
        verify(businessLinkRepository, times(0)).save(any(BusinessLink.class));

    }

    @Test
    public void validCreateUserLink() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(200, "1", "");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(new ArrayList<>());

        UserLinkRequest userLinkRequest = new UserLinkRequest(111111, "john", "smith", "john@mail.com");
        String result = businessLinkService.createUserLink(userLinkRequest);

        assertNull(result);
        verify(businessLinkRepository, times(1)).save(any(BusinessLink.class));
    }

    @Test
    public void invalidUserCreateUserLink() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(404, "-1", "");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        UserLinkRequest userLinkRequest = new UserLinkRequest(111111, "john", "smith", "john@mail.com");
        String result = businessLinkService.createUserLink(userLinkRequest);

        assertEquals("Unable to find user.", result);
        verify(businessLinkRepository, times(0)).save(any(BusinessLink.class));
    }

    @Test
    public void existingLinkCreateUserLink() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(200, "1", "");
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        List<BusinessLink> businessLinks = new ArrayList<>();
        businessLinks.add(new BusinessLink(1, 111111, 1));
        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(businessLinks);

        UserLinkRequest userLinkRequest = new UserLinkRequest(111111, "john", "smith", "john@mail.com");
        String result = businessLinkService.createUserLink(userLinkRequest);

        assertEquals("Link already exists between business and user.", result);
        verify(businessLinkRepository, times(0)).save(any(BusinessLink.class));
    }

    @Test
    public void validGetUsers() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("111111")
        );

        List<BusinessLink> businessLinks = new ArrayList<>();
        businessLinks.add(new BusinessLink(1, 111111, 1));
        businessLinks.add(new BusinessLink(2, 111111, 2));
        when(businessLinkRepository.findByBusinessCode(111111)).thenReturn(businessLinks);

        List<UserDto> users = new ArrayList<>();
        users.add(new UserDto(1, "john", "smith", "john@mail.com"));
        users.add(new UserDto(2, "jim", "cable", "jim@mail.com"));

        UserIdsResponse userIdsResponse = new UserIdsResponse(users, 200);
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(userIdsResponse))
        );

        List<UserDto> result = businessLinkService.getUsers("business@mail.com");
        assertEquals(2, result.size());
        assertEquals("john@mail.com", result.get(0).getEmail());
        assertEquals("jim@mail.com", result.get(1).getEmail());
    }

    @Test
    public void validGetBusinesses() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("1")
        );

        List<BusinessLink> businessLinks = new ArrayList<>();
        businessLinks.add(new BusinessLink(1, 111111, 1));
        businessLinks.add(new BusinessLink(2, 111112, 1));
        when(businessLinkRepository.findByUserId(1)).thenReturn(businessLinks);

        List<BusinessDto> businesses = new ArrayList<>();
        businesses.add(new BusinessDto(111111, "Business 1"));
        businesses.add(new BusinessDto(111112, "Business 2"));

        BusinessesResponse businessesResponse = new BusinessesResponse(businesses, 200);
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(businessesResponse))
        );

        List<BusinessDto> result = businessLinkService.getBusinesses("john@mail.com");
        assertEquals(2, result.size());
        assertEquals(111111, result.get(0).getBusinessCode());
        assertEquals(111112, result.get(1).getBusinessCode());
    }

    @Test
    public void validRemoveUserLink() {
        List<BusinessLink> businessLinks =  new ArrayList<>();
        businessLinks.add(new BusinessLink(1, 111111, 1));
        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(businessLinks);

        RemoveUserLinkRequest removeUserLinkRequest = new RemoveUserLinkRequest(111111, 1);
        String result = businessLinkService.removeUserLink(removeUserLinkRequest);

        assertNull(result);
        verify(businessLinkRepository, times(1)).deleteByBusinessCodeAndUserId(111111, 1);
    }

    @Test
    public void noExistingLinkRemoveUserLink() {
        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(new ArrayList<>());

        RemoveUserLinkRequest removeUserLinkRequest = new RemoveUserLinkRequest(111111, 1);
        String result = businessLinkService.removeUserLink(removeUserLinkRequest);

        assertEquals("Link doesn't exist.", result);
        verify(businessLinkRepository, times(0)).deleteByBusinessCodeAndUserId(anyInt(), anyInt());
    }

    @Test
    public void validRemoveBusinessLink() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("1")
        );

        List<BusinessLink> businessLinks =  new ArrayList<>();
        businessLinks.add(new BusinessLink(1, 111111, 1));
        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(businessLinks);

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        String result = businessLinkService.removeBusinessLink(businessLinkRequest);

        assertNull(result);
        verify(businessLinkRepository, times(1)).deleteByBusinessCodeAndUserId(111111, 1);
    }

    @Test
    public void invalidEmailRemoveBusinessLink() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("-1")
        );

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        String result = businessLinkService.removeBusinessLink(businessLinkRequest);

        assertEquals("Unable to find user.", result);
        verify(businessLinkRepository, times(0)).deleteByBusinessCodeAndUserId(anyInt(), anyInt());
    }

    @Test
    public void noExistingLinkRemoveBusinessLink() {
        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("1")
        );

        when(businessLinkRepository.findByBusinessCodeAndUserId(111111, 1)).thenReturn(new ArrayList<>());

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(111111, "john@mail.com");
        String result = businessLinkService.removeBusinessLink(businessLinkRequest);

        assertEquals("Link doesn't exist.", result);
        verify(businessLinkRepository, times(0)).deleteByBusinessCodeAndUserId(anyInt(), anyInt());
    }

}
