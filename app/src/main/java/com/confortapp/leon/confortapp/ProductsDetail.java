package com.confortapp.leon.confortapp;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.confortapp.leon.confortapp.Common.Common;
import com.confortapp.leon.confortapp.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductsDetail extends AppCompatActivity {

    TextView products_name, products_price, products_description;
    ImageView products_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    String productsId = "";

    FirebaseDatabase database;
    DatabaseReference products;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FuturaPTDemi.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_products_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        products_name = (TextView) findViewById(R.id.products_name);
        products_price = (TextView) findViewById(R.id.products_price);
        products_description = (TextView) findViewById(R.id.products_description);
        products_image = (ImageView) findViewById(R.id.img_products);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get ProductsId from Intent
        if (getIntent() != null)
            productsId = getIntent().getStringExtra("ProductsId");
        if (!productsId.isEmpty()) {
            if (Common.isConnectedToInternet(getBaseContext()))
                getDetailProducts(productsId);
            else {
                Toast.makeText(ProductsDetail.this, "Please check your Internet connection !", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetailProducts(String ProductId) {
        products.child(ProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

                //Set image
                Picasso.with(getBaseContext())
                        .load(product.getImage())
                        .into(products_image);

                collapsingToolbarLayout.setTitle(product.getName());

                products_price.setText(product.getPrice());
                products_name.setText(product.getName());
                products_description.setText(product.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
