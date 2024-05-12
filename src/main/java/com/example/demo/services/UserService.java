package com.example.demo.services;

import com.example.demo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> pageUsers(Pageable pageRequest);

    void banUser(Long id);

    void addProduct(User user, Product product);

    void deleteProduct(User user, int index);

    void updateProduct(User user, int index, Product product);

    List<? extends Product> otherUserProducts(Long id, String productType);

    User getUser(Long id);

    List<? extends Product> currentUserProducts(User user, String productType);
}

