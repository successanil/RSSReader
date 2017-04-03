package com.couriertracking.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.couriertracking.app.pojo.CourierHistoryListItem;
import com.couriertracking.app.pojo.CourierListItem;
import com.couriertracking.app.utility.DbHelper;
import com.ct.app.freewithads.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * created by relsell global
 */

public class HistoryActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private HistorytListAdapter mAdapter;
    private TextView mScreenTitleText;
    private ListView mListView;
    private ArrayList<CourierHistoryListItem> mItemList;
    private static int WEB_REQUEST = 1000;

    private AdView mAdView;
    private AdRequest mAdsRequest;
    private String mLogTagAds="ADS_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_fragment);
        initUI();
    }

    public void initUI() {
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        mScreenTitleText = (TextView)findViewById(R.id.titleText);
        mScreenTitleText.setText(R.string.history_screen_title);
        mListView = (ListView)findViewById(R.id.listview);
        populateAdapterWithDetals();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle args = new Bundle();
                Intent i = new Intent(HistoryActivity.this, WebActivity.class);
                CourierHistoryListItem localObj = mItemList.get(position);
                i.putExtra("courierno_str", localObj.getmCourierTrackNo());
                i.putExtra("courier_name", localObj.getmCourierName());
                i.putExtra("courier_url", localObj.getmCourierUrl());
                startActivityForResult(i, WEB_REQUEST);
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateAdapterWithDetals() {
        mItemList = null;
        mItemList = readFromDataBaseAndPopulateList();
        mAdapter = new HistorytListAdapter(mItemList, HistoryActivity.this);
        mListView.setAdapter(mAdapter);

    }


    public ArrayList<CourierHistoryListItem> readFromDataBaseAndPopulateList() {
        ArrayList<CourierHistoryListItem> list = new ArrayList<CourierHistoryListItem>();

        DbHelper db = new DbHelper(HistoryActivity.this);
        db.open();
        Cursor c = db.read_history_data();
        if (c.moveToFirst()) {
            do {
                String courierName = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_COURIER_NAME));
                String trackNo = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_TRACK_NO));
                String courierUrl = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_COURIER_URL));
                String lastSearchedDate = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_LAST_SEARCHED_DATE));
                list.add(new CourierHistoryListItem("",courierName, trackNo,courierUrl,lastSearchedDate));
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return list;
    }


}
