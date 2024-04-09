package com.example.electronics;

public class Order {
    String id, customerName, customerTel, Pharmacy, productName, ImageURL, price;

    public Order() {
    }

    public Order(String id, String customerName, String customerTel, String pharmacy, String productName, String imageURL, String price) {
        this.id = id;
        this.customerName = customerName;
        this.customerTel = customerTel;
        Pharmacy = pharmacy;
        this.productName = productName;
        ImageURL = imageURL;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getPharmacy() {
        return Pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        Pharmacy = pharmacy;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
