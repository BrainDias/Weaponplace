package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
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
@RequestMapping("/products")
public class ProductController {
    private final UserService userService;
    private final DtoMapper mapper;

    @SneakyThrows
    @GetMapping("/")
    public List<? extends Product> currentUserProducts(@AuthenticationPrincipal User user, @RequestParam String productType){
        Stream<Product> productStream = user.getProducts().stream().filter(product -> product.getClass().getName().equals(productType));
        return userService.mapToType(productStream,productType);
    }

    @GetMapping("/{id}")
    public List<? extends Product> otherUserProducts(@PathVariable Long id,@RequestParam String productType){
        return userService.otherUserProducts(id,productType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("")
    public void addProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO productDTO, @RequestParam String productType){
        Product product = null;
        switch (productType){
            case "ammo"-> product = mapper.productDtoToAmmo(productDTO);
            case "ar"-> product = mapper.productDtoToAr(productDTO);
            case "mg"-> product = mapper.productDtoToMachinegun(productDTO);
            case "pistol"-> product = mapper.productDtoToPistol(productDTO);
            case "sniper"-> product = mapper.productDtoToSniperRifle(productDTO);
        }
        userService.addProduct(user,product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{index}")
    public void deleteProduct(@AuthenticationPrincipal User user, @PathVariable int index) {
        userService.deleteProduct(user, index);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{index}")
    public void updateProduct(@AuthenticationPrincipal User user,@PathVariable int index,@RequestBody ProductDTO product){
        userService.updateProduct(user, index,product);
    }
}
