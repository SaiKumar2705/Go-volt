package com.quadrant.govolt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;

import de.hdodenhof.circleimageview.CircleImageView;


public class HelpFaq_Activity extends AppCompatActivity implements View.OnClickListener{

    private Context context;
    private  LinearLayout first_ll,second_ll;
    private ImageView menu;
    private TextView TermandCond;
    private ImageView phone_call, email_icon;
    private CircleImageView _profileImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_fragment);
        context = this;

        setUpView();


        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Help & FAQ");

        ImageView back  = (ImageView)findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView _homeIcon = (ImageView)findViewById(R.id.home_icon);
        _homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpFaq_Activity.this, HomeActivity.class);
                startActivity(i);
            }
        });


        String _avathar_loc_img = PreferenceUtil.getInstance().getString(context, Constants.AVATHAR_LOC_IMG, "");
        Glide.with(context)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.ic_navigation_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_navigation_icon)
                .skipMemoryCache(false)
                .into(_profileImg);

    }

    private void setUpView() {
        first_ll = (LinearLayout)findViewById(R.id.first_layout);
        second_ll = (LinearLayout)findViewById(R.id.second_layout);
        TermandCond=(TextView)findViewById(R.id.termsandcond);
        phone_call = (ImageView)findViewById(R.id.make_call);
        email_icon = (ImageView)findViewById(R.id.email_image);
        _profileImg = (CircleImageView)findViewById(R.id.profile_image);

        email_icon.setOnClickListener(this);
        phone_call.setOnClickListener(this);
        first_ll.setOnClickListener(this);
        second_ll.setOnClickListener(this);
        TermandCond.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first_layout:


                break;
            case R.id.second_layout:
                Intent i = new Intent(context, FAQ_Activity.class);
                startActivity(i);
                break;
            case  R.id.termsandcond:
                Uri uri = Uri.parse("https://govolt.it/termini-e-condizioni/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.make_call:

                String phone ="0283432432";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(phoneIntent);
                break;
            case R.id.email_image:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:servizioclient@govoltmobility.com"));
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
                break;

        }

    }
}
