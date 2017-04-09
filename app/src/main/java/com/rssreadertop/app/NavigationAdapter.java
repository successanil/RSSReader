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
public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_ITEM_NO_ICON = 101;
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
        ImageView profile;
        TextView Name;
        TextView email;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);


            if (ViewType == TYPE_ITEM_ACTIVE) {

                if(itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout)itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout)itemView.findViewById(R.id.ll);
                }

                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = TYPE_ITEM_ACTIVE;
            } else if (ViewType == TYPE_ITEM) {
                if(itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout)itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout)itemView.findViewById(R.id.ll);
                }
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = TYPE_ITEM;
            }  else if (ViewType == TYPE_ITEM_NO_ICON) {
                if(itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout)itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout)itemView.findViewById(R.id.ll);
                }
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = TYPE_ITEM;
            } else if (ViewType == TYPE_HEADER) {


                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = TYPE_HEADER;                                                // Setting holder id = 0 as the object being populated are of type header view
            } else if (ViewType == TYPE_ITEM_SIGNIN) {
                if(itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout)itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout)itemView.findViewById(R.id.ll);
                }
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.circleView);

                Holderid = TYPE_ITEM_SIGNIN;
            } else if (ViewType == TYPE_ITEM_SECTION_HEADER) {
                if(itemView.findViewById(R.id.ll) instanceof RelativeLayout) {
                    ll = (RelativeLayout)itemView.findViewById(R.id.ll);
                } else if (itemView.findViewById(R.id.ll) instanceof LinearLayout) {
                    linearll = (LinearLayout)itemView.findViewById(R.id.ll);
                }
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = TYPE_ITEM_SECTION_HEADER;
            }
        }

    }


    NavigationAdapter(String Titles[], int Icons[], String Name, String Email, int Profile) {
        mNavTitles = Titles;
        mIcons = Icons;
        name = Name;
        email = Email;
        profile = Profile;


    }

    NavigationAdapter(Context context, ArrayList<DrawerHeaderItem> drawerHeaderItemList, ArrayList<DrawerRowItem> drawerRowItemList) {

        mDrawerHeaderItemList = drawerHeaderItemList;
        mDrawerRowItemList = drawerRowItemList;
        mContext  = context;

    }


    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM_ACTIVE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        }  else if (viewType == TYPE_ITEM_NO_ICON) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_navigation_item_row_no_icon, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view


            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_navigation_header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader;


        } else if (viewType == TYPE_ITEM_SIGNIN) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_navigation_item_row, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader;


        } else if (viewType == TYPE_ITEM_SECTION_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharma_navigation_section_header, parent, false); //Inflating the layout

            ViewHolder vSectionHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vSectionHeader;


        }
        return null;

    }


    @Override
    public void onBindViewHolder(NavigationAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == (TYPE_ITEM_ACTIVE) || holder.Holderid == (TYPE_ITEM_SIGNIN)) {

            DrawerRowItem localObj = mDrawerRowItemList.get(position - 1);
            if(!localObj.ismSectionHeader()) {
                if(holder.ll != null) {
                    //holder.ll.setBackground(mContext.getResources().getDrawable(R.drawable.bg_drawer_item_drawable_focus));
                } else if (holder.linearll != null) {
                    //holder.linearll.setBackground(mContext.getResources().getDrawable(R.drawable.bg_drawer_item_drawable_focus));
                }

                holder.textView.setText(localObj.getmTitle());
                holder.imageView.setImageResource(localObj.getmIcon());
                if (holder.Holderid == (TYPE_ITEM_SIGNIN)) {

                }
            }
        } else if (holder.Holderid == TYPE_ITEM) {

            DrawerRowItem localObj = mDrawerRowItemList.get(position - 1);
            if(!localObj.ismSectionHeader()) {
                if(holder.ll != null) {
                    //holder.ll.setBackground(mContext.getResources().getDrawable(R.drawable.bg_drawer_item_drawable));
                } else if (holder.linearll != null) {
                    //holder.linearll.setBackground(mContext.getResources().getDrawable(R.drawable.bg_drawer_item_drawable));
                }


                holder.textView.setText(localObj.getmTitle());
                holder.imageView.setImageResource(localObj.getmIcon());
                if (holder.Holderid == (TYPE_ITEM_SIGNIN)) {

                }
            }
        } else if (holder.Holderid == TYPE_ITEM_NO_ICON) {

            DrawerRowItem localObj = mDrawerRowItemList.get(position - 1);
            if(!localObj.ismSectionHeader()) {
                holder.textView.setText(localObj.getmTitle());
            }
        } else if(holder.Holderid == TYPE_HEADER) {


        } else if(holder.Holderid == TYPE_ITEM_SECTION_HEADER) {

        }
    }

    @Override
    public int getItemCount() {
        return mDrawerHeaderItemList.size() + mDrawerRowItemList.size();
    }


    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                DrawerRowItem obj = mDrawerRowItemList.get(position-1);
                if(obj.ismSectionHeader()) {
                    return TYPE_ITEM_SECTION_HEADER;
                }else {
                    return obj.isShowIcon() ? TYPE_ITEM : TYPE_ITEM_NO_ICON;
                }
        }
    }

}