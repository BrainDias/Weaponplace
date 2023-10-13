package com.example.demo.dtos;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class ProfileDTO {
    private String username;
    //    @Email
    private String email;
    private boolean active;
    private Set<GrantedAuthority> Authorities;
}
