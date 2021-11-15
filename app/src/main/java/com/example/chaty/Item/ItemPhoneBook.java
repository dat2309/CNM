package com.example.chaty.Item;

public class ItemPhoneBook {
        private int imgActiveStatus,imgInfoPhoneBook;

        String name,sex,dob,avatar,frID;


    public int getImgActiveStatus() {
        return imgActiveStatus;
    }

    public int getImgInfoPhoneBook() {
        return imgInfoPhoneBook;
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

    public String getFrID() {
        return frID;
    }

    public void setImgActiveStatus(int imgActiveStatus) {
        this.imgActiveStatus = imgActiveStatus;
    }

    public void setImgInfoPhoneBook(int imgInfoPhoneBook) {
        this.imgInfoPhoneBook = imgInfoPhoneBook;
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

    public void setFrID(String frID) {
        this.frID = frID;
    }

    public ItemPhoneBook(int imgActiveStatus, int imgInfoPhoneBook, String name, String sex, String dob, String avatar, String frID) {
        this.imgActiveStatus = imgActiveStatus;
        this.imgInfoPhoneBook = imgInfoPhoneBook;
        this.name = name;
        this.sex = sex;
        this.dob = dob;
        this.avatar = avatar;
        this.frID = frID;
    }

    public ItemPhoneBook() {
    }

    @Override
    public String toString() {
        return "ItemPhoneBook{" +
                "imgActiveStatus=" + imgActiveStatus +
                ", imgInfoPhoneBook=" + imgInfoPhoneBook +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", dob='" + dob + '\'' +
                ", avatar='" + avatar + '\'' +
                ", frID='" + frID + '\'' +
                '}';
    }
}
