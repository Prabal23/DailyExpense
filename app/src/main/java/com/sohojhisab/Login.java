package com.sohojhisab;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    Typeface fontAwesomeFont;
    TextView user, pass, send, signup, err;
    EditText username, password;
    int count = 0;
    private DatabaseHandlerUser db1;
    private Contact dataModel;
    String Fullname = "", balance = "", dueborrow = "", useracc = "", imp = "", result = "0000-00-00,0,0,0";
    String dd = "", incinc = "", expexp = "", balbal = "";
    int ii = 0, ee = 0, bb = 0;
    boolean loggedIn;
    private DatabaseHandlerUser dbUser;
    private DatabaseHandler1 dbState;
    private DatabaseHandler db;
    private byte[] photo, photo1, photo2;
    Bitmap bitmap, bitmap1, bitmap2;
    Dialog dialog;
    static final Integer WRITE_EXST = 1;
    static final Integer READ_EXST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);*/
        //permissionTake();

        db = new DatabaseHandler(this);
        dbState = new DatabaseHandler1(this);
        db1 = new DatabaseHandlerUser(this);
        dbUser = new DatabaseHandlerUser(this);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        SharedPreferences sharedPreferences2 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

        if (sharedPreferences2.contains("balance")) {
            result = (sharedPreferences2).getString("balance", "");
        }
        if (!sharedPreferences2.contains("balance")) {
            result = "0000-00-00,0,0,0";
        }
        if (result.equals("")) {
            result = "0000-00-00,0,0,0";
        }
        String[] results = result.split(",");
        dd = results[0];
        incinc = results[1];
        ii = Integer.parseInt(incinc);
        expexp = results[2];
        ee = Integer.parseInt(expexp);
        balbal = results[3];
        bb = Integer.parseInt(balbal);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 1000 * 60 * 60 * 2;
        int interval = 1000 * 2;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        SharedPreferences sharedPreferences = getSharedPreferences("imported_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("data")) {
            imp = (sharedPreferences).getString("data", "");
            //Toast.makeText(this, imp, Toast.LENGTH_SHORT).show();
        } else {
            imp = "";
        }

        /*SharedPreferences preferences1 = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        //Getting editor
        SharedPreferences.Editor editor1 = preferences1.edit();

        //Puting the value false for loggedin
        editor1.putBoolean("mybalance", false);

        //Putting blank value to email
        editor1.putString("balance", "");

        //Saving the sharedpreferences
        editor1.commit();*/

        user = (TextView) findViewById(R.id.user);
        pass = (TextView) findViewById(R.id.pass);
        send = (TextView) findViewById(R.id.send);
        user.setTypeface(fontAwesomeFont);
        pass.setTypeface(fontAwesomeFont);
        send.setTypeface(fontAwesomeFont);

        LinearLayout login = (LinearLayout) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);*/
                loginUser();
            }
        });

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        final TextView viewpass = (TextView) findViewById(R.id.viewpass);
        viewpass.setTypeface(fontAwesomeFont);
        viewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count % 2 == 0) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    viewpass.setText(R.string.eyenot);
                    //Toast.makeText(Login.this, "Even", Toast.LENGTH_SHORT).show();
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    viewpass.setText(R.string.eye);
                    //Toast.makeText(Login.this, "Odd", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });

        err = (TextView) findViewById(R.id.err);

        permissionTake();
        //backupCheck();
    }

    public void permissionTake() {
        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Login.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    backupCheck();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void backupCheck() {
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
                balance = "1";

            }
            //Toast.makeText(getApplicationContext(), "সমস্ত লেনদেন সফলভাবে তালিকাভুক্ত করা হয়েছে", Toast.LENGTH_LONG).show();
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
                dueborrow = "1";
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
                useracc = "1";
            }
            csvReader2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ((balance.equals("1") || dueborrow.equals("1") || useracc.equals("1")) && !imp.equals("1")) {
            dialog = new Dialog(Login.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.backup_dialog);
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
                                    result = (sharedPreferences2).getString("balance", "");
                                }
                                if (!sharedPreferences2.contains("balance")) {
                                    result = "0000-00-00,0,0,0";
                                }
                                if (result.equals("")) {
                                    result = "0000-00-00,0,0,0";
                                }
                                String[] results = result.split(",");
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
                                    result = (sharedPreferences2).getString("balance", "");
                                }
                                if (!sharedPreferences2.contains("balance")) {
                                    result = "0000-00-00,0,0,0";
                                }
                                if (result.equals("")) {
                                    result = "0000-00-00,0,0,0";
                                }
                                String[] results = result.split(",");
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
                        //Toast.makeText(getApplicationContext(), "সমস্ত লেনদেন সফলভাবে তালিকাভুক্ত করা হয়েছে", Toast.LENGTH_LONG).show();
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
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            photo1 = baos.toByteArray();
                            //photo1 = img.getBytes();
                            /*Uri uri = Uri.parse(img);
                            if (uri != null) {
                                bitmap1 = decodeUri(uri, 100);
                            }
                            InputStream iStream =   getContentResolver().openInputStream(uri);
                            photo1 = getBytes(iStream);*/
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
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            photo2 = baos.toByteArray();
                            //photo2 = img.getBytes();
                            /*Uri uri = Uri.parse(img);
                            if (uri != null) {
                                bitmap2 = decodeUri(uri, 100);
                            }
                            InputStream iStream =   getContentResolver().openInputStream(uri);
                            photo2 = getBytes(iStream);*/
                            //photo2 = profileImage(bitmap2);
                            dbUser.addUser(new Contact(row[1], row[2], row[3], row[4], row[5], photo2));
                        }
                        csvReader2.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, "সব তথ্য সংযুক্তিকরণ সম্পন্ন হয়েছে", Toast.LENGTH_LONG).show();
                    imp = "1";
                    SharedPreferences sharedPreferences = getSharedPreferences("imported_data", Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putBoolean("imported", true);
                    editor.putString("data", imp);
                    //editor.putString(Config.NAME_SHARED_PREF, email);

                    //Saving values to editor
                    editor.commit();
                    dialog.dismiss();
                }
            });

            dialog.show();
        } else if ((balance.equals("1") || dueborrow.equals("1") || useracc.equals("1")) && imp.equals("1")) {

        } else {

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void loginUser() {
        String un = username.getText().toString();
        String pass = password.getText().toString();
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
        if (un.equals("")) {
            err.setText("ইউজারনেম দেওয়া হয়নি");
            err.setVisibility(View.VISIBLE);
        } else if (pass.equals("")) {
            err.setText("পাসওয়ার্ড দেওয়া হয়নি");
            err.setVisibility(View.VISIBLE);
        } else {
            final ArrayList<Contact> contacts = new ArrayList<>(db1.getUser(un, pass));
            int c = contacts.size();
            for (int i = 0; i < c; i++) {
                dataModel = contacts.get(i);
                Fullname = dataModel.getDate();
                //Toast.makeText(this, Fullname, Toast.LENGTH_SHORT).show();
            }
            if (Fullname.equals("")) {
                err.setText("ইউজারনেম/পাসওয়ার্ড ভুল হয়েছে");
                err.setVisibility(View.VISIBLE);
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("shared_login", Context.MODE_PRIVATE);

                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //Adding values to editor
                editor.putBoolean("user_login", true);
                editor.putString("login", Fullname + "/" + un + "/" + pass);
                //editor.putString(Config.NAME_SHARED_PREF, email);

                //Saving values to editor
                editor.commit();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("shared_login", Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean("user_login", false);

        //If we will get true
        if (loggedIn) {
            //We will start the Profile Activity
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
