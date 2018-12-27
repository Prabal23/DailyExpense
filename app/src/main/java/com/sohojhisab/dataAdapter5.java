package com.sohojhisab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Visit website http://www.whats-online.info
 **/

public class dataAdapter5 extends ArrayAdapter<Contact> {

    Context context;
    ArrayList<Contact> mcontact;
    Typeface fontAwesomeFont;


    public dataAdapter5(Context context, ArrayList<Contact> contact) {
        super(context, R.layout.borrow_list_item, contact);
        this.context = context;
        this.mcontact = contact;
    }

    public class Holder {
        TextView date, info, edit, del, d, t, des, phn, p;
        ImageView pic;
        LinearLayout savedpage;
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
            convertView = inflater.inflate(R.layout.borrow_list_item, parent, false);

            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.info = (TextView) convertView.findViewById(R.id.info);
            viewHolder.edit = (TextView) convertView.findViewById(R.id.edit);
            viewHolder.del = (TextView) convertView.findViewById(R.id.del);
            viewHolder.phn = (TextView) convertView.findViewById(R.id.phn);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.memo);
            viewHolder.date.setTypeface(fontAwesomeFont);
            viewHolder.info.setTypeface(fontAwesomeFont);
            viewHolder.edit.setTypeface(fontAwesomeFont);
            viewHolder.del.setTypeface(fontAwesomeFont);
            viewHolder.phn.setTypeface(fontAwesomeFont);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }

        String date = data.getDate();
        date = date.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        viewHolder.d = (TextView) convertView.findViewById(R.id.d);
        viewHolder.d.setText(Html.fromHtml(date));

        String tk = data.getAmount();
        tk = tk.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        viewHolder.t = (TextView) convertView.findViewById(R.id.t);
        viewHolder.t.setText(Html.fromHtml(tk));

        String desc = data.getSrc();
        viewHolder.des = (TextView) convertView.findViewById(R.id.des);
        viewHolder.des.setText(Html.fromHtml(desc));

        String phone_no = data.getDescription();
        viewHolder.p = (TextView) convertView.findViewById(R.id.phone);
        viewHolder.p.setText(Html.fromHtml(phone_no));

        /*viewHolder.savedpage = (LinearLayout) convertView.findViewById(R.id.r6);
        viewHolder.savedpage.setVisibility(View.VISIBLE);
        viewHolder.savedpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        // Return the completed view to render on screen
        return convertView;
    }
    //get bitmap image from byte array

    private Bitmap convertToBitmap(byte[] b) {

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}

