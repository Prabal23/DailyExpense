package com.sohojhisab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    Typeface fontAwesomeFont;
    TextView menu, back;
    String TAG = "RemindMe", tt_not = "", res = "", fname = "", uname = "", password = "", impo = "";
    String dd = "", incinc = "", expexp = "", balbal = "";
    int ii = 0, ee = 0, bb = 0;
    LocalData localData;
    SwitchCompat reminderSwitch;
    TextView tvTime, email, phone;
    LinearLayout ll_set_time, ll_terms;
    int hour, min, cc = 0;
    ClipboardManager myClipboard;
    CircleImageView dp;
    private DatabaseHandlerUser dbUser;
    private DatabaseHandler1 dbState;
    private DatabaseHandler db;
    private Contact dataModel, dataModelUser, dataModel7, dataModel8;
    final String DATABASE_NAME = "daily_balance";
    String rest = "0000-00-00,0,0,0";
    private byte[] photo, photo1, photo2;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);

        db = new DatabaseHandler(this);
        dbState = new DatabaseHandler1(this);
        dbUser = new DatabaseHandlerUser(this);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_login", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("login")) {
            res = (sharedPreferences).getString("login", "");
        } else {
            res = "//";
        }

        SharedPreferences sharedPreferences1 = getSharedPreferences("imported_data", Context.MODE_PRIVATE);
        if (sharedPreferences1.contains("data")) {
            impo = (sharedPreferences1).getString("data", "");
            //Toast.makeText(this, imp, Toast.LENGTH_SHORT).show();
        } else {
            impo = "";
        }

        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

        if (sharedPreferences2.contains("balance")) {
            rest = (sharedPreferences2).getString("balance", "");
        }
        if (!sharedPreferences2.contains("balance")) {
            rest = "0000-00-00,0,0,0";
        }
        if (rest.equals("")) {
            rest = "0000-00-00,0,0,0";
        }
        String[] results = rest.split(",");
        dd = results[0];
        incinc = results[1];
        ii = Integer.parseInt(incinc);
        expexp = results[2];
        ee = Integer.parseInt(expexp);
        balbal = results[3];
        bb = Integer.parseInt(balbal);

        String[] result = res.split("/");
        fname = result[0];
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        uname = result[1];
        //Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        password = result[2];

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        menu = (TextView) findViewById(R.id.menu);
        back = (TextView) findViewById(R.id.back);
        menu.setTypeface(fontAwesomeFont);
        back.setTypeface(fontAwesomeFont);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Settings.class);
                startActivity(intent);
            }
        });

        localData = new LocalData(getApplicationContext());

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ll_set_time = (LinearLayout) findViewById(R.id.ll_set_time);
        ll_terms = (LinearLayout) findViewById(R.id.ll_terms);

        tvTime = (TextView) findViewById(R.id.tv_reminder_time_desc);

        reminderSwitch = (SwitchCompat) findViewById(R.id.timerSwitch);

        hour = localData.get_hour();
        min = localData.get_min();

        tvTime.setText(getFormatedTime(hour, min));
        reminderSwitch.setChecked(localData.getReminderStatus());

        if (!localData.getReminderStatus())
            ll_set_time.setAlpha(0.4f);

        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localData.setReminderStatus(isChecked);
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: true");
                    NotificationScheduler.setReminder(Settings.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                    ll_set_time.setAlpha(1f);
                } else {
                    Log.d(TAG, "onCheckedChanged: false");
                    NotificationScheduler.cancelReminder(Settings.this, AlarmReceiver.class);
                    ll_set_time.setAlpha(0.4f);
                }
            }
        });

        ll_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.getReminderStatus())
                    showTimePickerDialog(localData.get_hour(), localData.get_min());
            }
        });

        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dp = (CircleImageView) findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                /*PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();*/
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(Settings.this);
                MenuInflater inflater = new MenuInflater(Settings.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(Settings.this, menuBuilder, view);
                optionsMenu.setForceShowIcon(true);

                // Set Item Click Listener
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile:
                                Intent intent1 = new Intent(getBaseContext(), Profile.class);
                                startActivity(intent1);
                                return true;
                            case R.id.logout:
                                final Dialog dialog = new Dialog(Settings.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.logout_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                Button no = (Button) dialog.findViewById(R.id.no);
                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                //contact.setText(msg3);
                                Button yes = (Button) dialog.findViewById(R.id.yes);
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences preferences = getSharedPreferences("shared_login", Context.MODE_PRIVATE);
                                        //Getting editor
                                        SharedPreferences.Editor editor = preferences.edit();

                                        //Puting the value false for loggedin
                                        editor.putBoolean("user_login", false);

                                        //Putting blank value to email
                                        editor.putString("login", "");

                                        //Saving the sharedpreferences
                                        editor.commit();
                                        Intent intent = new Intent(getBaseContext(), Login.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                                dialog.show();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {
                    }
                });

                // Display the menu
                optionsMenu.show();
            }
        });

        final ArrayList<Contact> contacts = new ArrayList<>(dbUser.getUser(uname, password));
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModelUser = contacts.get(i);
            byte[] pic = dataModelUser.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            dp.setImageBitmap(bitmap);
        }

        final DatabaseHandler dbhelper = new DatabaseHandler(getApplicationContext());

        TextView backup = (TextView) findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
            SQLiteDatabase sqldb = dbhelper.getReadableDatabase(); //My Database class
            Cursor c = null;

            @Override
            public void onClick(View view) {

                // Income Expense
                DatabaseHandler dbhelper = new DatabaseHandler(getApplicationContext());
                File exportDir = new File(Environment.getExternalStorageDirectory(), "/SohojHisab");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }

                File file = new File(exportDir, "AllBalance.csv");
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    Cursor curCSV = db.rawQuery("SELECT  * FROM income_expense WHERE status=1 OR status=2", null);
                    csvWrite.writeNext(curCSV.getColumnNames());
                    while (curCSV.moveToNext()) {
                        byte[] image = curCSV.getBlob(6);
                        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String s = Base64.encodeToString(b, Base64.DEFAULT);
                        //String s = new String(image);
                        //Which column you want to exprort
                        String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), s};
                        csvWrite.writeNext(arrStr);
                        //Toast.makeText(Settings.this, "সকল আয়ব্যায়ের ব্যাকআপ নেয়া হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                    csvWrite.close();
                    curCSV.close();
                } catch (Exception sqlEx) {
                    Log.e("Settings", sqlEx.getMessage(), sqlEx);
                }


                // Borrow Due
                DatabaseHandler dbhelper1 = new DatabaseHandler(getApplicationContext());

                File file1 = new File(exportDir, "AllDueBalance.csv");
                try {
                    file1.createNewFile();
                    CSVWriter csvWrite1 = new CSVWriter(new FileWriter(file1));
                    SQLiteDatabase db1 = dbhelper1.getReadableDatabase();
                    Cursor curCSV1 = db1.rawQuery("SELECT  * FROM income_expense WHERE status=3 OR status=4 OR status=33 OR status=44", null);
                    csvWrite1.writeNext(curCSV1.getColumnNames());
                    while (curCSV1.moveToNext()) {
                        byte[] image = curCSV1.getBlob(6);
                        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String s = Base64.encodeToString(b, Base64.DEFAULT);
                        //String s = new String(image);
                        //Which column you want to exprort
                        String arrStr[] = {curCSV1.getString(0), curCSV1.getString(1), curCSV1.getString(2), curCSV1.getString(3), curCSV1.getString(4), curCSV1.getString(5), s};
                        csvWrite1.writeNext(arrStr);
                        //Toast.makeText(Settings.this, "সকল পাওনাদেনার ব্যাকআপ নেয়া হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                    csvWrite1.close();
                    curCSV1.close();
                } catch (Exception sqlEx) {
                    Log.e("Settings", sqlEx.getMessage(), sqlEx);
                }


                // All User Account
                DatabaseHandlerUser dbhelper2 = new DatabaseHandlerUser(getApplicationContext());

                File file2 = new File(exportDir, "AllUser.csv");
                try {
                    file2.createNewFile();
                    CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file2));
                    SQLiteDatabase db2 = dbhelper2.getReadableDatabase();
                    Cursor curCSV2 = db2.rawQuery("SELECT  * FROM user_list", null);
                    csvWrite2.writeNext(curCSV2.getColumnNames());
                    while (curCSV2.moveToNext()) {
                        byte[] image = curCSV2.getBlob(6);
                        Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        String s = Base64.encodeToString(b, Base64.DEFAULT);
                        //String s = new String(image);
                        //Which column you want to exprort
                        String arrStr[] = {curCSV2.getString(0), curCSV2.getString(1), curCSV2.getString(2), curCSV2.getString(3), curCSV2.getString(4), curCSV2.getString(5), s};
                        csvWrite2.writeNext(arrStr);
                        //Toast.makeText(Settings.this, "ব্যবহারকারির একাউন্টের ব্যাকআপ নেয়া হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                    csvWrite2.close();
                    curCSV2.close();
                } catch (Exception sqlEx) {
                    Log.e("Settings", sqlEx.getMessage(), sqlEx);
                }


                // All Statement
                File file3 = new File(exportDir, "AllStatement.csv");
                try {
                    file3.createNewFile();
                    CSVWriter csvWrite2 = new CSVWriter(new FileWriter(file3));
                    String arrStr[] = {dd, incinc, expexp, balbal};
                    csvWrite2.writeNext(arrStr);
                    csvWrite2.close();
                    Toast.makeText(Settings.this, "সমস্তকিছুর ব্যাকআপ নেয়া সম্পন্ন হয়েছে", Toast.LENGTH_LONG).show();
                } catch (Exception sqlEx) {
                    Log.e("Settings", sqlEx.getMessage(), sqlEx);
                }
            }
        });

        TextView imp = (TextView) findViewById(R.id.imp);
        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //All Balance Import
                File dir = Environment.getExternalStorageDirectory();

                //Toast.makeText(Settings.this, "Balance", Toast.LENGTH_SHORT).show();
                File filename = new File(dir, "AllBalance.csv");
                String path = dir.getAbsolutePath() + "/SohojHisab/AllBalance.csv";
                CSVReader csvReader = null;
                try {
                    csvReader = new CSVReader(new FileReader(path));
                    String[] row = null;
                    int iteration = 0;
                    while ((row = csvReader.readNext()) != null) {
                        if (iteration == 0) {
                            iteration++;
                            continue;
                        }
                        /*Toast.makeText(Settings.this, row[0]
                                + "," + row[1]
                                + "," + row[2]
                                + "," + row[3]
                                + "," + row[4]
                                + "," + row[5]
                                + "," + row[6], Toast.LENGTH_LONG).show();*/
                        String st = row[5];
                        String amt = row[2];
                        int aa = Integer.parseInt(amt);
                        String img = row[6];
                        byte[] encodeByte = Base64.decode(img, Base64.DEFAULT);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        photo = baos.toByteArray();
                        //photo = img.getBytes();
                            /*Uri uri = Uri.parse(img);
                            if (uri != null) {
                                bitmap = decodeUri(uri, 100);
                            }
                            InputStream iStream =   getContentResolver().openInputStream(uri);
                            photo = getBytes(iStream);*/
                        if (st.equals("1")) {
                            int itotal = ii + aa;
                            int btotal = itotal - ee;
                            //Toast.makeText(this, "" + itotal + " " + ee + " " + btotal, Toast.LENGTH_SHORT).show();

                            String it = String.valueOf(itotal);
                            String bt = String.valueOf(btotal);
                            String et = String.valueOf(ee);
                            dbState.addBalance(new Balance(row[1], amt, "0", bt));
                            SharedPreferences sharedPreferences = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean("mybalance", true);
                            editor.putString("balance", row[1] + "," + itotal + "," + et + "," + +btotal);
                            //editor.putString(Config.NAME_SHARED_PREF, email);

                            //Saving values to editor
                            editor.commit();

                            SharedPreferences sharedPreferences2 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

                            if (sharedPreferences2.contains("balance")) {
                                rest = (sharedPreferences2).getString("balance", "");
                            }
                            if (!sharedPreferences2.contains("balance")) {
                                rest = "0000-00-00,0,0,0";
                            }
                            if (rest.equals("")) {
                                rest = "0000-00-00,0,0,0";
                            }
                            String[] results = rest.split(",");
                            dd = results[0];
                            incinc = results[1];
                            ii = Integer.parseInt(incinc);
                            expexp = results[2];
                            ee = Integer.parseInt(expexp);
                            balbal = results[3];
                            bb = Integer.parseInt(balbal);

                            int itotal1 = ii + aa;
                            int btotal1 = itotal1 - ee;
                            //Toast.makeText(this, "" + itotal + " " + ee + " " + btotal, Toast.LENGTH_SHORT).show();

                            String it1 = String.valueOf(itotal1);
                            String bt1 = String.valueOf(btotal1);
                            String et1 = String.valueOf(ee);
                            //dbState.addBalance(new Balance(row[1], amt, "0", bt1));
                            SharedPreferences sharedPreferences1 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                            //Adding values to editor
                            editor1.putBoolean("mybalance", true);
                            editor1.putString("balance", row[1] + "," + itotal1 + "," + et1 + "," + +btotal1);
                            //editor.putString(Config.NAME_SHARED_PREF, email);

                            //Saving values to editor
                            editor.commit();
                        }
                        if (st.equals("2")) {
                            int etotal = ee + aa;
                            int btotal = ii - etotal;
                            //Toast.makeText(this, "" + ii + " " + etotal + " " + btotal, Toast.LENGTH_SHORT).show();

                            String it = String.valueOf(ii);
                            String bt = String.valueOf(btotal);
                            String et = String.valueOf(etotal);
                            dbState.addBalance(new Balance(row[1], "0", amt, bt));
                            SharedPreferences sharedPreferences = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean("mybalance", true);
                            editor.putString("balance", row[1] + "," + it + "," + etotal + "," + +btotal);
                            //editor.putString(Config.NAME_SHARED_PREF, email);

                            //Saving values to editor
                            editor.commit();

                            SharedPreferences sharedPreferences2 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

                            if (sharedPreferences2.contains("balance")) {
                                rest = (sharedPreferences2).getString("balance", "");
                            }
                            if (!sharedPreferences2.contains("balance")) {
                                rest = "0000-00-00,0,0,0";
                            }
                            if (rest.equals("")) {
                                rest = "0000-00-00,0,0,0";
                            }
                            String[] results = rest.split(",");
                            dd = results[0];
                            incinc = results[1];
                            ii = Integer.parseInt(incinc);
                            expexp = results[2];
                            ee = Integer.parseInt(expexp);
                            balbal = results[3];
                            bb = Integer.parseInt(balbal);

                            int etotal1 = ee + aa;
                            int btotal1 = ii - etotal1;
                            //Toast.makeText(this, "" + ii + " " + etotal + " " + btotal, Toast.LENGTH_SHORT).show();

                            String it1 = String.valueOf(ii);
                            String bt1 = String.valueOf(btotal1);
                            String et1 = String.valueOf(etotal1);
                            //dbState.addBalance(new Balance(row[1], "0", amt, bt1));
                            SharedPreferences sharedPreferences1 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();

                            //Adding values to editor
                            editor1.putBoolean("mybalance", true);
                            editor1.putString("balance", row[1] + "," + it1 + "," + etotal1 + "," + +btotal1);
                            //editor.putString(Config.NAME_SHARED_PREF, email);

                            //Saving values to editor
                            editor.commit();
                        }
                        db.addContacts(new Contact(row[1], row[2], row[3], row[4], row[5], photo));
                    }
                    Toast.makeText(getApplicationContext(), "সমস্ত লেনদেন সফলভাবে তালিকাভুক্ত করা হয়েছে", Toast.LENGTH_LONG).show();
                    csvReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //All Borrow Due Import
                File dir1 = Environment.getExternalStorageDirectory();

                //Toast.makeText(Settings.this, "Borrow Due", Toast.LENGTH_SHORT).show();
                File filename1 = new File(dir1, "AllDueBalance.csv");
                String path1 = dir1.getAbsolutePath() + "/SohojHisab/AllDueBalance.csv";
                CSVReader csvReader1 = null;
                try {
                    csvReader1 = new CSVReader(new FileReader(path1));
                    String[] row = null;
                    int iteration = 0;
                    while ((row = csvReader1.readNext()) != null) {
                        if (iteration == 0) {
                            iteration++;
                            continue;
                        }
                        /*Toast.makeText(Settings.this, row[0]
                                + "," + row[1]
                                + "," + row[2]
                                + "," + row[3]
                                + "," + row[4]
                                + "," + row[5]
                                + "," + row[6], Toast.LENGTH_LONG).show();*/
                        String img = row[6];
                        byte[] encodeByte = Base64.decode(img, Base64.DEFAULT);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                                encodeByte.length);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        photo1 = baos.toByteArray();
                        //photo1 = img.getBytes();
                        //Toast.makeText(Settings.this, Arrays.toString(photo) + "", Toast.LENGTH_SHORT).show();
                        db.addContacts(new Contact(row[1], row[2], row[3], row[4], row[5], photo1));
                    }
                    csvReader1.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //All User Account Import
                File dir2 = Environment.getExternalStorageDirectory();

                //Toast.makeText(Settings.this, "User", Toast.LENGTH_SHORT).show();
                File filename2 = new File(dir2, "AllUser.csv");
                String path2 = dir2.getAbsolutePath() + "/SohojHisab/AllUser.csv";
                CSVReader csvReader2 = null;
                try {
                    csvReader2 = new CSVReader(new FileReader(path2));
                    String[] row = null;
                    int iteration = 0;
                    while ((row = csvReader2.readNext()) != null) {
                        if (iteration == 0) {
                            iteration++;
                            continue;
                        }
                        /*Toast.makeText(Settings.this, row[0]
                                + "," + row[1]
                                + "," + row[2]
                                + "," + row[3]
                                + "," + row[4]
                                + "," + row[5]
                                + "," + row[6], Toast.LENGTH_LONG).show();*/
                        String img = row[6];
                        byte[] encodeByte = Base64.decode(img, Base64.DEFAULT);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                                encodeByte.length);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        photo2 = baos.toByteArray();
                        //photo2 = img.getBytes();
                        dbUser.addUser(new Contact(row[1], row[2], row[3], row[4], row[5], photo2));
                    }
                    csvReader2.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (impo.equals("1")) {
            imp.setClickable(false);
            imp.setBackgroundResource(R.drawable.inactive_back);
            imp.setTextColor(Color.parseColor("#5e5e5e"));
            imp.setText("সংযুক্ত করা আছে");
        } else {
            imp.setClickable(true);
            imp.setBackgroundResource(R.drawable.send_back);
            imp.setTextColor(Color.parseColor("#ffffff"));
            imp.setText("সংযুক্ত করুন");
        }
        //ShowRecords();

        email = (TextView) findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=&body=&to=itlslhelpdesk@gmail.com");
                testIntent.setData(data);
                startActivity(testIntent);
            }
        });

        phone = (TextView) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialContactPhone("01842485222");
            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Settings.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Settings.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Settings.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Settings.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            //Write external Storage
            //Read External Storage
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void showTimePickerDialog(int h, int m) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.timepicker_header, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        Log.d(TAG, "onTimeSet: hour " + hour);
                        Log.d(TAG, "onTimeSet: min " + min);
                        localData.set_hour(hour);
                        localData.set_min(min);
                        tvTime.setText(getFormatedTime(hour, min));
                        NotificationScheduler.setReminder(Settings.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                    }
                }, h, m, false);

        builder.setCustomTitle(view);
        builder.show();

    }

    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

}
