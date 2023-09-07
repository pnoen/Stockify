package com.stockify.usermanagementservice.model;

import lombok.Getter;

@Getter
public enum Role {
    OWNER(0, "Owner"),
    MANAGER(1,"Manager"),
    EMPLOYEE(2, "Employee");

    private int id;
    private String label;

    private Role(int id, String label) {
        this.id = id;
        this.label = label;
    }
}

