package com.example.transportationapp.Model;

public class Wish {
    private String cid, carName, carType, sname, sphone, accountNum;

    public Wish() {
    }

    public Wish(String cid, String carName, String carType, String sname, String sphone, String accountNum) {
        this.cid = cid;
        this.carName = carName;
        this.carType = carType;
        this.sname = sname;
        this.sphone = sphone;
        this.accountNum = accountNum;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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
