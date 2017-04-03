package com.rssreadertop.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ct.app.freewithads.R;


public class SplashActivity extends AppCompatActivity {


    private static int SPLASH_DISPLAY_LENGTH = 1000;

    public Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_splash_fragment);
        initUI();

    }

    public void initUI() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();


            }
        }, SPLASH_DISPLAY_LENGTH);


    }


}
