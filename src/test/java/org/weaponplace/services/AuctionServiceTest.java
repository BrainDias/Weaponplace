package org.weaponplace.services;

import org.weaponplace.entities.Auction;
import org.weaponplace.entities.User;
import org.weaponplace.filters.AuctionFilter;
import org.weaponplace.products.Product;
import org.weaponplace.repositories.AuctionRepository;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;
import org.weaponplace.services.implementations.AuctionServiceImpl;
import org.weaponplace.services.implementations.NotificationServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuctionServiceTest {

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationServiceImpl notificationService;

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

