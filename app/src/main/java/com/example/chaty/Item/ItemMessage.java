package com.example.chaty.Item;

public class ItemMessage {
    private String imgAvatarMessage;
    private String tvMessageChat;
    private String tvTimeMessage;
    private String senderID;
    public ItemMessage(){

    }

    public String getImgAvatarMessage() {
        return imgAvatarMessage;
    }

    public void setImgAvatarMessage(String imgAvatarMessage) {
        this.imgAvatarMessage = imgAvatarMessage;
    }

    public String getTvMessageChat() {
        return tvMessageChat;
    }

    public void setTvMessageChat(String tvMessageChat) {
        this.tvMessageChat = tvMessageChat;
    }

    public String getTvTimeMessage() {
        return tvTimeMessage;
    }

    public void setTvTimeMessage(String tvTimeMessage) {
        this.tvTimeMessage = tvTimeMessage;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public ItemMessage(String imgAvatarMessage, String tvMessageChat, String tvTimeMessage, String senderID) {
        this.imgAvatarMessage = imgAvatarMessage;
        this.tvMessageChat = tvMessageChat;
        this.tvTimeMessage = tvTimeMessage;
        this.senderID = senderID;
    }

    @Override
    public String toString() {
        return "ItemMessage{" +
                "imgAvatarMessage=" + imgAvatarMessage +
                ", tvMessageChat='" + tvMessageChat + '\'' +
                ", tvTimeMessage='" + tvTimeMessage + '\'' +
                ", senderID='" + senderID + '\'' +
                '}';
    }
}
