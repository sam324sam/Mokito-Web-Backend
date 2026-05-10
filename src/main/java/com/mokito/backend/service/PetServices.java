package com.mokito.backend.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mokito.backend.model.dto.SpriteMetadataDto;
import com.mokito.backend.model.entity.AnimationSpriteClient;
import com.mokito.backend.model.entity.PetClient;

@Component
public class PetServices {
    private final Map<String, PetClient> pets = new ConcurrentHashMap<>();
    // clave el id de usario
    private final Map<String, Map<String, AnimationSpriteClient>> userAnimationPet = new ConcurrentHashMap<>();

    public void saveSprites(List<MultipartFile> files, List<SpriteMetadataDto> metadata, String userId) {
        Map<String, AnimationSpriteClient> petAnimations = new ConcurrentHashMap<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            SpriteMetadataDto meta = metadata.get(i);
            try {
                // clave el nombre de la animacion

                AnimationSpriteClient animation = new AnimationSpriteClient(
                        meta.name(),
                        file.getBytes(),
                        meta.frameWidth(),
                        meta.frameHeight(),
                        meta.animationType(),
                        meta.frameCount());
                petAnimations.put(meta.name(), animation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        userAnimationPet.put(userId, petAnimations);
    }

    public Map<String, AnimationSpriteClient> getAnimations(String userId) {
        return userAnimationPet.getOrDefault(userId, new ConcurrentHashMap<>());
    }

    public byte[] getAnimationImage(String userId, String animationName) {
        return Optional.ofNullable(userAnimationPet.get(userId))
                .map(anims -> anims.get(animationName))
                .map(AnimationSpriteClient::getImageBytes)
                .orElseThrow(() -> new RuntimeException("Sprite no encontrado: " + animationName));
    }

    public Map<String, Map<String, AnimationSpriteClient>> getUserAnimationPet() {
        return userAnimationPet;
    }

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
