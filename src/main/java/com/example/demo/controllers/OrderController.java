package com.example.demo.controllers;

import com.example.demo.dtos.ProductOrderDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final DtoMapper mapper;
    //Подтверждаем доставку
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/delivered/{id}")
    public void delivered(@PathVariable Long id){
        orderService.delivered(id);
    }

    //Заказы данного пользователя
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/orders")
    public List<ProductOrderDTO> currentUserOrders(@AuthenticationPrincipal User currentUser){
        return currentUser.getOrders().stream().map(order->mapper.orderToDto(order)).toList();
    }
}
