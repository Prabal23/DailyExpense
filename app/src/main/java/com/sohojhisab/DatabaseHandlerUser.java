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

public class DatabaseHandlerUser extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION1 = 1;

    // Database Name
    private static final String DATABASE_NAME = "alluser";

    // Contacts table name
    private static final String TABLE_USER = "user_list";

    // Contacts Table Columns names
    private static final String KEY_IDS = "id";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_USER = "user";
    private static final String KEY_Email = "email";
    private static final String KEY_Phone = "phn";
    private static final String KEY_Pass = "pass";
    private static final String KEY_PIC = "picture";

    public DatabaseHandlerUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION1);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_USER + "("
                + KEY_IDS + " INTEGER PRIMARY KEY,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_USER + " TEXT,"
                + KEY_Email + " TEXT,"
                + KEY_Phone + " TEXT,"
                + KEY_Pass + " TEXT,"
                + KEY_PIC + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public void addUser(Contact contact) {
        SQLiteDatabase dbcat = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, contact.getDate());
        values.put(KEY_USER, contact.getAmount());
        values.put(KEY_Email, contact.getSrc());
        values.put(KEY_Phone, contact.getDescription());
        values.put(KEY_Pass, contact.getStatus());
        values.put(KEY_PIC, contact.getImage());

        dbcat.insert(TABLE_USER, null, values);
        dbcat.close();
    }

    /**
     * Getting All Contacts
     **/

    public List<Contact> getAllUser() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuerycat = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuerycat, null);

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

    public List<Contact> getUser(String username, String password) {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuerycat = "SELECT  * FROM " + TABLE_USER + " WHERE user='" + username + "' AND pass='" + password + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuerycat, null);

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

    public int updateUser(Contact contact, int id) {
        SQLiteDatabase dbcat = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, contact.getDate());
        values.put(KEY_USER, contact.getAmount());
        values.put(KEY_Email, contact.getSrc());
        values.put(KEY_Phone, contact.getDescription());
        values.put(KEY_Pass, contact.getStatus());
        values.put(KEY_PIC, contact.getImage());

        // updating row
        return dbcat.update(TABLE_USER, values, KEY_IDS + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteUser(int Id) {
        SQLiteDatabase dbcat = this.getWritableDatabase();
        dbcat.delete(TABLE_USER, KEY_IDS + " = ?",
                new String[]{String.valueOf(Id)});
        dbcat.close();
    }

}
