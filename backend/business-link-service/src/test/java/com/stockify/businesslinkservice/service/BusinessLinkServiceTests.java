package com.stockify.businesslinkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockify.businesslinkservice.dto.ApiCallResponse;
import com.stockify.businesslinkservice.dto.BusinessLinkRequest;
import com.stockify.businesslinkservice.dto.UserLinkRequest;
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
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("1")
        );

        when(businessLinkRepository.findByBusinessCodeAndUserId(1, 1)).thenReturn(new ArrayList<>());

        BusinessLinkRequest businessLinkRequest = new BusinessLinkRequest(1, "john@mail.com");
        String result = businessLinkService.createBusinessLink(businessLinkRequest);

        assertNull(result);
        verify(businessLinkRepository, times(1)).save(any(BusinessLink.class));
    }

    @Test
    public void validCreateUserLink() throws JsonProcessingException {
        ApiCallResponse apiCallResponse = new ApiCallResponse(200, "1", "");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(apiCallResponse))
        );

        when(businessLinkRepository.findByBusinessCodeAndUserId(1, 1)).thenReturn(new ArrayList<>());

        UserLinkRequest userLinkRequest = new UserLinkRequest(1, "john", "smith", "john@mail.com");
        String result = businessLinkService.createUserLink(userLinkRequest);

        assertNull(result);
        verify(businessLinkRepository, times(1)).save(any(BusinessLink.class));
    }


}
