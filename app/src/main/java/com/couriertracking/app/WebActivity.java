package com.couriertracking.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.ct.app.freewithads.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class WebActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private WebView mWebView;
    String mUser, mPass;
    private String courierName;
    private String courierNoStr;
    private String urlToHit;
    private String courierUrl;

    private Bitmap mBitMapToBeShared;
    private boolean mBitMapSavedInMemory;
    private Uri mBitMapUri;
    private String mSavedBitMapDir;

    ShareActionProvider mShareActionProvider;
    Intent mSharingIntent;
    private TextView mScreenTitleText;
    private ProgressBar mProgressBar;


    private AdView mAdView;
    private AdRequest mAdsRequest;
    private String mLogTagAds="ADS_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_fragment);
        Bundle b = getIntent().getExtras();
        courierNoStr = b.getString("courierno_str");
        courierName = b.getString("courier_name");
        courierUrl = b.getString("courier_url");
        initUI();
    }

    public void initUI() {

        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        mWebView = (WebView) findViewById(R.id.webview);
        mScreenTitleText = (TextView) findViewById(R.id.titleText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(1000);
        mScreenTitleText.setText(R.string.web_screen_title);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);


        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                mWebView.measure(View.MeasureSpec.makeMeasureSpec(
                                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mWebView.layout(0, 0, mWebView.getMeasuredWidth(),
                        mWebView.getMeasuredHeight());
                mWebView.setDrawingCacheEnabled(true);
                mWebView.buildDrawingCache();
                int webviewH = mWebView.getMeasuredHeight();
                int webviewW = mWebView.getMeasuredWidth();
                if (webviewH > 0 && webviewW > 0) {
                    mBitMapToBeShared = Bitmap.createBitmap(mWebView.getMeasuredWidth(),
                            mWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                    Canvas bigcanvas = new Canvas(mBitMapToBeShared);
                    Paint paint = new Paint();
                    int iHeight = mBitMapToBeShared.getHeight();
                    bigcanvas.drawBitmap(mBitMapToBeShared, 0, iHeight, paint);
                    mWebView.draw(bigcanvas);

                    if (mBitMapToBeShared != null) {
                        mSavedBitMapDir = saveToInternalSorage(mBitMapToBeShared);
                    }
                    invalidateOptionsMenu();
                }
            }

        });

        new SendTask().execute();
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
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_web, menu);

        MenuItem shareItem = menu.findItem(R.id.shareButton);

     /*   mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(prepareShareIntent());*/


        return true;
    }


    private String saveToInternalSorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
        File mypath = new File(directory, "temp.png");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBitMapSavedInMemory = true;
        String path = directory.getAbsolutePath();
        return path;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem shareItem = menu.findItem(R.id.shareButton);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(prepareShareIntent());


        return true;
    }

    private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "/temp.png");
            mBitMapToBeShared = BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Intent prepareShareIntent() {
        mSharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        mSharingIntent.setType("image/png");
        String shareBody = "Shared By: "+getResources().getString(R.string.app_name_title) + " https://play.google.com/store/apps/details?id=" + getPackageName();

        if (mBitMapSavedInMemory) {
            loadImageFromStorage(mSavedBitMapDir);
            String path = MediaStore.Images.Media.insertImage(WebActivity.this.getContentResolver(), mBitMapToBeShared, "Title", null);
            mBitMapUri = Uri.parse(path);
            mSharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, mBitMapUri);
        }
        mSharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Courier Status");
        mSharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        return mSharingIntent;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cancel) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearCache(true);
        }
    }

    public class SendTask extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                if (courierName.equalsIgnoreCase("XpressBees Courier Tracking")) {



                }else if(courierName.equalsIgnoreCase("Airborn international")) {
                    courierUrl = "http://www.airborneinternational.in/tracking.aspx";
                } else if(courierName.equalsIgnoreCase("Atlanta courier")) {
                    courierUrl = "http://www.atlantacourierindia.com/Tracking.php";
                } else if(courierName.equalsIgnoreCase("Chips international courier")) {
                    courierUrl = "http://chipsairexp.com/Tracking.aspx";
                }

                URL url = new URL(courierUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.setDoInput(true);
                String postData = getPostData(courierNoStr);
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();
                mConnectionCode = connection.getResponseCode();

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
                    mProgressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    mWebView.loadDataWithBaseURL(courierUrl, responseString.toString(), "text/html", Xml.Encoding.UTF_8.toString(), null);
                }
            } else {

                if (mConnectionCode >= HttpURLConnection.HTTP_INTERNAL_ERROR) {

                    Toast.makeText(WebActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    if (courierName.equalsIgnoreCase("Palande Courier Tracking")) {
                        mWebView.loadUrl("http://www.palandecourier.com/Courier.aspx");
                    } else if (courierName.equalsIgnoreCase("Pigeon Courier Tracking")) {
                        mWebView.loadUrl("http://www.pigeonexpress.com/tracking.aspx");
                    }else {
                        WebActivity.this.finish();
                    }

                } else if (mConnectionCode >= HttpURLConnection.HTTP_BAD_REQUEST && mConnectionCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {

                    Toast.makeText(WebActivity.this, "Client Side Error", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    if (courierName.equalsIgnoreCase("Bonds Logistics Courier Tracking")) {
                        mWebView.loadUrl("http://bondslogistics.com/");
                    } else if (courierName.equalsIgnoreCase("Akash Ganga Courier Tracking")) {
                        mWebView.loadUrl("http://akashganga.info/tracker.htm");
                    } else if (courierName.equalsIgnoreCase("Madhur Courier Tracking")) {
                        mWebView.loadUrl("http://www.madhurcouriers.in/");
                    } else if (courierName.equalsIgnoreCase("Palande Courier Tracking")) {
                        mWebView.loadUrl("http://www.palandecourier.com/Courier.aspx");
                    }else if (courierName.equalsIgnoreCase("Pigeon Courier Tracking")) {
                        mWebView.loadUrl("http://www.palandecourier.com/Courier.aspx");
                    }else if (courierName.equalsIgnoreCase("United Courier Tracking")) {
                        mWebView.loadUrl("http://www.unitedcouriers.biz/");
                    }else if (courierName.equalsIgnoreCase("Delhivery Courier Tracking")) {
                        mWebView.loadUrl("https://track.delhivery.com/accounts/login/?next=/");
                    }else if (courierName.equalsIgnoreCase("Expressit Courier Tracking")) {
                        mWebView.loadUrl("http://www.expressit.in/");
                    }else if (courierName.equalsIgnoreCase("Airborn international")) {
                        mWebView.loadUrl("http://www.airborneinternational.in/");
                    }else {
                        WebActivity.this.finish();
                    }

                } else if (mConnectionCode >= HttpURLConnection.HTTP_MULT_CHOICE && mConnectionCode < HttpURLConnection.HTTP_BAD_REQUEST) {

                    Toast.makeText(WebActivity.this, "Server Page is temp moved", Toast.LENGTH_LONG).show();
                    mProgressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    if (courierName.equalsIgnoreCase("Ecom Courier Tracking")) {
                        mWebView.loadUrl("http://ecomexpress.in");
                    } else {
                        WebActivity.this.finish();
                    }

                }else {

                    Toast.makeText(WebActivity.this, "Unable to communicate with  Server", Toast.LENGTH_LONG).show();
                    WebActivity.this.finish();

                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (mProgressBar != null) {
                mProgressBar.setProgress(values[0]);
            }

        }
    }

    public String getPostDataStr(HashMap<String, String> map) {
        String result = "";

        Set<String> myset = map.keySet();
        int setSize = myset != null ? myset.size() : 0;
        int i = 0;

        for (String key : myset) {
            String value = map.get(key);
            result += key + "=" + value;
            if (i <= setSize - 1)
                result += "&";
            i++;
        }
        return result;
    }

    public String getPostData(String awbNo) throws UnsupportedEncodingException {

        String result = "";


        if (courierName.equals("Airstate Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("AwbNO", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Airwings Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("txtawbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Aramex Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ShipmentNumber", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Blazeflash Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("awbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Blue Dart Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("awb", "awb");
            map.put("action", "awbquery");
            map.put("handler", "tnt");
            map.put("numbers", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Bombino Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("txtAwb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Bom-Gim Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Submit.x", "49");
            map.put("Submit.y", "9");
            map.put("txtawbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Eagle Courier Tracking")) {
           /* HashMap<String, String> map = new HashMap<String, String>();
            map.put("Track_button", "Track");
            map.put("rdb", "A");
            map.put("txt_Awb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);*/
        } else if (courierName.equalsIgnoreCase("FedEx India Courier Tracking")) {
            /*HashMap<String, String> map = new HashMap<String, String>();
            map.put("data", "{\"TrackPackagesRequest\":{\"appType\":\"WTRK\",\"uniqueKey\":\"\",\"processingParameters\":{}," +
                    "\"trackingInfoList\":[{\"trackNumberInfo\":{\"trackingNumber\":\"" + URLEncoder.encode(awbNo, "UTF-8") + "\",\"trackingQualifier\":\"\"," +
                    "\"trackingCarrier\":\"\"}}]}}");
            map.put("action", "trackpackages");
            map.put("locale", "en_IN");
            map.put("version", "1");
            map.put("format", "json");
            map.put("cntry_code", "in");*/
            //result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("First Flight Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("conRadio", "c");
            map.put("reqLoc", "");
            map.put("consignmentNo", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Flyking Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("cnote", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("GMS Express Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("contact_us", "SUBMIT");
            map.put("message", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Akash Ganga Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("cr", URLEncoder.encode("Akash Ganga", "UTF-8"));
            map.put("wb", "true");
            map.put("tn", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Corporate Courier Tracking")) {
            /*HashMap<String, String> map = new HashMap<String, String>();
            map.put("conno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);*/
        } else if (courierName.equalsIgnoreCase("XpressBees Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("tracking_id", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Vulcan Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("awb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("UPS Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("track.x", "Track");
            map.put("HTMLVersion", "USERHISTORYLIST");
            map.put("HTMLVersion", "5.0");
            map.put("loc", "en_US");
            map.put("trackNums", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("TNT Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("navigation", "1");
            map.put("searchType", "CON");
            map.put("requestType", "GEN");
            map.put("plazakey", "");
            map.put("sourceCountry", "ww");
            map.put("sourceID", "1");
            map.put("trackType", "CON");
            map.put("genericSiteIdent", "");
            map.put("page", "1");
            map.put("respLang", "en");
            map.put("respCountry", "au");
            map.put("cons", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("TCI XPS Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("dwbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("ST Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("B1", "Track");
            map.put("awb_nos", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Speed & Safe Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("track_cn", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("SkyNet Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("radiobutton", "SB");
            map.put("textfield", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Shree Tirupati Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("docno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Shree Maruti Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("DTDC Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("action", "track");
            map.put("sec", "tr");
            map.put("ctlActiveVal", "1");
            map.put("TType", "awb_no");
            map.put("strCnno", URLEncoder.encode(awbNo, "UTF-8"));
            map.put("GES", "N");
            map.put("TrkType2", "awb_no");
            map.put("strCnno2", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Indiaontime Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("cx", "Track");
            map.put("cof", "FORDID:9");
            map.put("id", "UTF-8");
            map.put("sa.x", "1");
            map.put("sa.y", "3");
            map.put("q", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Shree Balaji Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("docno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Overnite Express Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("AwbNo", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Metro Maruti Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ShipmentNum", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Mirakle Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ewd-otp-action", "track");
            map.put("fields-labels", "");
            map.put("Tracking_Number", URLEncoder.encode(awbNo, "UTF-8"));
            map.put("Login_Submit", "Track");
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Om Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("txtawbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Ondot Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("txtAwb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("PAFEX Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("search", "Track");
            map.put("txtawb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Palande Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("wb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Pegasus Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("wb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Pigeon Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("wb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Poonam Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("wb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Professional Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("type", "0");
            map.put("service", "0");
            map.put("id", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Pushpak Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("type", "0");
            map.put("service", "0");
            map.put("id", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Quantium Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("type", "0");
            map.put("service", "0");
            map.put("id", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Rapidconnect Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("T2", "[object RadioNodeList]");
            map.put("T1", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Red Express Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Type", "WayBill");
            map.put("IsOutSidePage", "yes");
            map.put("WayBills", URLEncoder.encode(awbNo, "UTF-8") + "_WayBill");
            map.put("Ref", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Safexpress Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Type", "WayBill");
            map.put("IsOutSidePage", "yes");
            map.put("WayBills", URLEncoder.encode(awbNo, "UTF-8") + "_WayBill");
            map.put("Ref", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Registered Post Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Type", "WayBill");
            map.put("IsOutSidePage", "yes");
            map.put("WayBills", URLEncoder.encode(awbNo, "UTF-8") + "_WayBill");
            map.put("Ref", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Trackon Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Type", "WayBill");
            map.put("IsOutSidePage", "yes");
            map.put("WayBills", URLEncoder.encode(awbNo, "UTF-8") + "_WayBill");
            map.put("Ref", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("UBX Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Type", "WayBill");
            map.put("IsOutSidePage", "yes");
            map.put("WayBills", URLEncoder.encode(awbNo, "UTF-8") + "_WayBill");
            map.put("Ref", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("United Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Submit", "track");
            map.put("afno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Ecom Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("news_go", "track+now");
            map.put("order", "yes");
            map.put("awb", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Ecart Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Type", "WayBill");
            map.put("IsOutSidePage", "yes");
            map.put("WayBills", URLEncoder.encode(awbNo, "UTF-8") + "_WayBill");
            map.put("Ref", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Oxford Express Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("isShipmentFrom", "true");
            map.put("emailId", "success_anil@yahoo.co.in");
            map.put("trackingId", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("DHL Courier Tracking")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("brand", "DHL");
            map.put("AWB", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        }else if (courierName.equalsIgnoreCase("Airborn international")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("txtawbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        }else if (courierName.equalsIgnoreCase("Atlanta courier")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("button", "");
            map.put("ConsinmentNo", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        } else if (courierName.equalsIgnoreCase("Chips international courier")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rdb", "A");
            map.put("txtawbno", URLEncoder.encode(awbNo, "UTF-8"));
            result = getPostDataStr(map);
        }

        return result;

    }


}
