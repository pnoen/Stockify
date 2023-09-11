package com.stockify.useraccountservice.Controller;

import com.stockify.useraccountservice.Repository.UserRepository;
import com.stockify.useraccountservice.Model.User;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam String password,
            HttpServletRequest request
    ) {
        // Get the session of the request and store data in the session
        HttpSession session = request.getSession();
        session.setAttribute("current_user_email", email);

        Optional<User> existingUser = userRepository.findByEmail(email);

        // Check user exists
        if (existingUser.isPresent()) {
            User user = existingUser.orElse(new User());

            // Check password matches
            if (password.equals(user.getPassword())) {
                // Move onto the next page
                return "User logged in successfully!";
            } else {
                return "Incorrect password!";
            }
        }

        return "User does not exist!";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Check if the session is new or doesn't contain attributes indicating a logged-in user
        if (session.isNew() || session.getAttribute("current_user_email") == null) {
            return "Error: No active session or user not logged in!";
        }

        // Invalidate the session
        session.invalidate();
        return "User logged out and session cleared!";
    }

    // Just with email
//    @GetMapping("/getUserId")
//    public int getUserIdByFirstNameAndLastName(
//            @RequestParam String firstName,
//            @RequestParam String lastName,
//            @RequestParam String email
//    ) {
//        Optional<User> userOptional = userRepository.findByEmail(email);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            return user.getId();
//        } else {
//            return -1;
//        }
//    }

    // Get user id with first name, last name and email
    @PostMapping("/getUserId")
    public int getUserIdByFirstNameAndLastName(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email
    ) {
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isPresent()) {
            User existingUser = userByEmail.get();
            if (existingUser.getFirstName().equals(firstName) && existingUser.getLastName().equals(lastName)) {
                return existingUser.getId();
            }
        }

        return -1;
    }

    // Just with email
//    @GetMapping("/checkUserExist")
//    public Boolean checkUserExist(
//            @RequestParam String email
//    ) {
//        Optional<User> userExists = userRepository.findByEmail(email);
//
//        if (userExists.isPresent()) {
//            return true;
//        }
//
//        return false;
//    }

    // Check user exist with first name, last name and email
    @PostMapping("/checkUserExist")
    public Boolean checkUserExist(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email
    ) {
        Optional<User> userExistsByEmail = userRepository.findByEmail(email);

        if (userExistsByEmail.isPresent()) {
            User existingUser = userExistsByEmail.get();
            return existingUser.getFirstName().equals(firstName) && existingUser.getLastName().equals(lastName);
        }

        return false;
    }
}