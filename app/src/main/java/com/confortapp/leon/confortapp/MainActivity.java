package com.confortapp.leon.confortapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    //Slider
    SliderLayout mSlider;
    ArrayList<String> image_list;

    private static final String getURL = "https://confort.kz/new.json";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FuturaPTDemi.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        mSlider = (SliderLayout) findViewById(R.id.slider);
        image_list = new ArrayList<String>();

        JsonArrayRequest billionaireReq = new JsonArrayRequest(getURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                image_list.add(obj.getString("image"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for (int i = 0; i < image_list.size(); i++) {
                            final DefaultSliderView defaultSliderView = new DefaultSliderView(getBaseContext());
                            defaultSliderView
                                    .image(image_list.get(i))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            //Add extra bundle
                            defaultSliderView.bundle(new Bundle());
                            mSlider.addSlider(defaultSliderView);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), " " + error, Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(billionaireReq);

        mSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(5000);
        mSlider.stopAutoCycle();
        mSlider.startAutoCycle(4000,4000,false);


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSlider.startAutoCycle();
//            }
//        }, 4000);


    }

}
