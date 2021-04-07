package com.quadrant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.quadrant.govolt.R;

public class bottomsheetfragment extends BottomSheetDialogFragment {

    public static bottomsheetfragment newInstance() {
        return new bottomsheetfragment();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.curve, container, false);

        // get the views and attach the listener

        return view;

    }
}
