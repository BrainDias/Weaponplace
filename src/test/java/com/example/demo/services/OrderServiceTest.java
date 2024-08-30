package com.example.demo.services;

import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private User buyer;
    private User seller;
    private Product product;
    private ProductOrder order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        buyer = new User();
        seller = new User();

        product = new Product();
        product.setPrice(100.0f);
        product.setForSale(true);

        buyer.setProducts(new ArrayList<>());
        buyer.getProducts().add(product);

        seller.setProducts(new ArrayList<>());
        seller.getProducts().add(product);

        order = new ProductOrder();
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setProducts(List.of(product));
        order.setPrice(100.0f);
        order.setDelivered(false);
        order.setConfirmed(false);
    }

    @Test
    void testPageOrders() {
        Pageable pageable = Pageable.unpaged();
        Page<ProductOrder> ordersPage = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(pageable)).thenReturn(ordersPage);

        Page<ProductOrder> result = orderService.pageOrders(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository).findAll(pageable);
    }

    @Test
    void testCloseOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.closeOrder(1L, buyer);

        assertTrue(order.getDelivered());
        verify(orderRepository).save(order);
    }

    @Test
    void testCloseOrderUnauthorizedUser() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.closeOrder(1L, new User());

        assertFalse(order.getDelivered());
        verify(orderRepository, never()).save(any(ProductOrder.class));
    }

    @Test
    void testSelectedUserOrdersHistory() {
        buyer.setOrderHistoryHidden(false);
        buyer.setSellingOrders(List.of(order));
        when(userService.getUser(anyLong())).thenReturn(buyer);

        Optional<List<ProductOrder>> result = orderService.selectedUserOrdersHistory(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(userService).getUser(anyLong());
    }

    @Test
    void testSelectedUserOrdersHistoryHidden() {
        buyer.setOrderHistoryHidden(true);
        when(userService.getUser(anyLong())).thenReturn(buyer);

        Optional<List<ProductOrder>> result = orderService.selectedUserOrdersHistory(1L);

        assertTrue(result.isEmpty());
        verify(userService).getUser(anyLong());
    }

    @Test
    void testMakeOrder() throws MessagingException {
        when(userService.getUser(anyLong())).thenReturn(seller);

        HttpStatusCode result = orderService.makeOrder(buyer, List.of(product), seller.getId());

        assertEquals(HttpStatus.CREATED, result);
        verify(notificationService).notifyPendingOrder(seller);
        verify(orderRepository).save(any(ProductOrder.class));
    }

    @Test
    void testMakeOrderBadRequest() throws MessagingException {
        when(userService.getUser(anyLong())).thenReturn(seller);

        HttpStatusCode result = orderService.makeOrder(buyer, List.of(product), seller.getId() + 1);

        assertEquals(HttpStatus.BAD_REQUEST, result);
        verify(notificationService, never()).notifyPendingOrder(any(User.class));
        verify(orderRepository, never()).save(any(ProductOrder.class));
    }

    @Test
    void testConfirmOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.confirmOrder(seller, 1L);

        assertTrue(order.getConfirmed());
        verify(userRepository).save(seller);
    }

    @Test
    void testConfirmOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderService.confirmOrder(seller, 1L));
        assertEquals("Order not found", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCancelOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.cancelOrder(buyer, 1L);

        assertTrue(order.getSeller().getProducts().contains(product));
        verify(orderRepository).delete(order);
    }

    @Test
    void testCancelOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> orderService.cancelOrder(buyer, 1L));
        assertEquals("Order not found", thrown.getMessage());
        verify(orderRepository, never()).delete(any(ProductOrder.class));
    }

    @Test
    void testCancelOrderNotAuthorized() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.cancelOrder(new User(), 1L);

        assertFalse(order.getSeller().getProducts().contains(product));
        verify(orderRepository).delete(order);
    }
}

