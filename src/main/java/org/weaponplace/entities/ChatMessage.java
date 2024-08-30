package org.weaponplace.entities;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String recipient;
    private String content;
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    // Getters and Setters
}

