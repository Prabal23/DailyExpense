package com.sohojhisab;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    Typeface fontAwesomeFont;
    TextView menu, inc, exp, balance, add1, add2, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, view1, view2, view3, income, expense, total;
    CircleImageView dp;
    private DatabaseHandler db;
    private byte[] photo;
    Bitmap bp;
    private DatabaseHandlerUser dbUser;
    private Contact dataModel, dataModel1, dataModelUser;
    private dataAdapter data;
    private dataAdapter1 data1;
    int exp_sum = 0, inc_sum = 0;
    String res = "//", fname = "", uname = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbUser = new DatabaseHandlerUser(this);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_login", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("login")) {
            res = (sharedPreferences).getString("login", "");
        } else {
            res = "//";
        }

        String[] result = res.split("/");
        fname = result[0];
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        uname = result[1];
        //Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        password = result[2];

        TextView usern = (TextView)findViewById(R.id.user);
        usern.setText(fname);
        db = new DatabaseHandler(this);

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 1000 * 60 * 60 * 2;
        int interval = 1000 * 2;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        menu = (TextView) findViewById(R.id.menu);
        inc = (TextView) findViewById(R.id.ok);
        exp = (TextView) findViewById(R.id.notok);
        balance = (TextView) findViewById(R.id.money);
        add1 = (TextView) findViewById(R.id.add1);
        add2 = (TextView) findViewById(R.id.add2);
        i1 = (TextView) findViewById(R.id.i1);
        i2 = (TextView) findViewById(R.id.i2);
        i3 = (TextView) findViewById(R.id.i3);
        i4 = (TextView) findViewById(R.id.i4);
        i5 = (TextView) findViewById(R.id.i5);
        i6 = (TextView) findViewById(R.id.i6);
        i7 = (TextView) findViewById(R.id.i7);
        i8 = (TextView) findViewById(R.id.i8);
        i9 = (TextView) findViewById(R.id.i9);
        i10 = (TextView) findViewById(R.id.i10);
        view1 = (TextView) findViewById(R.id.view1);
        view2 = (TextView) findViewById(R.id.view2);
        view3 = (TextView) findViewById(R.id.view3);
        menu.setTypeface(fontAwesomeFont);
        inc.setTypeface(fontAwesomeFont);
        exp.setTypeface(fontAwesomeFont);
        balance.setTypeface(fontAwesomeFont);
        add1.setTypeface(fontAwesomeFont);
        add2.setTypeface(fontAwesomeFont);
        i1.setTypeface(fontAwesomeFont);
        i2.setTypeface(fontAwesomeFont);
        i3.setTypeface(fontAwesomeFont);
        i4.setTypeface(fontAwesomeFont);
        i5.setTypeface(fontAwesomeFont);
        i6.setTypeface(fontAwesomeFont);
        i7.setTypeface(fontAwesomeFont);
        i8.setTypeface(fontAwesomeFont);
        i9.setTypeface(fontAwesomeFont);
        i10.setTypeface(fontAwesomeFont);
        view1.setTypeface(fontAwesomeFont);
        view2.setTypeface(fontAwesomeFont);
        view3.setTypeface(fontAwesomeFont);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Settings.class);
                startActivity(intent);
            }
        });

        income = (TextView) findViewById(R.id.inc);
        expense = (TextView) findViewById(R.id.exp);
        total = (TextView) findViewById(R.id.tk);

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddIncome.class);
                startActivity(intent);
            }
        });

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllIncome.class);
                startActivity(intent);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddExpense.class);
                startActivity(intent);
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllExpense.class);
                startActivity(intent);
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllBalance.class);
                startActivity(intent);
            }
        });

        LinearLayout l11 = (LinearLayout) findViewById(R.id.l11);
        l11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllIncome.class);
                startActivity(intent);
            }
        });

        LinearLayout l12 = (LinearLayout) findViewById(R.id.l12);
        l12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllExpense.class);
                startActivity(intent);
            }
        });

        LinearLayout l21 = (LinearLayout) findViewById(R.id.l21);
        l21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DueBorrow.class);
                startActivity(intent);
            }
        });

        LinearLayout l22 = (LinearLayout) findViewById(R.id.l22);
        l22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllBorrowDue.class);
                startActivity(intent);
            }
        });

        LinearLayout l31 = (LinearLayout) findViewById(R.id.l31);
        l31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CategoryReport.class);
                startActivity(intent);
            }
        });

        LinearLayout l32 = (LinearLayout) findViewById(R.id.l32);
        l32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Calculator.class);
                startActivity(intent);
            }
        });

        LinearLayout l41 = (LinearLayout) findViewById(R.id.l41);
        l41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllBalance.class);
                startActivity(intent);
            }
        });

        LinearLayout l42 = (LinearLayout) findViewById(R.id.l42);
        l42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BalanceStatement.class);
                startActivity(intent);
            }
        });

        LinearLayout l51 = (LinearLayout) findViewById(R.id.l51);
        l51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddIncome.class);
                startActivity(intent);
            }
        });

        LinearLayout l52 = (LinearLayout) findViewById(R.id.l52);
        l52.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddExpense.class);
                startActivity(intent);
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
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(MainActivity.this);
                MenuInflater inflater = new MenuInflater(MainActivity.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(MainActivity.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(MainActivity.this);
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

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                exp_sum = 0;
                                inc_sum = 0;
                                ShowIncome();
                                ShowExpense();
                                //Toast.makeText(MainActivity.this, "dhur", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        /*ShowIncome();
        ShowExpense();*/
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.profile:
                // do your code
                return true;
            case R.id.logout:
                // do your code
                return true;
        }
        return false;
    }

    private void ShowIncome() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllIncome());
        data = new dataAdapter(this, contacts);
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel = contacts.get(i);
            String amount = dataModel.getAmount();
            int tk = Integer.parseInt(amount);
            inc_sum += tk;
        }
        String inc_tk = String.valueOf(inc_sum);
        inc_tk = inc_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        income.setText("" + inc_tk);
        if (inc_sum == 0) {
            add2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "প্রথমে আয় যোগ করুন" +
                            "" +
                            "" +
                            "!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            add2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), AddExpense.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void ShowExpense() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllExpense());
        data1 = new dataAdapter1(this, contacts);
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel1 = contacts.get(i);
            String amount = dataModel1.getAmount();
            int tk = Integer.parseInt(amount);
            exp_sum += tk;
        }
        String exp_tk = String.valueOf(exp_sum);
        exp_tk = exp_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        expense.setText("" + exp_tk);
        int total_tk = inc_sum - exp_sum;
        String tot_tk = String.valueOf(total_tk);
        tot_tk = tot_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        total.setText("" + tot_tk);
    }
}
