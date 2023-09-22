package com.stockify.businesslinkservice.repository;

import com.stockify.businesslinkservice.model.BusinessLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessLinkRepository extends JpaRepository<BusinessLink, Integer> {
    List<BusinessLink> findByBusinessCodeAndUserId(int businessCode, int userId);

    List<BusinessLink> findByBusinessCode(int businessCode);

    List<BusinessLink> findByUserId(int userId);

    void deleteByBusinessCodeAndUserId(int businessCode, int userId);
}
