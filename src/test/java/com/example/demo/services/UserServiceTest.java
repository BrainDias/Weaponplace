package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Ammo;
import com.example.demo.products.Product;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DtoMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPageUsers() {
        // Arrange
        Pageable pageRequest = PageRequest.of(0, 10);
        List<User> users = new ArrayList<>(); // Create a list of users

        // Mock the repository's findAll method
        Mockito.when(userRepository.findAll(pageRequest)).thenReturn(Page.empty());

        // Act
        Page<User> result = userService.pageUsers(pageRequest);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testBanUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(true);

        // Mock the repository methods
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.banUser(userId);

        // Assert
        assertFalse(user.isActive());
        // Verify that save method was called
        Mockito.verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAddProduct() {
        // Arrange
        User user = new User();
        ProductDTO productDto = new ProductDTO();
        Product product = new Ammo();

        // Mock the repository methods and mapper
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(mapper.productDtoToAmmo(productDto)).thenReturn((Ammo) product);

        // Act
        userService.addProduct(user, productDto);

        // Assert
        assertEquals(1, user.getProducts().size());
        // Verify that save method was called
        Mockito.verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        User user = new User();
        Product product = new Ammo();
        user.getProducts().add(product);
        int index = 0;

        // Mock the repository method
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.deleteProduct(user, index);

        // Assert
        assertEquals(0, user.getProducts().size());
        // Verify that save method was called
        Mockito.verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        User user = new User();
        user.setId(1L);
        List<Product> products = new ArrayList<>();
        products.add(new Ammo()); // You may need to create instances of different Product subclasses
        user.setProducts(products);
        int index = 0;
        ProductDTO productDTO = new ProductDTO();

        // Mock the mapper to return a new instance of the updated product
        Ammo updatedAmmo = new Ammo();
        Mockito.when(mapper.productDtoToAmmo(productDTO)).thenReturn(updatedAmmo);

        // Mock the UserRepository
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.updateProduct(user, index, productDTO);

        // Assert
        // Verify that the mapper method was called
        Mockito.verify(mapper, times(1)).productDtoToAmmo(productDTO);

        // Verify that the product at the specified index has been updated
        Product updatedProduct = user.getProducts().get(index);
        assertEquals(updatedAmmo, updatedProduct);
    }

    @Test
    public void testOtherUserProducts() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Product product1 = new Ammo();
        Product product2 = new Ammo();
        product2.setHidden(true);
        user.getProducts().add(product1);
        user.getProducts().add(product2);

        String productType = "ammo";

        // Act
        List<? extends Product> result = userService.otherUserProducts(userId, productType);

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.get(0).isHidden());
    }

    @Test
    public void testGetUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Mock the repository method
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }
}

