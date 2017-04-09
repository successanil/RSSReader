package com.rssreadertop.utility;

/**
 * Created by anil on 8/7/15.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.LruCache;
import android.view.Display;
import android.widget.Toast;


import com.rssreadertop.pojo.RSSPageParam;


public class MyAppUtility extends Application {

    //sensible place to declare a log tag for the application
    public static final String LOG_TAG = "myapp";
    private static Display sDisplay = null;
    private boolean showDBToastMsg = true;
    private boolean settingsEnabled = true;
    private boolean mDebugModeForBeta = true;
    private boolean mUserLoggedIn = false;
    private String mUserName;
    private String base64ConfigKey;


    private ActionBar mActionBar;

    ActionBarDrawerToggle mDrawerToggle;


    private String mUserPhoneNo;
    private boolean isLocal = false;








    public ActionBarDrawerToggle getmDrawerToggle() {
        return mDrawerToggle;
    }

    public void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
        this.mDrawerToggle = mDrawerToggle;
    }

    public ActionBar getmActionBar() {
        return mActionBar;
    }

    public void setmActionBar(ActionBar mActionBar) {
        this.mActionBar = mActionBar;
    }

    public boolean ismUserLoggedIn() {
        return mUserLoggedIn;
    }

    public void setmUserLoggedIn(boolean mUserLoggedIn) {
        this.mUserLoggedIn = mUserLoggedIn;
    }


    public boolean ismDebugModeForBeta() {
        return mDebugModeForBeta;
    }

    public void setmDebugModeForBeta(boolean mDebugModeForBeta) {
        this.mDebugModeForBeta = mDebugModeForBeta;
    }


    public String getmUserPhoneNo() {
        return mUserPhoneNo;
    }

    public void setmUserPhoneNo(String mUserPhoneNo) {
        this.mUserPhoneNo = mUserPhoneNo;
    }


    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }


    //instance
    private static MyAppUtility instance = null;

    //keep references to our global resources
    private static LruCache<String, Bitmap> mMemoryCache;





    /**
     * Convenient accessor, saves having to call and cast getApplicationContext()
     */
    public static MyAppUtility getInstance() {
        checkInstance();
        return instance;
    }

    /**
     * Accessor for some resource that depends on a context
     */
    public LruCache<String, Bitmap> getMemoryCache() {
        if (mMemoryCache == null) {
            checkInstance();
            // check here for cache if available
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/2th of the available memory for this memory cache.
            final int cacheSize = maxMemory;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        return mMemoryCache;
    }















    private static void checkInstance() {
        if (instance == null)
            throw new IllegalStateException("Application not created yet!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //provide an instance for our static accessors
        instance = this;
    }

    public void showToastMsgForDB(Context context, String msg, boolean major) {
        if (showDBToastMsg || major) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void writeToPrefs(String fileName, String key, String value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void writeToLongPrefs(String fileName, String key, long value) {

        SharedPreferences sharedpreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getDataFromPefs(String fileName, String key) {
        String value = null;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getString(key, null);
        return value;
    }

    public long getLongDataFromPerfs(String fileName, String key) {
        long value = -1;
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        value = prefs.getLong(key, -1);
        return value;
    }

    public String getServer() {

        if (isLocal) {
            return "192.168.1.6";
        }
        return "www.relsellglobal.in";
    }

    public String getPort() {

        if (isLocal) {
            return "9093";
        }
        return "80";
    }

    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }


    public String getURLForPage(int lessonNo,int level,int id) {

        String res = null;
        if(lessonNo == 0 && level == 0) {
            return "http://www.torrins.com/services/lesson/list?parent=1&children=0&required[]=instructor";
        } else if(lessonNo == 0 && level == 1) {
            return "http://www.torrins.com/services/lesson/list?parent="+id+"&children=0&required[]=instructor";
        } else if(lessonNo == 0 && level == 2) {
            return "http://www.torrins.com/services/lesson/list?id="+id+"&children=0&required[]=instructor";
        } else if(lessonNo == 1 && level == 0) {
            return "http://www.torrins.com/services/lesson/list?parent=10900&children=0&required[]=instructor";
        } else if(lessonNo == 1 && level == 1) {
            return "http://www.torrins.com/services/lesson/list?id="+id+"&children=0&required[]=instructor";
        } else if(lessonNo == 1 && level == 2) {
            return "http://www.torrins.com/services/lesson/list?id="+id+"&children=0&required[]=instructor";
        } else if(lessonNo == 2 && level == 0) {
            return "http://www.torrins.com/services/lesson/list?parent=3772&children=0&required[]=instructor";
        } else if(lessonNo == 2 && level == 1) {
            return "http://www.torrins.com/services/lesson/list?id="+id+"&children=0&required[]=instructor";
        } else if(lessonNo == 2 && level == 2) {
            return "http://www.torrins.com/services/lesson/list?id="+id+"&children=0&required[]=instructor";
        } else if(level == 3) {
            return "http://torrins-cdn.torrins.com/?service=streaming-secure&token=TmV3LVRvcnJpbnMvQmVnaW5uZXJzLVNlcmllcy9CZWdpbm5lcnMtU2VyaWVzLWJ5LUJvYmJ5L1Nlcmllcy0xL1ZpZGVvLTEtTWVkaXVtLUludHJvLXRvLUd1aXRhci1hbmQtQW5hdG9teS5tcDQ=";
        }
        return res;

    }


    public RSSPageParam getPageParams(int no) {

        RSSPageParam obj = null;
        if(no == 0 || no == 1 || no == 2 || no == 3) {
            obj = new RSSPageParam();
            obj.setPageTitle("Recent Articles");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            obj.setXmlHandler(1);
         return obj;
        } else  if(no == 4) {
            obj = new RSSPageParam();
            obj.setPageTitle("FDA Updates");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            obj.setXmlHandler(1);
            return obj;
        } else  if(no == 5) {
            obj = new RSSPageParam();
            obj.setPageTitle("MHRA Updates");
            obj.setPageUrl("https://www.gov.uk/government/announcements.atom?announcement_filter_option=news-stories&departments%5B%5D=medicines-and-healthcare-products-regulatory-agency");
            obj.setXmlHandler(2);
            return obj;
        } else  if(no == 6) {
            obj = new RSSPageParam();
            obj.setPageTitle("ICH Updates");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            return obj;
        } else  if(no == 7) {
            obj = new RSSPageParam();
            obj.setPageTitle("USP Updates");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            return obj;
        } else  if(no == 8) {
            obj = new RSSPageParam();
            obj.setPageTitle("EU Updates");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            return obj;
        } else  if(no == 9) {
            obj = new RSSPageParam();
            obj.setPageTitle("Clinical Trials");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            return obj;
        } else  if(no == 10) {
            obj = new RSSPageParam();
            obj.setPageTitle("FDA Warning Letters");
            obj.setPageUrl("http://feeds.pharmaguideline.com/pharmaguideline");
            return obj;
        }

        return null;
    }


    public String getURLForPage(int videoId) {
        return "http://www.torrins.com/services/lesson/video?id="+videoId;
    }

    public String getWebUrl(int pageNo) {

        String result = "";
        if(pageNo == 11) {
            result = "http://www.pharmaguideline.com/";
        }else if (pageNo == 12) {
            result = "http://www.pharmaguideline.com/2008/01/quality-assurance.html";
        } else if (pageNo == 13) {
            result = "http://www.pharmaguideline.com/2013/01/quality-control.html";
        } else if (pageNo == 14) {
            result = "http://www.pharmaguideline.com/2013/01/microbiology.html";
        } else if (pageNo == 15) {
            result = "http://www.pharmaguideline.com/2008/01/production.html";
        } else if (pageNo == 16) {
            result = "http://www.pharmaguideline.com/2008/01/pharma-sops.html";
        } else if (pageNo == 17) {
            result = "http://www.pharmaguideline.com/";
        } else if (pageNo == 18) {
            result = "http://www.pharmaguideline.com/";
        } else if (pageNo == 19) {
            result = "http://www.pharmaguideline.com/";
        } else if (pageNo == 20) {
            result = "http://www.pharmaguideline.com/2008/04/about.html";
        }


        return result;

    }

}
