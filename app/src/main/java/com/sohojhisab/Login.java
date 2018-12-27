package com.sohojhisab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    Typeface fontAwesomeFont;
    TextView user, pass, send, signup, err;
    EditText username, password;
    int count = 0;
    private DatabaseHandlerUser db, db1;
    private Contact dataModel;
    String Fullname = "";
    boolean loggedIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db1 = new DatabaseHandlerUser(this);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 1000 * 60 * 60 * 2;
        int interval = 1000 * 2;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

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
