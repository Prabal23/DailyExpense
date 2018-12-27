package com.sohojhisab;

public class Contact {

    //private variables
    int id;
    String date, amount, src, description, status;
    byte[] img;

    // Empty constructor
    public Contact() {

    }

    // constructor
    public Contact(int id, String date, String amount, String src, String description, String status, byte[] img) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.src = src;
        this.description = description;
        this.status = status;
        this.img = img;
    }

    // constructor
    public Contact(String date, String amount, String src, String description, String status, byte[] img) {

        this.date = date;
        this.amount = amount;
        this.src = src;
        this.description = description;
        this.status = status;
        this.img = img;
    }

    // getting ID
    public int getID() {
        return this.id;
    }

    // setting id
    public void setID(int id) {
        this.id = id;
    }

    // getting first name
    public String getDate() {
        return this.date;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getSrc() {
        return this.src;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }
    // setting first name
    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    //getting profile pic
    public byte[] getImage() {
        return this.img;
    }

    //setting profile pic

    public void setImage(byte[] b) {
        this.img = b;
    }
}

