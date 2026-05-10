package com.mokito.backend.model.dto;

public record SpriteResponseDto(
    String name,
    String src,
    int frameWidth,
    int frameHeight,
    int frameCount,
    String animationType
) {}