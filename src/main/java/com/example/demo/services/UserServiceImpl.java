package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    public static UserRepository userRepository;

    public Page<User> pageUsers(Pageable pageRequest){
        return userRepository.findAll(pageRequest);
    }

    public void banUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
        });
    }

    public void addProduct(User user, Product product) {
        user.getProducts().add(product);
        userRepository.save(user);
    }

    public void deleteProduct(User user, int index) {
        user.getProducts().remove(index);
        userRepository.save(user);
    }

    public void updateProduct(User user, int index, Product product) {
        user.getProducts().set(index, product);
        userRepository.save(user);
    }

    public List<? extends Product> otherUserProducts(Long id, String productType) {
        User user = getUser(id);
        return user.getProducts().stream().filter(checkProductTypeAndVisibility(productType)).toList();
    }

    private Predicate<? super Product> checkProductTypeAndVisibility(String productType) {
        return product -> product.getClass().getName().equals(productType) && (!product.hidden);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User doesn't exist"));
    }

    public List<? extends Product> currentUserProducts(User user, String productType) {
        return user.getProducts().stream().filter(product -> product.getClass().getName().equals(productType)).toList();
    }
}
