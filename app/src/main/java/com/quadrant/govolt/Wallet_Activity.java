package com.quadrant.govolt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Wallet_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView menu, img_home;

    CircleImageView _profileImg;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wallet);
        SetUpView();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);


        ImageView back = (ImageView) findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LoadProfileImage();

    }

    private void SetUpView() {
        _profileImg = (CircleImageView) findViewById(R.id.profile_image);
        img_home = findViewById(R.id.home_icon);
        img_home.setOnClickListener(this);
    }

    private void LoadProfileImage() {

        String _avathar_loc_img = PreferenceUtil.getInstance().getString(this, Constants.AVATHAR_LOC_IMG, "");
        Glide.with(this)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.ic_navigation_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_navigation_icon)
                .skipMemoryCache(false)
                .into(_profileImg);
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new Wallet_Display_Fragment(), "Wallet");
        adapter.addFragment(new Pacchetti_Display_Fragment(), "Pacchetti");
        viewPager.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.home_icon:

                Intent I = new Intent(Wallet_Activity.this, HomeActivity.class);
                startActivity(I);

                break;

        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}