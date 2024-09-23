package com.example.appstore.Model;

import java.io.Serializable;

public class Cart implements Serializable {
    private String nameProduct ;
    private String id_product;
    private int number;
    private String size;
    private String color ;
    private  String imgProduct ;
    private int price;

    public Cart() {
    }

    public Cart(String nameProduct, String id_product, int number, String size, String color, String imgProduct, int price) {
        this.nameProduct = nameProduct;
        this.id_product = id_product;
        this.number = number;
        this.size = size;
        this.color = color;
        this.imgProduct = imgProduct;
        this.price = price;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        this.imgProduct = imgProduct;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "nameProduct='" + nameProduct + '\'' +
                ", id_product=" + id_product +
                ", number=" + number +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", imgProduct='" + imgProduct + '\'' +
                ", price=" + price +
                '}';
    }
}
