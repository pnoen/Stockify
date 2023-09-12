package com.stockify.usermanagementservice.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        if (role != null) {
            return role.getId();
        }
        return null;
    }

    @Override
    public Role convertToEntityAttribute(Integer id) {
        if (id != null) {
            for (Role role : Role.values()) {
                if (role.getId() == id) {
                    return role;
                }
            }
        }
        return null;
    }
}
