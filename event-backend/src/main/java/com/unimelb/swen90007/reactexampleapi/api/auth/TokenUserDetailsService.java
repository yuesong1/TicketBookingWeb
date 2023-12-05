package com.unimelb.swen90007.reactexampleapi.api.auth;

import com.unimelb.swen90007.reactexampleapi.api.objects.User;
import com.unimelb.swen90007.reactexampleapi.api.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TokenUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        // Assuming you have a method in UserService that retrieves a user by its token
        User user = userService.findUserByToken(token);

        if (user != null) {
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("No user found with token: " + token);
        }
    }
}
