package com.example.demo.repository;

import com.example.demo.entities.User;
import com.example.demo.entities.User.Address;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findUserByName(String name);
}
