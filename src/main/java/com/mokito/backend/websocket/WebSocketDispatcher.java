package com.mokito.backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.mokito.backend.service.WebSocketSessionManager;

@Component
public class WebSocketDispatcher {

    private final WebSocketSessionManager sessionManager;

    public WebSocketDispatcher(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void broadcast(String message) {
        for (WebSocketSession s : sessionManager.getAllSessions()) {
            sendSafe(s, message);
        }
    }

    public void sendToUser(String userId, String message) {
        WebSocketSession session = sessionManager.getSession(userId);
        sendSafe(session, message);
    }

    private void sendSafe(WebSocketSession session, String message) {
        if (session == null || !session.isOpen())
            return;

        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception ignored) {
        }
    }
}