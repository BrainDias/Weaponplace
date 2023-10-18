package com.example.demo.services;

import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.repositories.OrderRepository;
import jakarta.mail.MessagingException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService; // Assuming a UserService for getUser

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPageOrders() {
        // Arrange
        Pageable pageRequest = PageRequest.of(0, 10);
        List<ProductOrder> orders = new ArrayList<>(); // Create a list of orders

        // Mock the repository's findAll method
        Mockito.when(orderRepository.findAll(pageRequest)).thenReturn((Page<ProductOrder>) orders);

        // Act
        Collection<ProductOrder> result = orderService.pageOrders(pageRequest);

        // Assert
        assertSame(orders, result);
    }

    @Test
    public void testCloseOrder() {
        // Arrange
        Long orderId = 1L;
        ProductOrder order = new ProductOrder();
        order.setId(orderId);
        order.setDelivered(false);

        // Mock the repository methods
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(order)).thenReturn(order);

        // Act
        orderService.closeOrder(orderId);

        // Assert
        assertTrue(order.isDelivered());
        // Verify that save method was called
        Mockito.verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testSelectedUserOrdersHistory() {
        // Arrange
        Long userId = 1L;
        User selectedUser = new User();
        selectedUser.setId(userId);
        selectedUser.setOrderHistoryHidden(false);
        List<ProductOrder> userOrders = new ArrayList<>(); // Create a list of orders
        userOrders.add(new ProductOrder());
        userOrders.add(new ProductOrder());
        selectedUser.setSellingOrders(userOrders);

        // Mock the UserService method
        Mockito.when(userService.getUser(userId)).thenReturn(selectedUser);

        // Act
        Optional<List<ProductOrder>> result = orderService.selectedUserOrdersHistory(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    public void testMakeOrder() throws MessagingException {
        // Arrange
        User buyer = new User();
        List<Product> products = new ArrayList<>(); // Create a list of products
        Long sellerId = 2L;
        User seller = new User();
        seller.setId(sellerId);
        seller.setProducts(new ArrayList<>()); // Create a list of seller's products

        // Mock the UserService method
        Mockito.when(userService.getUser(sellerId)).thenReturn(seller);

        // Mock the repository's save methods
        Mockito.when(orderRepository.save(Mockito.any(ProductOrder.class))).thenReturn(new ProductOrder());

        // Act
        HttpStatusCode result = orderService.makeOrder(buyer, products, sellerId);

        // Assert
        assertEquals(HttpStatus.CREATED, result);
        // Verify that save method for seller's products and orderRepository.save were called
        Mockito.verify(userService, times(1)).save(seller);
        Mockito.verify(orderRepository, times(1)).save(Mockito.any(ProductOrder.class));
    }

    @Test
    public void testCreateOrder() {
        // Arrange
        User seller = new User();
        seller.setProducts(new ArrayList<>()); // Create a list of seller's products
        Product product1 = new Product();
        product1.setPrice(50.0f);
        Product product2 = new Product();
        product2.setPrice(30.0f);
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        User buyer = new User();

        // Mock the UserService method
        Mockito.when(userService.getUser(seller.getId())).thenReturn(seller);

        // Mock the repository methods
        Mockito.when(orderRepository.save(Mockito.any(ProductOrder.class)).thenAnswer(invocation -> {
            ProductOrder savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L); // Set an ID for the saved order
            return savedOrder;
        });

        // Act
        orderService.createOrder(buyer, products, seller, seller.getProducts());

        // Assert
        assertEquals(0, seller.getProducts().size()); // Check that seller's products are removed
        // Verify that user service's save method and orderRepository.save were called
        Mockito.verify(userService, times(1)).save(seller);
        Mockito.verify(orderRepository, times(1)).save(Mockito.any(ProductOrder.class));
    }
}

