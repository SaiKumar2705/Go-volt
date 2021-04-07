package com.quadrant.govolt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Premium_Activity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private ImageView menu;
    private  RelativeLayout first_rr;
    private RelativeLayout second_rr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium_menu);
        context = this;

        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Premium");

        ImageView back  = (ImageView)findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setUpView();


    }

    private void setUpView() {
         first_rr = (RelativeLayout)findViewById(R.id.rrlayout);
         second_rr = (RelativeLayout)findViewById(R.id.rr_lay_two);

        first_rr.setOnClickListener(this);
        second_rr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rrlayout:
                Intent in = new Intent(context, Tienime_Activity.class);
                startActivity(in);
                break;
            case R.id.rr_lay_two:
                Intent inn = new Intent(context, Portami_Premium.class);
                startActivity(inn);
                break;


        }

    }




}
