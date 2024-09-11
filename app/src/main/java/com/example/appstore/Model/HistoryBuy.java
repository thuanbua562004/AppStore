package com.example.appstore.Model;

import java.util.List;

public class HistoryBuy {
    String nameClient ;
    int totalPrice ;
    List<Cart> list ;
    String adress ;
    String phoneNumber;
    String date ;
    String methodPay;
    public HistoryBuy() {
    }


    public HistoryBuy(String nameClient, int totalPrice, List<Cart> list, String adress, String phoneNumber, String date, String methodPay) {
        this.nameClient = nameClient;
        this.totalPrice = totalPrice;
        this.list = list;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.methodPay = methodPay;
    }

    public String getMethodPay() {
        return methodPay;
    }

    public void setMethodPay(String methodPay) {
        this.methodPay = methodPay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
