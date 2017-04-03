package com.couriertracking.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.couriertracking.app.pojo.CourierListItem;
import com.couriertracking.app.utility.DbHelper;
import com.couriertracking.app.utility.MyAppUtility;
import com.ct.app.freewithads.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static int WEB_REQUEST = 1000;
    private static int HISTORY_REQUEST = 1001;
    private Toolbar mToolBar;
    private LinearLayout searchButtonContainer;
    private EditText mCourierTackingNoET;
    private TextView mErrorMsg;
    private TextView mScreenTitleText;
    private Spinner mCourierSpinner;
    private CourierListAdapter mAdapter;
    private CourierListAdapterFilter mFilterAdapter;
    static String mDBPrefsfileName = "DBFile";
    ArrayList<CourierListItem> mItemList;
    private AdView mAdView;
    private AdRequest mAdsRequest;
    private String mLogTagAds = "ADS_TAG";

    private CourierListItem mCourierListItemToSearch;

    private AutoCompleteTextView mAutoCompleteTextView;


    private CourierAlarmReceiver mAlarmReceiver;

    private ProgressDialog mProgressInitialDialog;
    public Handler mainActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mProgressInitialDialog != null) {
                mProgressInitialDialog.dismiss();
                mFilterAdapter.notifyDataSetChanged();
                writeToPrefs(mDBPrefsfileName, "dbupdated", "true");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment);

        String key = "dbupdated";
        String value = getDataFromPefs(mDBPrefsfileName, key);

        if (value == null || value.equalsIgnoreCase("false")) {
            mProgressInitialDialog = new ProgressDialog(this);
            mProgressInitialDialog.setMessage("Initializing application for first time!");
            mProgressInitialDialog.setCancelable(false);
            mProgressInitialDialog.show();
        }
        MyAppUtility.getInstance().setmMainActivityCallback(this);
        initUI();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //populateAdapterWithDetals();

        if (mAlarmReceiver != null) {
            mAlarmReceiver.setAlarm(MainActivity.this);
            new DBLoader().execute();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void initUI() {
        mAlarmReceiver = new CourierAlarmReceiver();
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        searchButtonContainer = (LinearLayout) findViewById(R.id.button_layout);
        mCourierSpinner = (Spinner) findViewById(R.id.courier_service);
        mCourierSpinner.setFocusable(true);
        mCourierSpinner.setFocusableInTouchMode(true);
        mCourierSpinner.requestFocus();
        mCourierTackingNoET = (EditText) findViewById(R.id.trackno);
        mScreenTitleText = (TextView) findViewById(R.id.titleText);
        mScreenTitleText.setText(R.string.app_name_title);
        mErrorMsg = (TextView) findViewById(R.id.errortxt);

        mCourierTackingNoET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mErrorMsg.setVisibility(View.GONE);
            }
        });

        mCourierSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mErrorMsg.setVisibility(View.GONE);
            }
        });
        mCourierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCourierListItemToSearch = mItemList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mErrorMsg.setVisibility(View.GONE);
                if (isConnectingToInternet()) {
                    if (mCourierTackingNoET != null && !mCourierTackingNoET.getText().toString().equalsIgnoreCase("")) {
                        Bundle args = new Bundle();
                        Intent i = new Intent(MainActivity.this, WebActivity.class);
                        i.putExtra("courierno_str", mCourierTackingNoET.getText().toString());
                        i.putExtra("courier_name", mCourierListItemToSearch.getmCourierName());
                        i.putExtra("courier_url", mCourierListItemToSearch.getmCourierUrl());
                        startActivityForResult(i, WEB_REQUEST);
                    } else {
                        mErrorMsg.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Internet Connection not available", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
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


        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index,
                                    long arg3) {
                CourierListItem localObj = (CourierListItem) av.getItemAtPosition(index);

                String name = localObj.getmCourierName();
                //String number = map.get("Phone");

                mAutoCompleteTextView.setText(name);

                mCourierListItemToSearch = localObj;

            }


        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEB_REQUEST) {
            DbHelper db = new DbHelper(MainActivity.this);
            db.open();
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            db.insert_history_data(mCourierListItemToSearch.getmCourierName(), mCourierTackingNoET.getText().toString(), mCourierListItemToSearch
                    .getmCourierUrl(), formattedDate);
            db.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(i, HISTORY_REQUEST);
            return true;
        } else if (id == R.id.action_add) {
            Intent i = new Intent(MainActivity.this, SuggestToAdd.class);
            startActivityForResult(i, HISTORY_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateAdapterWithDetals() {
        mItemList = null;
        populateDataBaseWithInitialValues();
        //mItemList = readFromDataBaseAndPopulateList();
        //mAdapter = new CourierListAdapter(mItemList, MainActivity.this);
        //mFilterAdapter = new CourierListAdapterFilter(mItemList, MainActivity.this);
        //mCourierSpinner.setAdapter(mAdapter);
        //mAutoCompleteTextView.setAdapter(mFilterAdapter);

    }

    public void populateDataBaseWithInitialValues() {

        String key = "dbcreated";
        String value = getDataFromPefs(mDBPrefsfileName, key);

        //  if (value == null || value.equalsIgnoreCase("false")) {


        // write a logic here to  read file from url


        if (isConnectingToInternet()) {
            new GetConfigDataTask().execute();
        } else {
            Toast.makeText(MainActivity.this, "Internet Connection not available", Toast.LENGTH_SHORT).show();
        }



            /*/////

            // write a logic here to  read file from asset
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new InputStreamReader(getAssets().open("courirelist_sample.txt")));
                String line = null;
                while((line = reader.readLine()) != null) {

                    String []arrCourierData = line.split(";;");
                    String strCourierName = arrCourierData[0];
                    String strCourierUrl = arrCourierData[1];
                    db.insert_courier_data("", strCourierName,strCourierUrl);
                }

            }catch(IOException e) {
                e.printStackTrace();
            }finally {
                if(reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/


        writeToPrefs(mDBPrefsfileName, key, "true");
        // }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public ArrayList<CourierListItem> readFromDataBaseAndPopulateList() {
        ArrayList<CourierListItem> list = new ArrayList<CourierListItem>();

        DbHelper db = new DbHelper(MainActivity.this);
        db.open();
        Cursor c = db.read_courier_data();
        if (c.moveToFirst()) {
            do {
                String imgSrc = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_IMG_SRC));
                String courierName = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_COURIER_NAME));
                String courierUrl = c.getString(c.getColumnIndexOrThrow(DbHelper.FIELD_COURIER_URL));
                list.add(new CourierListItem(imgSrc, courierName, courierUrl));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }


    public void writeToPrefs(String fileName, String key, String value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDataFromPefs(String fileName, String key) {
        String value = null;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getString(key, null);
        return value;
    }

    public void updateUI() {
        new DBLoader().execute();
    }


    private class DBLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mItemList = null;
            mItemList = readFromDataBaseAndPopulateList();
            return mItemList != null;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                mFilterAdapter = new CourierListAdapterFilter(mItemList, MainActivity.this);
                mAutoCompleteTextView.setAdapter(mFilterAdapter);
            }

        }
    }


    public class GetConfigDataTask extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                URL url = new URL("http://relsellglobal.in/courier_list_sample.txt");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setDoInput(true);


                mConnectionCode = connection.getResponseCode();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mConnectionCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

                    String line = "";

                    while ((line = rd.readLine()) != null) {
                        responseString.append(line);
                    }

                    return true;
                }
            } catch (IOException e) {
                Log.v("Message", e.getMessage());
                e.printStackTrace();
                return false;

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                if (responseString != null && !responseString.toString().equalsIgnoreCase("")) {

                    DbHelper db = new DbHelper(MainActivity.this);
                    db.open();

                    db.emptyTables();

                    String[] arrCourierLine = responseString.toString().split(";;;");

                    for (int i = 0; i < arrCourierLine.length; i++) {

                        String[] arrCourierData = arrCourierLine[i].split(";;");
                        String strCourierName = arrCourierData[0];
                        String strCourierUrl = arrCourierData[1];
                        db.insert_courier_data("", strCourierName, strCourierUrl);


                    }

                    db.close();

                    new DBLoader().execute();


                } else if (mConnectionCode >= HttpURLConnection.HTTP_BAD_REQUEST && mConnectionCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {


                } else if (mConnectionCode >= HttpURLConnection.HTTP_MULT_CHOICE && mConnectionCode < HttpURLConnection.HTTP_BAD_REQUEST) {


                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlarmReceiver != null) {
            mAlarmReceiver.cancelAlarm(MainActivity.this);
        }
    }
}
