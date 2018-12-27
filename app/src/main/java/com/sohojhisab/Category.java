package com.sohojhisab;

public class Category {

    //private variables
    int id;
    String cat;

    // Empty constructor
    public Category() {

    }

    // constructor
    public Category(int id, String cat) {
        this.id = id;
        this.cat = cat;
    }

    // constructor
    public Category(String cat) {
        this.cat = cat;
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
    public String getCat() {
        return this.cat;
    }

    // setting first name
    public void setCat(String cat) {
        this.cat = cat;
    }
}

