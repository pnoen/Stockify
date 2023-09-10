package com.stockify.usermanagementservice.repository;

import com.stockify.usermanagementservice.model.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BusinessUser, Integer> {
}
