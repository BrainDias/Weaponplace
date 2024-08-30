package org.weaponplace.controllers;

import org.weaponplace.dtos.ProductDTO;
import org.weaponplace.filters.ProductFilter;
import org.weaponplace.entities.User;
import org.weaponplace.enums.SortingType;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.products.Product;
import org.weaponplace.products.ProductType;
import org.weaponplace.services.ProductService;
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
    public List<Product> filteredProducts(@RequestBody ProductFilter filter, @RequestParam int pageNum,
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
