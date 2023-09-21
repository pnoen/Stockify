package com.stockify.businesslinkservice.service;

import com.stockify.businesslinkservice.dto.CreateLinkRequest;
import com.stockify.businesslinkservice.model.BusinessLink;
import com.stockify.businesslinkservice.repository.BusinessLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessLinkService {

    private final BusinessLinkRepository businessLinkRepository;

    public String createLink(CreateLinkRequest createLinkRequest) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndCustomerId(
                createLinkRequest.getBusinessCode(),
                createLinkRequest.getCustomerId()
        );

        if (!businessLinks.isEmpty()) {
            return "Link already exists between business and customer.";
        }

        BusinessLink businessLink = BusinessLink.builder()
                .businessCode(createLinkRequest.getBusinessCode())
                .customerId(createLinkRequest.getCustomerId())
                .build();
        businessLinkRepository.save(businessLink);

        return null;
    }

}
