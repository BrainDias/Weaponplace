package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.services.UserService;
import com.example.demo.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final UserService userService;
    private final DtoMapper mapper;

    @SneakyThrows
    @GetMapping("/")
    public List<? extends Product> currentUserProducts(@AuthenticationPrincipal User user, @RequestParam String productType){
        List<Product> productList = userService.currentUserProducts(user);
        return mapper.mapToType(productList, productType);
    }

    @GetMapping("/{id}")
    public List<? extends Product> otherUserProducts(@PathVariable Long id, @RequestParam String productType){
        List<Product> productList = userService.otherUserProducts(id);
        return mapper.mapToType(productList, productType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public void addProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO productDTO, @RequestParam String productType){
        Product product = mapper.mapToType(productDTO);
        userService.addProduct(user, product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{index}")
    public void deleteProduct(@AuthenticationPrincipal User user, @PathVariable int index) {
        userService.deleteProduct(user, index);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{index}")
    public void updateProduct(@AuthenticationPrincipal User user,@PathVariable int index,@RequestBody ProductDTO productDTO,@RequestParam String productType){
        Product product = mapper.mapToType(productDTO);
        userService.updateProduct(user, index, product);
    }
}
