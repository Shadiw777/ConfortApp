package com.confortapp.leon.confortapp;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.confortapp.leon.confortapp.Model.Banner;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    //Slider
    ArrayList<String> image_list;
    SliderLayout mSlider;
    FirebaseDatabase database;
    DatabaseReference category;



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

        setContentView(R.layout.activity_main);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("ProductsId");


        mSlider = (SliderLayout) findViewById(R.id.slider);
        image_list = new ArrayList<>();

        final DatabaseReference banner = database.getReference("Banner");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Banner banner = postSnapShot.getValue(Banner.class);
                    //PRODUCT_01 => And we will use PRODUCT for show description, 01 for product id to click
                    image_list.add(banner.getImage());
                }

                for (int i = 0; i < image_list.size(); i++) {
                    //Create Slider
                    final DefaultSliderView defaultSliderView = new DefaultSliderView(getBaseContext());
                    defaultSliderView
                            .image(image_list.get(i))
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    //Add extra bundle
                    defaultSliderView.bundle(new Bundle());
                    mSlider.addSlider(defaultSliderView);

                    //Remove event after finish
                    banner.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(4000);
        mSlider.stopAutoCycle();
        mSlider.startAutoCycle(4000,4000,false);


    }


}
