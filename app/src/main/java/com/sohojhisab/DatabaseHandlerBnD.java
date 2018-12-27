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

public class DatabaseHandlerBnD extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "all_due_borrow";

    // Contacts table name
    private static final String TABLE_DUE_BORROW = "due_borrow";

    // Contacts Table Columns names
    private static final String KEY_ID2 = "ids";
    private static final String KEY_DATE2 = "in_date";
    private static final String KEY_AMOUNT2 = "amounts";
    private static final String KEY_SOURCE2 = "sources";
    private static final String KEY_PHONE2 = "phones";
    private static final String KEY_STATUS2 = "statuses";
    private static final String KEY_PHOTO2 = "photos";

    public DatabaseHandlerBnD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase db2) {
        String CREATE_TABLE_CONTACTS2 = "CREATE TABLE " + TABLE_DUE_BORROW + "("
                + KEY_ID2 + " INTEGER PRIMARY KEY,"
                + KEY_DATE2 + " TEXT,"
                + KEY_AMOUNT2 + " TEXT,"
                + KEY_SOURCE2 + " TEXT,"
                + KEY_PHONE2 + " TEXT,"
                + KEY_STATUS2 + " TEXT,"
                + KEY_PHOTO2 + " BLOB" + ")";
        db2.execSQL(CREATE_TABLE_CONTACTS2);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {

        // Drop older table if existed
        db2.execSQL("DROP TABLE IF EXISTS " + TABLE_DUE_BORROW);

        // Create tables again
        onCreate(db2);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public void addBnD(BnD contact) {
        SQLiteDatabase db2 = this.getReadableDatabase();
        ContentValues values2 = new ContentValues();
        values2.put(KEY_DATE2, contact.getDate2());
        values2.put(KEY_AMOUNT2, contact.getAmount2());
        values2.put(KEY_SOURCE2, contact.getSrc2());
        values2.put(KEY_PHONE2, contact.getDescription2());
        values2.put(KEY_STATUS2, contact.getStatus2());
        values2.put(KEY_PHOTO2, contact.getImage2());

        db2.insert(TABLE_DUE_BORROW, null, values2);
        db2.close();
    }

    /**
     * Getting All Contacts
     **/

    public List<BnD> getAllDue() {
        List<BnD> bnD = new ArrayList<BnD>();
        // Select All Query
        String selectQuery2 = "SELECT  * FROM " + TABLE_DUE_BORROW + " WHERE status=1";

        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {
            do {
                BnD bnDs = new BnD();
                bnDs.setID2(Integer.parseInt(cursor2.getString(0)));
                bnDs.setDate2(cursor2.getString(1));
                bnDs.setAmount2(cursor2.getString(2));
                bnDs.setSrc2(cursor2.getString(3));
                bnDs.setDescription2(cursor2.getString(4));
                bnDs.setStatus2(cursor2.getString(5));
                bnDs.setImage2(cursor2.getBlob(6));

                // Adding contact to list
                bnD.add(bnDs);
            } while (cursor2.moveToNext());
        }
        // return contact list
        return bnD;
    }

    public List<BnD> getAllBorrow() {
        List<BnD> bnD = new ArrayList<BnD>();
        // Select All Query
        String selectQuery3 = "SELECT  * FROM " + TABLE_DUE_BORROW + " WHERE status=2";

        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor cursor2 = db2.rawQuery(selectQuery3, null);

        // looping through all rows and adding to list
        if (cursor2.moveToFirst()) {
            do {
                BnD bnDs = new BnD();
                bnDs.setID2(Integer.parseInt(cursor2.getString(0)));
                bnDs.setDate2(cursor2.getString(1));
                bnDs.setAmount2(cursor2.getString(2));
                bnDs.setSrc2(cursor2.getString(3));
                bnDs.setDescription2(cursor2.getString(4));
                bnDs.setStatus2(cursor2.getString(5));
                bnDs.setImage2(cursor2.getBlob(6));

                // Adding contact to list
                bnD.add(bnDs);
            } while (cursor2.moveToNext());
        }
        // return contact list
        return bnD;
    }

    /**
     * Updating single contact
     **/

    /*public int updateBalances(BnD bnD, int id) {
        SQLiteDatabase db2 = this.getWritableDatabase();

        ContentValues values2 = new ContentValues();
        values2.put(KEY_DATE2, bnD.getDate2());
        values2.put(KEY_AMOUNT2, bnD.getAmount2());
        values2.put(KEY_SOURCE2, bnD.getSrc2());
        values2.put(KEY_PHONE2, bnD.getDescription2());
        values2.put(KEY_STATUS2, bnD.getStatus2());

        // updating row
        return db2.update(TABLE_DUE_BORROW, values2, KEY_ID2 + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteBalances(int Id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.delete(TABLE_DUE_BORROW, KEY_ID2 + " = ?",
                new String[]{String.valueOf(Id)});
        db2.close();
    }*/

}
