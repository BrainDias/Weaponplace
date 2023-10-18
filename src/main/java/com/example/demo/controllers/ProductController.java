package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final UserService userService;

    @SneakyThrows
    @GetMapping("/products")
    public List<? extends Product> currentUserProducts(@AuthenticationPrincipal User user, @RequestParam String productType){
        Stream<Product> productStream = user.getProducts().stream().filter(product -> product.getClass().getName().equals(productType));
        return userService.mapToType(productStream,productType);
    }

    @GetMapping("/products/{id}")
    public List<? extends Product> otherUserProducts(@PathVariable Long id,@RequestParam String productType){
        return userService.otherUserProducts(id,productType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/products")
    public void addProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO product, @RequestParam String productType){
        userService.addProduct(user,product,productType);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/products/{index}")
    public void deleteProduct(@AuthenticationPrincipal User user, @PathVariable int index) {
        userService.deleteProduct(user, index);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/products/{index}")
    public void updateProduct(@AuthenticationPrincipal User user,@PathVariable int index,@RequestBody ProductDTO product){
        userService.updateProduct(user, index,product);
    }
}
