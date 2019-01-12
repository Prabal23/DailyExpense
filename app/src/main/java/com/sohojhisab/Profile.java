package com.sohojhisab;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    int count = 0;
    TextView password, signin, err, menu, back;
    Typeface fontAwesomeFont;
    TextView name, user, email, phone, pass, conpass, send, gallery;
    private Bitmap bitmap;
    LinearLayout choose;
    CircleImageView dp, profile;
    private Uri filePath;
    private byte[] photo;
    private DatabaseHandlerUser db, db1;
    private DatabaseHandlerUser dbUser;
    private Contact dataModel, dataModelUser;
    String Fullname = "", Username="", Email="", Phone="", Pass="", Conpass="", User="";
    int Uid;
    EditText fullname, username, mail, phn, passwrd, passcon;
    String result = "//", fname = "", uname = "", pswrd = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        db = new DatabaseHandlerUser(this);
        db1 = new DatabaseHandlerUser(this);
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
        pswrd = result1[2];

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        menu = (TextView) findViewById(R.id.menu);
        back = (TextView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        user = (TextView) findViewById(R.id.user);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        pass = (TextView) findViewById(R.id.pass);
        conpass = (TextView) findViewById(R.id.conpass);
        send = (TextView) findViewById(R.id.send);
        gallery = (TextView) findViewById(R.id.gallery);
        menu.setTypeface(fontAwesomeFont);
        name.setTypeface(fontAwesomeFont);
        user.setTypeface(fontAwesomeFont);
        email.setTypeface(fontAwesomeFont);
        phone.setTypeface(fontAwesomeFont);
        pass.setTypeface(fontAwesomeFont);
        conpass.setTypeface(fontAwesomeFont);
        send.setTypeface(fontAwesomeFont);
        gallery.setTypeface(fontAwesomeFont);
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

        fullname = (EditText) findViewById(R.id.fullname);
        username = (EditText) findViewById(R.id.username);
        mail = (EditText) findViewById(R.id.mail);
        phn = (EditText) findViewById(R.id.phn);
        passwrd = (EditText) findViewById(R.id.password);
        passcon = (EditText) findViewById(R.id.passcon);
        final TextView viewpass = (TextView) findViewById(R.id.viewpass);
        viewpass.setTypeface(fontAwesomeFont);
        viewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count % 2 == 0) {
                    passwrd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    viewpass.setText(R.string.eyenot);
                    //Toast.makeText(Login.this, "Even", Toast.LENGTH_SHORT).show();
                } else {
                    passwrd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    viewpass.setText(R.string.eye);
                    //Toast.makeText(Login.this, "Odd", Toast.LENGTH_SHORT).show();
                }
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
        uris = Uri.parse("android.resource://com.sohojhisab/drawable/member_icon");
        if (uris != null) {
            bitmap = decodeUri(uris, 100);
        }

        dp = (CircleImageView) findViewById(R.id.dp);
        profile = (CircleImageView) findViewById(R.id.profile);

        dp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                /*PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();*/
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(Profile.this);
                MenuInflater inflater = new MenuInflater(Profile.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(Profile.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(Profile.this);
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

        err = (TextView) findViewById(R.id.err);

        LinearLayout submit = (LinearLayout) findViewById(R.id.signup);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRes();
            }
        });

        final ArrayList<Contact> contacts = new ArrayList<>(dbUser.getUser(uname, pswrd));
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModelUser = contacts.get(i);
            byte[] pic = dataModelUser.getImage();
            bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            dp.setImageBitmap(bitmap);
            profile.setImageBitmap(bitmap);

            Uid = dataModelUser.getID();
            User=String.valueOf(Uid);
            Fullname = dataModelUser.getDate();
            fullname.setText(Fullname);
            Username = dataModelUser.getAmount();
            username.setText(Username);
            Email = dataModelUser.getSrc();
            mail.setText(Email);
            Phone = dataModelUser.getDescription();
            phn.setText(Phone);
            Pass = dataModelUser.getStatus();
            passwrd.setText(Pass);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    if (filePath != null) {
                        bitmap = decodeUri(filePath, 100);
                        //originalImage = getResizedBitmap(bitmap, 134, 304);
                        profile.setImageBitmap(bitmap);
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
        String fn = fullname.getText().toString();
        String un = username.getText().toString();
        String em = mail.getText().toString();
        String num = phn.getText().toString();
        String pass = passwrd.getText().toString();
        String conpass = passcon.getText().toString();

        if (fn.equals("")) {
            err.setText("পূর্ণনামের ঘর পূরণ করা হয়নি");
            err.setVisibility(View.VISIBLE);
            fullname.setHintTextColor(Color.RED);
        } else if (un.equals("")) {
            err.setText("ইউজারনেমের ঘর পূরণ করা হয়নি");
            err.setVisibility(View.VISIBLE);
            username.setHintTextColor(Color.RED);
        } else if (em.equals("")) {
            err.setText("ইমেইলের ঘর পূরণ করা হয়নি");
            err.setVisibility(View.VISIBLE);
            mail.setHintTextColor(Color.RED);
        } else if (num.equals("")) {
            err.setText("ফোন নাম্বারের ঘর পূরণ করা হয়নি");
            err.setVisibility(View.VISIBLE);
            phn.setHintTextColor(Color.RED);
        } else if (pass.equals("")) {
            err.setText("পাসওয়ার্ডের ঘর পূরণ করা হয়নি");
            err.setVisibility(View.VISIBLE);
            passwrd.setHintTextColor(Color.RED);
        } else if (conpass.equals("")) {
            err.setText("কনফার্ম পাসওয়ার্ডের ঘর পূরণ করা হয়নি");
            err.setVisibility(View.VISIBLE);
            passcon.setHintTextColor(Color.RED);
        } else if (!pass.equals(conpass)) {
            err.setText("পাসওয়ার্ডের সাথে মিলেনি");
            err.setVisibility(View.VISIBLE);
            passcon.setHintTextColor(Color.RED);
        } else {
            photo = profileImage(bitmap);
            db.updateUser(new Contact(fn, un, em, num, pass, photo), Uid);
            Toast.makeText(getApplicationContext(), "সফলভাবে সম্পাদন করা হয়েছে", Toast.LENGTH_LONG).show();
            err.setVisibility(View.GONE);
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }
}