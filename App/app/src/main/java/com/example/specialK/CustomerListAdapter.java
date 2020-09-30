package com.example.specialK;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomerListAdapter extends ArrayAdapter<Customer> {
    private static final String TAG = "CustomerListAdapter";
    private Context mContext;
    private int mResource;

    /**
     * Default constructor for the JobListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CustomerListAdapter(Context context, int resource, ArrayList<Customer> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int cusID = getItem(position).getCustomerID();
        String cusName = getItem(position).getCustomerName();
        String cusNumber = getItem(position).getCustomerPhoneNum();
        String cusPhone = getItem(position).getCustomerPhone();
        String cusPart = getItem(position).getCustomerPart();
        String cusService = getItem(position).getCustomerService();
        String cusDate = getItem(position).getCustomerDate();
        String cusDelivery = getItem(position).getCustomerDelivery();
        String cusHavePhone = getItem(position).getCustomerHavePhone();
        String cusContacted = getItem(position).getCustomerContacted();
        String cusNotes = getItem(position).getCustomerNotes();
        Boolean havePhone = (cusHavePhone.equals("Yes")) ? true : false;
        Boolean contacted = (cusContacted.equals("Yes")) ? true : false;

        //Create the person object with the information
        Customer customer = new Customer(cusID, cusName, cusNumber, cusPhone, cusPart, cusService, cusDate, cusDelivery, cusHavePhone, cusContacted, cusNotes);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.textName);
        //TextView tvNumber = (TextView) convertView.findViewById(R.id.textNumber);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.textPhone);
        TextView tvPart = (TextView) convertView.findViewById(R.id.textPart);
        //TextView tvDate = (TextView) convertView.findViewById(R.id.textDate);
        TextView tvDelivery = (TextView) convertView.findViewById(R.id.textDelivery);
        CheckBox tvHavePhone = (CheckBox) convertView.findViewById(R.id.checkBox1);
        CheckBox tvContacted = (CheckBox) convertView.findViewById(R.id.checkBox2);

        if (position % 2 == 1) {
            tvName.setBackgroundColor(Color.parseColor("#414141"));
            tvPhone.setBackgroundColor(Color.parseColor("#414141"));
            tvPart.setBackgroundColor(Color.parseColor("#414141"));
            tvDelivery.setBackgroundColor(Color.parseColor("#414141"));
            tvHavePhone.setBackgroundColor(Color.parseColor("#4f4f4f"));
            tvContacted.setBackgroundColor(Color.parseColor("#4f4f4f"));
        } else {
            tvName.setBackgroundColor(Color.parseColor("#2e2e2e"));
            tvPhone.setBackgroundColor(Color.parseColor("#2e2e2e"));
            tvPart.setBackgroundColor(Color.parseColor("#2e2e2e"));
            tvDelivery.setBackgroundColor(Color.parseColor("#2e2e2e"));
            tvHavePhone.setBackgroundColor(Color.parseColor("#404040"));
            tvContacted.setBackgroundColor(Color.parseColor("#404040"));
        }

        tvName.setText(cusName);
        //tvNumber.setText(cusNumber);
        tvPhone.setText(cusPhone);
        tvPart.setText(cusPart);
        //tvDate.setText(cusDate);
        tvDelivery.setText(cusDelivery);
        tvHavePhone.setChecked(havePhone);
        tvContacted.setChecked(contacted);

        //Log.d(TAG, "CustomerListAdapter: " + cusPhone + " - " + havePhone);

        return convertView;
    }
}
