package com.mokito.backend.model.entity;

public class AnimationSpriteClient {

    private String name;
    private byte[] imageBytes;
    private int frameWidth;
    private int frameHeight;
    private int frameCount;
    private String animationType;

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public String getName() {
        return name;
    }

    public AnimationSpriteClient(String name, byte[] imageBytes, int frameWidth, int frameHeight,
            String animationType, int frameCount) {
        this.name = name;
        this.imageBytes = imageBytes;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.animationType = animationType;
        this.frameCount = frameCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
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