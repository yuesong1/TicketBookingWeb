package com.unimelb.swen90007.reactexampleapi.api.auth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TokenAuthentication implements Authentication {

    private final String token;
    private boolean isAuthenticated;
    private UserDetails userDetails; // Your custom UserDetails class

    public TokenAuthentication(String token) {
        this.token = token;
        this.isAuthenticated = false;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    // Other getters and setters for userDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can implement this method to provide user authorities/roles if needed.
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null; // Typically, credentials are not needed for token-based authentication
    }

    @Override
    public Object getDetails() {
        return userDetails; // You can return any additional details about the user here
    }

    @Override
    public Object getPrincipal() {
        return userDetails; // Typically, the principal is the user details
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername(); // Return the username as the name
    }
}
