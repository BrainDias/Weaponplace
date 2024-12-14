package org.weaponplace.services.interfaces;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.weaponplace.entities.User;

public interface UserService {

    Page<User> pageUsers(Pageable pageRequest);

    void banUser(Long id);

    @Cacheable("users")
    User getUser(Long id);

    void updateRating(Long userId, int newRating);
}

