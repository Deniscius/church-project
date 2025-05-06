package com.example.church_project.models;


public enum Role {
    SUPER_ADMIN,
    ADMIN,
    USER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}