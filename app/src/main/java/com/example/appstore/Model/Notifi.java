package com.example.appstore.Model;

public class Notifi {
    String title;
    String infoNoti ;
    String img1 ;
    String img2;
    String dateCreate ;

    public Notifi() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoNoti() {
        return infoNoti;
    }

    public void setInfoNoti(String infoNoti) {
        this.infoNoti = infoNoti;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Notifi(String title, String infoNoti, String img1, String img2, String dateCreate) {
        this.title = title;
        this.infoNoti = infoNoti;
        this.img1 = img1;
        this.img2 = img2;
        this.dateCreate = dateCreate;
    }
}
