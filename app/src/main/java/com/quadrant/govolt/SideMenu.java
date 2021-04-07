package com.quadrant.govolt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;


import de.hdodenhof.circleimageview.CircleImageView;

public class SideMenu extends AppCompatActivity implements View.OnClickListener {
    private TextView premium_tv, wallet_tv,tv_slide_fullname,limo_profile_tv, lime_corse_tv,community_tv,business_tv,help_tv ;
    private  Button logout_btn;
    private ImageView close;
    private Context context;

    private AlertDialog alertDialog;
    private CircleImageView _profileImg;

    private  ImageView _smallImgCam;

    private ImageButton fbButton;
    private ImageButton linkedinButton;
    private ImageButton instagramButton;

    private static final int CAMERA_REQUEST = 1888;

    Bitmap mphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_menu);
        context = this;
        tv_slide_fullname = (TextView)findViewById(R.id.slide_fullname);
        limo_profile_tv = (TextView)findViewById(R.id.profile);
        lime_corse_tv = (TextView)findViewById(R.id.corse);
        wallet_tv = (TextView)findViewById(R.id.wallet);
       // premium_tv = (TextView)findViewById(R.id.premium);
       // wallet_tv = (TextView)findViewById(R.id.wallet);
       // premium_tv = (TextView)findViewById(R.id.premium);
     //   community_tv = (TextView)findViewById(R.id.community);
        business_tv = (TextView)findViewById(R.id.business);
        help_tv = (TextView)findViewById(R.id.help);
         logout_btn = (Button)findViewById(R.id.logout);
         close = (ImageView)findViewById(R.id.close_btn);
         _profileImg = (CircleImageView)findViewById(R.id.profile_image);

        _smallImgCam = (ImageView)findViewById(R.id.ic_camera_small);

        fbButton = (ImageButton) findViewById(R.id.fb);
        linkedinButton = (ImageButton) findViewById(R.id.linkedin);
        instagramButton  = (ImageButton) findViewById(R.id.instra);

        limo_profile_tv.setOnClickListener(this);
        lime_corse_tv.setOnClickListener(this);
        wallet_tv.setOnClickListener(this);
       // premium_tv.setOnClickListener(this);
       // wallet_tv.setOnClickListener(this);
       // premium_tv.setOnClickListener(this);
      //  community_tv.setOnClickListener(this);
        business_tv.setOnClickListener(this);
        help_tv.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
        close.setOnClickListener(this);
        _smallImgCam.setOnClickListener(this);

        fbButton.setOnClickListener(this);
        linkedinButton.setOnClickListener(this);
        instagramButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = PreferenceUtil.getInstance().getString(context, Constants.NAME, "");
        String surname = PreferenceUtil.getInstance().getString(context, Constants.SUR_NAME, "");
        String fullname =surname +" "+name;

        String _avathar_loc_img = PreferenceUtil.getInstance().getString(context, Constants.AVATHAR_LOC_IMG, "");
        Log.e("SURESH", "AVATHAR_IMG--"+_avathar_loc_img);

        Glide.with(context)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.ic_navigation_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_navigation_icon)
                .skipMemoryCache(false)
                .into(_profileImg);

        if(fullname!=null){
            tv_slide_fullname.setText(""+fullname);
        }


    }

    @Override
    public void onClick(View v) {

            displaySelectedFragment(v.getId());


    }

    private void displaySelectedFragment(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        String url_;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.profile:

                Intent intent_one = new Intent(SideMenu.this, ProfileScreen_Activity.class);
                startActivity(intent_one);

               /* Intent intent_one = new Intent(SideMenu.this, DamageCheckActivity.class);
                startActivity(intent_one);*/

                break;
            case R.id.corse:
                Intent intent_two = new Intent(SideMenu.this, LieMieCourse_Activity.class);
                startActivity(intent_two);
                break;

            case R.id.wallet:
                Intent intent_three = new Intent(SideMenu.this, Wallet_Activity.class);
                startActivity(intent_three);

                break;


          /*  case R.id.premium:
                Intent intent_four = new Intent(SideMenu.this, Premium_Activity.class);
                startActivity(intent_four);

                break;*/
           /* case R.id.wallet:
                Intent intent_three = new Intent(SideMenu.this, Wallet_Activity.class);
                startActivity(intent_three);

                break;
            case R.id.premium:
                Intent intent_four = new Intent(SideMenu.this, Premium_Activity.class);
                startActivity(intent_four);

                break;

            case R.id.community:
                Intent intent_five = new Intent(SideMenu.this, Community_Activity.class);
                startActivity(intent_five);
                break;
*/
            case R.id.business:
               // Intent intent_six = new Intent(SideMenu.this, BusinessActivityNew.class);
                Intent intent_six = new Intent(SideMenu.this, BusinessActivity_Screen.class);
                startActivity(intent_six);
                break;

            case R.id.help:
                Intent intent_seven = new Intent(SideMenu.this, HelpFaq_Activity.class);
                startActivity(intent_seven);
                break;

            case R.id.logout:

                signOut();
            break;

            case R.id.close_btn:
                Intent i = new Intent(SideMenu.this, HomeActivity.class);
                startActivity(i);
                //finish();
                break;
            case R.id.ic_camera_small:

                OpenCameraPopup();

                //finish();
                break;
            case R.id.fb:

                url_ = "https://www.facebook.com/GoVoltMobility/";
                OpenLink(url_);

                break;
            case R.id.linkedin:

                url_ = "https://www.linkedin.com/company/go-volt/";
                OpenLink(url_);

                break;
            case R.id.instra:

                url_ = "https://www.instagram.com/govoltmobility/";
                OpenLink(url_);

                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    private void OpenCameraPopup() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(SideMenu.this).inflate(R.layout.new_img_alert, viewGroup, false);

        // ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.new_err);
        TextView remove = (TextView) dialogView.findViewById(R.id.remove);

        ImageView Closebtn =(ImageView) dialogView.findViewById(R.id.vpclose);
        // tv_error.setText("" + error_message);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(SideMenu.this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();

        tv_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);



            }


        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                _profileImg.setImageResource(R.drawable.menu_govolt_icon);



            }
        });

        Closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            mphoto = (Bitmap) data.getExtras().get("data");
            _profileImg.setImageBitmap(mphoto);


        }
    }

    private void signOut() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(SideMenu.this).inflate(R.layout.logoutalert, viewGroup, false);


        Button cancel = (Button) dialogView.findViewById(R.id.cancel);
        Button ok = (Button) dialogView.findViewById(R.id.ok);

        //   tv_error.setText("" + error_message);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceUtil.clearSharedPreferences(context);
                Intent i = new Intent(SideMenu.this, SplashScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

    }

    private void OpenLink(String url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    }
