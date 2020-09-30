package com.example.specialK;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewCustomer extends AppCompatActivity {
    private static final String TAG = "ViewCustomer";

    private String cusID;
    DatabaseHelper mDatabaseHelper;

    TextView name, number, phone, part, service, date, delivery, have, contacted, notes;
    Button editButton, deleteButton;
    AlertDialog.Builder builder;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);

        builder = new AlertDialog.Builder(this);

        //get the intent extra
        Intent receivedIntent = getIntent();
        cusID = receivedIntent.getStringExtra("id");

        mDatabaseHelper = new DatabaseHelper(this);
        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        phone = (TextView) findViewById(R.id.device);
        part = (TextView) findViewById(R.id.part);
        service = (TextView) findViewById(R.id.service);
        date = (TextView) findViewById(R.id.date);
        delivery = (TextView) findViewById(R.id.delivery);
        have = (TextView) findViewById(R.id.havePhone);
        contacted = (TextView) findViewById(R.id.contacted);
        notes = (TextView) findViewById(R.id.notes);

        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        getCustomerinfo("http://159.65.248.122/CPR/getCustomer.php?id=" + cusID);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editCustomer = new Intent(ViewCustomer.this, MainActivity.class);
                editCustomer.putExtra("id", cusID);
                startActivity(editCustomer);
            }
        });

        final DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteCustomer("http://159.65.248.122/CPR/deleteCustomer.php?id=" + cusID);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Are you sure you want to delete this customer?").setPositiveButton("Yes", deleteListener).setNegativeButton("No", deleteListener).show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getCustomerinfo("http://159.65.248.122/CPR/getCustomer.php?id=" + cusID);
    }

    private void getCustomerinfo(String url) {
        Log.d(TAG, "getCustomerinfo: called function...");
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "getCustomerinfo: trying to get json results...");
                    JSONArray ja = response.getJSONArray("results");
                    JSONObject jsonObject = ja.getJSONObject(0);
                    int itemID = -1;

                    itemID = jsonObject.getInt("id");
                    String itemName = jsonObject.getString("name");
                    String itemNumber = jsonObject.getString("number");
                    String itemPhone = jsonObject.getString("phone");
                    String itemPart = jsonObject.getString("part");
                    String itemService = jsonObject.getString("service");
                    String itemDate = jsonObject.getString("date");
                    String itemDelivery = jsonObject.getString("expectedDelivery");
                    String itemHave = jsonObject.getString("havePhone");
                    String itemContacted = jsonObject.getString("contacted");
                    String itemNotes = jsonObject.getString("notes");

                    if(itemID > -1) {
                        Log.d(TAG, "onCreate: The ID is: " + itemID);
                        name.setText(itemName);
                        number.setText(itemNumber);
                        phone.setText(itemPhone);
                        part.setText(itemPart);
                        service.setText(itemService);
                        date.setText(itemDate);
                        delivery.setText(itemDelivery);
                        have.setText(itemHave);
                        contacted.setText(itemContacted);
                        notes.setText(itemNotes);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        requestQueue.add(jor);
    }

    private void deleteCustomer(String url) {
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String res = response.getString("success");
                    if(res.equals("true")) {
                        toastMessage("Customer has been deleted.");
                        finish();
                    } else {
                        toastMessage("Something went wrong.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

        requestQueue.add(jor);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
