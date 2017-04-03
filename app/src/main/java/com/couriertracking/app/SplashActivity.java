package com.couriertracking.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.couriertracking.app.pojo.CourierHistoryListItem;
import com.couriertracking.app.utility.DbHelper;
import com.ct.app.freewithads.R;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextView mScreenTitleText;
    private static int SPLASH_DISPLAY_LENGTH = 3000;

    public Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_fragment);
        initUI();

    }

    public void initUI() {
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        mScreenTitleText = (TextView) findViewById(R.id.titleText);
        mScreenTitleText.setTextColor(getResources().getColor(R.color.app_prominent));
        //mScreenTitleText.setText(R.string.splash_screen_title);

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
