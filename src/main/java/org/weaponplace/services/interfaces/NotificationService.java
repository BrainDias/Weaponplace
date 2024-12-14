package org.weaponplace.services.interfaces;

import org.weaponplace.entities.User;
import org.weaponplace.products.Product;

import jakarta.mail.MessagingException;
import java.util.List;

public interface NotificationService {

    void notifyAuctionClosed(User owner, User pretender) throws MessagingException;

    void notifyPendingOrder(User seller) throws MessagingException;

    void notifyWishedItemsAdded(User currUser, List<Product> wishedProducts) throws MessagingException;
}

