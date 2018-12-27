package com.sohojhisab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddExpense extends AppCompatActivity {
    Typeface fontAwesomeFont;
    TextView menu, calendar, tk, source, info, send, back, camera, gallery, paste, reset, no;
    TextView calc, date, err, rep;
    CircleImageView dp;
    private ImageView memo_pic;
    LinearLayout take, choose, submit, refresh, cross;
    int mYear, mMonth, mDay;
    String text = "", exp_date = "", amt = "", srcs = "", desc = "", res = "0000-00-00,0,0,0", dd = "", incinc = "0", expexp = "0", balbal = "0";
    private final int requestCode = 20;
    int ii = 0, ee = 0, bb = 0;
    private Contact dataModel, dataModelUser;
    private Bitmap bitmap, bitmap1;
    private Uri filePath, filePath1;
    EditText amount, des;
    AutoCompleteTextView src;
    private DatabaseHandler db;
    private DatabaseHandler1 db1;
    private DatabaseHandlerUser dbUser;
    private byte[] photo;
    private String[] category = {};
    ArrayList<String> mylist;
    String result = "//", fname = "", uname = "", password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);

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

        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("balance")) {
            res = (sharedPreferences).getString("balance", "");
        }
        if (!sharedPreferences.contains("balance")) {
            res = "0000-00-00,0,0,0";
        }
        if (res.equals("")) {
            res = "0000-00-00,0,0,0";
        }
        //Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
        String[] result = res.split(",");
        dd = result[0];
        incinc = result[1];
        ii = Integer.parseInt(incinc);
        expexp = result[2];
        ee = Integer.parseInt(expexp);
        balbal = result[3];
        bb = Integer.parseInt(balbal);
        /*Toast.makeText(this, incinc, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, expexp, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, balbal, Toast.LENGTH_SHORT).show();*/
        //cc = category.length;
        //Toast.makeText(this, "" + cc, Toast.LENGTH_SHORT).show();
        menu = (TextView) findViewById(R.id.menu);
        calendar = (TextView) findViewById(R.id.calendar);
        tk = (TextView) findViewById(R.id.tk);
        source = (TextView) findViewById(R.id.source);
        info = (TextView) findViewById(R.id.info);
        send = (TextView) findViewById(R.id.send);
        back = (TextView) findViewById(R.id.back);
        camera = (TextView) findViewById(R.id.camera);
        gallery = (TextView) findViewById(R.id.gallery);
        paste = (TextView) findViewById(R.id.paste);
        reset = (TextView) findViewById(R.id.reset);
        no = (TextView) findViewById(R.id.no);
        no.setTypeface(fontAwesomeFont);
        menu.setTypeface(fontAwesomeFont);
        calendar.setTypeface(fontAwesomeFont);
        tk.setTypeface(fontAwesomeFont);
        source.setTypeface(fontAwesomeFont);
        info.setTypeface(fontAwesomeFont);
        send.setTypeface(fontAwesomeFont);
        camera.setTypeface(fontAwesomeFont);
        gallery.setTypeface(fontAwesomeFont);
        paste.setTypeface(fontAwesomeFont);
        reset.setTypeface(fontAwesomeFont);
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

        rep = (TextView) findViewById(R.id.rep);
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AllExpense.class);
                startActivity(intent);
            }
        });

        memo_pic = (ImageView) findViewById(R.id.memo);
        amount = (EditText) findViewById(R.id.amount);
        src = (AutoCompleteTextView) findViewById(R.id.src);
        des = (EditText) findViewById(R.id.des);
        err = (TextView) findViewById(R.id.err);

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);

                text = item.getText().toString();
                amount.setText(text);
            }
        });

        date = (TextView) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpense.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int d = monthOfYear;
                                int day = dayOfMonth;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, d, day);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                exp_date = sdf.format(calendar.getTime());
                                date.setText("ব্যয়ের তারিখ : " + exp_date);
                                //date.setText("ব্যয়ের তারিখ : " + year + "-" + d + "-" + day);
                                //exp_date = year + "-" + d + "-" + day;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        take = (LinearLayout) findViewById(R.id.take);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });

        choose = (LinearLayout) findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });

        Uri uris = null;
        uris = Uri.parse("android.resource://com.dailyexpense/drawable/no_image");
        if (uris != null) {
            bitmap = decodeUri(uris, 100);
        }

        submit = (LinearLayout) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRes();
            }
        });

        calc = (TextView) findViewById(R.id.calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Calculator.class);
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
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(AddExpense.this);
                MenuInflater inflater = new MenuInflater(AddExpense.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(AddExpense.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(AddExpense.this);
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

        refresh = (LinearLayout) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.setText("");
                date.setHintTextColor(Color.parseColor("#9e9e9e"));
                amount.setText("");
                amount.setHintTextColor(Color.parseColor("#9e9e9e"));
                src.setText("");
                src.setHintTextColor(Color.parseColor("#9e9e9e"));
                des.setText("");
                des.setHintTextColor(Color.parseColor("#9e9e9e"));
                Uri uri = Uri.parse("android.resource://com.dailyexpense/drawable/no_image");
                if (uri != null) {
                    bitmap = decodeUri(uri, 100);
                    memo_pic.setImageBitmap(bitmap);
                    memo_pic.setVisibility(View.GONE);
                    cross.setVisibility(View.GONE);
                }
            }
        });

        cross = (LinearLayout) findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("android.resource://com.dailyexpense/drawable/no_image");
                if (uri != null) {
                    bitmap = decodeUri(uri, 100);
                    memo_pic.setImageBitmap(bitmap);
                    memo_pic.setVisibility(View.GONE);
                    cross.setVisibility(View.GONE);
                }
            }
        });

        ShowRecords();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(this.requestCode == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }*/
        switch (requestCode) {
            case 20:
                if (resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    memo_pic.setImageBitmap(bitmap);
                    memo_pic.setVisibility(View.VISIBLE);
                    cross.setVisibility(View.VISIBLE);
                }
                break;
        }
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    if (filePath != null) {
                        bitmap = decodeUri(filePath, 100);
                        //originalImage = getResizedBitmap(bitmap, 134, 304);
                        memo_pic.setImageBitmap(bitmap);
                        memo_pic.setVisibility(View.VISIBLE);
                        cross.setVisibility(View.VISIBLE);
                    }
                    //super.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, mylist);

        src.setThreshold(1);
        src.setAdapter(adapter);
    }

    public void submitRes() {
        amt = amount.getText().toString();
        int aa = Integer.parseInt(amt);
        srcs = src.getText().toString();
        desc = des.getText().toString();

        if (exp_date.equals("")) {
            err.setText("ব্যয়ের তারিখ বাছাই করা হয়নি");
            err.setVisibility(View.VISIBLE);
            date.setHintTextColor(Color.RED);
        } else if (amt.equals("")) {
            err.setText("টাকার ঘর পূরণ হয়নি");
            err.setVisibility(View.VISIBLE);
            amount.setHintTextColor(Color.RED);
        } else if (srcs.equals("")) {
            err.setText("ব্যয়ের খাত দেওয়া হয়নি");
            err.setVisibility(View.VISIBLE);
            src.setHintTextColor(Color.RED);
        } else {
            /*photo = profileImage(bitmap);
            db.addContacts(new Contact(exp_date, amt, srcs, desc, "2", photo));
            Toast.makeText(getApplicationContext(), "Expense Added Successfully", Toast.LENGTH_LONG).show();
            err.setVisibility(View.GONE);
*/
            int etotal = ee + aa;
            int btotal = ii - etotal;
            //Toast.makeText(this, "" + ii + " " + etotal + " " + btotal, Toast.LENGTH_SHORT).show();

            String it = String.valueOf(ii);
            String bt = String.valueOf(btotal);
            String et = String.valueOf(etotal);

            photo = profileImage(bitmap);
            db1.addBalance(new Balance(exp_date, "0", amt, bt));
            db.addContacts(new Contact(exp_date, amt, srcs, desc, "2", photo));
            Toast.makeText(getApplicationContext(), "লেনদেনটি সফলভাবে তালিকাভুক্ত করা হয়েছে", Toast.LENGTH_LONG).show();
            err.setVisibility(View.GONE);

            SharedPreferences sharedPreferences = getSharedPreferences("shared_pref", Context.MODE_PRIVATE);

            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Adding values to editor
            editor.putBoolean("mybalance", true);
            editor.putString("balance", exp_date + "," + it + "," + etotal + "," + +btotal);
            //editor.putString(Config.NAME_SHARED_PREF, email);

            //Saving values to editor
            editor.commit();

            date.setText("");
            date.setHintTextColor(Color.parseColor("#9e9e9e"));
            amount.setText("");
            amount.setHintTextColor(Color.parseColor("#9e9e9e"));
            src.setText("");
            src.setHintTextColor(Color.parseColor("#9e9e9e"));
            des.setText("");
            des.setHintTextColor(Color.parseColor("#9e9e9e"));
            Uri uri = Uri.parse("android.resource://com.dailyexpense/drawable/no_image");
            if (uri != null) {
                bitmap = decodeUri(uri, 100);
                memo_pic.setImageBitmap(bitmap);
                memo_pic.setVisibility(View.GONE);
                cross.setVisibility(View.GONE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }
}
