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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionDetails extends AppCompatActivity {
    Typeface fontAwesomeFont;
    TextView menu, calendar, tk, source, info, send, back, camera, gallery, paste, reset, no;
    TextView calc, date, err, title, det;
    CircleImageView dp;
    private ImageView memo_pic;
    LinearLayout take, choose, submit, refresh, cross;
    int mYear, mMonth, mDay;
    String all_date = "", amt = "", srcs = "", desc = "";
    String id = "", d = "", a = "", s = "", de = "", p = "", st = "";
    private final int requestCode = 20;
    private Bitmap bitmap, bitmap1;
    private Uri filePath, filePath1;
    EditText amount, src, des;
    private DatabaseHandler db;
    private DatabaseHandlerUser dbUser;
    private byte[] photo;
    int uid;
    Contact dataModelUser;
    String results = "//", fname = "", uname = "", password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_det);

        db = new DatabaseHandler(this);
        dbUser = new DatabaseHandlerUser(this);

        SharedPreferences sharedPreferences1 = getSharedPreferences("shared_login", Context.MODE_PRIVATE);
        if (sharedPreferences1.contains("login")) {
            results = (sharedPreferences1).getString("login", "");
        } else {
            results = "//";
        }

        String[] result1 = results.split("/");
        fname = result1[0];
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        uname = result1[1];
        //Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        password = result1[2];

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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

        Uri uris = null;
        uris = Uri.parse("android.resource://com.sohojhisab/drawable/no_image");
        if (uris != null) {
            bitmap = decodeUri(uris, 100);
        }

        memo_pic = (ImageView) findViewById(R.id.memo);
        amount = (EditText) findViewById(R.id.amount);
        src = (EditText) findViewById(R.id.src);
        des = (EditText) findViewById(R.id.des);
        err = (TextView) findViewById(R.id.err);

        id = getIntent().getStringExtra("id");
        uid = Integer.parseInt(id);
        all_date = getIntent().getStringExtra("date");
        amt = getIntent().getStringExtra("amount");
        String amt1 = getIntent().getStringExtra("amount");
        amt1 = amt1.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        srcs = getIntent().getStringExtra("src");
        desc = getIntent().getStringExtra("des");
        st = getIntent().getStringExtra("status");
        p = getIntent().getStringExtra("page");
        bitmap = (Bitmap) getIntent().getParcelableExtra("img");
        memo_pic.setImageBitmap(bitmap);

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);

                String text = item.getText().toString();
                amount.setText(text);
            }
        });

        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        det = (TextView) findViewById(R.id.det);
        if (st.equals("1")) {
            all_date = all_date.replace("1", "১").replace("2", "২").replace("3", "৩")
                    .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                    .replace("8", "৮").replace("9", "৯").replace("0", "০");
            title.setText("আয়ের বিবরণ");
            date.setText("আয়ের তারিখ : " + all_date);
        }
        if (st.equals("2")) {
            all_date = all_date.replace("1", "১").replace("2", "২").replace("3", "৩")
                    .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                    .replace("8", "৮").replace("9", "৯").replace("0", "০");
            title.setText("ব্যয়ের বিবরণ");
            date.setText("ব্যয়ের তারিখ : " + all_date);
        }
        amount.setText(amt1);
        src.setText(srcs);
        des.setText(desc);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionDetails.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int d = monthOfYear + 1;
                                int day = dayOfMonth;
                                if (st.equals("1")) {
                                    String dd = year + "-" + d + "-" + day;
                                    dd = dd.replace("1", "১").replace("2", "২").replace("3", "৩")
                                            .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                                            .replace("8", "৮").replace("9", "৯").replace("0", "০");
                                    date.setText("আয়ের তারিখ : " + dd);
                                    all_date = year + "-" + d + "-" + day;
                                }
                                if (st.equals("2")) {
                                    String dd = year + "-" + d + "-" + day;
                                    dd = dd.replace("1", "১").replace("2", "২").replace("3", "৩")
                                            .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                                            .replace("8", "৮").replace("9", "৯").replace("0", "০");
                                    date.setText("ব্যয়ের তারিখ : " + dd);
                                    all_date = year + "-" + d + "-" + day;
                                }
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
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(TransactionDetails.this);
                MenuInflater inflater = new MenuInflater(TransactionDetails.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(TransactionDetails.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(TransactionDetails.this);
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
                DeleteJob alert1 = new DeleteJob();
                alert1.showDialog(TransactionDetails.this, id);
            }
        });

        cross = (LinearLayout) findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("android.resource://com.sohojhisab/drawable/no_image");
                if (uri != null) {
                    bitmap = decodeUri(uri, 100);
                    memo_pic.setImageBitmap(bitmap);
                    memo_pic.setVisibility(View.GONE);
                    cross.setVisibility(View.GONE);
                }
            }
        });
    }

    public class DeleteJob {

        public void showDialog(AppCompatActivity activity, final String job_apply_id) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.delete_trans_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button yes = (Button) dialog.findViewById(R.id.yes);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int i = Integer.parseInt(id);
                    db.deleteContact(i);
                    if (p.equals("1")) {
                        Intent intent = new Intent(getBaseContext(), AllIncome.class);
                        startActivity(intent);
                    }
                    if (p.equals("2")) {
                        Intent intent = new Intent(getBaseContext(), AllExpense.class);
                        startActivity(intent);
                        //finish();
                    }
                    if (p.equals("")) {
                        Intent intent = new Intent(getBaseContext(), AllBalance.class);
                        startActivity(intent);
                    }
                    finish();
                    dialog.dismiss();
                }
            });

            Button no = (Button) dialog.findViewById(R.id.no);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
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

    public void submitRes() {
        amt = amount.getText().toString();
        srcs = src.getText().toString();
        desc = des.getText().toString();

        if (all_date.equals("")) {
            if (st.equals("1")) {
                err.setText("আয়ের তারিখ বাছাই করা হয়নি");
            }
            if (st.equals("2")) {
                err.setText("ব্যয়ের তারিখ বাছাই করা হয়নি");
            }
            err.setVisibility(View.VISIBLE);
            date.setHintTextColor(Color.RED);
        } else if (amt.equals("")) {
            if (st.equals("1")) {
                err.setText("টাকার ঘর পূরণ হয়নি");
            }
            if (st.equals("2")) {
                err.setText("টাকার ঘর পূরণ হয়নি");
            }
            err.setVisibility(View.VISIBLE);
            amount.setHintTextColor(Color.RED);
        } else if (srcs.equals("")) {
            if (st.equals("1")) {
                err.setText("আয়ের উৎস দেওয়া হয়নি");
            }
            if (st.equals("2")) {
                err.setText("ব্যয়ের খাত দেওয়া হয়নি");
            }
            err.setVisibility(View.VISIBLE);
            src.setHintTextColor(Color.RED);
        } else {
            photo = profileImage(bitmap);
            db.updateContact(new Contact(all_date, amt, srcs, desc, st, photo), uid);
            Toast.makeText(getApplicationContext(), "সফলভাবে সম্পাদন করা হয়েছে", Toast.LENGTH_LONG).show();
            err.setVisibility(View.GONE);
            if (p.equals("1")) {
                Intent intent = new Intent(getBaseContext(), AllIncome.class);
                startActivity(intent);
            }
            if (p.equals("2")) {
                Intent intent = new Intent(getBaseContext(), AllExpense.class);
                startActivity(intent);
            }
            if (p.equals("")) {
                Intent intent = new Intent(getBaseContext(), AllBalance.class);
                startActivity(intent);
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
