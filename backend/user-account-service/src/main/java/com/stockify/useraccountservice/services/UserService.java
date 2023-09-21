package com.stockify.useraccountservice.services;

import java.util.Random;

public class UserService {

    public int generateUniqueBusinessCode() {
        Random random = new Random();
        return random.nextInt(90000) + 10000;
    }



}
