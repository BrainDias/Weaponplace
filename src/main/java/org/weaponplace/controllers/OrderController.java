package org.weaponplace.controllers;

import org.springframework.data.domain.Pageable;
import org.weaponplace.dtos.ProductOrderDTO;
import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.products.Product;
import org.weaponplace.services.implementations.OrderServiceImpl;
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
    private final OrderServiceImpl orderService;
    private final DtoMapper mapper;
    //Подтверждаем доставку
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/delivered/{id}")
    //TODO: обработать случай обращения от чужого пользователя
    public void closeOrder(@AuthenticationPrincipal User user, @PathVariable Long id){
        orderService.closeOrder(id,user);
    }

    //Заказы данного пользователя
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/buying")
    public List<ProductOrderDTO> currentUserBuyingOrders(@AuthenticationPrincipal User currentUser){
        return mapCollectionToDto(currentUser.getBuyingOrders());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/selling")
    public List<ProductOrderDTO> currentUserSellingOrders(@AuthenticationPrincipal User currentUser){
        return mapCollectionToDto(currentUser.getSellingOrders());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public List<ProductOrderDTO> ordersAdmin(@RequestParam int pageNum, @RequestParam int size){
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<ProductOrder> productOrders = orderService.pageOrders(pageable);
        return productOrders.map(mapper::orderToDto).toList();
    }

    //Посмотреть проданные товары выбранного пользователя, если он разрешил такой просмотр
    @GetMapping("/{id}")
    public ResponseEntity<List<ProductOrderDTO>> selectedUserOrdersHistory(@PathVariable Long id){
        Optional<List<ProductOrder>> productOrders = orderService.selectedUserOrdersHistory(id);
        if(productOrders.isEmpty()) return new ResponseEntity<>(HttpStatus.LOCKED);
        List<ProductOrderDTO> dtos = mapCollectionToDto(productOrders.get());
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    @PostMapping("/{sellerId}")
    public HttpStatusCode makeOrder(@AuthenticationPrincipal User buyer,
                                    @PathVariable Long sellerId,
                                    @RequestBody List<Long> productIds ) throws MessagingException {
        return orderService.makeOrder(buyer,productIds, sellerId);
    }

    @PatchMapping("/{orderId}")
    public HttpStatus confirmOrder(@AuthenticationPrincipal User seller, @PathVariable Long orderId){
        return orderService.confirmOrder(seller,orderId);
    }

    @DeleteMapping("/{orderId}")
    public HttpStatus cancelOrder(@AuthenticationPrincipal User buyerOrSeller, @PathVariable Long orderId){
        return orderService.cancelOrder(buyerOrSeller,orderId);
    }

    List<ProductOrderDTO> mapCollectionToDto(Collection<ProductOrder> orders){
        return orders.stream().map(mapper::orderToDto).toList();
    }
}
