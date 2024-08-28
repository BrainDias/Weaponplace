package com.example.demo.services;

import com.example.demo.dtos.AuctionDTO;
import com.example.demo.entities.Auction;
import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.repositories.AuctionRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.Instant;
import java.util.List;
import java.util.Optional;

import static feign.ReflectiveFeign.TargetSpecificationVerifier.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
public class AuctionServiceTest {

    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAuction() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        AuctionDTO dto = new AuctionDTO();
        Float startPrice = 100.0f;
        String title = "Test Auction";

        // Create a list of products
        List<Product> products = new ArrayList<>();

        // Create product instances
        Ammo ammo = new Ammo();
        ammo.setPrice(10.0f);
        ammo.setForSale(true);
        ammo.setHidden(false);

        AssaultRifle ar = new AssaultRifle();
        ar.setPrice(50.0f);
        ar.setForSale(true);
        ar.setHidden(false);

        // Add products to the list
        products.add(ammo);
        products.add(ar);

        // Mock the repository's save methods
        when(userRepository.save(owner)).thenReturn(owner);
        when(auctionRepository.save(Mockito.any(Auction.class))).thenAnswer(invocation -> {
            Auction savedAuction = invocation.getArgument(0);
            savedAuction.setId(1L); // Set an ID for the saved auction
            return savedAuction;
        });

        //TODO: test should process Auction object not DTO
        // Act
        auctionService.createAuction(owner, dto);

        // Assert
        assertEquals(1, owner.getOwnedAuctions().size());

        Auction createdAuction = owner.getOwnedAuctions().iterator().next();
        assertNotNull(createdAuction);
        assertEquals(owner, createdAuction.getOwner());
        assertEquals(startPrice, createdAuction.getStartPrice());
        assertEquals(title, createdAuction.getTitle());
        assertEquals(dto.getDescription(), createdAuction.getDescription());

        // Verify that save methods were called
        Mockito.verify(userRepository, times(1)).save(owner);
        Mockito.verify(auctionRepository, times(1)).save(Mockito.any(Auction.class));

        // Additional assertions for products
        List<Product> auctionProducts = createdAuction.getProducts();
        assertEquals(2, auctionProducts.size());
        assertTrue(auctionProducts.contains(ammo));
        assertTrue(auctionProducts.contains(ar));
    }
    @Test
    public void testCloseAuctions() {
        // Arrange
        Auction auction1 = new Auction();
        Auction auction2 = new Auction();
        List<Auction> openAuctions = new ArrayList<>();
        openAuctions.add(auction1);
        openAuctions.add(auction2);

        // Mock the repository to return open auctions
        when(auctionRepository.findAll()).thenReturn(openAuctions);

        // Set the closing time of the auctions to a past time
        Instant pastTime = new Instant(System.currentTimeMillis() - 10000);
        auction1.setClosingAt(pastTime);
        auction2.setClosingAt(pastTime);

        // Mock the repository's save method
        when(auctionRepository.saveAll(Mockito.any())).thenReturn(new ArrayList<>());

        // Act
        auctionService.closeAuctions();

        // Assert
        assertEquals(true, auction1.getClosed());
        assertEquals(true, auction2.getClosed());
        // Add more assertions as needed
    }

    @Test
    public void testEndAuctions() {
        // Create test data
        User pretender = new User();
        List<Product> products = new ArrayList<>();
        // Add products to the list
        // Create a test auction with the above data
        Auction testAuction = new Auction();
        testAuction.setPretender(pretender);
        testAuction.setProducts(products);

        // Mock repository methods
        when(auctionRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Auction> savedAuctions = invocation.getArgument(0);
            // Here you can assert or manipulate the saved auctions as needed.
            return savedAuctions;
        });

        // Execute the method
        auctionService.endAuctions(Streamable.of(testAuction));

        // Assert or verify your expectations
        assertTrue(testAuction.getClosed());
        verify(userRepository).save(pretender);
        // Add more verifications or assertions as needed
    }

    @Test
    public void testPlaceBidToAuction() {
        // Create test data
        User pretender = new User();
        Auction auction = new Auction();
        auction.setLastPrice(100.0f);
        auction.setClosed(false);

        // Mock repository methods
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(auctionRepository.save(auction)).thenReturn(auction);

        // Execute the method
        HttpStatusCode result = auctionService.placeBidToAuction(pretender, 150.0f, 1L);

        // Assert or verify your expectations
        assertEquals(HttpStatus.ACCEPTED, result);
        assertEquals(pretender, auction.getPretender());
        assertEquals(150.0f, auction.getLastPrice());
        assertTrue(auction.getClosed());
    }

    @Test
    public void testPlaceBidToAuctionAuctionNotFound() {
        // Create test data
        User pretender = new User();

        // Mock repository methods
        when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        // Execute the method
        HttpStatusCode result = auctionService.placeBidToAuction(pretender, 150.0f, 1L);

        // Assert or verify your expectations
        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void testPlaceBidToAuctionInvalidPrice() {
        // Create test data
        User pretender = new User();
        Auction auction = new Auction();
        auction.setLastPrice(200.0f); // Higher than the bid price
        auction.setClosed(false);

        // Mock repository methods
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        // Execute the method
        HttpStatusCode result = auctionService.placeBidToAuction(pretender, 150.0f, 1L);

        // Assert or verify your expectations
        assertEquals(HttpStatus.PRECONDITION_FAILED, result);
    }
}
