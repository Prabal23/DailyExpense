package com.sohojhisab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Calculator extends AppCompatActivity {
    private TextView _screen, res;
    private String display = "", rr = "0*0";
    private String currentoparator = "";
    private String result = "", count = "0", result1 = "0";
    TextView menu, back, clip, clip1;
    Typeface fontAwesomeFont;
    CircleImageView dp;
    LinearLayout copy, copy1;
    Button per;
    TextView n1, n2, nres;
    private DatabaseHandlerUser dbUser;
    Contact dataModelUser;
    String results = "//", fname = "", uname = "", password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        dbUser = new DatabaseHandlerUser(this);

        SharedPreferences sharedPreferences1 = getSharedPreferences("shared_login", Context.MODE_PRIVATE);
        if (sharedPreferences1.contains("login")) {
            results = (sharedPreferences1).getString("login", "");
        } else {
            results = "//";
        }

        String[] result2 = results.split("/");
        fname = result2[0];
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        uname = result2[1];
        //Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        password = result2[2];

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        menu = (TextView) findViewById(R.id.menu);
        back = (TextView) findViewById(R.id.back);
        clip = (TextView) findViewById(R.id.clip);
        clip1 = (TextView) findViewById(R.id.clip1);
        menu.setTypeface(fontAwesomeFont);
        clip.setTypeface(fontAwesomeFont);
        clip1.setTypeface(fontAwesomeFont);
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

        _screen = (TextView) findViewById(R.id.textView);
        _screen.setText("0");
        res = (TextView) findViewById(R.id.res);
        res.setText("0");

        n1 = (TextView) findViewById(R.id.n1);
        n1.setHint("0");
        n2 = (TextView) findViewById(R.id.n2);
        n2.setHint("0");
        nres = (TextView) findViewById(R.id.nres);
        nres.setHint("0");

        copy = (LinearLayout) findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyRes();
            }
        });

        copy1 = (LinearLayout) findViewById(R.id.copy1);
        copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyRes1();
            }
        });

        per = (Button) findViewById(R.id.btnpercent);
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //percentage();
                String num1 = n1.getText().toString();
                String num2 = n2.getText().toString();
                String numres = nres.getText().toString();

                int r1 = Integer.parseInt(num1);
                int r2 = Integer.parseInt(num2);
                int t = (r1*r2)/100;
                float f = (float)t;
                result1 = String.valueOf(f);
                nres.setText(result1);
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
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(Calculator.this);
                MenuInflater inflater = new MenuInflater(Calculator.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(Calculator.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(Calculator.this);
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
    }

    private void updateScreen() {
        _screen.setText(display);
    }

    public void onClickNumber(View v) {
        if (result != "") {
            clear();
            updateScreen();
        }
        Button b = (Button) v;
        display += b.getText();
        updateScreen();
    }

    private boolean isOperator(char op) {
        switch (op) {
            case '+':
            case '-':
            case '*':
            case '/':
                //case '%':
                return true;
            default:
                return false;
        }
    }

    public void onClickOperator(View v) {
        if (display == "") return;
        Button b = (Button) v;
        if (result != "") {
            String _display = result;
            clear();
            display = _display;
        }

        if (currentoparator != "") {
            //Log.d("Calc", "" +display.charAt(display.length()-1));
            if (isOperator(display.charAt(display.length() - 1))) {
                display = display.replace(display.charAt(display.length() - 1), b.getText().charAt(0));
                updateScreen();
                return;
            } else {
                getResult();
                display = result;
                result = "";
            }
            currentoparator = b.getText().toString();
        }

        display += b.getText();
        currentoparator = b.getText().toString();
        updateScreen();
    }

    private void clear() {
        display = "";
        currentoparator = "";
        result = "";
        _screen.setText("0");
        res.setText("0");
        //count = "0";
    }

    public void onClickClear(View v) {
        clear();
        updateScreen();
    }

    private double operate(String a, String b, String op) {
        switch (op) {
            case "+":
                return Double.valueOf(a) + Double.valueOf(b);
            case "-":
                return Double.valueOf(a) - Double.valueOf(b);
            case "*":
                return Double.valueOf(a) * Double.valueOf(b);
            case "/":
                try {
                    return Double.valueOf(a) / Double.valueOf(b);
                } catch (Exception e) {
                    //Log.d("Calc", e.getMessage());
                }
            /*case "%":
                return Double.valueOf(a) * (Double.valueOf(b)/100);*/
            default:
                return -1;
        }
    }

    private boolean getResult() {
        if (currentoparator == "") return false;
        String[] operation = display.split(Pattern.quote(currentoparator));
        if (operation.length < 2) return false;
        result = String.valueOf(operate(operation[0], operation[1], currentoparator));
        return true;
    }

    public void percentage() {
        String st = _screen.getText().toString();
        Toast.makeText(this, "/"+st+"/", Toast.LENGTH_SHORT).show();
        if (st.equals("0") || st.equals("")) {
            rr = "0*0";
        } else {
            rr = st;
        }
        String[] rr1 = rr.split("[*]");
        String r1 = rr1[0];
        String r2 = rr1[1];
        int ri1 = Integer.parseInt(r1);
        int ri2 = Integer.parseInt(r2);
        int t = (ri1 * ri2) / 100;
        float tt = (float) t;
        result = String.valueOf(tt);
        res.setText(result);
        count = "1";
    }

    public void onClickEqual(View v) {

        if (display == "") return;
        if (!getResult()) return;
       /* if (count.equals("1")) {
            res.setText(result);
        } else {
            _screen.setText(display*//* + "\n" + String.valueOf(result)*//*);
            res.setText(String.valueOf(result));
        }*/
        _screen.setText(display/* + "\n" + String.valueOf(result)*/);
        res.setText(String.valueOf(result));
    }

    public void copyRes() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", result);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Result copied", Toast.LENGTH_SHORT).show();
    }

    public void copyRes1() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", result1);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Result copied", Toast.LENGTH_SHORT).show();
    }

}
