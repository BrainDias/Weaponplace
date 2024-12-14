package org.weaponplace.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.weaponplace.dtos.AuctionDTO;
import org.weaponplace.entities.Auction;
import org.weaponplace.entities.User;
import org.weaponplace.filters.AuctionFilter;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.services.implementations.AuctionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@WebMvcTest(AuctionController.class)
public class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuctionServiceImpl auctionService;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private AuctionController auctionController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController).build();
    }

    @Test
    @WithMockUser
    void createAuction_shouldReturnCreatedStatus() throws Exception {
        AuctionDTO auctionDTO = new AuctionDTO(); // заполните как необходимо
        Auction auction = new Auction(); // заполните как необходимо

        when(mapper.auctionDtoToAuction(any(AuctionDTO.class))).thenReturn(auction);

        mockMvc.perform(post("/auction/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auctionDTO)))
                .andExpect(status().isCreated());

        verify(auctionService, times(1)).createAuction(any(User.class), any(Auction.class));
    }

    @Test
    @WithMockUser
    void placeBidToAuction_shouldReturnAcceptedStatus() throws Exception {
        mockMvc.perform(put("/auction/{auctionId}/bid", 1L)
                        .param("price", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(auctionService, times(1)).placeBidToAuction(any(User.class), anyFloat(), anyLong());
    }

    @Test
    @WithMockUser
    void currentAuctions_shouldReturnFoundStatusAndAuctions() throws Exception {
        Auction auction = new Auction(); // заполните как необходимо
        AuctionDTO auctionDTO = new AuctionDTO(); // заполните как необходимо

        when(auctionService.currentAuctions(any(AuctionFilter.class))).thenReturn(List.of(auction));
        when(mapper.auctionToAuctionDto(any(Auction.class))).thenReturn(auctionDTO);

        mockMvc.perform(get("/auction/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuctionFilter())))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$[0]").exists());

        verify(auctionService, times(1)).currentAuctions(any(AuctionFilter.class));
    }
}

