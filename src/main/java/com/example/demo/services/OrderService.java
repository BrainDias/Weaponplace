package com.example.demo.services;

import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    public Page<ProductOrder> pageUsers(Pageable pageRequest){
        return orderRepository.findAll(pageRequest);
    }

    public void delivered(Long id) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setDelivered(true);
            orderRepository.save(order);
        });
    }
}
