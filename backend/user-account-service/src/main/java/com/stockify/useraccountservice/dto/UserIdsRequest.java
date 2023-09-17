package com.stockify.useraccountservice.dto;

import java.util.List;

public class UserIdsRequest {
    private List<Integer> userIds;

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
