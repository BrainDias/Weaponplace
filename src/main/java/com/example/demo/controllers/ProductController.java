package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.products.ProductType;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

//TODO: отфильтрованный список продуктов на продажу, фильтрация объявлений на продажу по отзывам на продавцов, по релевантности, возрастанию цены, убыванию цены
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final UserService userService;
    private final DtoMapper mapper;

    @SneakyThrows
    @GetMapping("/")
    public List<Product> currentUserProducts(@AuthenticationPrincipal User user, @RequestParam ProductType productType){
        return userService.mapToType(user,productType);
    }

    //TODO: сделать фильтр по forSale
    @GetMapping("/{id}")
    public List<Product> otherUserProducts(@PathVariable Long id,@RequestParam ProductType productType){
        return userService.otherUserProducts(id,productType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("")
    public void addProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO productDto){
        Product product = mapper.productDtoToProduct(productDto);
        userService.addProduct(user,product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{index}")
    public void deleteProduct(@AuthenticationPrincipal User user, @PathVariable int index) {
        userService.deleteProduct(user, index);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{index}")
    public void updateProduct(@AuthenticationPrincipal User user,@PathVariable int index,@RequestBody ProductDTO productDto){
        Product product = mapper.productDtoToProduct(productDto);
        userService.updateProduct(user, index,product);
    }
}
