package com.example.demo.controllers;

import com.example.demo.dtos.ProductOrderDTO;
import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.products.Product;
import com.example.demo.services.OrderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final DtoMapper mapper;
    //Подтверждаем доставку

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/delivered/{id}")
    public void closeOrder(@PathVariable Long id){
        orderService.closeOrder(id);
    }

    //Заказы данного пользователя
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/orders/buying")
    public List<ProductOrderDTO> currentUserBuyingOrders(@AuthenticationPrincipal User currentUser){
        return mapCollectionToDto(currentUser.getBuyingOrders());
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/orders/selling")
    public List<ProductOrderDTO> currentUserSellingOrders(@AuthenticationPrincipal User currentUser){
        return mapCollectionToDto(currentUser.getSellingOrders());
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/orders/admin")
    //TODO: Return Specifical DTOs for admins
    public List<ProductOrderDTO> ordersAdmin(@RequestBody PageRequest pageRequest){
        return mapCollectionToDto(orderService.pageOrders(pageRequest));
    }

    //Посмотреть проданные товары выбранного пользователя, если он разрешил такой просмотр
    @GetMapping("/orders/{id}")
    public ResponseEntity<List<ProductOrderDTO>> selectedUserOrdersHistory(@PathVariable Long id){
        Optional<List<ProductOrder>> productOrders = orderService.selectedUserOrdersHistory(id);
        if(productOrders.isEmpty()) return new ResponseEntity<>(HttpStatus.LOCKED);
        List<ProductOrderDTO> dtos = mapCollectionToDto(productOrders.get());
        return new ResponseEntity<>(dtos,HttpStatus.FOUND);
    }

    @PostMapping("/orders/{seller_id}")
    public HttpStatusCode makeOrder(@AuthenticationPrincipal User buyer, @PathVariable Long seller_id, @RequestBody List<Product> products ) throws MessagingException {
        return orderService.makeOrder(buyer,products, seller_id);
    }

    List<ProductOrderDTO> mapCollectionToDto(Collection<ProductOrder> orders){
        return orders.stream().map(order -> mapper.orderToDto(order)).toList();
    }
}
