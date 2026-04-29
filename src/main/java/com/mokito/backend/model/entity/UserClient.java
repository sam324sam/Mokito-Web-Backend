package com.mokito.backend.model.entity;

public class UserClient {
    private String name;
    private String userId;
    private CursorClient cursor;
    private CanvasClient canvas;

    public CanvasClient getCanvas() {
        return canvas;
    }

    public void setCanvas(CanvasClient canvas) {
        this.canvas = canvas;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CursorClient getCursor() {
        return cursor;
    }

    public void setCursor(CursorClient cursor) {
        this.cursor = cursor;
    }
}