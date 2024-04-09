package com.example.electronics;

public class MyItems {
    private String pharmacy,email,phone,address,key;

    public MyItems() {
    }

    public MyItems(String pharmacy, String email, String phone, String address) {
        this.pharmacy = pharmacy;
        this.email = email;
        this.phone=phone;
        this.address=address;
    }



    public String getPharmacy() {
        return pharmacy;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
