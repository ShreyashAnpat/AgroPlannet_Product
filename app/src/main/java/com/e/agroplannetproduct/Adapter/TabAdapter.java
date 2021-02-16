package com.e.agroplannetproduct.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.e.agroplannetproduct.tabApp.DeliveredOrder;
import com.e.agroplannetproduct.tabApp.PendingOrder;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    int totalTabs;

    public TabAdapter(@NonNull FragmentManager fm,int behavior, int tabCount) {
        super(fm, behavior);
        this.totalTabs = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new PendingOrder();
            case 1 :
                return new DeliveredOrder();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
