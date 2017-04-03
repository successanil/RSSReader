package com.couriertracking.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.couriertracking.app.pojo.CourierListItem;
import com.ct.app.freewithads.R;

/**
 * Created by Relsell Global on 13/10/15.
 */
public class HomeFragment extends Fragment {


    private static HomeFragment shomefragment;
    public static final String HOME_FRAGMENT_TAG="homefragment";




    public static HomeFragment createorFindFragment(FragmentManager fm,int containerLayoutID,Bundle args) {
        shomefragment = (HomeFragment) fm.findFragmentByTag(HOME_FRAGMENT_TAG);
        if (shomefragment == null) {
            shomefragment = new HomeFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerLayoutID, shomefragment);
        }
        return shomefragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.home_fragment, container);

    }





}
