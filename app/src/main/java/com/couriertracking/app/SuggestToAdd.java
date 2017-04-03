package com.couriertracking.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.couriertracking.app.email.MailSender;
import com.couriertracking.app.pojo.CourierListItem;
import com.ct.app.freewithads.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by anilkukreti on 29/11/15.
 */
public class SuggestToAdd extends AppCompatActivity {


    private Toolbar mToolBar;
    private LinearLayout submitButtonContainer;
    private EditText mCourierTackingName;
    private EditText mCourierUrlET;
    private TextView mScreenTitleText;
    private AdView mAdView;
    private AdRequest mAdsRequest;
    private String mLogTagAds="ADS_TAG";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_to_add);

        initUI();

    }


    public void initUI() {
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        submitButtonContainer = (LinearLayout) findViewById(R.id.button_layout);

        mCourierTackingName = (EditText)findViewById(R.id.courier_name);
        mCourierUrlET = (EditText)findViewById(R.id.courier_url);
        mScreenTitleText = (TextView)findViewById(R.id.titleText);
        mScreenTitleText.setText(R.string.suggest_courier_screen_title);


        mCourierTackingName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });




        submitButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnectingToInternet()) {

                    final String suggestedCourierName = mCourierTackingName != null ? mCourierTackingName.getText().toString() : "";
                    final String suggestedCourierUrl = mCourierUrlET != null ? mCourierUrlET.getText().toString() : "";

                    final String recipientAddress = "buzzsubash@gmail.com";
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                new MailSender("vivekchaudh2@gmail.com", "kl09B9623").sendMail("Test Mail By Our Courier Tracking App", "Kindly add courier name :" + suggestedCourierName + " and courier url :" + suggestedCourierUrl + ". Thanks !", "successmotivates@gmail.com", recipientAddress);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Toast.makeText(SuggestToAdd.this, "Thanks for suggesting. Mail Will be sent to support  team.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SuggestToAdd.this,"Internet Connection not available",Toast.LENGTH_SHORT).show();
                }




            }
        });

        mAdView = (AdView)findViewById(R.id.adView);
        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mAdsRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(mAdsRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                // called when ad is closed.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                // called when ad is failed to Load
                Log.v(mLogTagAds, "onAdFailedToLoad : " + errorCode);

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                // called when ad Left Application
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // called when ad is opened
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // called when ad is loaded
                Log.v(mLogTagAds, "AddLoaded : ");
            }
        });




    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }



}
