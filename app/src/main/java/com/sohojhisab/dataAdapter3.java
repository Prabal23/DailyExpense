package com.sohojhisab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Visit website http://www.whats-online.info
 **/

public class dataAdapter3 extends ArrayAdapter<Balance> {

    Context context;
    ArrayList<Balance> mcontact;
    Typeface fontAwesomeFont;

    public dataAdapter3(Context context, ArrayList<Balance> contact) {
        super(context, R.layout.balance_statement_item, contact);
        this.context = context;
        this.mcontact = contact;
    }

    public class Holder {
        TextView date, inc, exp, bal;
        ImageView pic;
        LinearLayout savedpage;
        View view;
        String tk = "0", in = "0", ex = "0", ba = "0";
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        fontAwesomeFont = Typeface.createFromAsset(getContext().getAssets(), "fa-solid-900.ttf");

        Balance data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.balance_statement_item, parent, false);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }

        String date1 = data.getDate();
        date1 = date1.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        viewHolder.date = (TextView) convertView.findViewById(R.id.date);
        viewHolder.date.setText(Html.fromHtml(date1));
        String inc1 = data.getIncome();
        inc1 = inc1.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        viewHolder.inc = (TextView) convertView.findViewById(R.id.inc);
        viewHolder.inc.setText(Html.fromHtml(inc1));
        String exp1 = data.getExpense();
        exp1 = exp1.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        viewHolder.exp = (TextView) convertView.findViewById(R.id.exp);
        viewHolder.exp.setText(Html.fromHtml(exp1));
        String bal1 = data.getBalance();
        bal1 = bal1.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
        viewHolder.bal = (TextView) convertView.findViewById(R.id.bal);
        viewHolder.bal.setText(Html.fromHtml(bal1));
        viewHolder.view = (View) convertView.findViewById(R.id.v);

        //Toast.makeText(context, inc1, Toast.LENGTH_SHORT).show();
        if (inc1.equals("০")) {
            viewHolder.view.setBackgroundColor(Color.parseColor("#f11b30"));
        } else {
            viewHolder.view.setBackgroundColor(Color.parseColor("#12d757"));
        }
            /*String tk;
            tk = data.getAmount();
            if (tk.equals("") || tk.equals("null") || tk.equals("0")) {
                tk = "0";
            }
            if (stts.equals("1")) {
                viewHolder.inc = (TextView) convertView.findViewById(R.id.inc);
                viewHolder.inc.setText(Html.fromHtml(tk));
                viewHolder.exp = (TextView) convertView.findViewById(R.id.exp);
                viewHolder.exp.setText(Html.fromHtml("0"));
                if (position == 0) {
                    Toast.makeText(context, "s1", Toast.LENGTH_SHORT).show();
                    String i = viewHolder.inc.getText().toString();
                    String e = viewHolder.exp.getText().toString();
                    int ii = Integer.parseInt(i);
                    int ei = Integer.parseInt(e);
                    int total = ii - ei;
                    viewHolder.bal = (TextView) convertView.findViewById(R.id.bal);
                    viewHolder.bal.setText(Html.fromHtml("" + total));
                } else {
                    Toast.makeText(context, "s2", Toast.LENGTH_SHORT).show();
                    String t = total.getText().toString();
                    //Toast.makeText(BalanceStatement.this, t, Toast.LENGTH_SHORT).show();
                    String i = viewHolder.inc.getText().toString();
                    //Toast.makeText(BalanceStatement.this, i, Toast.LENGTH_SHORT).show();
                    String e = viewHolder.exp.getText().toString();
                    //Toast.makeText(BalanceStatement.this, e, Toast.LENGTH_SHORT).show();
                    int ti = Integer.parseInt(t);
                    int ii = Integer.parseInt(i);
                    int ei = Integer.parseInt(e);
                    int in = ti + ii;
                    int ex = ti + ei;
                    int total = in - ei;
                    viewHolder.bal = (TextView) convertView.findViewById(R.id.bal);
                    viewHolder.bal.setText(Html.fromHtml("" + total));
                }
            }
            if (stts.equals("2")) {
                Toast.makeText(context, "s3", Toast.LENGTH_SHORT).show();
                viewHolder.inc = (TextView) convertView.findViewById(R.id.inc);
                viewHolder.inc.setText(Html.fromHtml("0"));
                viewHolder.exp = (TextView) convertView.findViewById(R.id.exp);
                viewHolder.exp.setText(Html.fromHtml(tk));
                String t = total.getText().toString();
                String i = viewHolder.inc.getText().toString();
                String e = viewHolder.exp.getText().toString();
                int ti = Integer.parseInt(t);
                int ii = Integer.parseInt(i);
                int ei = Integer.parseInt(e);
                int in = ti + ii;
                int ex = ti + ei;
                int total = in - ei;
                viewHolder.bal = (TextView) convertView.findViewById(R.id.bal);
                viewHolder.bal.setText(Html.fromHtml("" + total));
            }

            if (tot == 1) {
                String i = viewHolder.inc.getText().toString();
                String e = viewHolder.exp.getText().toString();
                int ii = Integer.parseInt(i);
                int ei = Integer.parseInt(e);
                int total = ii - ei;
                viewHolder.bal = (TextView) convertView.findViewById(R.id.bal);
                viewHolder.bal.setText(Html.fromHtml("" + total));
            } else {
                String t = total.getText().toString();
                //Toast.makeText(BalanceStatement.this, t, Toast.LENGTH_SHORT).show();
                String i = viewHolder.inc.getText().toString();
                //Toast.makeText(BalanceStatement.this, i, Toast.LENGTH_SHORT).show();
                String e = viewHolder.exp.getText().toString();
                //Toast.makeText(BalanceStatement.this, e, Toast.LENGTH_SHORT).show();
                int ti = Integer.parseInt(t);
                int ii = Integer.parseInt(i);
                int ei = Integer.parseInt(e);
                int in = ti + ii;
                int ex = ti + ei;
                int total = ti - ei;
                viewHolder.bal = (TextView) convertView.findViewById(R.id.bal);
                viewHolder.bal.setText(Html.fromHtml("" + total));
            }
            //viewHolder.ba=data.getAmount();*/

        // Return the completed view to render on screen
        return convertView;
    }
}


