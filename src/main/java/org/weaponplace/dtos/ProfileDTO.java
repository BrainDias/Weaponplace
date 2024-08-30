package org.weaponplace.dtos;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class ProfileDTO {
    private String username;
    //    @Email
    private String email;
    private Boolean active;
    private Set<GrantedAuthority> Authorities;

    private byte[] avatar;
}
