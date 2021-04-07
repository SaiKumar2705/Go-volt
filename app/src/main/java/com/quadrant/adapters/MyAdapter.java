package com.quadrant.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.quadrant.govolt.Pacchetti_Display_Fragment;
import com.quadrant.govolt.Wallet_Display_Fragment;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {

        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Toast.makeText(myContext, "This is my Toast message!", Toast.LENGTH_LONG).show();
                Wallet_Display_Fragment wallet_display_fragment = new Wallet_Display_Fragment();
                return wallet_display_fragment;
            case 1:
                Pacchetti_Display_Fragment pacchetti_display_fragment = new Pacchetti_Display_Fragment();
                return pacchetti_display_fragment;

            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}