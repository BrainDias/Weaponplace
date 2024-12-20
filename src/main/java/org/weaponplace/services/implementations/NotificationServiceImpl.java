package org.weaponplace.services.implementations;

import org.weaponplace.entities.User;
import org.weaponplace.products.Product;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.weaponplace.services.interfaces.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailServiceImpl emailService;

    @Value("${auction.owner.subject}")
    String ownerSubject;
    @Value("${auction.owner.text}")
    String ownerText;

    @Value("${auction.pretender.subject}")
    String pretenderSubject;
    @Value("${auction.pretender.text}")
    String pretenderText;

    @Value("${order.seller.subject}")
    String sellerSubject;
    @Value("${order.seller.text}")
    String sellerText;

    @Value("${wishlist.added.notification.subject}")
    String wishListSubject;
    @Value("${wishlist.added.notification.before}")
    String wishNotificationBefore;
    @Value("${wishlist.added.notification.after}")
    String wishNotificationAfter;

    public void notifyAuctionClosed(User owner, User pretender) throws MessagingException {
        emailService.sendEmail(owner.getEmail(), ownerSubject,ownerText);
        emailService.sendEmail(pretender.getEmail(), pretenderSubject,pretenderText);
    }

    public void notifyPendingOrder(User seller) throws MessagingException {
        emailService.sendEmail(seller.getEmail(), sellerSubject,sellerText);
    }

    public void notifyWishedItemsAdded(User currUser, List<Product> wishedProducts) throws MessagingException {
        List<String> wishedProductsString = new ArrayList<>();
        wishedProducts.forEach(product -> wishedProductsString.add("<li>"+product.getName()+"</li>"));
        String finalEmailText = wishNotificationBefore + wishedProductsString + wishNotificationAfter;
        emailService.sendEmail(currUser.getEmail(),wishListSubject,finalEmailText);
    }
}
