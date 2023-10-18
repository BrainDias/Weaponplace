package com.example.demo.services;

import com.example.demo.entities.User;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.demo.services.EmailService.sendEmail;

@Service
public class NotificationService {
    @Value("${auction.owner.subject}")
    static String ownerSubject;
    @Value("${auction.owner.text}")
    static String ownerText;

    @Value("${auction.pretender.subject}")
    static String pretenderSubject;
    @Value("${auction.pretender.text}")
    static String pretenderText;

    @Value("${orer.seller.subject}")
    static String sellerSubject;
    @Value("${order.seller.text}")
    static String sellerText;

    public static void notifyAuctionClosed(User owner, User pretender) throws MessagingException {
        sendEmail(owner.getEmail(), ownerSubject,ownerText);
        sendEmail(pretender.getEmail(), pretenderSubject,pretenderText);
    }

    public static void notifyPendingOrder(User seller) throws MessagingException {
        sendEmail(seller.getEmail(), sellerSubject,sellerText);
    }
}
