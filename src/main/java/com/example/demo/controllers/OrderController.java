package com.example.demo.controllers;

import com.example.demo.dtos.ProductOrderDTO;
import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.services.OrderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final DtoMapper mapper;
    //Подтверждаем доставку
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/delivered/{id}")
    public void closeOrder(@AuthenticationPrincipal User user, @PathVariable Long id){
        orderService.closeOrder(id,user);
    }

    //Заказы данного пользователя
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/buying")
    public List<ProductOrderDTO> currentUserBuyingOrders(@AuthenticationPrincipal User currentUser){
        return mapCollectionToDto(currentUser.getBuyingOrders());
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/selling")
    public List<ProductOrderDTO> currentUserSellingOrders(@AuthenticationPrincipal User currentUser){
        return mapCollectionToDto(currentUser.getSellingOrders());
    }

    @ResponseStatus(HttpStatus.FOUND)
    @PreAuthorize("hasAuthority(ROLE_ADMIN)")
    @GetMapping("/admin")
    public Page<ProductOrder> ordersAdmin(@RequestBody PageRequest pageRequest){
        return orderService.pageOrders(pageRequest);
    }

    //Посмотреть проданные товары выбранного пользователя, если он разрешил такой просмотр
    @GetMapping("/{id}")
    public ResponseEntity<List<ProductOrderDTO>> selectedUserOrdersHistory(@PathVariable Long id){
        Optional<List<ProductOrder>> productOrders = orderService.selectedUserOrdersHistory(id);
        if(productOrders.isEmpty()) return new ResponseEntity<>(HttpStatus.LOCKED);
        List<ProductOrderDTO> dtos = mapCollectionToDto(productOrders.get());
        return new ResponseEntity<>(dtos,HttpStatus.FOUND);
    }

    @PostMapping("/{sellerId}")
    public HttpStatusCode makeOrder(@AuthenticationPrincipal User buyer, @PathVariable Long sellerId, @RequestBody List<Product> products ) throws MessagingException {
        return orderService.makeOrder(buyer,products, sellerId);
    }

    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void confirmOrder(@AuthenticationPrincipal User seller, @PathVariable Long orderId){
        orderService.confirmOrder(seller,orderId);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@AuthenticationPrincipal User buyerOrSeller, @PathVariable Long orderId){
        orderService.cancelOrder(buyerOrSeller,orderId);
    }

    List<ProductOrderDTO> mapCollectionToDto(Collection<ProductOrder> orders){
        return orders.stream().map(order -> mapper.orderToDto(order)).toList();
    }
}
