package org.weaponplace.controllers;

import org.weaponplace.dtos.ProductDTO;
import org.weaponplace.filters.ProductFilter;
import org.weaponplace.entities.User;
import org.weaponplace.enums.SortingType;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.products.Product;
import org.weaponplace.products.ProductType;
import org.weaponplace.services.implementations.ProductServiceImpl;
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
    private final ProductServiceImpl productService;
    private final DtoMapper mapper;

    @GetMapping("/query")
    public List<ProductDTO> filteredProducts(@RequestBody ProductFilter filter, @RequestParam int pageNum,
                                          @RequestParam int size, @RequestParam SortingType sortingType){
        return productService.filteredProducts(filter,pageNum,size,sortingType)
                .stream()
                .map(mapper::productToDto)
                .toList();
    }

    @SneakyThrows
    @GetMapping("")
    public List<ProductDTO> currentUserProducts(@AuthenticationPrincipal User user, @RequestParam ProductType productType){
        List<Product> products = productService.mapToType(user, productType);
        return products.stream().map(mapper::productToDto).toList();
    }

    @GetMapping("/wishlist")
    public List<ProductDTO> wishlist(@AuthenticationPrincipal User user){
        List<Product> wishList = user.getWishList();
        return wishList.stream().map(mapper::productToDto).toList();
    }

    @GetMapping("/{id}")
    public List<ProductDTO> otherUserProducts(@PathVariable Long id,@RequestParam ProductType productType){
        List<Product> products = productService.otherUserProducts(id, productType);
        return products.stream().map(mapper::productToDto).toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/")
    public void addProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO productDto){
        Product product = mapper.productDtoToProduct(productDto);
        productService.addProduct(user,product);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteProduct(@AuthenticationPrincipal User user, @PathVariable int id) {
        return productService.deleteProduct(user, id);
    }

    @PatchMapping("/")
    public HttpStatus updateProduct(@AuthenticationPrincipal User user, @RequestBody ProductDTO productDto){
        Product product = mapper.productDtoToProduct(productDto);
        return productService.updateProduct(user, product);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/{index}")
    public void addToWishList(@AuthenticationPrincipal User wisher, @PathVariable int index,
                              @RequestParam Long sellerId){
        productService.addToWishList(wisher,index,sellerId);
    }
}
