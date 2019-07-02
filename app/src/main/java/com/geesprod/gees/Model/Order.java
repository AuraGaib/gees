package com.geesprod.gees.Model;

public class Order {
    private String alamat, date, kota, penerima, state, telpon, time, totalAmount;

    public Order() {
    }

    public Order(String alamat, String date, String kota, String penerima, String state, String telpon, String time, String totalAmount) {
        this.alamat = alamat;
        this.date = date;
        this.kota = kota;
        this.penerima = penerima;
        this.state = state;
        this.telpon = telpon;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
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
}
