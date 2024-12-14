package org.weaponplace.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.products.Product;

import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    Page<ProductOrder> pageOrders(Pageable pageRequest);

    void closeOrder(Long id, User user);

    Optional<List<ProductOrder>> selectedUserOrdersHistory(Long id);

    HttpStatusCode makeOrder(User buyer, List<Product> products, Long sellerId) throws MessagingException;

    void createOrder(User buyer, List<Product> products, User seller);

    void confirmOrder(User seller, Long orderId);

    void cancelOrder(User buyerOrSeller, Long orderId);
}

