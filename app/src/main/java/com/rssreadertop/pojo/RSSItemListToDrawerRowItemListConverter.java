package com.rssreadertop.pojo;

import java.util.ArrayList;

/**
 * Created by anilkukreti on 05/02/16.
 */
public class RSSItemListToDrawerRowItemListConverter {

    private ArrayList<DrawerRowItem> drawerRowItemArrayList;
    private ArrayList<IRSSItem> rssItemArrayList;

    public void setDrawerRowItemArrayList(ArrayList<DrawerRowItem> drawerRowItemArrayList) {
        this.drawerRowItemArrayList = drawerRowItemArrayList;
    }

    public void setRssItemArrayList(ArrayList<IRSSItem> rssItemArrayList) {
        this.rssItemArrayList = rssItemArrayList;
    }

    public ArrayList<DrawerRowItem> getDrawerRowItemArrayList() {
        for(IRSSItem rssItem : rssItemArrayList) {
            DrawerRowItem obj = new DrawerRowItem();
            if(rssItem instanceof RSSItem) {
                obj.setmTitle(rssItem.getTitle());
                obj.setmView(1);
            }else if (rssItem instanceof RSSItem2) {
                obj.setmTitle(rssItem.getTitle());
                obj.setmView(2);
            }
            drawerRowItemArrayList.add(obj);
        }
        return drawerRowItemArrayList;
    }
}
