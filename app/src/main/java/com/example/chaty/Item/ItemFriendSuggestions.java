package com.example.chaty.Item;

public class ItemFriendSuggestions {
    private int  imgInforFriendSuggestions;
    private String tvNameFriendSuggestions,imgFriendSuggestions;
    private String tvPhoneNumberFriendSuggestions,obj,phone;

    public String getImgFriendSuggestions() {
        return imgFriendSuggestions;
    }

    public void setImgFriendSuggestions(String imgFriendSuggestions) {
        this.imgFriendSuggestions = imgFriendSuggestions;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImgInforFriendSuggestions() {
        return imgInforFriendSuggestions;
    }

    public void setImgInforFriendSuggestions(int imgInforFriendSuggestions) {
        this.imgInforFriendSuggestions = imgInforFriendSuggestions;
    }

    public String getTvNameFriendSuggestions() {
        return tvNameFriendSuggestions;
    }

    public void setTvNameFriendSuggestions(String tvNameFriendSuggestions) {
        this.tvNameFriendSuggestions = tvNameFriendSuggestions;
    }

    public String getTvPhoneNumberFriendSuggestions() {
        return tvPhoneNumberFriendSuggestions;
    }

    public void setTvPhoneNumberFriendSuggestions(String tvPhoneNumberFriendSuggestions) {
        this.tvPhoneNumberFriendSuggestions = tvPhoneNumberFriendSuggestions;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public ItemFriendSuggestions(String imgFriendSuggestions, int imgInforFriendSuggestions, String tvNameFriendSuggestions, String tvPhoneNumberFriendSuggestions, String obj,String phone) {
        this.imgFriendSuggestions = imgFriendSuggestions;
        this.imgInforFriendSuggestions = imgInforFriendSuggestions;
        this.tvNameFriendSuggestions = tvNameFriendSuggestions;
        this.tvPhoneNumberFriendSuggestions = tvPhoneNumberFriendSuggestions;
        this.obj = obj;
        this.phone= phone;
    }

    public ItemFriendSuggestions() {
    }

    @Override
    public String toString() {
        return "ItemFriendSuggestions{" +
                "imgInforFriendSuggestions=" + imgInforFriendSuggestions +
                ", tvNameFriendSuggestions='" + tvNameFriendSuggestions + '\'' +
                ", imgFriendSuggestions='" + imgFriendSuggestions + '\'' +
                ", tvPhoneNumberFriendSuggestions='" + tvPhoneNumberFriendSuggestions + '\'' +
                ", obj='" + obj + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
