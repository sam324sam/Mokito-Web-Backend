package com.mokito.backend.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mokito.backend.model.entity.UserClient;
import com.mokito.backend.service.WebSocketSessionManager;

@RestController
@RequestMapping("/user")
public class UsersController {
    private final WebSocketSessionManager sessionManager;

    public UsersController(WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("/all")
    public Collection<UserClient> getUsers() {
        return sessionManager.getAllUsers();
    }

    @GetMapping("/debug")
    public List<Object> debugSessions() {
        return sessionManager.getAllSessions().stream()
                .map(session -> session.getAttributes().get("user"))
                .toList();
    }

    @GetMapping("/ping")
    public boolean ping() {
        return true;
    }
}
