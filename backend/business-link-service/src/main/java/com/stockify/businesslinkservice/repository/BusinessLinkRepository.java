package com.stockify.businesslinkservice.repository;

import com.stockify.businesslinkservice.model.BusinessLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessLinkRepository extends JpaRepository<BusinessLink, Integer> {
    List<BusinessLink> findByBusinessCodeAndCustomerId(int businessCode, int customerId);

    List<BusinessLink> findByBusinessCode(int businessCode);

    void deleteByBusinessCodeAndCustomerId(int businessCode, int customerId);
}
