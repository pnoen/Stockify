package com.stockify.useraccountservice.Repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import com.stockify.useraccountservice.Model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    Optional<User> findByBusinessCode(int businessCode);
    Optional<User> findByBusinessCodeAndBusinessIsNotNull(int businessCode);
}