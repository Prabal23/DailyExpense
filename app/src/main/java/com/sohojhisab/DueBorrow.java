package com.sohojhisab;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DueBorrow extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView menu, back;
    CircleImageView dp;
    Typeface fontAwesomeFont;
    private DatabaseHandlerUser dbUser;
    Contact dataModelUser;
    String results = "//", fname = "", uname = "", password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.due_borrow);

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

        menu = (TextView) findViewById(R.id.menu);
        back = (TextView) findViewById(R.id.back);
        menu.setTypeface(fontAwesomeFont);
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        dp = (CircleImageView) findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                /*PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_main);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.show();*/
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(DueBorrow.this);
                MenuInflater inflater = new MenuInflater(DueBorrow.this);
                inflater.inflate(R.menu.menu_main, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(DueBorrow.this, menuBuilder, view);
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
                                final Dialog dialog = new Dialog(DueBorrow.this);
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllDueFragment(), "দেনা");
        adapter.addFragment(new AllBorrowFragment(), "পাওনা");
        //adapter.addFragment(new AdminFragment(), "Admin");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
