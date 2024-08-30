package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    private User owner;
    private User pretender;
    private User seller;
    private User currUser;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        pretender = new User();
        seller = new User();
        currUser = new User();

        owner.setEmail("owner@example.com");
        pretender.setEmail("pretender@example.com");
        seller.setEmail("seller@example.com");
        currUser.setEmail("user@example.com");

        product = new Product();
        product.setName("Product 1");
    }

    @Test
    void testNotifyAuctionClosed() throws MessagingException {
        // Выполняем вызов метода
        notificationService.notifyAuctionClosed(owner, pretender);

        // Проверяем, что emailService был вызван с правильными параметрами
        verify(emailService).sendEmail(owner.getEmail(), "Auction Closed", "Auction owner text");
        verify(emailService).sendEmail(pretender.getEmail(), "Auction Closed", "Auction pretender text");
    }

    @Test
    void testNotifyPendingOrder() throws MessagingException {
        // Выполняем вызов метода
        notificationService.notifyPendingOrder(seller);

        // Проверяем, что emailService был вызван с правильными параметрами
        verify(emailService).sendEmail(seller.getEmail(), "Pending Order", "Order text for seller");
    }

    @Test
    void testNotifyWishedItemsAdded() throws MessagingException {
        // Создаем список продуктов
        List<Product> wishedProducts = List.of(product);

        // Выполняем вызов метода
        notificationService.notifyWishedItemsAdded(currUser, wishedProducts);

        // Проверяем, что emailService был вызван с правильными параметрами
        verify(emailService).sendEmail(eq(currUser.getEmail()), eq("Wishlist Updated"), anyString());
    }

    @Test
    void testNotifyWishedItemsAddedWithMultipleProducts() throws MessagingException {
        // Создаем несколько продуктов
        Product product2 = new Product();
        product2.setName("Product 2");

        List<Product> wishedProducts = List.of(product, product2);

        // Выполняем вызов метода
        notificationService.notifyWishedItemsAdded(currUser, wishedProducts);

        // Проверяем, что emailService был вызван с правильными параметрами
        verify(emailService).sendEmail(eq(currUser.getEmail()), eq("Wishlist Updated"), anyString());
    }
}

