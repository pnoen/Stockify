package com.stockify.usermanagementservice.controller;

import com.stockify.usermanagementservice.dto.*;
import com.stockify.usermanagementservice.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businessUser")
@RequiredArgsConstructor
public class UserController {

    private final userService userService;

    @PostMapping("/addUser")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addUser(@RequestBody UserRequest userRequest){
        return userService.addUser(userRequest);

    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
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
    public List<BusinessUserDto> getBusinessUsers(@RequestBody GetBusinessUsersRequest getBusinessUsersRequest) {
        return userService.getBusinessUsers(getBusinessUsersRequest);
    }
}
