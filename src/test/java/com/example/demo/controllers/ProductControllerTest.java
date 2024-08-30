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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private DtoMapper mapper;

    @Test
    @WithMockUser
    void filteredProducts_shouldReturnFilteredProducts() throws Exception {
        // Arrange
        ProductFilter filter = new ProductFilter();
        SortingType sortingType = SortingType.NEW;
        List<Product> products = List.of(new Product());

        when(productService.filteredProducts(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/products/query")
                        .param("pageNum", "0")
                        .param("size", "10")
                        .param("sortingType", "NEW")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))  // JSON body for filter
                .andExpect(status().isOk())
                .andExpect(content().json("[ ]"));  // Expected JSON output
    }

    @Test
    @WithMockUser
    void currentUserProducts_shouldReturnCurrentUserProducts() throws Exception {
        // Arrange
        User user = new User();
        ProductType productType = ProductType.WEAPON;
        List<Product> products = List.of(new Product());

        when(productService.mapToType(user, productType)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/products/")
                        .param("productType", "ELECTRONICS"))
                .andExpect(status().isOk())
                .andExpect(content().json("[ ]"));  // Expected JSON output
    }

    @Test
    @WithMockUser
    void wishlist_shouldReturnWishList() throws Exception {
        // Arrange
        User user = new User();
        List<Product> wishList = List.of(new Product());

        when(productService.mapToType(Mockito.any(), Mockito.any())).thenReturn(wishList);

        // Act & Assert
        mockMvc.perform(get("/products/wishlist"))
                .andExpect(status().isOk())
                .andExpect(content().json("[ ]"));  // Expected JSON output
    }

    @Test
    @WithMockUser
    void addProduct_shouldReturnCreated() throws Exception {
        // Arrange
        ProductDTO productDto = new ProductDTO();
        Product product = new Product();

        when(mapper.productDtoToProduct(productDto)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(put("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))  // JSON body for productDto
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void deleteProduct_shouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/products/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void updateProduct_shouldReturnOk() throws Exception {
        // Arrange
        ProductDTO productDto = new ProductDTO();
        Product product = new Product();

        when(mapper.productDtoToProduct(productDto)).thenReturn(product);

        // Act & Assert
        mockMvc.perform(patch("/products/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))  // JSON body for productDto
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void addToWishList_shouldReturnAccepted() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/products/0")
                        .param("sellerId", "1"))
                .andExpect(status().isAccepted());
    }
}

