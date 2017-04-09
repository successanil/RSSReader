package com.rssreadertop.app;

/**
 * Created by anil on 10/7/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.rssreadertop.R;
import com.rssreadertop.pojo.DrawerHeaderItem;
import com.rssreadertop.pojo.DrawerRowItem;

import java.util.ArrayList;

/**
 * Created by relsell global on 28-12-2014.
 */
public class FirstSectionAdapter extends RecyclerView.Adapter<FirstSectionAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM_N = 1;
    private static final int TYPE_ITEM_W = 101;
    private static final int TYPE_ITEM_SIGNIN = 11;
    private static final int TYPE_ITEM_ACTIVE = 2;
    private static final int TYPE_ITEM_SECTION_HEADER = 3;

    private String mNavTitles[];
    private int mIcons[];

    private String name;
    private int profile;
    private String email;
    private ArrayList<DrawerHeaderItem> mDrawerHeaderItemList;
    private ArrayList<DrawerRowItem> mDrawerRowItemList;
    private Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;
        RelativeLayout ll;
        LinearLayout linearll;
        TextView textView;
        ImageView imageView;
        TextView titleText;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);


            if (ViewType == TYPE_ITEM_ACTIVE) {
                titleText = (TextView) itemView.findViewById(R.id.titleText);
                imageView = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = TYPE_ITEM_ACTIVE;
            } else if (ViewType == TYPE_ITEM_N) {
                if (itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout) itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout) itemView.findViewById(R.id.ll);
                }
                titleText = (TextView) itemView.findViewById(R.id.titleText);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = TYPE_ITEM_N;
            } else if (ViewType == TYPE_ITEM_W) {
                if (itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout) itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout) itemView.findViewById(R.id.ll);
                }
                titleText = (TextView) itemView.findViewById(R.id.titleText);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = TYPE_ITEM_W;
            }
        }

    }


    FirstSectionAdapter(String Titles[], int Icons[], String Name, String Email, int Profile) {
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
        email = Email;
        profile = Profile;


    }

    FirstSectionAdapter(Context context, ArrayList<DrawerHeaderItem> drawerHeaderItemList, ArrayList<DrawerRowItem> drawerRowItemList) {

        mDrawerHeaderItemList = drawerHeaderItemList;
        mDrawerRowItemList = drawerRowItemList;
        mContext = context;

    }


    @Override
    public FirstSectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM_ACTIVE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_first_section_navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        } else if (viewType == TYPE_ITEM_N) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_first_section_navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        } else if (viewType == TYPE_ITEM_W) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_first_section_navigation_item_row_w, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_first_section_navigation_header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader;


        } else if (viewType == TYPE_ITEM_SIGNIN) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_first_section_navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader;


        } else if (viewType == TYPE_ITEM_SECTION_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_first_section_navigation_section_header, parent, false); //Inflating the layout

            ViewHolder vSectionHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vSectionHeader;


        }
        return null;

    }


    @Override
    public void onBindViewHolder(FirstSectionAdapter.ViewHolder holder, int position) {

        DrawerRowItem localObj = mDrawerRowItemList.get(position);
        if (holder.titleText != null) {
            holder.titleText.setText(localObj.getmTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerHeaderItemList.size() + mDrawerRowItemList.size();
    }


    @Override
    public int getItemViewType(int position) {

        switch (position) {
            default:
                DrawerRowItem obj = mDrawerRowItemList.get(position);
                if (obj.ismSectionHeader()) {
                    return TYPE_ITEM_SECTION_HEADER;
                } else {
                    return obj.getmView() == 1 ? TYPE_ITEM_N : TYPE_ITEM_W;
                }
        }
    }

}