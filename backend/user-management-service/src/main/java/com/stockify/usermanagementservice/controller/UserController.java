package com.stockify.usermanagementservice.controller;

import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.service.userService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/businessUser")
@AllArgsConstructor
public class UserController {

    private userService userService;

    @PostMapping("/addUser")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addUser(@RequestBody UserRequest userRequest){
        return userService.addUser(userRequest);

    }

    @PostMapping("/deleteUser")
    public void deleteUser(@RequestBody deleteRequest deleteRequest) {
        userService.deleteUser(deleteRequest);
    }

    @PutMapping("/updateUser")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateUser(@RequestBody UpdateRequest updateRequest) {
        return userService.updateUser(updateRequest);
    }

    @GetMapping("/getBusinessUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<BusinessUserDto> getBusinessUsers(@RequestParam String email) {
        return userService.getBusinessUsers(email);
    }
}
