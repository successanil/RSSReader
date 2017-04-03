package com.couriertracking.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.couriertracking.app.pojo.CourierHistoryListItem;
import com.ct.app.freewithads.R;

import java.util.ArrayList;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class HistorytListAdapter extends BaseAdapter{

    ArrayList<CourierHistoryListItem> mList;
    Context mContext;

    public HistorytListAdapter(ArrayList<CourierHistoryListItem> mList, Context mContext) {
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
            convertView = li.inflate(R.layout.history_list_item,null);
            viewholder.tvCourierName = (TextView)convertView.findViewById(R.id.couriername);
            viewholder.tvTrackNo = (TextView)convertView.findViewById(R.id.trackno);
            viewholder.tvDate = (TextView)convertView.findViewById(R.id.date);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder)convertView.getTag();
        }

        CourierHistoryListItem localObj = mList.get(position);
        viewholder.tvCourierName.setText(localObj.getmCourierName());
        viewholder.tvTrackNo.setText(localObj.getmCourierTrackNo());
        viewholder.tvDate.setText(viewholder.tvDate.getText()+localObj.getmLastSearchedDate());
        return convertView;
    }

    static class ViewHolder {
        TextView tvCourierName;
        TextView tvTrackNo;
        TextView tvDate;
    }

}
