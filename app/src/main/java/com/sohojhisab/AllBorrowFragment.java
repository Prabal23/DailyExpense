package com.sohojhisab;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AllBorrowFragment extends Fragment {

    TextInputLayout InputName, InputPass;
    EditText name, pass;
    TextView sign_up;
    String text = "", res, brw_date = "", amts = "", srcss = "", phns = "";
    boolean loggedIn;
    int count;
    Typeface fontAwesomeFont;
    ProgressBar progressBar;
    TextView menu, calendar, tk, source, info, send, back, camera, gallery, paste, reset, no;
    TextView calc, date, err, rep;
    EditText amount, src, des;
    int mYear, mMonth, mDay;
    LinearLayout submit, refresh, take, choose, cross;
    private DatabaseHandler db2;
    private byte[] photo;
    private final int requestCode = 20;
    private Bitmap bitmap;
    private ImageView memo_pic;
    private Uri filePath;

    public AllBorrowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.add_borrow, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        db2 = new DatabaseHandler(getActivity());

        fontAwesomeFont = Typeface.createFromAsset(getActivity().getAssets(), "fa-solid-900.ttf");
        final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        Uri uris = null;
        uris = Uri.parse("android.resource://com.dailyexpense/drawable/no_image");
        if (uris != null) {
            bitmap = decodeUri(uris, 100);
        }

        calendar = (TextView) myview.findViewById(R.id.calendar);
        tk = (TextView) myview.findViewById(R.id.tk);
        source = (TextView) myview.findViewById(R.id.source);
        info = (TextView) myview.findViewById(R.id.info);
        send = (TextView) myview.findViewById(R.id.send);
        paste = (TextView) myview.findViewById(R.id.paste);
        reset = (TextView) myview.findViewById(R.id.reset);
        camera = (TextView) myview.findViewById(R.id.camera);
        gallery = (TextView) myview.findViewById(R.id.gallery);
        calendar.setTypeface(fontAwesomeFont);
        tk.setTypeface(fontAwesomeFont);
        source.setTypeface(fontAwesomeFont);
        info.setTypeface(fontAwesomeFont);
        send.setTypeface(fontAwesomeFont);
        paste.setTypeface(fontAwesomeFont);
        reset.setTypeface(fontAwesomeFont);
        camera.setTypeface(fontAwesomeFont);
        gallery.setTypeface(fontAwesomeFont);

        memo_pic = (ImageView) myview.findViewById(R.id.memo);
        amount = (EditText) myview.findViewById(R.id.amount);
        src = (EditText) myview.findViewById(R.id.src);
        des = (EditText) myview.findViewById(R.id.des);
        err = (TextView) myview.findViewById(R.id.err);

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);

                text = item.getText().toString();
                amount.setText(text);
            }
        });

        date = (TextView) myview.findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int d = monthOfYear + 1;
                                int day = dayOfMonth;
                                date.setText("ধারের তারিখ : " + year + "-" + d + "-" + day);
                                brw_date = year + "-" + d + "-" + day;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        take = (LinearLayout) myview.findViewById(R.id.take);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });

        choose = (LinearLayout) myview.findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });

        submit = (LinearLayout) myview.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRes();
            }
        });

        calc = (TextView) myview.findViewById(R.id.calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Calculator.class);
                startActivity(intent);
            }
        });

        refresh = (LinearLayout) myview.findViewById(R.id.refresh);
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

        cross = (LinearLayout) myview.findViewById(R.id.cross);
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

        rep = (TextView) myview.findViewById(R.id.rep);
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllBorrow.class);
                startActivity(intent);
            }
        });

        return myview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);
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
            return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void submitRes() {
        amts = amount.getText().toString();
        srcss = src.getText().toString();
        phns = des.getText().toString();

        if (brw_date.equals("")) {
            err.setText("ধারের তারিখ বাছাই করা হয়নি");
            err.setVisibility(View.VISIBLE);
            date.setHintTextColor(Color.RED);
        } else if (amts.equals("")) {
            err.setText("টাকার ঘর পূরণ হয়নি");
            err.setVisibility(View.VISIBLE);
            amount.setHintTextColor(Color.RED);
        } else if (srcss.equals("")) {
            err.setText("ধারকারীর নাম দেওয়া হয়নি");
            err.setVisibility(View.VISIBLE);
            src.setHintTextColor(Color.RED);
        } else if (phns.equals("")) {
            err.setText("ধারকারীর ফোন নাম্বার দেওয়া হয়নি");
            err.setVisibility(View.VISIBLE);
            des.setHintTextColor(Color.RED);
        } else {
            photo = profileImage(bitmap);
            /*Toast.makeText(getActivity(), brw_date, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), amts, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), srcss, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), phns, Toast.LENGTH_SHORT).show();*/
            db2.addContacts(new Contact(brw_date, amts, srcss, phns, "4", photo));
            Toast.makeText(getActivity(), "লেনদেনটি সফলভাবে তালিকাভুক্ত করা হয়েছে", Toast.LENGTH_LONG).show();
            err.setVisibility(View.GONE);

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
