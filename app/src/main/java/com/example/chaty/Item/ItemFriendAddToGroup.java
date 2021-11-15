package com.example.chaty.Item;


public class ItemFriendAddToGroup {
    private String imgAvatarFriendAddToGroup;
    private String tvNameFriendAddToGroup;
    private String tvTimeAddToGroup;
    private boolean radAdd;

    public ItemFriendAddToGroup()
    {

    }

    public String getImgAvatarFriendAddToGroup() {
        return imgAvatarFriendAddToGroup;
    }

    public void setImgAvatarFriendAddToGroup(String imgAvatarFriendAddToGroup) {
        this.imgAvatarFriendAddToGroup = imgAvatarFriendAddToGroup;
    }

    public String getTvNameFriendAddToGroup() {
        return tvNameFriendAddToGroup;
    }

    public void setTvNameFriendAddToGroup(String tvNameFriendAddToGroup) {
        this.tvNameFriendAddToGroup = tvNameFriendAddToGroup;
    }

    public String getTvTimeAddToGroup() {
        return tvTimeAddToGroup;
    }

    public void setTvTimeAddToGroup(String tvTimeAddToGroup) {
        this.tvTimeAddToGroup = tvTimeAddToGroup;
    }

    public boolean isRadAdd() {
        return radAdd;
    }

    public void setRadAdd(boolean radAdd) {
        this.radAdd = radAdd;
    }

    public ItemFriendAddToGroup(String imgAvatarFriendAddToGroup, String tvNameFriendAddToGroup, String tvTimeAddToGroup, boolean radAdd) {
        this.imgAvatarFriendAddToGroup = imgAvatarFriendAddToGroup;
        this.tvNameFriendAddToGroup = tvNameFriendAddToGroup;
        this.tvTimeAddToGroup = tvTimeAddToGroup;
        this.radAdd = radAdd;
    }

    @Override
    public String toString() {
        return "ItemFriendAddToGroup{" +
                "imgAvatarFriendAddToGroup=" + imgAvatarFriendAddToGroup +
                ", tvNameFriendAddToGroup='" + tvNameFriendAddToGroup + '\'' +
                ", tvTimeAddToGroup='" + tvTimeAddToGroup + '\'' +
                ", radAdd=" + radAdd +
                '}';
    }
}
