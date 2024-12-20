package org.weaponplace.services.interfaces;

import jakarta.mail.MessagingException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
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

    HttpStatus deleteProduct(User user, int index);

    HttpStatus updateProduct(User user, Product product);

    List<Product> otherUserProducts(Long id, ProductType productType);

    List<Product> mapToType(User user, ProductType productType);

    void addToWishList(User wisher, int index, Long sellerId);

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRateString = "${wishlist.update_cooldown}")
    void notifyWishLists() throws MessagingException;

    List<Product> filteredProducts(ProductFilter filter, int pageNum, int size, SortingType sortingType);
}

