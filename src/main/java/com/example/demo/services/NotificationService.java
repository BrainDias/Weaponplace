package com.example.demo.services;

import com.example.demo.entities.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

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

    public void notifyAuctionClosed(User owner, User pretender) throws MessagingException {
        emailService.sendEmail(owner.getEmail(), ownerSubject,ownerText);
        emailService.sendEmail(pretender.getEmail(), pretenderSubject,pretenderText);
    }

    public void notifyPendingOrder(User seller) throws MessagingException {
        emailService.sendEmail(seller.getEmail(), sellerSubject,sellerText);
    }
}
