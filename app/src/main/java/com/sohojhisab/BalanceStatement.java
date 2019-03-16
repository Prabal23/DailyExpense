package com.sohojhisab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class BalanceStatement extends AppCompatActivity implements View.OnClickListener{
    private DatabaseHandler db;
    private DatabaseHandler1 db1;
    private byte[] photo;
    Bitmap bp;
    private Balance dataModel;
    private dataAdapter3 data;
    ListView listView;
    Typeface fontAwesomeFont;
    TextView menu, back, inc, exp, balance, income, expense, total, c1, c2, s, st, end, pdf;
    CircleImageView dp;
    private Contact dataModel2, dataModel1, dataModelUser;
    private Balance dataModel3;
    String st_date = "", end_date = "";
    int exp_sum = 0, inc_sum = 0, tot = 0, total_tk = 0;
    int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private DatabaseHandlerUser dbUser;
    String result = "//", fname = "", uname = "", password = "";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    LinearLayout pdf1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_statement);

        db = new DatabaseHandler(this);
        db1 = new DatabaseHandler1(this);
        dbUser = new DatabaseHandlerUser(this);

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
        inc = (TextView) findViewById(R.id.ok);
        exp = (TextView) findViewById(R.id.notok);
        balance = (TextView) findViewById(R.id.money);
        c1 = (TextView) findViewById(R.id.c1);
        c2 = (TextView) findViewById(R.id.c2);
        s = (TextView) findViewById(R.id.s);
        menu.setTypeface(fontAwesomeFont);
        inc.setTypeface(fontAwesomeFont);
        exp.setTypeface(fontAwesomeFont);
        balance.setTypeface(fontAwesomeFont);
        c1.setTypeface(fontAwesomeFont);
        c2.setTypeface(fontAwesomeFont);
        s.setTypeface(fontAwesomeFont);
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

        income = (TextView) findViewById(R.id.inc);
        expense = (TextView) findViewById(R.id.exp);
        total = (TextView) findViewById(R.id.tk);
        st = (TextView) findViewById(R.id.st);
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BalanceStatement.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int d = monthOfYear;
                                int day = dayOfMonth;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, d, day);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                st_date = sdf.format(calendar.getTime());
                                st.setText("শুরুর তারিখ\n" + st_date);
                                //st.setText("Start Date\n" + year + "-" + d + "-" + day);
                                //st_date = year + "-" + d + "-" + day;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        end = (TextView) findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear1 = c.get(Calendar.YEAR);
                mMonth1 = c.get(Calendar.MONTH);
                mDay1 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BalanceStatement.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int d = monthOfYear;
                                int day = dayOfMonth;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, d, day);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                end_date = sdf.format(calendar.getTime());
                                end.setText("শেষের তারিখ\n" + end_date);
                                //end.setText("End Date\n" + year + "-" + d + "-" + day);
                                //end_date = year + "-" + d + "-" + day;
                            }
                        }, mYear1, mMonth1, mDay1);
                datePickerDialog.show();
            }
        });

        listView = (ListView) findViewById(R.id.list);
        pdf1 = (LinearLayout)findViewById(R.id.sub);

        dp = (CircleImageView) findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                /*PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();*/
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(BalanceStatement.this);
                MenuInflater inflater = new MenuInflater(BalanceStatement.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(BalanceStatement.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(BalanceStatement.this);
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

        ShowIncome();
        ShowExpense();

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st.setText("");
                st_date = "";
                st.setHintTextColor(Color.parseColor("#9e9e9e"));
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end.setText("");
                end_date = "";
                end.setHintTextColor(Color.parseColor("#9e9e9e"));
            }
        });

        LinearLayout search = (LinearLayout) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (st_date.equals("")) {
                    st.setHintTextColor(Color.RED);
                }
                if (end_date.equals("")) {
                    end.setHintTextColor(Color.RED);
                }
                if (st_date.equals("") && end_date.equals("")) {
                    exp_sum = 0;
                    inc_sum = 0;
                    ShowIncome();
                    ShowExpense();
                }
                if (!st_date.equals("") || !end_date.equals("")) {
                    ShowIncome1();
                    //ShowExpense1();
                }
            }
        });

        init();
        fn_permission();
    }

    private void ShowIncome() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllIncome());
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel2 = contacts.get(i);
            String amount = dataModel2.getAmount();
            int tk = Integer.parseInt(amount);
            inc_sum += tk;
        }
        String inc_tk = String.valueOf(inc_sum);
        inc_tk = inc_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        income.setText("" + inc_tk);
        if (c != 0) {
            ShowRecords();
        }
    }

    private void ShowExpense() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllExpense());
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

    private void ShowRecords() {
        ArrayList<Balance> balances = new ArrayList<>(db1.getBalance());
        data = new dataAdapter3(this, balances);
        listView.setAdapter(data);
        tot = listView.getAdapter().getCount();
        String total = String.valueOf(tot);
    }

    private void ShowIncome1() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllIncome());
        int c = contacts.size();
        if (c != 0) {
            ShowRecords1();
        }
    }

    private void ShowRecords1() {
        ArrayList<Balance> balances = new ArrayList<>(db1.getFilterBalance(st_date, end_date));
        data = new dataAdapter3(this, balances);
        listView.setAdapter(data);
        tot = listView.getAdapter().getCount();
        String total = String.valueOf(tot);
    }

    private void init() {
        pdf = (TextView) findViewById(R.id.pdf);
        pdf.setClickable(false);
        listener();
    }

    private void listener() {
        pdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pdf:
                if (boolean_save) {

                } else {
                    if (boolean_permission) {
                        bitmap = loadBitmapFromView(pdf1, pdf1.getWidth(), pdf1.getHeight());
                        createPdf();

                        String targetPdf = "/sdcard/ব্যালেন্স স্ট্যাটমেন্ট.pdf";
                        Snackbar snackbar = Snackbar
                                .make(pdf1, "Saved to - " + targetPdf, Snackbar.LENGTH_LONG);
                                /*.setAction("View", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (boolean_save) {
                                            Intent intent = new Intent(getBaseContext(), CVPDFViewActivity.class);
                                            intent.putExtra("name", fullname);
                                            startActivity(intent);
                                        }
                                    }
                                });*/

                        snackbar.setActionTextColor(Color.RED);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();

                    } else {

                    }

                    //createPdf();
                    break;
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        //PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 2).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/ব্যালেন্স স্ট্যাটমেন্ট.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            boolean_save = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
        //Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BalanceStatement.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BalanceStatement.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BalanceStatement.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BalanceStatement.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
