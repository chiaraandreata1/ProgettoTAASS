package com.example.shared.rabbithole;

import com.example.shared.models.users.UserType;

import java.util.List;

public class UserStatusRequest {

    private Long id;
    private List<UserType> types;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserType> getTypes() {
        return types;
    }

    public void setTypes(List<UserType> types) {
        this.types = types;
    }
}
