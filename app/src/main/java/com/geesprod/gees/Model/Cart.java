package com.geesprod.gees.Model;

public class Cart {
    private String pid, pname,price, banyak, diskon;

    public Cart() {
    }

    public Cart(String pid, String pname, String price, String banyak, String diskon) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.banyak = banyak;
        this.diskon = diskon;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBanyak() {
        return banyak;
    }

    public void setBanyak(String banyak) {
        this.banyak = banyak;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }
}
