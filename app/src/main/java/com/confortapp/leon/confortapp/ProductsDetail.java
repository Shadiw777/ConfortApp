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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductsDetail extends AppCompatActivity {

    TextView products_name, products_price, products_description;
    ImageView products_image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView products_color1, products_color2, products_color3, products_color4;

    String productsId = "";

    FirebaseDatabase database;
    DatabaseReference products;

    //Slider
    SliderLayout sliderProduct;
    ArrayList<String> imagesList;

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
        //products_image = (ImageView) findViewById(R.id.img_products);

        products_color1 = (ImageView) findViewById(R.id.products_color1);
        products_color2 = (ImageView) findViewById(R.id.products_color2);
        products_color3 = (ImageView) findViewById(R.id.products_color3);
        products_color4 = (ImageView) findViewById(R.id.products_color4);

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

        setupSliderProduct(productsId);
    }

    private void getDetailProducts(String ProductId) {
        products.child(ProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

//                //Set image
//                Picasso.with(getBaseContext())
//                        .load(product.getImage())
//                        .into(products_image);

                collapsingToolbarLayout.setTitle(product.getName());


                products_price.setText(product.getPrice());
                products_name.setText(product.getName());


                Picasso.with(getBaseContext())
                        .load(product.getProducts_color1())
                        .into(products_color1);


                Picasso.with(getBaseContext())
                        .load(product.getProducts_color2())
                        .into(products_color2);


                Picasso.with(getBaseContext())
                        .load(product.getProducts_color3())
                        .into(products_color3);

                Picasso.with(getBaseContext())
                        .load(product.getProducts_color4())
                        .into(products_color4);

                products_description.setText(product.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupSliderProduct(String ProductId) {
        sliderProduct = (SliderLayout) findViewById(R.id.sliderProduct);
        imagesList = new ArrayList<>();

        products.child(ProductId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Product product = dataSnapshot.getValue(Product.class);

                        imagesList.add(product.getImage());
                        imagesList.add(product.getImage_2());

                        for (int i = 0; i < imagesList.size(); i++) {
                            //Create Slider
                            final DefaultSliderView defaultSliderView = new DefaultSliderView(getBaseContext());
                            defaultSliderView
                                    .image(imagesList.get(i))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            //Add extra bundle
                            defaultSliderView.bundle(new Bundle());
                            sliderProduct.addSlider(defaultSliderView);

                            //Remove event after finish
                            products.removeEventListener(this);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        sliderProduct.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderProduct.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderProduct.setCustomAnimation(new DescriptionAnimation());
        sliderProduct.setDuration(4000);
        sliderProduct.stopAutoCycle();
        sliderProduct.startAutoCycle(4000, 4000, false);

    }


    @Override
    protected void onStop() {
        sliderProduct.stopAutoCycle();
        finish();
        super.onStop();
    }
}
