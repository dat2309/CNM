package com.example.chaty;

public class ItemChat {

    private int imgAvatar;
    private String name;
    private String message;
    private String time;

    public ItemChat(int imgAvatar, String name, String message, String time) {
        this.imgAvatar = imgAvatar;
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public int getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(int imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
