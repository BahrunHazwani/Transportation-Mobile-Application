package com.example.transportationapp.Model;

public class ServiceProvider {

    private String accountNum, image, semail,sid, smatricID, sname, spassword, sphone;

    public ServiceProvider() {

    }

    public ServiceProvider(String accountNum, String image, String semail, String sid, String smatricID, String sname, String spassword, String sphone) {
        this.accountNum = accountNum;
        this.image = image;
        this.semail = semail;
        this.sid = sid;
        this.smatricID = smatricID;
        this.sname = sname;
        this.spassword = spassword;
        this.sphone = sphone;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSmatricID() {
        return smatricID;
    }

    public void setSmatricID(String smatricID) {
        this.smatricID = smatricID;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSpassword() {
        return spassword;
    }

    public void setSpassword(String spassword) {
        this.spassword = spassword;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }
}
