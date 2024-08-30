package org.weaponplace.services;

import org.weaponplace.entities.User;
import org.weaponplace.enums.SortingType;
import org.weaponplace.filters.ProductFilter;
import org.weaponplace.products.Product;
import org.weaponplace.products.ProductType;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public void addProduct(User user, Product product) {
        user.getProducts().add(product);
        userRepository.save(user);
    }

    public void deleteProduct(User user, int index) {
        user.getProducts().remove(index);
        userRepository.save(user);
    }

    public void updateProduct(User user, int index, Product product) {
        user.getProducts().set(index,product);
        userRepository.save(user);
    }
    @Cacheable("products")
    public List<Product> otherUserProducts(Long id, ProductType productType) {
        User user = userService.getUser(id);
        List<Product> allProducts = mapToType(user, productType);
        return allProducts.stream().filter(Product::isForSale).toList();
    }

    public List<Product> mapToType(User user, ProductType productType) {
        return user.getProducts().stream().filter(procuct->procuct.getProductType().equals(productType) && !procuct.isHidden()).toList();
    }

    public void addToWishList(User wisher, int index, Long sellerId) {
        userRepository.findById(sellerId).ifPresentOrElse(seller -> {
            Product wishedProduct = seller.getProducts().get(index);
            wisher.getWishList().add(wishedProduct);
        },()->{
            throw new IllegalArgumentException("User or product doesn't exist");
        });
    }

    @Scheduled(timeUnit =  TimeUnit.HOURS, fixedRateString = "${wishlist.update_cooldown}")
    public void notifyWishLists() throws MessagingException {
        List<User> activeUsers = userRepository.findAllByActiveIsTrue();
        for (User currUser : activeUsers) {
            List<ProductFilter> wishFilters = currUser.getWishFilters();
            List<Product> wishedProducts = activeUsers.stream().flatMap(user -> user.getProducts().stream())
                    .filter(Product::isForSale)
                    .filter(product -> !currUser.getWishNotifiedProducts().contains(product))
                    .filter(product -> wishFilters.stream().anyMatch(productFilter -> productFilter.matches(product)))
                    .toList();
            if (!wishedProducts.isEmpty()) {
                notificationService.notifyWishedItemsAdded(currUser,wishedProducts);
            }
        }
    }

    public List<Product> filteredProducts(ProductFilter filter, int pageNum, int size, SortingType sortingType) {
        List<Product> products = userRepository.findAllByActiveIsTrue().stream()
                .flatMap(user -> user.getProducts().stream())
                .filter(Product::isForSale)
                .filter(filter::matches)
                .skip(pageNum * (size - 1))
                .limit(size)
                .toList();
        switch (sortingType){
            case NEW -> products.sort((product1,product2)->product1.getCreatedAt().compareTo(product2.getCreatedAt()));
            case PRICE_ASC -> products.sort((product1,product2)->product1.getPrice().compareTo(product2.getPrice()));
            case PRICE_DESC -> products.sort((product1,product2)->product2.getPrice().compareTo(product1.getPrice()));
        }
        return products;
    }
}
