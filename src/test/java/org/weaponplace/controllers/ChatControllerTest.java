package org.weaponplace.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.weaponplace.entities.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class ChatControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_shouldReturnChatMessage() {
        // Arrange
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hello World");

        // Act
        ChatMessage result = chatController.sendMessage(chatMessage);

        // Assert
        assertEquals(chatMessage, result);
    }

    @Test
    void sendPrivateMessage_shouldInvokeMessagingTemplate() {
        // Arrange
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Private message");
        chatMessage.setRecipient("user1");

        // Act
        chatController.sendPrivateMessage(chatMessage);

        // Assert
        verify(messagingTemplate, times(1))
                .convertAndSendToUser(anyString(), anyString(), any(ChatMessage.class));
    }
}

