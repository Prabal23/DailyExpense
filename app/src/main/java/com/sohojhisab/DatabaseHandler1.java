package com.sohojhisab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Visit website http://www.whats-online.info
 **/

public class DatabaseHandler1 extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "balance_statement";

    private static final String TABLE_USER = "user_info";
    private static final String TABLE_BALANCE = "mybal";

    private static final String KEY_ID1 = "id";
    private static final String KEY_DATE1 = "dates";
    private static final String KEY_INC1 = "income";
    private static final String KEY_EXP1 = "expense";
    private static final String KEY_BAL1 = "balance";

    public DatabaseHandler1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_CONTACTS1 = "CREATE TABLE " + TABLE_BALANCE + "("
                + KEY_ID1 + " INTEGER PRIMARY KEY,"
                + KEY_DATE1 + " TEXT,"
                + KEY_INC1 + " TEXT,"
                + KEY_EXP1 + " TEXT,"
                + KEY_BAL1 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_CONTACTS1);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public void addBalance(Balance balance) {
        SQLiteDatabase db1 = this.getReadableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put(KEY_DATE1, balance.getDate());
        values1.put(KEY_INC1, balance.getIncome());
        values1.put(KEY_EXP1, balance.getExpense());
        values1.put(KEY_BAL1, balance.getBalance());

        db1.insert(TABLE_BALANCE, null, values1);
        db1.close();
    }

    public List<Balance> getBalance() {
        List<Balance> contactList1 = new ArrayList<Balance>();
        // Select All Query
        String selectQuery1 = "SELECT * FROM " + TABLE_BALANCE;

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor1 = db1.rawQuery(selectQuery1, null);

        // looping through all rows and adding to list
        if (cursor1.moveToFirst()) {
            do {
                Balance balance = new Balance();
                balance.setID(Integer.parseInt(cursor1.getString(0)));
                balance.setDate(cursor1.getString(1));
                balance.setIncome(cursor1.getString(2));
                balance.setExpense(cursor1.getString(3));
                balance.setBalance(cursor1.getString(4));

                // Adding contact to list
                contactList1.add(balance);
            } while (cursor1.moveToNext());
        }
        // return contact list
        return contactList1;
    }

    public List<Balance> getFilterBalance(String st, String end) {
        List<Balance> contactList1 = new ArrayList<Balance>();
        // Select All Query
        //String selectQuery1 = "SELECT * FROM " + TABLE_BALANCE + " WHERE " + KEY_DATE1 + " BETWEEN '" + st + "' AND '" + end + "'";
        String selectQuery1 = "SELECT * FROM " + TABLE_BALANCE + " WHERE " + KEY_DATE1 + " >= '" + st + "' AND " + KEY_DATE1 + " <= '" + end + "'";

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor1 = db1.rawQuery(selectQuery1, null);

        // looping through all rows and adding to list
        if (cursor1.moveToFirst()) {
            do {
                Balance balance = new Balance();
                balance.setID(Integer.parseInt(cursor1.getString(0)));
                balance.setDate(cursor1.getString(1));
                balance.setIncome(cursor1.getString(2));
                balance.setExpense(cursor1.getString(3));
                balance.setBalance(cursor1.getString(4));

                // Adding contact to list
                contactList1.add(balance);
            } while (cursor1.moveToNext());
        }
        // return contact list
        return contactList1;
    }

}
