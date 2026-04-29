package com.mokito.backend.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.mokito.backend.model.entity.PetClient;

@Component
public class PetServices {
    private final Map<String, PetClient> pets = new ConcurrentHashMap<>();

    public void addPet(String userId, PetClient pet) {
        pets.put(userId, pet);
    }

    public PetClient getPet(String userId) {
        return pets.get(userId);
    }

    public Collection<PetClient> getAllPets() {
        return pets.values();
    }
}
