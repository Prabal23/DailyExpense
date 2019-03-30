package com.sohojhisab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllDue extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHandler db;
    private byte[] photo;
    Bitmap bp;
    private Contact dataModel7, dataModelUser;
    private dataAdapter5 data4;
    ListView listView;
    Typeface fontAwesomeFont;
    TextView menu, back, inc, exp, balance, income, expense, total, pdf;
    CircleImageView dp;
    private Contact dataModel2, dataModel1;
    int exp_sum = 0, inc_sum = 0;
    private DatabaseHandlerUser dbUser;
    String result = "//", fname = "", uname = "", password = "";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    LinearLayout pdf1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_due);

        db = new DatabaseHandler(this);
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
        balance = (TextView) findViewById(R.id.money);
        menu.setTypeface(fontAwesomeFont);
        balance.setTypeface(fontAwesomeFont);
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

        total = (TextView) findViewById(R.id.tk);

        listView = (ListView) findViewById(R.id.list);
        ShowRecords();

        dp = (CircleImageView) findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                /*PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();*/
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(AllDue.this);
                MenuInflater inflater = new MenuInflater(AllDue.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(AllDue.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(AllDue.this);
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

        ShowExpense();

        init();
        try {
            createPdfWrapper();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //fn_permission();
    }

    private void ShowRecords() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllDue());
        data4 = new dataAdapter5(this, contacts);
        listView.setAdapter(data4);
        int tot = listView.getAdapter().getCount();
        String total = String.valueOf(tot);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataModel7 = contacts.get(position);
                int idd = dataModel7.getID();
                String id1 = String.valueOf(idd);
                String date = dataModel7.getDate();
                String amount = dataModel7.getAmount();
                String src = dataModel7.getSrc();
                String des = dataModel7.getDescription();
                String status = dataModel7.getStatus();
                String img = "";
                byte[] pic = dataModel7.getImage();
                int l = pic.length;
                String imglength = String.valueOf(l);
                String imageString = Base64.encodeToString(pic, Base64.DEFAULT);
                //Toast.makeText(MyDonor.this, l+"", Toast.LENGTH_LONG).show();
                Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                Intent intent = new Intent(AllDue.this, BorrowDueDetails.class);
                intent.putExtra("id", id1);
                intent.putExtra("date", date);
                intent.putExtra("amount", amount);
                intent.putExtra("src", src);
                intent.putExtra("des", des);
                intent.putExtra("status", status);
                intent.putExtra("page", "1");
                intent.putExtra("img", bitmap);
                startActivity(intent);
            }
        });
    }

    private void ShowExpense() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllDue());
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel7 = contacts.get(i);
            String amount = dataModel7.getAmount();
            int tk = Integer.parseInt(amount);
            exp_sum += tk;
        }
        String exp_tk = String.valueOf(exp_sum);
        exp_tk = exp_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        total.setText("" + exp_tk);
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
                    File file = saveBitMap(AllDue.this, listView);
                    if (file != null) {
                        //Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(this, "Cannot saved", Toast.LENGTH_SHORT).show();
                    }
                    if (boolean_permission) {
                        /*bitmap = loadBitmapFromView(listView, listView.getWidth(), listView.getHeight());
                        createPdf();

                        String targetPdf = "/sdcard/দেনাসমূহ.pdf";
                        Snackbar snackbar = Snackbar
                                .make(listView, "Saved to - " + targetPdf, Snackbar.LENGTH_LONG);
                                *//*.setAction("View", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (boolean_save) {
                                            Intent intent = new Intent(getBaseContext(), CVPDFViewActivity.class);
                                            intent.putExtra("name", fullname);
                                            startActivity(intent);
                                        }
                                    }
                                });*//*

                        snackbar.setActionTextColor(Color.RED);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();*/

                    } else {

                    }

                    //createPdf();
                    break;
                }
        }
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SHImg");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("TAG", "ফোল্ডার তৈরি করা যায়নি");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + "allDue.jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "ছবি সংরক্ষণে সমস্যা হয়েছে");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        convertPDF();
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "সমস্যা হয়েছে");
        }
    }

    public void convertPDF() {
        String targetPdf = "/দেনাসমূহ.pdf";
        //Toast.makeText(this, targetPdf, Toast.LENGTH_SHORT).show();
        //Document doc = new Document();

        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
        //File exportDir = new File(Environment.getExternalStorageDirectory(), targetPdf);
        try {
            /*PdfWriter.getInstance(doc, new FileOutputStream(directoryPath + "/" + targetPdf));
            doc.open();*/

            String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/SHImg/allDue.jpg";
            //Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
            Image image = Image.getInstance(filename);
            /*float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);*/
            //image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            /*float documentWidth = doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin();
            float documentHeight = doc.getPageSize().getHeight() - doc.topMargin() - doc.bottomMargin();
            //image.scaleAbsolute(documentWidth, documentHeight);
            image.scaleToFit(documentWidth, documentHeight);
            doc.add(image);
            doc.close();*/
            float width = image.getScaledWidth();
            float height = image.getScaledHeight();
            Rectangle page = new Rectangle(width, height);

            Document document = new Document(page);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/" + targetPdf));
            document.open();
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.addImage(image, width, 0, 0, height, 0, 0);
            document.newPage();
            document.close();

            //Toast.makeText(this, "PDF Saved", Toast.LENGTH_SHORT).show();
            String targetPdf1 = "storage/দেনাসমূহ.pdf";
            final Snackbar snackbar = Snackbar.make(listView, targetPdf1 + " এ সংরক্ষণ করা হয়েছে", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("ঠিক আছে", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("অনুগ্রহ করে স্টোরেজ পার্মিশন প্রদাণ করুন", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        }
                    });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            /*File file = saveBitMap(CV.this, pdf_linear);
            if (file != null) {
                progres.setVisibility(View.GONE);
                Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
            } else {
                progres.setVisibility(View.GONE);
                Toast.makeText(this, "Cannot saved", Toast.LENGTH_SHORT).show();
            }*/
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "সটোরেজ পার্মিশন দেওয়ার ক্ষেত্রে সমস্যা দেখা গিয়েছে", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("ঠিক আছে", okListener)
                .setNegativeButton("বাতিল", null)
                .create()
                .show();
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
        String targetPdf = "/sdcard/দেনাসমূহ.pdf";
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AllDue.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AllDue.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AllDue.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AllDue.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }

/*
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
*/
}
