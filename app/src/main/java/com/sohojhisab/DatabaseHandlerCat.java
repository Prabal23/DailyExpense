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

public class DatabaseHandlerCat extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION1 = 1;

    // Database Name
    private static final String DATABASE_NAME1 = "category";

    // Contacts table name
    private static final String TABLE_CAT = "my_cat";

    // Contacts Table Columns names
    private static final String KEY_IDS = "id";
    private static final String KEY_CAT = "cat";

    public DatabaseHandlerCat(Context context) {
        super(context, DATABASE_NAME1, null, DATABASE_VERSION1);
    }

    //Create tables
    @Override
    public void onCreate(SQLiteDatabase dbcat) {
        String CREATE_TABLE_CONTACTS_CAT = "CREATE TABLE " + TABLE_CAT + "("
                + KEY_IDS + " INTEGER PRIMARY KEY,"
                + KEY_CAT + " TEXT" + ")";
        dbcat.execSQL(CREATE_TABLE_CONTACTS_CAT);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase dbcat, int oldVersion, int newVersion) {

        // Drop older table if existed
        dbcat.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);

        // Create tables again
        onCreate(dbcat);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    //Insert values to the table contacts
    public void addCat(Category category) {
        SQLiteDatabase dbcat = this.getReadableDatabase();
        ContentValues valuescat = new ContentValues();
        valuescat.put(KEY_CAT, category.getCat());

        dbcat.insert(TABLE_CAT, null, valuescat);
        dbcat.close();
    }

    /**
     * Getting All Contacts
     **/

    public List<Category> getAllCat() {
        List<Category> contactList = new ArrayList<Category>();
        // Select All Query
        String selectQuerycat = "SELECT  * FROM " + TABLE_CAT;

        SQLiteDatabase dbcat = this.getWritableDatabase();
        Cursor cursorcat = dbcat.rawQuery(selectQuerycat, null);

        // looping through all rows and adding to list
        if (cursorcat.moveToFirst()) {
            do {
                Category category = new Category();
                category.setID(Integer.parseInt(cursorcat.getString(0)));
                category.setCat(cursorcat.getString(1));

                // Adding contact to list
                contactList.add(category);
            } while (cursorcat.moveToNext());
        }
        // return contact list
        return contactList;
    }
    /**
     * Updating single contact
     **/

    public int updateCat(Category category, int id) {
        SQLiteDatabase dbcat = this.getWritableDatabase();

        ContentValues valuescat = new ContentValues();
        valuescat.put(KEY_CAT, category.getCat());

        // updating row
        return dbcat.update(TABLE_CAT, valuescat, KEY_IDS + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteCat(int Id) {
        SQLiteDatabase dbcat = this.getWritableDatabase();
        dbcat.delete(TABLE_CAT, KEY_IDS + " = ?",
                new String[]{String.valueOf(Id)});
        dbcat.close();
    }

}
