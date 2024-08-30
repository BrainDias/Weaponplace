package com.example.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Конфигурация брокера сообщений
        config.enableSimpleBroker("/topic", "/queue");  // Префиксы для брокера сообщений
        config.setApplicationDestinationPrefixes("/app");  // Префиксы для адресов сообщений от клиента к серверу
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Регистрация WebSocket конечной точки
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();  // Поддержка SockJS для более старых браузеров
    }
}
