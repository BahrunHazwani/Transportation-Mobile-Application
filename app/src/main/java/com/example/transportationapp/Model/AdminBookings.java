package com.example.transportationapp.Model;

public class AdminBookings {
    private String bdate, bdestination, bduration, bname, bpassenger, bpayment, paymentMethod, bphone, btime, date, status, time, totalAmount, ic, license;

    public AdminBookings() {
    }

    public AdminBookings(String bdate, String bdestination, String bduration, String bname, String bpassenger, String bpayment, String paymentMethod, String bphone, String btime, String date, String status, String time, String totalAmount, String ic, String license) {
        this.bdate = bdate;
        this.bdestination = bdestination;
        this.bduration = bduration;
        this.bname = bname;
        this.bpassenger = bpassenger;
        this.bpayment = bpayment;
        this.paymentMethod = paymentMethod;
        this.bphone = bphone;
        this.btime = btime;
        this.date = date;
        this.status = status;
        this.time = time;
        this.totalAmount = totalAmount;
        this.ic = ic;
        this.license = license;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getBdestination() {
        return bdestination;
    }

    public void setBdestination(String bdestination) {
        this.bdestination = bdestination;
    }

    public String getBduration() {
        return bduration;
    }

    public void setBduration(String bduration) {
        this.bduration = bduration;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBpassenger() {
        return bpassenger;
    }

    public void setBpassenger(String bpassenger) {
        this.bpassenger = bpassenger;
    }

    public String getBpayment() {
        return bpayment;
    }

    public void setBpayment(String bpayment) {
        this.bpayment = bpayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBphone() {
        return bphone;
    }

    public void setBphone(String bphone) {
        this.bphone = bphone;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
