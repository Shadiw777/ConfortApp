package com.confortapp.leon.confortapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                    sleep(3000);
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