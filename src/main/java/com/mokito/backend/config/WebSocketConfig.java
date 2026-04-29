package com.mokito.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.mokito.backend.websocket.WebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler handler;

    public WebSocketConfig(WebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/")
                .setAllowedOrigins("*");

    }
}