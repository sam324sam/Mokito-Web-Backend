package com.mokito.backend.model.dto;

public record SpriteMetadataDto(
                String name,
                int frameWidth,
                int frameHeight,
                int frameCount,
                String animationType
) {

}
