package com.example.chaty;

public class ItemPhoneBook {
        private int imgAvatarPhoneBook,imgActiveStatus,imgInfoPhoneBook;
        private String txtNamePhoneBook;

    public int getImgAvatarPhoneBook() {
        return imgAvatarPhoneBook;
    }

    public void setImgAvatarPhoneBook(int imgAvatarPhoneBook) {
        this.imgAvatarPhoneBook = imgAvatarPhoneBook;
    }

    public int getImgActiveStatus() {
        return imgActiveStatus;
    }

    public void setImgActiveStatus(int imgActiveStatus) {
        this.imgActiveStatus = imgActiveStatus;
    }

    public int getImgInfoPhoneBook() {
        return imgInfoPhoneBook;
    }

    public void setImgInfoPhoneBook(int imgInfoPhoneBook) {
        this.imgInfoPhoneBook = imgInfoPhoneBook;
    }

    public String getTxtNamePhoneBook() {
        return txtNamePhoneBook;
    }

    public void setTxtNamePhoneBook(String txtNamePhoneBook) {
        this.txtNamePhoneBook = txtNamePhoneBook;
    }

    public ItemPhoneBook(int imgAvatarPhoneBook, int imgActiveStatus, int imgInfoPhoneBook, String txtNamePhoneBook) {
        this.imgAvatarPhoneBook = imgAvatarPhoneBook;
        this.imgActiveStatus = imgActiveStatus;
        this.imgInfoPhoneBook = imgInfoPhoneBook;
        this.txtNamePhoneBook = txtNamePhoneBook;
    }

    @Override
    public String toString() {
        return "ItemPhoneBook{" +
                "imgAvatarPhoneBook=" + imgAvatarPhoneBook +
                ", imgActiveStatus=" + imgActiveStatus +
                ", imgInfoPhoneBook=" + imgInfoPhoneBook +
                ", txtNamePhoneBook='" + txtNamePhoneBook + '\'' +
                '}';
    }
}
