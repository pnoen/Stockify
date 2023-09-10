package com.stockify.useraccountservice.Controller;

import com.stockify.useraccountservice.UserRepository;
import com.stockify.useraccountservice.Model.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
    public String login(@RequestParam User user) {

        return "Invalid email or password!";
    }
}