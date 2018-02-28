package com.confortapp.leon.confortapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.confortapp.leon.confortapp.Model.Banner;
import com.confortapp.leon.confortapp.Model.Category;
import com.confortapp.leon.confortapp.R;
import com.confortapp.leon.confortapp.ViewHolder.MenuViewHolder;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.FacebookSdk;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    //Slider
    HashMap<String, String> image_list;
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
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/FuturaPTDemi.otf");

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //printKeyHash();

        mSlider = (SliderLayout) findViewById(R.id.slider);
        image_list = new HashMap<>();

        final DatabaseReference banner = database.getReference("Banner");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Banner banner = postSnapShot.getValue(Banner.class);
                    //We will concat string name and id like
                    //PIZZA_01 => And we will use PIZZA for show description, 01 for food id to click
                    image_list.put(banner.getName() + "@" + banner.getId(), banner.getImage());
                }
                for (String key : image_list.keySet()) {
                    String[] keySplit = key.split("@");
                    String nameOfProduct = keySplit[0];
                    String idOfProduct = keySplit[1];

                    //Create Slider
                    final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            //.description(nameOfProduct)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent intent = new Intent(MainActivity.this, ProductsDetail.class);
                                    //We will send food id to FoodDetail
                                    intent.putExtras(textSliderView.getBundle());
                                    startActivity(intent);
                                }
                            });
                    //Add extra bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("ProductsId", idOfProduct);
                    mSlider.addSlider(textSliderView);

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
    }


//    private void printKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.confortapp.leon.confortapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
}
