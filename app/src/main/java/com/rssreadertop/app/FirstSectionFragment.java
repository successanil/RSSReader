package com.rssreadertop.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ct.app.freewithads.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rssreadertop.pojo.DrawerHeaderItem;
import com.rssreadertop.pojo.DrawerRowItem;
import com.rssreadertop.pojo.IRSSItem;
import com.rssreadertop.pojo.RSSItemListToDrawerRowItemListConverter;
import com.rssreadertop.rssreader.IRSSHandler;
import com.rssreadertop.rssreader.RSSHandleXml;
import com.rssreadertop.rssreader.RSSHandleXml2;

import java.util.ArrayList;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class FirstSectionFragment extends Fragment {


    private static FirstSectionFragment shomefragment;
    public static final String HOME_FRAGMENT_TAG="firstsectionfragment";


    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager


    ListView mList;
    ArrayList<IRSSItem> mItemList;
    ArrayList<String> mStringList;

    ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    ArrayList<DrawerRowItem> mDrawerRowItemList;


    private AdView mAdView;
    private AdRequest mAdsRequest;
    private String mLogTagAds = "ADS_TAG";



    private String finalUrl="https://www.taxmann.com/rss/news.ashx";
    private String pageTitle = "";
    private IRSSHandler obj;
    private int xmlHandler;
    private LinearLayout mLinearLayoutForNoFeed;

    Handler mHandlerForRSSData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                RSSItemListToDrawerRowItemListConverter localObj = new RSSItemListToDrawerRowItemListConverter();
                localObj.setDrawerRowItemArrayList(mDrawerRowItemList);
                localObj.setRssItemArrayList(mItemList);
                mDrawerRowItemList = localObj.getDrawerRowItemArrayList();
                if(mDrawerRowItemList != null && mDrawerRowItemList.size() != 0) {
                    mLinearLayoutForNoFeed.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }

        }
    };




    public static FirstSectionFragment createorFindFragment(FragmentManager fm,int containerLayoutID,Bundle args) {
        shomefragment = (FirstSectionFragment) fm.findFragmentByTag(HOME_FRAGMENT_TAG);
        if (shomefragment == null) {
            shomefragment = new FirstSectionFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerLayoutID, shomefragment);
            shomefragment.setArguments(args);
            ft.commit();
        }
        return shomefragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.pharma_first_section_fragment,null);

        mAdView = (AdView)v.findViewById(R.id.adView);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        mLinearLayoutForNoFeed = (LinearLayout)v.findViewById(R.id.no_feed_available_layout);



        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //addDummyDataListUIFrom();
        setRetainInstance(true);
        finalUrl = getArguments().getString("url");
        pageTitle = getArguments().getString("pageTitle");
        xmlHandler = getArguments().getInt("xmlHandler");




         // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        loadUIFromServer(xmlHandler);


        //mAdapter = new FirstSectionAdapter(getActivity(), mDrawerHeaderItemList, mDrawerRowItemList);


        //mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        // mRecyclerView.addItemDecoration(new ListCustomDivider(this, R.drawable.listitem_divider));

        mLayoutManager = new LinearLayoutManager(getActivity());                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

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
                   // getFragmentManager().beginTransaction().remove(FirstSectionFragment.this).commit();
                    FirstSectionFragmentDetail fSD = FirstSectionFragmentDetail.createorFindFragment(getFragmentManager(), R.id.mycustomPanel, null);

                    fSD.setRSSItemList(mItemList,position);
                    fSD.setRSSItemList(mItemList,position);
                    //((MainActivity)getActivity()).callFragment(1, position);
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



        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
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


    public void loadUIFromServer(int xmlParser) {
        mItemList = new ArrayList();
        mDrawerHeaderItemList = new ArrayList<DrawerHeaderItem>();
        mDrawerRowItemList = new ArrayList<DrawerRowItem>();
        if(!finalUrl.equalsIgnoreCase("")) {
            if (xmlParser == 1) {
                obj = new RSSHandleXml(finalUrl, mItemList, mHandlerForRSSData);
            } else if (xmlParser == 2) {
                obj = new RSSHandleXml2(finalUrl, mItemList, mHandlerForRSSData);
            }
            obj.fetchXML();
        }
        mAdapter = new FirstSectionAdapter(getActivity(), mDrawerHeaderItemList, mDrawerRowItemList);
        mRecyclerView.setAdapter(mAdapter);
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
        //mDrawerHeaderItemList.add(obj);


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
        obj1.setmIcon(R.mipmap.app_icon);
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
        obj1.setmTitle("Social");
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Facebook");
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);

        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Twitter");
        obj1.setmIcon(R.mipmap.app_icon);
        mDrawerRowItemList.add(obj1);


        obj1 = new DrawerRowItem();
        obj1.setmSectionHeader(false);
        obj1.setmActive(false);
        obj1.setmTitle("Youtube");
        obj1.setmIcon(R.mipmap.app_icon);
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
        obj1.setmIcon(R.mipmap.app_icon);
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



}