package org.weaponplace.services.implementations;

import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.products.Product;
import org.weaponplace.repositories.OrderRepository;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weaponplace.services.interfaces.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final NotificationServiceImpl notificationService;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    public Page<ProductOrder> pageOrders(Pageable pageRequest){
        return orderRepository.findAll(pageRequest);
    }

    public void closeOrder(Long id, User user) {
            orderRepository.findById(id).ifPresent(order -> {
                if (order.getBuyer().equals(user)) {
                    order.setDelivered(true);
                    orderRepository.save(order);
                }
            });
    }

    public Optional<List<ProductOrder>> selectedUserOrdersHistory(Long id) {
        User selectedUser = userService.getUser(id);
        if(selectedUser.getOrderHistoryHidden()) return Optional.empty();
        return Optional.of(selectedUser.getSellingOrders().stream().filter(order -> order.getDelivered()).toList());
    }


    public HttpStatusCode makeOrder(User buyer, List<Product> products, Long sellerId) throws MessagingException {
        User seller = userService.getUser(sellerId);
        List<Product> sellerProducts = seller.getProducts();
        Stream<Product> productsForSale = sellerProducts.stream().filter(product -> product.isForSale());
        if(!products.stream().allMatch(product -> productsForSale.toList().contains(product))) return HttpStatus.BAD_REQUEST;
        notificationService.notifyPendingOrder(seller);
        createOrder(buyer, products, seller);
        return HttpStatus.CREATED;
    }

    @Transactional
    public void createOrder(User buyer, List<Product> products, User seller) {
        ProductOrder newOrder = new ProductOrder();
        newOrder.setBuyer(buyer);
        newOrder.setSeller(seller);
        newOrder.setDelivered(false);
        newOrder.setConfirmed(false);
        Float price=0f;
        for (Product product :
                products) {
            price+=product.getPrice();
        }
        newOrder.setPrice(price);
        newOrder.setProducts(products);
        orderRepository.save(newOrder);
    }

    @Transactional
    public void confirmOrder(User seller, Long orderId) {
        ProductOrder order = orderRepository.findById(orderId).orElseThrow(() ->
                new RuntimeException("Order not found"));
        seller.getProducts().removeAll(order.getProducts());
        userRepository.save(seller);
        order.setConfirmed(true);
    }

    public void cancelOrder(User buyerOrSeller, Long orderId) {
        ProductOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        User seller = order.getSeller();
        if(order.getBuyer().equals(buyerOrSeller) || seller.equals(buyerOrSeller)) {
            if(order.getConfirmed()) {
                List<Product> orderProducts = order.getProducts();
                seller.getProducts().addAll(orderProducts);
                userRepository.save(seller);
            }
            orderRepository.delete(order);
        }
    }
}
