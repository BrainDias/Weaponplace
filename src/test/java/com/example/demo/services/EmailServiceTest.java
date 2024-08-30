package com.example.demo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendEmail() throws MessagingException {
        // Устанавливаем тестовые данные
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test email body";

        // Выполняем вызов метода sendEmail
        emailService.sendEmail(to, subject, text);

        // Проверяем, что emailSender создал MimeMessage
        verify(emailSender).createMimeMessage();

        // Проверяем, что письмо было корректно заполнено и отправлено
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        // Проверяем, что сообщение было отправлено
        verify(emailSender).send(mimeMessage);
    }
}

