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

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "daily_balance";

    // Contacts table name
    private static final String TABLE_CONTACTS = "income_expense";
    private static final String TABLE_USER = "user_info";
    private static final String TABLE_BALANCE = "mybalance";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_SOURCE = "src";
    private static final String KEY_DES = "description";
    private static final String KEY_STATUS = "status";
    private static final String KEY_PHOTO = "photo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_SOURCE + " TEXT,"
                + KEY_DES + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_PHOTO + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public void addContacts(Contact contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, contact.getDate());
        values.put(KEY_AMOUNT, contact.getAmount());
        values.put(KEY_SOURCE, contact.getSrc());
        values.put(KEY_DES, contact.getDescription());
        values.put(KEY_STATUS, contact.getStatus());
        values.put(KEY_PHOTO, contact.getImage());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    /**
     * Getting All Contacts
     **/

    public List<Contact> getAllIncome() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getAllExpense() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getAllDue() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=3 OR status=33";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getAllBorrow() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=4 OR status=44";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getAllBalance() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=1 OR status=2";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getAllBorrowDue() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=3 OR status=4 OR status=33 OR status=44";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getNotifyBorrowDue() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE status=33 OR status=44";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Contact> getMonthlyBalance(String source, String start, String today) {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE src='" + source + /*"'"*/ "' AND "+ KEY_DATE + " BETWEEN '" + start + "' AND '" + today + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
                contact.setAmount(cursor.getString(2));
                contact.setSrc(cursor.getString(3));
                contact.setDescription(cursor.getString(4));
                contact.setStatus(cursor.getString(5));
                contact.setImage(cursor.getBlob(6));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    /**
     * Updating single contact
     **/

    public int updateContact(Contact contact, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, contact.getDate());
        values.put(KEY_AMOUNT, contact.getAmount());
        values.put(KEY_SOURCE, contact.getSrc());
        values.put(KEY_DES, contact.getDescription());
        values.put(KEY_STATUS, contact.getStatus());
        values.put(KEY_PHOTO, contact.getImage());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteContact(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(Id)});
        db.close();
    }

}
