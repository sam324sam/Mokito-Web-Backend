package com.mokito.backend.model.entity;

import java.sql.Blob;

public class AnimationSpriteClient {
    private Blob image;
    private int frameWidth;
    private int frameHeight;
    private String animationType;

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public String getAnimationType() {
        return animationType;
    }

    public void setAnimationType(String animationType) {
        this.animationType = animationType;
    }
}