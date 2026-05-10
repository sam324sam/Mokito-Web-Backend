package com.mokito.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mokito.backend.model.dto.SpriteMetadataDto;
import com.mokito.backend.model.dto.SpriteResponseDto;
import com.mokito.backend.model.entity.AnimationSpriteClient;
import com.mokito.backend.service.PetServices;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetServices petService;

    public PetController(PetServices petService) {
        this.petService = petService;
    }

    @PostMapping("/{userId}/sprites")
    public ResponseEntity<Void> uploadSprites(
            @PathVariable String userId,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("metadata") List<SpriteMetadataDto> metadata) {

        this.petService.saveSprites(files, metadata, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/sprites/{animationName}/image")
    public ResponseEntity<byte[]> getAnimationImage(
            @PathVariable String userId,
            @PathVariable String animationName) {

        byte[] imageBytes = petService.getAnimationImage(userId, animationName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);
    }

    @GetMapping("/sprites/all")
    public Map<String, Map<String, AnimationSpriteClient>> getAnimationImage() {
        return this.petService.getUserAnimationPet();
    }

    @GetMapping("/{userId}/sprites")
    public ResponseEntity<List<SpriteResponseDto>> getAllSprites(@PathVariable String userId) {
        Map<String, AnimationSpriteClient> animations = petService.getAnimations(userId);

        List<SpriteResponseDto> result = animations.entrySet().stream()
                .map(entry -> new SpriteResponseDto(
                        entry.getKey(),
                        "/pet/" + userId + "/sprites/" + entry.getKey() + "/image",
                        entry.getValue().getFrameWidth(),
                        entry.getValue().getFrameHeight(),
                        entry.getValue().getFrameCount(),
                        entry.getValue().getAnimationType()))
                .toList();

        return ResponseEntity.ok(result);
    }

}
