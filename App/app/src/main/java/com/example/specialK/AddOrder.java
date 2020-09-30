package com.example.specialK;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddOrder extends AppCompatActivity {
    private static final String TAG = "AddOrder";
    Button buttonSave;
    EditText inputName, inputEmail, inputAddress, inputProduct_id;
    RequestQueue requestQueue;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        mDatabaseHelper = new DatabaseHelper(this);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputAddress = (EditText) findViewById(R.id.inputAddress);
        inputProduct_id = (EditText) findViewById(R.id.inputProduct_id);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                String address = inputAddress.getText().toString();
                String product_id = inputProduct_id.getText().toString();

                if(!name.equals("") && !email.equals("") && !address.equals("")) {
                    insertCustomer("http://159.65.248.122/app/insertOrder.php", name, email, address, product_id);
                } else {
                    toastMessage("Name, Email, and Address is required.");
                }
            }
        });

    }

    public void insertCustomer(String url, String name, String email, String address, String product_id) {
        String uri = Uri.parse(url)
                .buildUpon()
                .appendQueryParameter("name", name)
                .appendQueryParameter("email", email)
                .appendQueryParameter("address", address)
                .appendQueryParameter("product_id", product_id)
                .build().toString();

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jor = new JsonObjectRequest(uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "insertOrder: inserted order...");
                try {
                    String res = response.getString("success");
                    //JSONObject jsonObject = response.getJSONObject("success");
                    if(res.equals("true")) {
                        toastMessage("Order Successfully Inserted.");
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
