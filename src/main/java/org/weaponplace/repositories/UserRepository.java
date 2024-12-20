package org.weaponplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weaponplace.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByActiveIsTrue();
    Optional<User> findByUsername(String username);
}


