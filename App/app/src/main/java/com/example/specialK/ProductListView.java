package com.example.specialK;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class ProductListView extends ArrayAdapter<String> {
    private String[] pName;
    private String[] pPrice;
    private String[] pStock;
    private Integer[] pImg;
    private Activity context;

    public ProductListView(Activity context, String[] pName, String[] pPrice, String[] pStock, Integer[] pImg) {
        super(context, R.layout.listview_layout, pName);

        this.context = context;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pStock = pStock;
        this.pImg = pImg;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;

        if(r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.imageP.setImageResource(pImg[position]);
        viewHolder.textName.setText(pName[position]);
        viewHolder.textPrice.setText(pPrice[position]);
        viewHolder.textStock.setText(pStock[position]);

        return r;
    }

    class ViewHolder {
        TextView textName;
        TextView textPrice;
        TextView textStock;
        ImageView imageP;

        ViewHolder(View v) {
            textName = (TextView) v.findViewById(R.id.productName);
            textPrice = (TextView) v.findViewById(R.id.productPrice);
            textStock = (TextView) v.findViewById(R.id.productStock);
            imageP = (ImageView) v.findViewById(R.id.imageView);
        }
    }
}
