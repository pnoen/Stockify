package com.stockify.useraccountservice.Controller;

import com.stockify.useraccountservice.dto.*;
import com.stockify.useraccountservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stockify.useraccountservice.Repository.UserRepository;
import com.stockify.useraccountservice.Model.User;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path ="/account")
public class MainController {

    @Autowired
    private UserRepository userRepository;
    private UserService userService = new UserService();

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        String firstName = registrationRequest.getFirstName();
        String lastName = registrationRequest.getLastName();
        String email = registrationRequest.getEmail();
        String password = registrationRequest.getPassword();
        String confirmPassword = registrationRequest.getConfirmPassword();
        String business = registrationRequest.getBusiness();
        int businessCode = 0;
        if(business != null){
            businessCode = userService.generateUniqueBusinessCode();

        }

        // Make sure required fields are not empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error: All fields are required."));
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        // Check email
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Email already exists."));
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Passwords do not match."));
        }

        // Create a new user object
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setBusiness(business);
        newUser.setBusinessCode(businessCode);


        // Save the new user
        userRepository.save(newUser);

        return ResponseEntity.ok(new ApiResponse(200, "User registered successfully."));
    }


    // Login an existing user
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Optional<User> existingUser = userRepository.findByEmail(email);

        ApiResponse response = new ApiResponse();

        // Check if the user exists
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Check if the password matches
            if (password.equals(user.getPassword())) {
                response.setStatusCode(200); // You can use appropriate HTTP status codes
                response.setMessage("User logged in successfully!");
                response.setUserToken(email);
            } else {
                response.setStatusCode(401); // Unauthorized status code
                response.setMessage("Incorrect password!");
            }
        } else {
            response.setStatusCode(404); // Not Found status code
            response.setMessage("User does not exist!");
        }

        return ResponseEntity.ok(response);
    }



    // Get user id with first name, last name and email
    @PostMapping("/getUserId")
    public ResponseEntity<ApiResponse> getUserIdByFirstNameAndLastName(@RequestBody NameEmailRequest request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String email = request.getEmail();

        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isPresent()) {
            User existingUser = userByEmail.get();
            if (existingUser.getFirstName().equals(firstName) && existingUser.getLastName().equals(lastName)) {
                return ResponseEntity.ok(new ApiResponse(200, String.valueOf(existingUser.getId())));
            }
        }

        return ResponseEntity.ok(new ApiResponse(404, "-1"));
    }


    // Check user exist with first name, last name and email
    @PostMapping("/checkUserExist")
    public ResponseEntity<ApiResponse> checkUserExist(@RequestBody NameEmailRequest request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String email = request.getEmail();

        Optional<User> userExistsByEmail = userRepository.findByEmail(email);

        if (userExistsByEmail.isPresent()) {
            User existingUser = userExistsByEmail.get();
            boolean userExists = existingUser.getFirstName().equals(firstName) && existingUser.getLastName().equals(lastName);
            return ResponseEntity.ok(new ApiResponse(200, String.valueOf(userExists)));
        }

        return ResponseEntity.ok(new ApiResponse(404, "false"));
    }


    // Get list of user ids and return list of users
    @GetMapping("/getUsers")
    public ResponseEntity<UserIdsResponse> getUsers(@RequestParam List<Integer> userIds) {
        List<User> userList = new ArrayList<>();

        for (int userId : userIds) {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userList.add(user);
            }
        }


        if (!userList.isEmpty()) {
            return ResponseEntity.ok(new UserIdsResponse(200, userList));
        } else {
            return ResponseEntity.ok(new UserIdsResponse(404, new ArrayList<>()));
        }
    }

    @GetMapping("/checkIfBusiness")
    public ResponseEntity<ApiResponse> checkIfBusiness(@RequestParam String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        ApiResponse response = new ApiResponse();

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            boolean isBusiness = user.getBusiness() != null && !user.getBusiness().isEmpty();

            if (isBusiness) {
                response.setStatusCode(200); // OK status code
                response.setMessage("User is a business.");
            } else {
                response.setStatusCode(204); // No Content status code
                response.setMessage("User is not a business.");
            }

        } else {
            response.setStatusCode(404); // Not Found status code
            response.setMessage("User does not exist!");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getBusinessDetails")
    public ResponseEntity<?> getBusinessDetails(@RequestParam String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.ok(new BusinessDetailsResponse(200, existingUser.get().getBusiness(), existingUser.get().getBusinessCode() ));


        } else {
            return ResponseEntity.ok(new ApiResponse(200, "Business Details failed to retrieve" ));
        }

    }
    @GetMapping("/getBusinessCode")
    public int getBusinessCode(@RequestParam String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return user.getBusinessCode();


        } else {
            return -1;
        }

    }

    @GetMapping("/getBusinessName")
    public String getBusinessName(@RequestParam int businessCode) {

        Optional<User> existingUser = userRepository.findByBusinessCodeAndBusinessIsNotNull(businessCode);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return user.getBusiness();
        } else {
            return null;
        }
    }
    @GetMapping("/getUserDetails")
    public User getUserDetails(@RequestParam int userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        return existingUser.orElseGet(User::new);

    }


    @GetMapping("/getUserEmail")
    public ResponseEntity<UserEmailResponse> getUserEmail(@RequestParam int userId) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            return ResponseEntity.ok(new UserEmailResponse(200, existingUser.get().getEmail()));
        } else {
            return ResponseEntity.ok(new UserEmailResponse(404, null));
        }
    }
        
        
    @PostMapping("/updateUserBusinessCode")
    public ResponseEntity<ApiResponse> updateUserBusinessCode(@RequestBody UpdateBusinessRequest updateBusinessRequest) {
        Optional<User> existingUser = userRepository.findByEmail(updateBusinessRequest.getEmail());
        System.out.println();
        User user;
        if (existingUser.isPresent()) {
             user = existingUser.get();
             user.setBusinessCode(updateBusinessRequest.getBusinessCode());
             userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse(200, "User successfully added to business"));
        }else{
            return ResponseEntity.ok(new ApiResponse(400, "Error: User add unsuccessful"));
        }
    }

    @GetMapping("/getBusinesses")
    public ResponseEntity<BusinessesResponse> getBusinesses(@RequestParam List<Integer> businessCodes) {
        List<BusinessDto> businesses = new ArrayList<>();

        for (int businessCode : businessCodes) {
            Optional<User> userOptional = userRepository.findByBusinessCodeAndBusinessIsNotNull(businessCode);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                businesses.add(new BusinessDto(user.getBusinessCode(), user.getBusiness()));
            }
        }

        if (!businesses.isEmpty()) {
            return ResponseEntity.ok(new BusinessesResponse(200, businesses));
        } else {
            return ResponseEntity.ok(new BusinessesResponse(200, new ArrayList<>()));
        }
    }

    @GetMapping("/getUserIdByEmail")
    public int getUserIdByEmail(@RequestParam String email) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return user.getId();
        } else {
            return -1;
        }
    }

    @PostMapping("/editUser")
    public ResponseEntity<ApiResponse> editUser(@RequestBody UpdateRequest updateRequest) {
        int userId = updateRequest.getId();
        String firstName = updateRequest.getFirstName();
        String lastName = updateRequest.getLastName();
        String currentPassword = updateRequest.getCurrentPassword();
        String newPassword = updateRequest.getNewPassword();
        String confirmPassword = updateRequest.getConfirmPassword();
        String business = updateRequest.getBusiness();

        if(firstName.isEmpty() &&
                lastName.isEmpty() &&
                newPassword.isEmpty() &&
                confirmPassword.isEmpty() &&
                business.isEmpty()
        ) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "At least one field is required"));
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Password mismatch"));
        }

        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()){
                throw new Exception();
            }

            User user = userOptional.get();
            if (!firstName.isEmpty()) {
                user.setFirstName(firstName);
            }

            if (!lastName.isEmpty()) {
                user.setLastName(lastName);
            }

            if (!newPassword.isEmpty()) {
                if (!user.getPassword().equals(currentPassword)) {
                    return ResponseEntity.badRequest().body(new ApiResponse(400, "Current password doesn't match with the database"));
                }
                user.setPassword(newPassword);
            }

            if (!business.isEmpty()) {
                user.setBusiness(business);
            }

            userRepository.save(user);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, "Error occurred when attempted to update fields in database"));
        }
        return ResponseEntity.ok(new ApiResponse(200, "User edited successfully."));

    }

}