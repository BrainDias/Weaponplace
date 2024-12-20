package org.weaponplace.services.implementations;

import org.weaponplace.entities.ProductOrder;
import org.weaponplace.entities.User;
import org.weaponplace.products.Product;
import org.weaponplace.repositories.OrderRepository;
import org.weaponplace.repositories.ProductRepository;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weaponplace.services.TransactionalOrderService;
import org.weaponplace.services.interfaces.OrderService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final NotificationServiceImpl notificationService;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final TransactionalOrderService transactionalOrderService;
    private final ProductRepository productRepository;

    public Page<ProductOrder> pageOrders(Pageable pageRequest) {
        return orderRepository.findAll(pageRequest);
    }

    public void closeOrder(Long id, User user) {
        orderRepository.findById(id).ifPresent(order -> {
            if (order.getBuyer().equals(user)) {
                order.setDelivered(true);
                orderRepository.saveAndFlush(order);
            }
        });
    }

    public Optional<List<ProductOrder>> selectedUserOrdersHistory(Long id) {
        User selectedUser = userService.getUser(id);
        if (selectedUser.getOrderHistoryHidden()) return Optional.empty();
        return Optional.of(selectedUser.getSellingOrders().stream().filter(ProductOrder::getDelivered).toList());
    }


    public HttpStatusCode makeOrder(User buyer, List<Long> productIds, Long sellerId) throws MessagingException {
        User seller = userService.getUser(sellerId);
        List<Product> sellerProducts = seller.getProducts();
        Stream<Product> productsForSale = sellerProducts.stream().filter(Product::isForSale);
        Stream<Long> ids = productsForSale.map(Product::getId);
        if (!new HashSet<>(ids.toList()).containsAll(productIds))
            return HttpStatus.BAD_REQUEST;
        //notificationService.notifyPendingOrder(seller);
        // Загружаем продукты в текущую сессию
        List<Product> productsInOrder = productIds.stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found")))
                .toList();
        transactionalOrderService.createOrder(buyer, productsInOrder, seller);
        return HttpStatus.CREATED;
    }

    @Transactional
    public HttpStatus confirmOrder(User seller, Long orderId) {
        if(seller.getSellingOrders().stream().mapToLong(ProductOrder::getId).anyMatch(orderId::equals)) {
            ProductOrder order = orderRepository.findById(orderId).orElseThrow(() ->
                    new RuntimeException("Order not found"));
            seller.getProducts().removeAll(order.getProducts());
            userRepository.save(seller);
            order.setConfirmed(true);
            return HttpStatus.OK;
        }
        else return HttpStatus.FORBIDDEN;
    }

    @Transactional
    public HttpStatus cancelOrder(User buyerOrSeller, Long orderId) {
        ProductOrder order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        User seller = order.getSeller();
        if (order.getBuyer().equals(buyerOrSeller) || seller.equals(buyerOrSeller)) {
            if (order.getConfirmed()) {
                List<Product> orderProducts = order.getProducts();
                seller.getProducts().addAll(orderProducts);
                userRepository.save(seller);
            }
            orderRepository.delete(order);
            return HttpStatus.ACCEPTED;
        }
        else return HttpStatus.FORBIDDEN;
    }
}
