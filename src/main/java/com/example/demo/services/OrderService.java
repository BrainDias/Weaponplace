package com.example.demo.services;

import com.example.demo.entities.ProductOrder;
import com.example.demo.entities.User;
import com.example.demo.products.Product;
import com.example.demo.repositories.OrderRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.demo.services.UserService.getUser;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    public Collection<ProductOrder> pageOrders(Pageable pageRequest){
        return (Collection<ProductOrder>) orderRepository.findAll(pageRequest);
    }

    public void closeOrder(Long id) {
            orderRepository.findById(id).ifPresent(order -> {
            order.setDelivered(true);
            orderRepository.save(order);
            });
    }

    public Optional<List<ProductOrder>> selectedUserOrdersHistory(Long id) {
        User selectedUser = getUser(id);
        if(selectedUser.isOrderHistoryHidden()) return Optional.empty();
        return Optional.of(selectedUser.getSellingOrders().stream().filter(order -> order.getDelivered()).toList());
    }


    public HttpStatusCode makeOrder(User buyer, List<Product> products, Long user_id) throws MessagingException {
        User seller = getUser(user_id);
        Class<?> productType = products.getClass().arrayType();
        List<Product> sellerProducts = seller.getProducts();
        Stream<Product> productsForSale = sellerProducts.stream().filter(product -> product.isForSale());
        if(!products.stream().allMatch(product -> productsForSale.toList().contains(product))) return HttpStatus.BAD_REQUEST;
        createOrder(buyer, products, seller, sellerProducts);
        notificationService.notifyPendingOrder(seller);
        return HttpStatus.CREATED;
    }

    @Transactional
    public void createOrder(User buyer, List<Product> products, User seller, List<Product> sellerProducts) {
        sellerProducts.removeAll(products);
        UserService.userRepository.save(seller);
        ProductOrder newOrder = new ProductOrder();
        newOrder.setBuyer(buyer);
        newOrder.setSeller(seller);
        newOrder.setDelivered(false);
        Float price=0f;
        for (Product product :
                products) {
            price+=product.getPrice();
        }
        newOrder.setPrice(price);
        newOrder.setProducts(products);
        orderRepository.save(newOrder);
    }
}
