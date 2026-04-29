package com.mokito.backend.model.entity;

import java.sql.Blob;
import java.util.Map;

public class PetClient {
    private int x;
    private int y;
    private Blob img;
    private Map<String, AnimationSpriteClient> animationSprite;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Blob getImg() {
        return img;
    }

    public void setImg(Blob img) {
        this.img = img;
    }

    public Map<String, AnimationSpriteClient> getAnimationSprite() {
        return animationSprite;
    }

    public void setAnimationSprite(Map<String, AnimationSpriteClient> animationSprite) {
        this.animationSprite = animationSprite;
    }

}