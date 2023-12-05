package com.unimelb.swen90007.reactexampleapi.api.objects;

import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User extends DomainObject implements UserDetails {
    private UserAccess role = null;
//    private String email = null;
    private String password = null;

    private String token= null;

    // getters & setters
    public String getEmail() { return (String) getPrimaryKey().getKey(0); }
    public void setEmail(String email) {
//        this.email = email;
        Object[] key = {email};
        setPrimaryKey(new Key(key));
    }

    public User(Key primaryKey, UserAccess role, String password) {
        super(primaryKey);
        this.role = role;
        this.password = password;
//        this.token = token;

//        this.markNew();
    }

    public User(String email) {
//        this.email = email;
        Object[] pk = {email};
        setPrimaryKey(new Key(pk));
    }

//    public User(Key primaryKey){
//        super(primaryKey);
////        this.email = (String) primaryKey.getKey(0);
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<UserAccess> auths = new ArrayList<>();
        auths.add(role);
        return auths;
    }

    public String getPassword() { return password; }

    @Override
    public String getUsername() { return (String) getPrimaryKey().getKey(0); }
    public String getId() { return (String) getPrimaryKey().getKey(0); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public void setPassword(String password) { this.password = password; }
    public UserAccess getRole() { return role; }
    public void setRole(String role) { this.role = UserAccess.valueOf(role.toLowerCase()); };

    @Override
    public String toString() {
        return "{" +
                "\"type\": \"" + role + "\", " +
                "\"email\": \"" + getId() + "\"" +
                "}";
    }
    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
//    @Override
//    public Key getPrimaryKey() {
//        String s;
//        s = (String) super.getPrimaryKey().getKey(0);
//        return s;
//    }
}
