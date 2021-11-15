package com.example.chaty.Item;

public class ItemFriendRequest {

    private int imgInforFriendRequest;
    String name,sex,dob,avatar,reqID;



    public int getImgInforFriendRequest() {
        return imgInforFriendRequest;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDob() {
        return dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public ItemFriendRequest() {
    }



    public void setImgInforFriendRequest(int imgInforFriendRequest) {
        this.imgInforFriendRequest = imgInforFriendRequest;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public ItemFriendRequest(int imgInforFriendRequest, String name, String sex, String dob, String avatar, String reqID) {
        this.imgInforFriendRequest = imgInforFriendRequest;
        this.name = name;
        this.sex = sex;
        this.dob = dob;
        this.avatar = avatar;
        this.reqID = reqID;
    }
}
