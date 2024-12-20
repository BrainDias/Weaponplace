package org.weaponplace.dtos;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class ProfileDTO {
    private String username;
    //    @Email
    private String email;
    private Boolean active;

    private byte[] avatar;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
