package com.example.chaty;

public class ItemFriendRequest {
    private int imgAvatarFriendRequest;
    private int imgInforFriendRequest;
    private String tvNameFriendRequest;

    public ItemFriendRequest(int imgAvatarFriendRequest, int imgInforFriendRequest, String tvNameFriendRequest) {
        this.imgAvatarFriendRequest = imgAvatarFriendRequest;
        this.imgInforFriendRequest = imgInforFriendRequest;
        this.tvNameFriendRequest = tvNameFriendRequest;
    }

    public int getImgAvatarFriendRequest() {
        return imgAvatarFriendRequest;
    }

    public void setImgAvatarFriendRequest(int imgAvatarFriendRequest) {
        this.imgAvatarFriendRequest = imgAvatarFriendRequest;
    }

    public int getImgInforFriendRequest() {
        return imgInforFriendRequest;
    }

    public void setImgInforFriendRequest(int imgInforFriendRequest) {
        this.imgInforFriendRequest = imgInforFriendRequest;
    }

    public String getTvNameFriendRequest() {
        return tvNameFriendRequest;
    }

    public void setTvNameFriendRequest(String tvNameFriendRequest) {
        this.tvNameFriendRequest = tvNameFriendRequest;

    }

    @Override
    public String toString() {
        return "ItemFriendRequest{" +
                "imgAvatarFriendRequest=" + imgAvatarFriendRequest +
                ", imgInforFriendRequest=" + imgInforFriendRequest +
                ", tvNameFriendRequest='" + tvNameFriendRequest + '\'' +
                '}';
    }
}
