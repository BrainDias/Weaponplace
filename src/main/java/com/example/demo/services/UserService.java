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
        Stream<Product> productStream = user.getProducts().stream().filter(product -> product.getClass().getName().equals(productType)&&(!product.hidden));
        return mapToType(productStream,productType);
    }

    public static User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) throw new IllegalArgumentException("User doesn't exist");
        User selectedUser = optionalUser.get();
        return selectedUser;
    }
    public List<? extends Product> mapToType(Stream<Product> productStream, String productType) {
        switch (productType){
            case "ammo"-> productStream.map(product -> mapper.productToAmmo(product));
            case "ar"-> productStream.map(product -> mapper.productToAr(product));
            case "mg"-> productStream.map(product -> mapper.productToMachinegun(product));
            case "pistol"-> productStream.map(product -> mapper.productToPistol(product));
            case "sniper"-> productStream.map(product -> mapper.productToSniperRifle(product));
        }
        return productStream.toList();
    }
}
