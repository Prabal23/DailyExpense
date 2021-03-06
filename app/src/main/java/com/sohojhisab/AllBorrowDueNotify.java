package com.sohojhisab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllBorrowDueNotify extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHandler db;
    private byte[] photo;
    Bitmap bp;
    private Contact dataModel, dataModelUser, dataModel7, dataModel8;
    private dataAdapter6 data;
    ListView listView;
    Typeface fontAwesomeFont;
    TextView menu, back, inc, exp, balance, income, expense, total, bal, pdf;
    CircleImageView dp;
    private Contact dataModel2, dataModel1, dataModel3;
    int exp_sum = 0, inc_sum = 0, cc = 0;
    private DatabaseHandlerUser dbUser;
    String result = "//", fname = "", uname = "", password = "";
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    LinearLayout pdf1;
    private byte[] photos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_borrow_due_notify);

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
        inc = (TextView) findViewById(R.id.ok);
        exp = (TextView) findViewById(R.id.notok);
        balance = (TextView) findViewById(R.id.money);
        menu.setTypeface(fontAwesomeFont);
        inc.setTypeface(fontAwesomeFont);
        exp.setTypeface(fontAwesomeFont);
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

        income = (TextView) findViewById(R.id.inc);
        expense = (TextView) findViewById(R.id.exp);
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
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(AllBorrowDueNotify.this);
                MenuInflater inflater = new MenuInflater(AllBorrowDueNotify.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(AllBorrowDueNotify.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(AllBorrowDueNotify.this);
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

        bal = (TextView) findViewById(R.id.balance);
        bal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BalanceStatement.class);
                startActivity(intent);
            }
        });

        ShowIncome();
        ShowExpense();

        init();
        fn_permission();
    }

    private void ShowRecords() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getNotifyBorrowDue());
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel = contacts.get(i);
            String sts = dataModel.getStatus();
            if (sts.equals("33") || sts.equals("44")) {
                cc++;
            }
        }

        String tt_not = String.valueOf(cc);
        tt_not = tt_not.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        TextView tt = (TextView) findViewById(R.id.notif);
        tt.setText(tt_not);

        data = new dataAdapter6(this, contacts);
        listView.setAdapter(data);
        int tot = listView.getAdapter().getCount();
        String total = String.valueOf(tot);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataModel = contacts.get(position);
                int idd = dataModel.getID();
                String id1 = String.valueOf(idd);
                String date = dataModel.getDate();
                String amount = dataModel.getAmount();
                String src = dataModel.getSrc();
                String des = dataModel.getDescription();
                String status = dataModel.getStatus();
                String img = "";
                byte[] pic = dataModel.getImage();
                int l = pic.length;
                String imglength = String.valueOf(l);
                String imageString = Base64.encodeToString(pic, Base64.DEFAULT);
                //Toast.makeText(MyDonor.this, l+"", Toast.LENGTH_LONG).show();
                Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                Intent intent = new Intent(AllBorrowDueNotify.this, TransactionDetails.class);
                intent.putExtra("id", id1);
                intent.putExtra("date", date);
                intent.putExtra("amount", amount);
                intent.putExtra("src", src);
                intent.putExtra("des", des);
                intent.putExtra("status", status);
                intent.putExtra("page", "");
                intent.putExtra("img", bitmap);
                startActivity(intent);
            }
        });
    }

    private void ShowIncome() {
        final ArrayList<Contact> contacts = new ArrayList<>(db.getAllBorrow());
        int c = contacts.size();
        for (int i = 0; i < c; i++) {
            dataModel8 = contacts.get(i);
            String amount = dataModel8.getAmount();
            int tk = Integer.parseInt(amount);
            inc_sum += tk;
        }
        String inc_tk = String.valueOf(inc_sum);
        inc_tk = inc_tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        income.setText("" + inc_tk);
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
        expense.setText("" + exp_tk);
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
                        bitmap = loadBitmapFromView(listView, listView.getWidth(), listView.getHeight());
                        createPdf();

                        String targetPdf = "/sdcard/মোট ব্যালেন্স.pdf";
                        Snackbar snackbar = Snackbar
                                .make(listView, "Saved to - " + targetPdf, Snackbar.LENGTH_LONG);
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
        String targetPdf = "/sdcard/মোট ব্যালেন্স.pdf";
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AllBorrowDueNotify.this, Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AllBorrowDueNotify.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AllBorrowDueNotify.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(AllBorrowDueNotify.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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

    public class dataAdapter6 extends ArrayAdapter<Contact> {

        Context context;
        ArrayList<Contact> mcontact;
        Typeface fontAwesomeFont;


        public dataAdapter6(Context context, ArrayList<Contact> contact) {
            super(context, R.layout.notify_due_list_item, contact);
            this.context = context;
            this.mcontact = contact;
        }

        public class Holder {
            TextView date, info, edit, del, d, t, des, phn, p, notify;
            ImageView pic;
            LinearLayout savedpage;
            View v;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            fontAwesomeFont = Typeface.createFromAsset(getContext().getAssets(), "fa-solid-900.ttf");

            Contact data = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view

            Holder viewHolder; // view lookup cache stored in tag

            if (convertView == null) {

                viewHolder = new Holder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.notify_due_list_item, parent, false);

                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                viewHolder.info = (TextView) convertView.findViewById(R.id.info);
                viewHolder.edit = (TextView) convertView.findViewById(R.id.edit);
                viewHolder.del = (TextView) convertView.findViewById(R.id.del);
                viewHolder.phn = (TextView) convertView.findViewById(R.id.phn);
                viewHolder.notify = (TextView) convertView.findViewById(R.id.notify);
                viewHolder.pic = (ImageView) convertView.findViewById(R.id.memo);
                viewHolder.date.setTypeface(fontAwesomeFont);
                viewHolder.info.setTypeface(fontAwesomeFont);
                viewHolder.edit.setTypeface(fontAwesomeFont);
                viewHolder.del.setTypeface(fontAwesomeFont);
                viewHolder.phn.setTypeface(fontAwesomeFont);
                viewHolder.notify.setTypeface(fontAwesomeFont);
                viewHolder.v = (View) convertView.findViewById(R.id.v);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Holder) convertView.getTag();
            }
            final Integer uids = data.getID();
            final String date = data.getDate();
            viewHolder.d = (TextView) convertView.findViewById(R.id.d);
            viewHolder.d.setText(Html.fromHtml(date));

            final String tk = data.getAmount();
            viewHolder.t = (TextView) convertView.findViewById(R.id.t);
            viewHolder.t.setText(Html.fromHtml(tk));

            final String desc = data.getSrc();
            viewHolder.des = (TextView) convertView.findViewById(R.id.des);
            viewHolder.des.setText(Html.fromHtml(desc));

            final String phone_no = data.getDescription();
            viewHolder.p = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.p.setText(Html.fromHtml(phone_no));

            final String stts = data.getStatus();
            if (stts.equals("3") || stts.equals("3")) {
                viewHolder.v.setBackgroundColor(Color.parseColor("#12d757"));
            }
            if (stts.equals("4") || stts.equals("44")) {
                viewHolder.v.setBackgroundColor(Color.parseColor("#f11b30"));
            }
            if (stts.equals("44") || stts.equals("33")) {
                viewHolder.notify.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.notify.setBackgroundResource(R.drawable.send_back);
                viewHolder.notify.setText(getString(R.string.bell));
            }

            bitmap = convertToBitmap(data.getImage());

            viewHolder.notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (stts.equals("3")) {
                        photos = profileImage(bitmap);
                        db.updateContact(new Contact(date, tk, desc, phone_no, "33", photos), uids);
                        Toast.makeText(getApplicationContext(), "নোটিফিকেশনে যোগ করা হয়েছে", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getBaseContext(), AllBorrowDueNotify.class);
                        startActivity(intent);
                    }
                    if (stts.equals("4")) {
                        photos = profileImage(bitmap);
                        db.updateContact(new Contact(date, tk, desc, phone_no, "44", photos), uids);
                        Toast.makeText(getApplicationContext(), "নোটিফিকেশনে যোগ করা হয়েছে", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getBaseContext(), AllBorrowDueNotify.class);
                        startActivity(intent);
                    }
                    if (stts.equals("33")) {
                        photos = profileImage(bitmap);
                        db.updateContact(new Contact(date, tk, desc, phone_no, "3", photos), uids);
                        Toast.makeText(getApplicationContext(), "নোটিফিকেশন থেকে বাদ দেয়া হয়েছে", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getBaseContext(), AllBorrowDueNotify.class);
                        startActivity(intent);
                    }
                    if (stts.equals("44")) {
                        photos = profileImage(bitmap);
                        db.updateContact(new Contact(date, tk, desc, phone_no, "4", photos), uids);
                        Toast.makeText(getApplicationContext(), "নোটিফিকেশন থেকে বাদ দেয়া হয়েছে", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getBaseContext(), AllBorrowDueNotify.class);
                        startActivity(intent);
                    }
                }
            });
            // Return the completed view to render on screen
            return convertView;
        }

        private Bitmap convertToBitmap(byte[] b) {

            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
        private byte[] profileImage(Bitmap b) {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 0, bos);
            return bos.toByteArray();
        }
    }
}
