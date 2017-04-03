package com.couriertracking.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.couriertracking.app.pojo.CourierListItem;
import com.ct.app.freewithads.R;

import java.util.ArrayList;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class CourierListAdapter extends BaseAdapter{

    ArrayList<CourierListItem> mList;
    Context mContext;

    public CourierListAdapter(ArrayList<CourierListItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if(convertView == null) {
            viewholder = new ViewHolder();
            LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.spinner_list_item,null);
            viewholder.tv = (TextView)convertView.findViewById(R.id.couriername);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder)convertView.getTag();
        }

        CourierListItem localObj = mList.get(position);
        viewholder.tv.setText(localObj.getmCourierName());

        return convertView;
    }

    static class ViewHolder {
        TextView tv;
    }

}
