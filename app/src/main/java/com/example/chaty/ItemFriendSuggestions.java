package com.example.chaty;

public class ItemFriendSuggestions {
    private int imgFriendSuggestions, imgInforFriendSuggestions;
    private String tvNameFriendSuggestions;
    private String tvPhoneNumberFriendSuggestions;

    public ItemFriendSuggestions(int imgFriendSuggestions, int imgInforFriendSuggestions, String tvNameFriendSuggestions, String tvPhoneNumberFriendSuggestions) {
        this.imgFriendSuggestions = imgFriendSuggestions;
        this.imgInforFriendSuggestions = imgInforFriendSuggestions;
        this.tvNameFriendSuggestions = tvNameFriendSuggestions;
        this.tvPhoneNumberFriendSuggestions = tvPhoneNumberFriendSuggestions;
    }

    public int getImgFriendSuggestions() {
        return imgFriendSuggestions;
    }

    public void setImgFriendSuggestions(int imgFriendSuggestions) {
        this.imgFriendSuggestions = imgFriendSuggestions;
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

    @Override
    public String toString() {
        return "ItemFriendSuggestions{" +
                "imgFriendSuggestions=" + imgFriendSuggestions +
                ", imgInforFriendSuggestions=" + imgInforFriendSuggestions +
                ", tvNameFriendSuggestions='" + tvNameFriendSuggestions + '\'' +
                ", tvPhoneNumberFriendSuggestions='" + tvPhoneNumberFriendSuggestions + '\'' +
                '}';
    }
}
