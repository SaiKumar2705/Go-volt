package com.quadrant.govolt;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.widget.TextView;

import com.quadrant.govolt.Others.TypefaceUtil;

public class MyApplication extends Application {

    private Typeface normalFont;
    private Typeface boldFont;




    @Override
    public void onCreate() {
        super.onCreate();


        TypefaceUtil.overrideFont(getApplicationContext(), "normal", "fonts/TruenoRg.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

    }


    public  void setTypeface(TextView textView) {
        if(textView != null) {
            if(textView.getTypeface() != null && textView.getTypeface().isBold()) {
                textView.setTypeface(getBoldFont());
            } else {
                textView.setTypeface(getNormalFont());
            }
        }
    }

    private Typeface getNormalFont() {
        if(normalFont == null) {
            normalFont = Typeface.createFromAsset(getAssets(),"fonts/TruenoRg.ttf");
        }
        return this.normalFont;
    }

    private Typeface getBoldFont() {
        if(boldFont == null) {
            boldFont = Typeface.createFromAsset(getAssets(),"fonts/TruenoRg.ttf");
        }
        return this.boldFont;
    }

}