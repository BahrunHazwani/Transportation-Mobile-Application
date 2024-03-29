package com.example.transportationapp.Model;

public class Users {
    private String name, matricId, phone, email, password, image;

    public Users() {

    }

    public Users(String name, String matricId, String phone, String email, String password, String image) {
        this.name = name;
        this.matricId = matricId;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricId() {
        return matricId;
    }

    public void setMatricId(String matricId) {
        this.matricId = matricId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
