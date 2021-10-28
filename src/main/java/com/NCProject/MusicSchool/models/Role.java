package com.NCProject.MusicSchool.models;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    USER,
    STUDENT,
    TEACHER,
    ADMIN;

    @Override
    public String getAuthority() {
        return String.valueOf(this);
//        return "ROLE_" + this;
    }

//    public static void main(String[] args) {
//        Role role = Role.STUDENT;
//
//        System.out.println(role.getAuthority());
//    }
}
