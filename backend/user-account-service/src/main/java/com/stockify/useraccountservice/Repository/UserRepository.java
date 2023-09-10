package com.stockify.useraccountservice.Repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import com.stockify.useraccountservice.Model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByFirstName(String firstName);

    Optional<User> findByLastName(String lastName);
}