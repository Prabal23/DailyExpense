package com.sohojhisab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryReport extends AppCompatActivity {
    private Contact dataModel, dataModelUser;
    private DatabaseHandler db;
    private DatabaseHandler1 db1;
    private DatabaseHandlerUser dbUser;
    //private DatabaseHandlerCat dbcat;
    private byte[] photo;
    CircleImageView dp;
    private Contact dataModel2;
    private String[] category = {};
    ArrayList<String> mylist;
    AutoCompleteTextView src;
    Typeface fontAwesomeFont;
    TextView menu, search, back, inc, exp, tk;
    int inc_sum = 0, exp_sum = 0, total_sum = 0;
    String result = "//", fname = "", uname = "", password = "", srcs = "", startDate = "", endDate = "", tot_sum = "";
    ListView listView;
    private dataAdapter2 data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_report);

        db = new DatabaseHandler(this);

        src = (AutoCompleteTextView) findViewById(R.id.sources);

        SharedPreferences sharedPreferences1 = getSharedPreferences("shared_login", Context.MODE_PRIVATE);
        if (sharedPreferences1.contains("login")) {
            result = (sharedPreferences1).getString("login", "");
        } else {
            result = "//";
        }

        String[] result1 = result.split("/");
        fname = result1[0];
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        uname = result1[1];
        //Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        password = result1[2];

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        menu = (TextView) findViewById(R.id.menu);
        back = (TextView) findViewById(R.id.back);
        search = (TextView) findViewById(R.id.s);
        menu.setTypeface(fontAwesomeFont);
        search.setTypeface(fontAwesomeFont);
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

        listView = (ListView) findViewById(R.id.list);

        inc = (TextView) findViewById(R.id.inc);
        exp = (TextView) findViewById(R.id.exp);
        tk = (TextView) findViewById(R.id.tk);

        dp = (CircleImageView) findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                /*PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();*/
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(CategoryReport.this);
                MenuInflater inflater = new MenuInflater(CategoryReport.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(CategoryReport.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(CategoryReport.this);
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

        ShowRecords();

        LinearLayout searching = (LinearLayout) findViewById(R.id.search);
        searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitResult();
            }
        });
    }

    private void ShowRecords() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllBalance());
        int c = contacts.size();
        mylist = new ArrayList<String>();
        for (int i = 0; i < c; i++) {
            dataModel = contacts.get(i);
            String srcs = dataModel.getSrc();
            mylist.add(srcs);
            category = mylist.toArray(category);
            String cat = Arrays.toString(category);
            //Toast.makeText(this, "" + cat, Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, mylist);

        src.setThreshold(1);
        src.setAdapter(adapter);
    }

    public void submitResult() {
        srcs = src.getText().toString();
        if (srcs.equals("")) {
            src.setHintTextColor(Color.RED);
        } else {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            Date begining = calendar.getTime();

            Calendar calendar1 = getCalendarForNow();
            calendar1.set(Calendar.DAY_OF_MONTH,
                    calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar1);
            Date end = calendar1.getTime();

            SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy");
            String t = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            startDate = spf.format(begining);
            endDate = spf.format(end);
            src.setHintTextColor(Color.parseColor("#9e9e9e"));

            final ArrayList<Contact> contacts = new ArrayList<>(db.getMonthlyBalance(srcs, startDate, t));
            int c = contacts.size();
            data = new dataAdapter2(this, contacts);
            listView.setAdapter(data);
            int tot = listView.getAdapter().getCount();
            String total = String.valueOf(tot);
            for (int i = 0; i < c; i++) {
                dataModel2 = contacts.get(i);
                String amount = dataModel2.getAmount();
                String status = dataModel2.getStatus();
                if (status.equals("1")) {
                    int tks = Integer.parseInt(amount);
                    inc_sum += tks;
                }
                if (status.equals("2")) {
                    int tks = Integer.parseInt(amount);
                    exp_sum += tks;
                }
            }
            String inc_tk = String.valueOf(inc_sum);
            inc_tk = inc_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                    .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                    .replace("8", "৮").replace("9", "৯").replace("0", "০");
            inc.setText("" + inc_tk);

            String exp_tk = String.valueOf(exp_sum);
            exp_tk = exp_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                    .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                    .replace("8", "৮").replace("9", "৯").replace("0", "০");
            exp.setText("" + exp_tk);
            total_sum = inc_sum - exp_sum;
            String tot_tk = String.valueOf(total_sum);
            tot_tk = tot_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                    .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                    .replace("8", "৮").replace("9", "৯").replace("0", "০");
            tk.setText("" + tot_tk);
        }
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
