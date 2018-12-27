package com.sohojhisab;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    int count = 0;
    TextView password, signin, err;
    Typeface fontAwesomeFont;
    TextView name, user, email, phone, pass, conpass, send, gallery;
    private Bitmap bitmap;
    LinearLayout choose;
    CircleImageView dp;
    private Uri filePath;
    private byte[] photo;
    private DatabaseHandlerUser db, db1;
    private Contact dataModel;
    String Fullname = "";
    EditText fullname, username, mail, phn, passwrd, passcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        db = new DatabaseHandlerUser(this);
        db1 = new DatabaseHandlerUser(this);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fa-solid-900.ttf");

        name = (TextView) findViewById(R.id.name);
        user = (TextView) findViewById(R.id.user);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        pass = (TextView) findViewById(R.id.pass);
        conpass = (TextView) findViewById(R.id.conpass);
        send = (TextView) findViewById(R.id.send);
        gallery = (TextView) findViewById(R.id.gallery);
        name.setTypeface(fontAwesomeFont);
        user.setTypeface(fontAwesomeFont);
        email.setTypeface(fontAwesomeFont);
        phone.setTypeface(fontAwesomeFont);
        pass.setTypeface(fontAwesomeFont);
        conpass.setTypeface(fontAwesomeFont);
        send.setTypeface(fontAwesomeFont);
        gallery.setTypeface(fontAwesomeFont);

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

        signin = (TextView) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Login.class);
                startActivity(intent);
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
        uris = Uri.parse("android.resource://com.dailyexpense/drawable/member_icon");
        if (uris != null) {
            bitmap = decodeUri(uris, 100);
        }

        dp = (CircleImageView) findViewById(R.id.dp);

        err = (TextView) findViewById(R.id.err);

        LinearLayout submit = (LinearLayout) findViewById(R.id.signup);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRes();
            }
        });

        showUsers();
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
                        dp.setImageBitmap(bitmap);
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
            db.addUser(new Contact(fn, un, em, num, pass, photo));
            Toast.makeText(getApplicationContext(), "সফলভাবে রেজিস্ট্রেশন সম্পন্ন হয়েছে", Toast.LENGTH_LONG).show();
            err.setVisibility(View.GONE);
            Intent intent = new Intent(getBaseContext(), Login.class);
            startActivity(intent);

            fullname.setText("");
            fullname.setHintTextColor(Color.parseColor("#9e9e9e"));
            username.setText("");
            username.setHintTextColor(Color.parseColor("#9e9e9e"));
            mail.setText("");
            mail.setHintTextColor(Color.parseColor("#9e9e9e"));
            phn.setText("");
            phn.setHintTextColor(Color.parseColor("#9e9e9e"));
            passwrd.setText("");
            passwrd.setHintTextColor(Color.parseColor("#9e9e9e"));
            passcon.setText("");
            passcon.setHintTextColor(Color.parseColor("#9e9e9e"));
            Uri uri = Uri.parse("android.resource://com.dailyexpense/drawable/member_icon");
            if (uri != null) {
                bitmap = decodeUri(uri, 100);
                dp.setImageBitmap(bitmap);
            }
        }
    }

    private void showUsers() {
        final ArrayList<Contact> contacts = new ArrayList<>(db1.getAllUser());
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel = contacts.get(i);
            Fullname = dataModel.getDate();
            //Toast.makeText(this, Fullname, Toast.LENGTH_SHORT).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }
}
