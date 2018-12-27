package com.sohojhisab;

public class BnD {

    //private variables
    int id2;
    String date2, amount2, src2, description2, status2;
    byte[] img2;

    // Empty constructor
    public BnD() {

    }

    // constructor
    public BnD(int ids2, String dates2, String amounts2, String srcs2, String phn2, String statuss2, byte[] img2) {
        this.id2 = ids2;
        this.date2 = dates2;
        this.amount2 = amounts2;
        this.src2 = srcs2;
        this.description2 = phn2;
        this.status2 = statuss2;
        this.img2 = img2;
    }

    // constructor
    public BnD(String dates2, String amounts2, String srcs2, String phn2, String statuss2, byte[] img2) {

        this.date2 = dates2;
        this.amount2 = amounts2;
        this.src2 = srcs2;
        this.description2 = phn2;
        this.status2 = statuss2;
        this.img2 = img2;
    }

    // getting ID
    public int getID2() {
        return this.id2;
    }

    // setting id
    public void setID2(int id2) {
        this.id2 = id2;
    }

    // getting first name
    public String getDate2() {
        return this.date2;
    }

    public String getAmount2() {
        return this.amount2;
    }

    public String getSrc2() {
        return this.src2;
    }

    public String getDescription2() {
        return this.description2;
    }

    public String getStatus2() {
        return this.status2;
    }
    // setting first name
    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public void setAmount2(String amount2) {
        this.amount2 = amount2;
    }

    public void setSrc2(String src2) {
        this.src2 = src2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    //getting profile pic
    public byte[] getImage2() {
        return this.img2;
    }

    //setting profile pic

    public void setImage2(byte[] b2) {
        this.img2 = b2;
    }
}

