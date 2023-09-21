package com.stockify.businesslinkservice.service;

import com.stockify.businesslinkservice.dto.LinkRequest;
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

    public String createLink(LinkRequest linkRequest) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndCustomerId(
                linkRequest.getBusinessCode(),
                linkRequest.getCustomerId()
        );

        if (!businessLinks.isEmpty()) {
            return "Link already exists between business and customer.";
        }

        BusinessLink businessLink = BusinessLink.builder()
                .businessCode(linkRequest.getBusinessCode())
                .customerId(linkRequest.getCustomerId())
                .build();
        businessLinkRepository.save(businessLink);

        return null;
    }

    public List<Integer> getCustomers(int businessCode) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCode(businessCode);

        return businessLinks.stream()
                .map(businessLink -> businessLink.getCustomerId())
                .toList();
    }

    public String removeLink(LinkRequest linkRequest) {
        List<BusinessLink> businessLinks = businessLinkRepository.findByBusinessCodeAndCustomerId(
                linkRequest.getBusinessCode(),
                linkRequest.getCustomerId()
        );

        if (businessLinks.isEmpty()) {
            return "Link doesn't exist.";
        }

        businessLinkRepository.deleteByBusinessCodeAndCustomerId(
                linkRequest.getBusinessCode(),
                linkRequest.getCustomerId()
        );
        return null;
    }
}
