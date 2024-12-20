package org.weaponplace.services;

import org.weaponplace.entities.User;
import org.weaponplace.enums.SortingType;
import org.weaponplace.filters.ProductFilter;
import org.weaponplace.products.Product;
import org.weaponplace.products.ProductType;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.weaponplace.services.implementations.NotificationServiceImpl;
import org.weaponplace.services.implementations.ProductServiceImpl;
import org.weaponplace.services.implementations.UserServiceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private NotificationServiceImpl notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    private Product product;
    private ProductType productType;
    private ProductFilter productFilter;
    private SortingType sortingType;

    private User user;
    private User anotherUser;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        user = new User();
        anotherUser = new User();

        product = new Product();
        productType = ProductType.WEAPON;
        productFilter = new ProductFilter();
        sortingType = SortingType.NEW; // Примерный тип сортировки

        // Initialize products
        product.setProductType(productType);
        product.setForSale(true);
        product.setHidden(false);
        product.setCreatedAt(Instant.now());
        product.setPrice(100.0f);

        // Initialize user
        user.setWishFilters(List.of(productFilter));
        user.setWishNotifiedProducts(new ArrayList<>());
        user.setProducts(List.of(product));
        user.setRating(4.0f);
        user.setRatingsNumber(5);
        anotherUser.setProducts(List.of(product));
        anotherUser.setWishFilters(new ArrayList<>());
    }

    @Test
    void testAddProduct() {
        user.setProducts(new ArrayList<>());

        productService.addProduct(user, product);

        assertTrue(user.getProducts().contains(product));
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteProduct() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        user.setProducts(products);

        productService.deleteProduct(user, 0);

        assertTrue(user.getProducts().isEmpty());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateProduct() {
        Product newProduct = new Product();
        List<Product> products = new ArrayList<>();
        products.add(product);
        user.setProducts(products);

        productService.updateProduct(user, newProduct);

        assertEquals(newProduct, user.getProducts().get(0));
        verify(userRepository).save(user);
    }

    @Test
    void testOtherUserProducts() {
        user.setProducts(List.of(product));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Product> result = productService.otherUserProducts(1L, productType);

        assertTrue(result.contains(product));
        assertEquals(1, result.size());
        verify(userRepository).findById(anyLong());
    }

    @Test
    void testOtherUserProductsEmpty() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Product> result = productService.otherUserProducts(1L, productType);

        assertTrue(result.isEmpty());
        verify(userRepository).findById(anyLong());
    }

    @Test
    void testMapToType() {
        user.setProducts(List.of(product));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Product> result = productService.mapToType(user, productType);

        assertTrue(result.contains(product));
        assertEquals(1, result.size());
    }

    @Test
    void testAddToWishList() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        anotherUser.setProducts(products);
        user.setWishList(new ArrayList<>());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(anotherUser));

        productService.addToWishList(user, 0, 1L);

        assertTrue(user.getWishList().contains(product));
        verify(userRepository).findById(anyLong());
    }

    @Test
    void testAddToWishListUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> productService.addToWishList(user, 0, 1L));
        assertEquals("User or product doesn't exist", thrown.getMessage());
        verify(userRepository).findById(anyLong());
    }

    @Test
    void testNotifyWishLists() throws MessagingException {
        List<User> activeUsers = List.of(user);
        when(userRepository.findAllByActiveIsTrue()).thenReturn(activeUsers);

        when(productFilter.matches(any(Product.class))).thenReturn(true);

        productService.notifyWishLists();

        verify(notificationService).notifyWishedItemsAdded(eq(user), anyList());
    }

    @Test
    void testFilteredProducts() {
        List<Product> products = List.of(product);
        when(userRepository.findAllByActiveIsTrue()).thenReturn(List.of(user));

        List<Product> result = productService.filteredProducts(productFilter, 0, 10, sortingType);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userRepository).findAllByActiveIsTrue();
    }
}
