package org.weaponplace.services.interfaces;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.weaponplace.entities.User;

public interface UserService extends UserDetailsService {

    Page<User> pageUsers(Pageable pageRequest);

    void banUser(Long id);

    User getUser(Long id);

    void updateRating(Long userId, int newRating);
}

