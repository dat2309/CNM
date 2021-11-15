package com.example.chaty.Item;

import org.json.JSONArray;

import java.util.List;

public class ItemChat {

    private String imgAvatar;
    private String name;
    private String message;
    private String time,id,admin;
    private int size;
    JSONArray member;

    public ItemChat(String imgAvatar, String name, String message, String time, String id,String admin,int size, JSONArray member) {
        this.imgAvatar = imgAvatar;
        this.name = name;
        this.message = message;
        this.time = time;
        this.id = id;
        this.admin = admin;
        this.size = size;
        this.member = member;
    }

    public String getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(String imgAvatar) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public JSONArray getMember() {
        return member;
    }

    public void setMember(JSONArray member) {
        this.member = member;
    }
}
