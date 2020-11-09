package com.example.specialK;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class Products extends AppCompatActivity {

    ListView listview;
    String[] pName = {"Apple", "Banana", "Gatorade", "Clorox Wipes", "iPhone 12", "Dog Food"};
    String[] pPrice = {"$1.48 each", "$0.20 each", "$5.98", "$4.98", "$799", "$21.84"};
    String[] pStock = {"68", "77", "30", "3", "7", "11"};
    Integer[] pImg = {R.drawable.apple, R.drawable.banana, R.drawable.gatorade, R.drawable.cloroxwipes, R.drawable.iphone12, R.drawable.dogfood};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        listview = (ListView) findViewById(R.id.listview);
        ProductListView productListView = new ProductListView(this, pName, pPrice, pStock, pImg);
        listview.setAdapter(productListView);
    }
}