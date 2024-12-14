package org.weaponplace.services.interfaces;

import jakarta.mail.MessagingException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.weaponplace.entities.User;
import org.weaponplace.enums.SortingType;
import org.weaponplace.filters.ProductFilter;
import org.weaponplace.products.Product;
import org.weaponplace.products.ProductType;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ProductService {

    void addProduct(User user, Product product);

    void deleteProduct(User user, int index);

    void updateProduct(User user, int index, Product product);

    @Cacheable("products")
    List<Product> otherUserProducts(Long id, ProductType productType);

    List<Product> mapToType(User user, ProductType productType);

    void addToWishList(User wisher, int index, Long sellerId);

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRateString = "${wishlist.update_cooldown}")
    void notifyWishLists() throws MessagingException;

    List<Product> filteredProducts(ProductFilter filter, int pageNum, int size, SortingType sortingType);
}

