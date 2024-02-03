package com.example.transportationapp.Model;

public class Cars {
    private String carName, description, image, carType, cid, date, time, carStatus, sname, sphone, accountNum;

    public Cars() {

    }


    public Cars(String carName, String description, String image, String carType, String cid, String date, String time, String carStatus, String sname, String sphone, String accountNum) {
        this.carName = carName;
        this.description = description;
        this.image = image;
        this.carType = carType;
        this.cid = cid;
        this.date = date;
        this.time = time;
        this.carStatus = carStatus;
        this.sname = sname;
        this.sphone = sphone;
        this.accountNum = accountNum;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }


    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }
}
