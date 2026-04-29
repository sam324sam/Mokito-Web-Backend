package com.mokito.backend.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.mokito.backend.model.entity.UserClient;

@Component
public class WebSocketSessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, UserClient> users = new ConcurrentHashMap<>();

    public void addSession(String userId, UserClient user, WebSocketSession session) {
        sessions.put(userId, session);
        users.put(userId, user);
    }

    public void removeSession(String userId) {
        sessions.remove(userId);
        users.remove(userId);
    }

    public WebSocketSession getSession(String userId) {
        return sessions.get(userId);
    }

    public UserClient getUser(String userId) {
        return users.get(userId);
    }

    public Collection<WebSocketSession> getAllSessions() {
        return sessions.values();
    }

    public Collection<UserClient> getAllUsers() {
        return users.values();
    }
}