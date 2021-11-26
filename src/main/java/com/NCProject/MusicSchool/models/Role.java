package com.NCProject.MusicSchool.models;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    USER,
    STUDENT,
    TEACHER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + this;
    }


}
