package com.couriertracking.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.couriertracking.app.pojo.CourierListItem;
import com.ct.app.freewithads.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class CourierListAdapterFilter extends BaseAdapter implements Filterable {

    ArrayList<CourierListItem> mList;
    Context mContext;
    ArrayList<CourierListItem> mFilteredItems;

    private ItemFilter mFilter ;

    public CourierListAdapterFilter(ArrayList<CourierListItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        this.mFilteredItems = mList;
    }

    @Override
    public int getCount() {
        return mFilteredItems != null ? mFilteredItems.size() : 0 ;
    }

    @Override
    public Object getItem(int position) {
        return mFilteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        CourierListItem localObj = (CourierListItem)getItem(position);
        if(convertView == null) {
            viewholder = new ViewHolder();
            LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.spinner_list_item,null);
            viewholder.tv = (TextView)convertView.findViewById(R.id.couriername);
            viewholder.im = (ImageView)convertView.findViewById(R.id.imgView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder)convertView.getTag();
        }

        //CourierListItem localObj = mFilteredItems.get(position);
        viewholder.tv.setText(localObj.getmCourierName());

        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint != null ? constraint.toString().toLowerCase() : null;
            FilterResults results = new FilterResults();

            final List<CourierListItem> tempItems = new ArrayList<CourierListItem>();


            if(filterString != null) {

                for (CourierListItem localObj : mList) {
                    if (localObj.getmCourierName().toLowerCase().contains(filterString.toLowerCase())) {
                        tempItems.add(localObj);
                    }
                }
            }

            results.values = tempItems;
            results.count = tempItems.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.count > 0) {
                mFilteredItems = (ArrayList<CourierListItem>) results.values;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    static class ViewHolder {
        TextView tv;
        ImageView im;
    }

}
