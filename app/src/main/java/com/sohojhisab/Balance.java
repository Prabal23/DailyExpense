package com.sohojhisab;

public class Balance {

    //private variables
    int id;
    String date, income, expense, balance;
    //byte[] img;

    // Empty constructor
    public Balance() {

    }

    // constructor
    public Balance(int id, String date, String income, String expense, String balance) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.balance = balance;
    }

    // constructor
    public Balance(String date, String income, String expense, String balance) {

        this.date = date;
        this.income = income;
        this.expense = expense;
        this.balance = balance;
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

    public String getIncome() {
        return this.income;
    }

    public String getExpense() {
        return this.expense;
    }

    public String getBalance() {
        return this.balance;
    }

    // setting first name
    public void setDate(String date) {
        this.date = date;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}

