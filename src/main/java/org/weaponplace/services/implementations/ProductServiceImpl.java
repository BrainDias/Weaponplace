package org.weaponplace.services.implementations;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.weaponplace.entities.User;
import org.weaponplace.enums.SortingType;
import org.weaponplace.filters.ProductFilter;
import org.weaponplace.products.Product;
import org.weaponplace.products.ProductType;
import org.weaponplace.repositories.ProductRepository;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.weaponplace.services.interfaces.ProductService;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final NotificationServiceImpl notificationService;
    private final ProductRepository productRepository;

    public void addProduct(User user, Product product) {
        user.getProducts().add(product);
        userRepository.save(user);
    }

    public HttpStatus deleteProduct(User user, int id) {
        if(user.getProducts().stream().mapToLong(Product::getId).anyMatch(i -> i == id)){
            productRepository.deleteById((long) id);
            productRepository.flush();
            return HttpStatus.NO_CONTENT;
        }
        else return HttpStatus.FORBIDDEN;
    }

    public HttpStatus updateProduct(User user, Product product) {
        if(user.getProducts().stream().mapToLong(Product::getId).anyMatch(i -> i == product.getId())) {
            productRepository.saveAndFlush(product);
            return HttpStatus.ACCEPTED;
        }
        else return HttpStatus.FORBIDDEN;
    }
    @Cacheable("products")
    public List<Product> otherUserProducts(Long id, ProductType productType) {
        User user = userService.getUser(id);
        List<Product> allProducts = mapToType(user, productType);
        return allProducts.stream().filter(Product::isForSale).toList();
    }

    public List<Product> mapToType(User user, ProductType productType) {
        List<Product> products = user.getProducts();
        return products.stream().filter(product->product.getProductType().equals(productType) && !product.isHidden()).toList();
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
        Pageable pageable = PageRequest.of(pageNum,size);
        List<Product> products = productRepository.findAllByForSaleIsTrue(pageable).stream()
                .filter(filter::matches)
                .sorted(getComparator(sortingType))
                .toList();
        return products;
    }

    private Comparator<Product> getComparator(SortingType sortingType) {
        return switch (sortingType) {
            case NEW -> Comparator.comparing(Product::getCreatedAt,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case PRICE_ASC -> Comparator.comparing(Product::getPrice,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case PRICE_DESC -> Comparator.comparing(Product::getPrice,
                    Comparator.nullsLast(Comparator.naturalOrder())).reversed();
        };
    }
}
