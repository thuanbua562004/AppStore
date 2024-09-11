package com.example.appstore.Model;

import java.io.Serializable;
import java.util.Map;

public class Product implements Serializable {
    public Map<String, String> color;
    public Map<String, String> size;
    public int price;
    public String name;
    public String type;
    public String img1;
    public String img2;
    public String info;
    public int id;

    public Product() {
    }
    public Map<String, String> getColor() {
        return color;
    }

    public void setColor(Map<String, String> color) {
        this.color = color;
    }

    public Map<String, String> getSize() {
        return size;
    }

    public void setSize(Map<String, String> size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId_product() {
        return id;
    }

    public void setId_product(int id_product) {
        this.id = id;
    }



    public Product(Map<String, String> color, Map<String, String> size, int price, String name, String type, String img1, String img2, String info, int id_product) {
        this.color = color;
        this.size = size;
        this.price = price;
        this.name = name;
        this.type = type;
        this.img1 = img1;
        this.img2 = img2;
        this.info = info;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "color=" + color +
                ", size=" + size +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", info='" + info + '\'' +
                ", id_product=" + id +
                '}';
    }
}
