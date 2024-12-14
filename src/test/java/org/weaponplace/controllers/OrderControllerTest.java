package org.weaponplace.controllers;

import org.weaponplace.dtos.ProductOrderDTO;
import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.products.Product;
import org.weaponplace.services.implementations.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private DtoMapper mapper;

    @Test
    @WithMockUser
    void closeOrder_shouldReturnAccepted() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/orders/delivered/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser
    void currentUserBuyingOrders_shouldReturnOrders() throws Exception {
        // Arrange
        User user = new User();
        List<ProductOrder> orders = List.of(new ProductOrder());
        user.setBuyingOrders(orders);

        List<ProductOrderDTO> dtos = List.of(new ProductOrderDTO());

        when(mapper.orderToDto(Mockito.any(ProductOrder.class))).thenReturn(dtos.get(0));

        // Act & Assert
        mockMvc.perform(get("/orders/buying"))
                .andExpect(status().isFound())
                .andExpect(content().json("[ ]"));  // Expected JSON output
    }

    @Test
    @WithMockUser
    void currentUserSellingOrders_shouldReturnOrders() throws Exception {
        // Arrange
        User user = new User();
        List<ProductOrder> orders = List.of(new ProductOrder());
        user.setSellingOrders(orders);

        List<ProductOrderDTO> dtos = List.of(new ProductOrderDTO());

        when(mapper.orderToDto(Mockito.any(ProductOrder.class))).thenReturn(dtos.get(0));

        // Act & Assert
        mockMvc.perform(get("/orders/selling"))
                .andExpect(status().isFound())
                .andExpect(content().json("[ ]"));  // Expected JSON output
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ordersAdmin_shouldReturnOrders() throws Exception {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductOrder> productOrders = Page.empty();

        when(orderService.pageOrders(pageRequest)).thenReturn(productOrders);

        // Act & Assert
        mockMvc.perform(get("/orders/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ }"))  // JSON body for PageRequest
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser
    void selectedUserOrdersHistory_shouldReturnOrdersIfUnlocked() throws Exception {
        // Arrange
        List<ProductOrder> orders = List.of(new ProductOrder());
        when(orderService.selectedUserOrdersHistory(Mockito.anyLong())).thenReturn(Optional.of(orders));

        List<ProductOrderDTO> dtos = List.of(new ProductOrderDTO());
        when(mapper.orderToDto(Mockito.any(ProductOrder.class))).thenReturn(dtos.get(0));

        // Act & Assert
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isFound())
                .andExpect(content().json("[ ]"));  // Expected JSON output
    }

    @Test
    @WithMockUser
    void selectedUserOrdersHistory_shouldReturnLockedIfOrdersHidden() throws Exception {
        // Arrange
        when(orderService.selectedUserOrdersHistory(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isLocked());
    }

    @Test
    @WithMockUser
    void makeOrder_shouldReturnCreated() throws Exception {
        // Arrange
        List<Product> products = List.of(new Product());

        when(orderService.makeOrder(Mockito.any(User.class), Mockito.anyList(), Mockito.anyLong()))
                .thenReturn(HttpStatus.CREATED);

        // Act & Assert
        mockMvc.perform(post("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[ ]"))  // JSON body for products
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void confirmOrder_shouldReturnAccepted() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/orders/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser
    void cancelOrder_shouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }
}

