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
import java.util.Optional;
import java.util.function.Predicate;

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

    public void updateProduct(User user, int index, ProductDTO product) {
        List<Product> products = user.getProducts();
        String productType = products.get(index).getClass().getName();
        switch (productType){
            case "Ammo" -> products.set(index,mapper.productDtoToAmmo(product));
            case "AssaultRifle"-> products.set(index,mapper.productDtoToAr(product));
            case "MachineGun"-> products.set(index,mapper.productDtoToMachinegun(product));
            case "Pistol"-> products.set(index,mapper.productDtoToPistol(product));
            case "SniperRifle"-> products.set(index,mapper.productDtoToSniperRifle(product));
        }
        userRepository.save(user);
    }

    public List<? extends Product> otherUserProducts(Long id, String productType) {
        User user = getUser(id);
        List<Product> productList = user.getProducts().stream().filter(checkProductTypeAndVisibility(productType)).toList();
        return mapToType(productList,productType);
    }

    private Predicate<? super Product> checkProductTypeAndVisibility(String productType) {
        return product -> product.getClass().getName().equals(productType) && (!product.hidden);
    }

    public static User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User doesn't exist"));
    }
    public List<? extends Product> mapToType(List<Product> productList, String productType) {
        switch (productType){
            case "ammo"-> productList.stream().map(product -> mapper.productToAmmo(product));
            case "ar"-> productList.stream().map(product -> mapper.productToAr(product));
            case "mg"-> productList.stream().map(product -> mapper.productToMachinegun(product));
            case "pistol"-> productList.stream().map(product -> mapper.productToPistol(product));
            case "sniper"-> productList.stream().map(product -> mapper.productToSniperRifle(product));
        }
        return productList;
    }
}
