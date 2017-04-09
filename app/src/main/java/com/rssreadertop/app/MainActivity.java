package com.rssreadertop.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rssreadertop.R;
import com.rssreadertop.pojo.DrawerHeaderItem;
import com.rssreadertop.pojo.DrawerRowItem;
import com.rssreadertop.pojo.RSSPageParam;
import com.rssreadertop.utility.MyAppUtility;

import java.util.ArrayList;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/*
   Developed by Relsell Global.
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> nameList;
    private int mSelectedFragment;


    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Relsell";
    String EMAIL = "relsell@relsellglobal.com";
    int PROFILE = R.mipmap.pharma_app_icon;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle


    ListView mList;
    ArrayList<String> mStringList;

    ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    ArrayList<DrawerRowItem> mDrawerRowItemList;

    public InterstitialAd mInterstitialAd;

    static String mDBPrefsfileName = "DBFile";

    /* Google Sign In vars declaration*/


    private static final int RC_SIGN_IN = 9001;

    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    // Is there a connection result resolution in progress
    private boolean mIsResolving = false;

    // Should we automatically resolve connection results when possible
    private boolean mShouldResolve = false;

    private GoogleApiClient mGoogleApiClient;

    private ViewPager mPager;

    LinearLayout mCustomLL;
    LinearLayout mCustomPanel;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_navigation_activity_main);
        //if (savedInstanceState == null) {
        mSelectedFragment = 0;
        //}
        addDummyDataListUIFrom();

        toolbar = (Toolbar) findViewById(R.id.pharma_tool_bar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size


        mAdapter = new NavigationAdapter(this, mDrawerHeaderItemList, mDrawerRowItemList);


        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        // mRecyclerView.addItemDecoration(new ListCustomDivider(this, R.drawable.listitem_divider));

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildLayoutPosition(child);
                    if (MyAppUtility.getInstance().ismDebugModeForBeta()) {
                        MyAppUtility.getInstance().showToastMsgForDB(MainActivity.this, "The Item Clicked is: " + position, false);
                    }
                    /*for (int i = 0; i < mDrawerRowItemList.size(); i++) {
                        DrawerRowItem obj = mDrawerRowItemList.get(i);
                        if (i + 1 == position) {
                            obj.setmActive(true);
                        } else {
                            obj.setmActive(false);
                        }
                    }

                    selectItem(position);
                    mAdapter.notifyDataSetChanged();*/


                    /*if(position == 3 ) {
                        DrawerRowItem obj1 = new DrawerRowItem();
                        obj1.setmActive(true);
                        obj1.setmTitle("MOBILE CHAT");
                        obj1.setmSectionHeader(false);
                        obj1.setmIcon(R.drawable.ss_icon);
                        mDrawerRowItemList.add(obj1);
                        mAdapter.notifyDataSetChanged();
                    }*/
                    selectItem(position);

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            /**
             * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
             * intercept touch events with
             * { ViewGroup#onInterceptTouchEvent(MotionEvent)}.
             *
             * @param disallowIntercept True if the child does not want the parent to
             *                          intercept touch events.
             *                          ViewParent#requestDisallowInterceptTouchEvent(boolean)
             */
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        initUI();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getFragmentManager().getBackStackEntryCount();
                if (count > 0) {
                    onBackPressed();
                } else {
                    Drawer.openDrawer(Gravity.LEFT);
                }
            }
        });


       /* mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.test_interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);*/





    }

    public void syncActionBarArrowState() {
        int backStackEntryCount =
                getSupportFragmentManager().getBackStackEntryCount();
        mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }



    private void initUI() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mCustomLL = (LinearLayout) findViewById(R.id.ll);
        mCustomPanel = (LinearLayout) findViewById(R.id.mycustomPanel);
        mPager.setVisibility(View.GONE);
        mCustomLL.setVisibility(View.GONE);
        mCustomPanel.setVisibility(View.VISIBLE);


        addDummyDataToDB();
        selectItem(mSelectedFragment);
    }

    public void addDummyDataListUIFrom() {
        mDrawerHeaderItemList = new ArrayList<DrawerHeaderItem>();
        mDrawerRowItemList = new ArrayList<DrawerRowItem>();
        //MyAppUtility.getInstance().setmDrawerHeaderItemList(mDrawerHeaderItemList);
        //MyAppUtility.getInstance().setmDrawerRowItemList(mDrawerRowItemList);
        DrawerHeaderItem obj = new DrawerHeaderItem();
        obj.setName("Relsell Global");
        obj.setEmail("relsellglobal@gmail.com");
        obj.setProfile(R.mipmap.pharma_app_icon);
        mDrawerHeaderItemList.add(obj);


        DrawerRowItem obj1 = new DrawerRowItem();
        obj1.setmActive(true);
        obj1.setmTitle("REGULARS");
        obj1.setmSectionHeader(true);
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmActive(true);
        obj1.setmTitle("Recent Articles");
        obj1.setmSectionHeader(false);
        obj1.setmIcon(R.mipmap.icon_one);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmActive(false);
        obj1.setmSectionHeader(false);
        obj1.setmTitle("News Update");
        obj1.setmIcon(R.mipmap.icon_two);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmActive(false);
        obj1.setmSectionHeader(false);
        obj1.setmTitle("FDA Updates");
        obj1.setmIcon(R.mipmap.icon_three);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("MHRA Updates");
        obj1.setmIcon(R.mipmap.icon_four);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("USP Updates");
        obj1.setmIcon(R.mipmap.icon_five);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("EU Updates");
        obj1.setmIcon(R.mipmap.icon_six);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Clinical Trials");
        obj1.setmIcon(R.mipmap.icon_seven);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("FDA Warning Letters");
        obj1.setmIcon(R.mipmap.icon_eight);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmActive(true);
        obj1.setmTitle("SECTION H");
        obj1.setmSectionHeader(true);
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Guidance");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Home");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Quality Assurance");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Quality Control");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Microbiology");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Production");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("SOPs");
        obj1.setmIcon(R.mipmap.pharma_app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Search");
        obj1.setmIcon(R.mipmap.icon_eleven);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Ask Us");
        obj1.setmIcon(R.mipmap.icon_twelve);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("About Us");
        obj1.setmIcon(R.mipmap.about_us);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmActive(true);
        obj1.setmTitle("SECTION H");
        obj1.setmSectionHeader(true);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setShowIcon(false);
        obj1.setmTitle("Social");
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Facebook");
        obj1.setmIcon(R.mipmap.facebook_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Twitter");
        obj1.setmIcon(R.mipmap.twitter_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Youtube");
        obj1.setmIcon(R.mipmap.youtube_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmActive(true);
        obj1.setmTitle("SECTION H");
        obj1.setmSectionHeader(true);
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Explore");
        obj1.setmIcon(R.mipmap.ww_seven);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Last 24 Hours");
        obj1.setmIcon(R.mipmap.ww_six);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("All Unreads");
        obj1.setmIcon(R.mipmap.fire_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("All Stars");
        obj1.setmIcon(R.mipmap.ww_five);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Downloads");
        obj1.setmIcon(R.mipmap.ww_four);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Sync Now");
        obj1.setmIcon(R.mipmap.ww_three);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Switch Theme");
        obj1.setmIcon(R.mipmap.ww_two);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Help");
        obj1.setmIcon(R.mipmap.ww_one);
        mDrawerRowItemList.add(obj1);


    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList getDataForSlidesTShow() {
        addDummyDataToDB();
        ArrayList<String> arrayList = getDataFromDB();
        return arrayList;
    }
    */
    public void addDummyDataToDB() {
        // dummy data for home & settings fragment

       /* String key = "dbcreated";
        String cookieValue = "cookievalue";


        String value = MyAppUtility.getInstance().getDataFromPefs(mDBPrefsfileName, key);
        MyAppUtility.getInstance().setmUserName("tillu");
        MyAppUtility.getInstance().setmUserPhoneNo("7838830735");

        if(value == null || value.equalsIgnoreCase("false")) {

            ArrayList<HomePageListItem> arrayList = new ArrayList<HomePageListItem>();

            arrayList.add(addDummyDataDBHelper("Tillu","/storage/emulated/0/WhatsApp/Profile Pictures/919760890735.jpg", "8", "tillu", "1"));
            arrayList.add(addDummyDataDBHelper("Kittu","/storage/emulated/0/WhatsApp/Profile Pictures/918586963348.jpg", "8", "tillu", "1"));
            arrayList.add(addDummyDataDBHelper("Golu","/storage/emulated/0/WhatsApp/Media/WhatsApp Images/2015-10-10 11.12.06.png", "8", "tillu",
                    "1"));
            arrayList.add(addDummyDataDBHelper("Aadi","/storage/emulated/0/WhatsApp/Profile Pictures/918859037749.jpg", "8", "tillu", "1"));
            arrayList.add(addDummyDataDBHelper("Sanu","/storage/emulated/0/WhatsApp/Profile Pictures/919760890735.jpg", "8", "tillu", "1"));
            arrayList.add(addDummyDataDBHelper("Ishu","/storage/emulated/0/WhatsApp/Profile Pictures/919760890735.jpg", "8", "tillu", "1"));


            for (HomePageListItem obj : arrayList) {
                ContentValues values = new ContentValues();
                values.put(SlidyProvider.NAME, obj.getName());
                values.put(SlidyProvider.SENDER_PROFILE_PIC,obj.getmSenderProfilePicPath());
                values.put(SlidyProvider.IMAGECOUNT, obj.getImageCount());
                values.put(SlidyProvider.FOLDERNAME, obj.getFolderName());
                values.put(SlidyProvider.GRADE, obj.getGrade());
                Uri uri = getContentResolver().insert(SlidyProvider.CONTENT_URI,
                        values);
                MyAppUtility.getInstance().showToastMsgForDB(getBaseContext(), uri.toString(),false);
            }

            ////////////////////////////////////////////////////////////////////////////


            ArrayList<ConnectionItem> arrayList2 = new ArrayList<ConnectionItem>();

            arrayList2.add(addDummyConnectionDataDBHelper("Tillu", "7838830735", "Kittu", "9582303659"));
            arrayList2.add(addDummyConnectionDataDBHelper("Tillu", "7838830735", "Kittu", "9582303659"));
            arrayList2.add(addDummyConnectionDataDBHelper("Tillu", "7838830735", "Kittu", "9582303659"));
            arrayList2.add(addDummyConnectionDataDBHelper("Tillu", "7838830735", "Kittu", "9582303659"));
            arrayList2.add(addDummyConnectionDataDBHelper("Tillu", "7838830735", "Kittu", "9582303659"));


            for (ConnectionItem obj : arrayList2) {
                ContentValues values = new ContentValues();
                values.put(SlidyProvider.UserConnections.USERNAME, obj.getmUsername());
                values.put(SlidyProvider.UserConnections.USERPHONENO, obj.getmUserPhoneno());
                values.put(SlidyProvider.UserConnections.CONNECTIONNAME, obj.getmConnectionName());
                values.put(SlidyProvider.UserConnections.CONNECTIONPHONENO, obj.getmConnectionPhoneNo());
                Uri uri = getContentResolver().insert(SlidyProvider.UserConnections.CONTENT_URI,
                        values);
                MyAppUtility.getInstance().showToastMsgForDB(getBaseContext(), uri.toString(),false);
            }*/


        // dummy data for uploadslide fragment

            /*ArrayList<SlideShareListItem> arrayList1 = new ArrayList<SlideShareListItem>();



            arrayList1.add(addDummyShareSlideDataDBHelper("tillu","golu","78388300", "0", "78383838"));
            arrayList1.add(addDummyShareSlideDataDBHelper("tillu","golu","78388300", "1", "78383838"));
            arrayList1.add(addDummyShareSlideDataDBHelper("tillu","golu","78388300", "0", "78383838"));


            for (SlideShareListItem obj : arrayList1) {
                ContentValues values = new ContentValues();
                values.put(SlidyProvider.ShareSlide.FOLDERNAME, obj.getmFolderName());
                values.put(SlidyProvider.ShareSlide.USERNAME, obj.getmUsername());
                values.put(SlidyProvider.ShareSlide.USERPHONENO, obj.getmPhoneno());
                values.put(SlidyProvider.ShareSlide.DRAFT, obj.getmDraft());
                values.put(SlidyProvider.ShareSlide.SHAREDTO, obj.getmSharedTo());
                Uri uri = getContentResolver().insert(SlidyProvider.ShareSlide.CONTENT_URI,
                        values);
                MyAppUtility.getInstance().showToastMsgForDB(getBaseContext(), uri.toString(),false);
            }*/

        ////////////////////////////////////////////////////////////////////////////////////

            /*ArrayList<SlideShareImagesWithItem> arrayList2 = new ArrayList<SlideShareImagesWithItem>();

            arrayList2.add(addDummyShareSlideImagesDataDBHelper("golu","78388300", "/storage/emulated/0/tmp_avatar_1435829864564.jpg"));
            arrayList2.add(addDummyShareSlideImagesDataDBHelper("golu", "78388300", "/storage/emulated/0/tmp_avatar_1435829864564.jpg"));


            for (SlideShareImagesWithItem obj : arrayList2) {
                ContentValues values = new ContentValues();
                values.put(SlidyProvider.ShareSlideImages.USERNAME, obj.getmUsername());
                values.put(SlidyProvider.ShareSlideImages.USERPHONENO, obj.getmPhoneNo());
                values.put(SlidyProvider.ShareSlideImages.IMGPATH, obj.getmImageSrc());
                Uri uri = getContentResolver().insert(SlidyProvider.ShareSlideImages.CONTENT_URI,
                        values);
                MyAppUtility.getInstance().showToastMsgForDB(getBaseContext(), uri.toString(),false);
            }
            */
        //MyAppUtility.getInstance().writeToPrefs(mDBPrefsfileName, key, "true");
        //MyAppUtility.getInstance().writeToPrefs(mDBPrefsfileName, cookieValue, "_memory=35c68340d3059352bd65edfbe0fc3dfb");
        // }
    }


    private void selectItem(int position) {
        Bundle args = new Bundle();
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                toolbar.setVisibility(View.VISIBLE);
                mCustomPanel.setVisibility(View.VISIBLE);
                mPager.setVisibility(View.GONE);
                mCustomLL.setVisibility(View.GONE);
                RSSPageParam obj = MyAppUtility.getInstance().getPageParams(position);
                if (obj != null) {
                    args.putString("pageTitle", obj.getPageTitle());
                    args.putString("url", obj.getPageUrl());
                    args.putInt("xmlHandler", obj.getXmlHandler());
                    callFragment(0, position);
                }
                Drawer.closeDrawer(Gravity.LEFT);
                break;
            case 6:
            case 7:
            case 8:
                toolbar.setVisibility(View.VISIBLE);
                mCustomPanel.setVisibility(View.VISIBLE);
                mPager.setVisibility(View.GONE);
                mCustomLL.setVisibility(View.GONE);
                RSSPageParam obj1 = MyAppUtility.getInstance().getPageParams(position);
                callFragment(0, position);
                Drawer.closeDrawer(Gravity.LEFT);
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 20:
                toolbar.setVisibility(View.VISIBLE);
                mCustomPanel.setVisibility(View.VISIBLE);
                mPager.setVisibility(View.GONE);
                mCustomLL.setVisibility(View.GONE);
                callFragment(2, position);
                Drawer.closeDrawer(Gravity.LEFT);

            break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    public void drawTabIndicatorView(int position, int noOfTabs) {
        // mCustomLL.setTabInditor(position, noOfTabs);
        mCustomLL.invalidate();
    }


    public void callFragment(int fragmentno, int position) {
        if (fragmentno == 0) {
            Bundle args = new Bundle();
            RSSPageParam obj = MyAppUtility.getInstance().getPageParams(position);
            if ((obj != null && position == 0) || (obj != null && position == 1) || (obj != null && position == 2) || (obj != null && position == 3) || (obj != null && position == 4) || (obj != null && position == 5)) {
                args.putString("pageTitle", obj.getPageTitle());
                args.putString("url", obj.getPageUrl());
                args.putInt("xmlHandler", obj.getXmlHandler());
            } else if (obj != null && position == 8) {
                args.putString("pageTitle", "ICH Updates");
                args.putString("url", "");
                args.putInt("xmlHandler", -1);
            }
            FirstSectionFragment.createorFindFragment(getFragmentManager(), R.id.mycustomPanel, args);
        } else if (fragmentno == 1) {
            Bundle args = new Bundle();
            RSSPageParam obj = MyAppUtility.getInstance().getPageParams(position);
            if (obj != null) {
                args.putString("pageTitle", obj.getPageTitle());
                args.putString("url", obj.getPageUrl());
                args.putInt("xmlHandler", obj.getXmlHandler());
            }
            //FirstSectionFragmentDetail.createorFindFragment(getFragmentManager(), R.id.mycustomPanel, args,mDrawerToggle);
        } else if (fragmentno == 2) {
            Bundle args = new Bundle();
            String weburl = MyAppUtility.getInstance().getWebUrl(position);
            args.putString("webUrl",weburl);
            FirstSectionFragmentWebView.createorFindFragment(getFragmentManager(), R.id.mycustomPanel, args);
        }

    }



    @Override
    public void onBackPressed() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        mDrawerToggle.syncState();
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }


}