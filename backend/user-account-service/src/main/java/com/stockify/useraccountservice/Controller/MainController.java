package com.stockify.useraccountservice.Controller;

import com.stockify.useraccountservice.UserRepository;
import com.stockify.useraccountservice.Model.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path ="/account")
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String business
    ) {

        // Make sure required fields are not empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "Error: All fields are required.";
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        // Check email
        if (existingUser.isPresent()) {
            return "Email already exists.";
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }

        // Create new user object
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setBusiness(business);

        // Save the new user
        userRepository.save(newUser);

        return "User registered successfully!";
    }

    // Login an existing user
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        // Check user exists
        if (existingUser.isPresent()) {
            User user = existingUser.orElse(new User());

            // Check password matches
            if (password.equals(user.getPassword())) {
                // Move onto the next page
                return "User registered successfully!";
            } else {
                return "Incorrect password!";
            }
        }

        return "User does not exist!";
    }
}