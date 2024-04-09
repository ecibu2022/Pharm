package com.example.electronics;

public class PharmacyRegistration {
    String pharmacy, email, phone, address, role;

    public PharmacyRegistration() {
    }

    public PharmacyRegistration(String pharmacy, String email, String phone, String address,String role) {
        this.pharmacy = pharmacy;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
