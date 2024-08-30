package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.filters.ProductFilter;
import com.example.demo.entities.User;
import com.example.demo.enums.SortingType;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.products.ProductType;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;
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
    private final ProductService productService;
    private final DtoMapper mapper;

    @GetMapping("/query")
    public List<Product> filteredProducts(@RequestBody ProductFilter filter,@RequestParam int pageNum,
                                          @RequestParam int size, @RequestParam SortingType sortingType){
        return productService.filteredProducts(filter,pageNum,size,sortingType);
    }

    @SneakyThrows
    @GetMapping("/")
    public List<Product> currentUserProducts(@AuthenticationPrincipal User user, @RequestParam ProductType productType){
        return productService.mapToType(user, productType);
    }

    @GetMapping("/wishlist")
    public List<Product> wishlist(@AuthenticationPrincipal User user){
        return user.getWishList();
    }

    @GetMapping("/{id}")
    public List<Product> otherUserProducts(@PathVariable Long id,@RequestParam ProductType productType){
        return productService.otherUserProducts(id,productType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("")
    public void addProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO productDto){
        Product product = mapper.productDtoToProduct(productDto);
        productService.addProduct(user,product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{index}")
    public void deleteProduct(@AuthenticationPrincipal User user, @PathVariable int index) {
        productService.deleteProduct(user, index);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{index}")
    public void updateProduct(@AuthenticationPrincipal User user,@PathVariable int index,@RequestBody ProductDTO productDto){
        Product product = mapper.productDtoToProduct(productDto);
        productService.updateProduct(user, index,product);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{index}")
    public void addToWishList(@AuthenticationPrincipal User wisher, @PathVariable int index,
                              @RequestParam Long sellerId){
        productService.addToWishList(wisher,index,sellerId);
    }
}
