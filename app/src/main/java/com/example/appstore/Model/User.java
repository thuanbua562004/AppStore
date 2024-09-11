package com.example.appstore.Model;

public class User {
    private String name;
    private String phoneNumber;
    private String address;
    private String dateBirth;

    // No-argument constructor
    public User() {
        // This constructor is required
    }

    // Parameterized constructor
    public User(String name, String phoneNumber, String address, String dateBirth) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateBirth = dateBirth;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", dateBirth='" + dateBirth + '\'' +
                '}';
    }
}
