package org.weaponplace.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.products.Product;
import org.weaponplace.repositories.OrderRepository;

import java.util.List;

//Created to avoid transactional self-invocation
@Service
@RequiredArgsConstructor
public class TransactionalOrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(User buyer, List<Product> products, User seller) {
        ProductOrder newOrder = new ProductOrder();
        newOrder.setBuyer(buyer);
        newOrder.setSeller(seller);
        newOrder.setDelivered(false);
        newOrder.setConfirmed(false);
        Float price = 0f;
        for (Product product :
                products) {
            price += product.getPrice();
        }
        newOrder.setPrice(price);
        newOrder.setProducts(products);
        orderRepository.save(newOrder);
    }
}
