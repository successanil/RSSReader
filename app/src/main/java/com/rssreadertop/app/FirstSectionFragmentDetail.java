package com.rssreadertop.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rssreadertop.R;
import com.rssreadertop.pojo.DrawerHeaderItem;
import com.rssreadertop.pojo.DrawerRowItem;
import com.rssreadertop.pojo.IRSSItem;
import com.rssreadertop.pojo.RSSItem;
import com.rssreadertop.pojo.RSSItem2;
import com.rssreadertop.pojo.RSSItemListToDrawerRowItemListConverter;
import com.rssreadertop.rssreader.IRSSHandler;
import com.rssreadertop.rssreader.RSSHandleXml;
import com.rssreadertop.rssreader.RSSHandleXml2;

import java.util.ArrayList;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class FirstSectionFragmentDetail extends Fragment {


    private static FirstSectionFragmentDetail shomefragment;
    public static final String HOME_FRAGMENT_TAG = "firstsectionfragmentdetail";


    ListView mList;
    ArrayList<IRSSItem> mItemList;
    int mSelectedItem;
    ArrayList<String> mStringList;

    ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    ArrayList<DrawerRowItem> mDrawerRowItemList;


    private AdView mAdView;
    private AdRequest mAdsRequest;
    private String mLogTagAds = "ADS_TAG";


    private String finalUrl = "";
    private String pageTitle = "";
    private IRSSHandler obj;
    private int xmlHandler;
    private LinearLayout mLinearLayoutForNoFeed;

    private RelativeLayout mRootLayout;
    private Button btn;
    private String linkUrl;
    private String headingText;
    private String descText;
    private String writtenDate;
    TextView headingTv;
    TextView descTv;
    TextView dateTv;




    Handler mHandlerForRSSData = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                RSSItemListToDrawerRowItemListConverter localObj = new RSSItemListToDrawerRowItemListConverter();
                localObj.setDrawerRowItemArrayList(mDrawerRowItemList);
                localObj.setRssItemArrayList(mItemList);
                mDrawerRowItemList = localObj.getDrawerRowItemArrayList();

            }

        }
    };


    public void setRSSItemList(ArrayList<IRSSItem> itemList,int selectedPosition) {
        this.mItemList = itemList;
        this.mSelectedItem = selectedPosition;
    }


    public static FirstSectionFragmentDetail createorFindFragment(FragmentManager fm, int containerLayoutID, Bundle args) {
        shomefragment = (FirstSectionFragmentDetail) fm.findFragmentByTag(HOME_FRAGMENT_TAG);
        if (shomefragment == null) {
            shomefragment = new FirstSectionFragmentDetail();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerLayoutID, shomefragment);
            ft.addToBackStack(null);
            //acd.setDrawerIndicatorEnabled(false);
            //acd.setHomeAsUpIndicator(R.mipmap.app_icon);
            shomefragment.setArguments(args);
            ft.commit();
        }
        return shomefragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.pharma_first_section_detail_fragment, null);
        mRootLayout = (RelativeLayout) v.findViewById(R.id.rootLayout);
        btn = (Button)v.findViewById(R.id.visitws_btn);
        headingTv = (TextView)v.findViewById(R.id.headingText);
        dateTv = (TextView)v.findViewById(R.id.written_date);
        descTv = (TextView)v.findViewById(R.id.descText);

        mAdView = (AdView) v.findViewById(R.id.adView);


        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(mItemList != null) {
            IRSSItem ir = mItemList.get(mSelectedItem);
            linkUrl = ir.getLink();
            if(ir instanceof RSSItem) {
                descText = ((RSSItem)ir).getDescription();
            }else if (ir instanceof RSSItem2) {
                descText = ((RSSItem2)ir).getSummary();
            }
            headingText = ir.getTitle();
            writtenDate = ir.getPubDate();

            headingTv.setText(headingText);
            dateTv.setText(writtenDate);
            descTv.setText(Html.fromHtml(descText));
            descTv.setMovementMethod(LinkMovementMethod.getInstance());

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle args = new Bundle();
                args.putString("webUrl",linkUrl);
                FirstSectionFragmentWebView.createorFindFragment(getFragmentManager(), R.id.mycustomPanel, args);*/

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkUrl));
                startActivity(i);

            }
        });


        /*if (((MainActivity) getActivity()).mInterstitialAd.isLoaded()) {
            ((MainActivity) getActivity()).mInterstitialAd.show();
        }*/


        //((MainActivity)getActivity()).syncActionBarArrowState();


       /* //addDummyDataListUIFrom();
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



                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            *//**
         * Called when a child of RecyclerView does not want RecyclerView and its ancestors to
         * intercept touch events with
         * { ViewGroup#onInterceptTouchEvent(MotionEvent)}.
         *
         * @param disallowIntercept True if the child does not want the parent to
         *                          intercept touch events.
         *                          ViewParent#requestDisallowInterceptTouchEvent(boolean)
         *//*
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/


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
        if (!finalUrl.equalsIgnoreCase("")) {
            if (xmlParser == 1) {
                obj = new RSSHandleXml(finalUrl, mItemList, mHandlerForRSSData);
            } else if (xmlParser == 2) {
                obj = new RSSHandleXml2(finalUrl, mItemList, mHandlerForRSSData);
            }
            obj.fetchXML();
        }
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
