package com.mokito.backend.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping("")
    public Map<String, String> login(@RequestBody Map<String, String> body) {

        String name = body.get("name");
        String userId = UUID.randomUUID().toString();

        return Map.of(
                "userId", userId,
                "name", name);
    }
}