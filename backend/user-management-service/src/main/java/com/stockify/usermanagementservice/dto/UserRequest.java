package com.stockify.usermanagementservice.dto;

import com.stockify.usermanagementservice.model.Role;

public class UserRequest {

    private String first_name;
    private String last_name;
//    private int role_id;
    private int companyId;

    private Role role;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

//    public int getRole_id() {
//        return role_id;
//    }
//
//    public void setRole_id(int role_id) {
//        this.role_id = role_id;
//    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
