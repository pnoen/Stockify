package com.stockify.usermanagementservice.repository;

import com.stockify.usermanagementservice.model.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<BusinessUser, Integer> {
    List<BusinessUser> findByBusinessCode(int businessCode);
}
