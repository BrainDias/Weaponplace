package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.products.ProductType;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    public static UserRepository userRepository;
    private final DtoMapper mapper;

    public Page<User> pageUsers(Pageable pageRequest){
        return userRepository.findAll(pageRequest);
    }

    public void banUser(Long id) {
        userRepository.findById(id).ifPresent(User -> {
            User.setActive(false);
            userRepository.save(User);
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
        user.getProducts().set(index,product);
        userRepository.save(user);
    }

    public List<Product> otherUserProducts(Long id, ProductType productType) {
        User user = getUser(id);
        return mapToType(user,productType);
    }

    public static User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) throw new IllegalArgumentException("User doesn't exist");
        User selectedUser = optionalUser.get();
        return selectedUser;
    }
    public List<Product> mapToType(User user, ProductType productType) {
        return user.getProducts().stream().filter(procuct->procuct.getProductType().equals(productType) && !procuct.isHidden()).toList();
    }
}
