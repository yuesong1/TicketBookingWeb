package com.unimelb.swen90007.reactexampleapi.api.objects;

import org.springframework.security.core.GrantedAuthority;

public enum UserAccess implements GrantedAuthority {
    customer,
    planner,
    admin;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
