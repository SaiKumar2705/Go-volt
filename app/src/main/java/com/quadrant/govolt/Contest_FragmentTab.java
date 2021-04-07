package com.quadrant.govolt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Contest_FragmentTab extends Fragment {
    private Context context;
    private View view;
    private AlertDialog alertDialog;

    public Contest_FragmentTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.contest_tab_layout, container, false);
        context = this.getActivity();


        return  view;
    }

}