package com.example.demo.services;

import com.example.demo.entities.Auction;
import com.example.demo.entities.User;
import com.example.demo.filters.AuctionFilter;
import com.example.demo.products.Product;
import com.example.demo.repositories.AuctionRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuctionServiceTest {

    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    private User owner;
    private User pretender;
    private Auction auction;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        pretender = new User();
        auction = new Auction();
        products = new ArrayList<>();

        Product product = new Product();
        product.setName("Product 1");
        products.add(product);

        auction.setProducts(products);
    }

    @Test
    @Transactional
    void testCreateAuction() {
        // Выполняем вызов метода
        auctionService.createAuction(owner, auction);

        // Проверяем, что владелец был сохранен с обновленным списком продуктов
        verify(userRepository).save(owner);

        // Проверяем, что аукцион был сохранен
        verify(auctionRepository).save(auction);

        assertFalse(auction.getClosed());
        assertEquals(owner, auction.getOwner());
    }

    @Test
    void testCloseAuctions() {
        Streamable<Auction> auctionsToClose = mock(Streamable.class);

        when(auctionRepository.findToClose()).thenReturn(auctionsToClose);

        // Выполняем вызов метода
        auctionService.closeAuctions();

        // Проверяем, что метод endAuctions был вызван
        verify(auctionRepository).findToClose();
        verify(auctionService).endAuctions(auctionsToClose);
    }

    @Test
    @Transactional
    void testEndAuctions() throws MessagingException {
        // Настраиваем аукцион
        auction.setPretender(pretender);

        Streamable<Auction> auctionsToClose = Streamable.of(auction);

        // Выполняем вызов метода
        auctionService.endAuctions(auctionsToClose);

        // Проверяем, что претендент получил товары
        assertTrue(pretender.getProducts().containsAll(products));

        // Проверяем, что аукцион был закрыт
        assertTrue(auction.getClosed());

        // Проверяем, что уведомление было отправлено
        verify(notificationService).notifyAuctionClosed(owner, pretender);

        // Проверяем, что претендент был сохранен
        verify(userRepository).save(pretender);

        // Проверяем, что аукционы были сохранены
        verify(auctionRepository).saveAll(auctionsToClose);
    }

    @Test
    void testPlaceBidToAuction() {
        Long auctionId = 1L;
        Float price = 100.0f;

        // Выполняем вызов метода
        auctionService.placeBidToAuction(pretender, price, auctionId);

        // Проверяем, что обновление заявки на аукцион вызвано
        verify(auctionRepository).updateWithBid(pretender, auctionId, price);
    }

    @Test
    void testCurrentAuctions() {
        AuctionFilter filter = mock(AuctionFilter.class);
        when(filter.matches(any(Auction.class))).thenReturn(true);

        List<Auction> auctions = List.of(auction);

        when(auctionRepository.findAllByClosedIsFalse()).thenReturn(auctions);

        // Выполняем вызов метода
        List<Auction> result = auctionService.currentAuctions(filter);

        // Проверяем, что результат содержит все аукционы, соответствующие фильтру
        assertEquals(auctions, result);
        verify(filter).matches(auction);
    }
}

