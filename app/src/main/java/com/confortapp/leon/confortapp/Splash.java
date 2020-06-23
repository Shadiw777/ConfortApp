package com.confortapp.leon.confortapp;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    TextView textConfort;
    ImageView logoConfort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textConfort = (TextView) findViewById(R.id.textConfort);
        logoConfort = (ImageView) findViewById(R.id.logoConfort);

        Animation myAnimation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        textConfort.startAnimation(myAnimation);
        logoConfort.startAnimation(myAnimation);

        final Intent intent = new Intent(this,Home.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();



    }

}