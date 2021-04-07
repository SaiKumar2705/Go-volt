package com.quadrant.govolt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.model.ImagesPhoto;
import com.quadrant.request.ImageselfieRequest;
import com.quadrant.response.ImageselfieResponse;
import com.quadrant.response.LoginErrorResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.quadrant.govolt.UploadImageActivity.getFromPref;

public class DamageCheckActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_bauletto, tv_selia, tv_cavaletto, tv_carena, tv_caschi, tv_altro, tv_speech, tv_levafreno, tv_parafango, tv_freece, tv_luci, tv_gomme;
    CircleImageView Image1, Image2, Image3;
    boolean buttonPressed = false;

    private ArrayList<Integer> codeAdd_array =  new ArrayList<Integer>();
    private ArrayList<Integer> codeRemove_array =  new ArrayList<Integer>();
    private ArrayList<Boolean> values_array =  new ArrayList<Boolean>();

    private boolean one = false;
    private boolean two = false;
    private boolean three = false;
    private boolean four = false;
    private boolean five = false;
    private boolean six = false;
    private boolean seven = false;
    private boolean eight = false;
    private boolean nine = false;
    private boolean ten = false;
    private boolean eleven = false;
    private boolean tweleve =false;

    private EditText commentbox;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    int count = 0;
    ArrayList<String> arrlist = new ArrayList<String>();
    private String TAG = "DammageScreen";
    private Context context;
    private  int type = 0;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;

    ArrayList<Bitmap> slider_image_list1;
    private TextView[] dots;
    int page_position = 0;

    boolean Images = false;
    boolean Images2 = false;
    boolean Images1 = false;
    private View dialogView,dialogView_image;
    private AlertDialog alertDialog,alertDialog_image;

    private String imagePathOne = "";
    private String imagePathTwo = "";
    private String imagePathThree = "";


    ArrayList<ImagesPhoto> imagePojjo = new ArrayList<ImagesPhoto>();


    public  void ImageRemover(final int removeID, final int pos, ArrayList<Bitmap> slider_image_list1, final ArrayList<ImagesPhoto> pojjoImg, Activity activity) {

        slider_image_list1=slider_image_list1;

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.damage_err_alert, viewGroup, false);

        // ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.new_err);
        TextView remove = (TextView) dialogView.findViewById(R.id.remove);
        // tv_error.setText("" + error_message);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog_image.dismiss();

                if(pojjoImg.size()>0){
                    pojjoImg.remove(pos);

                    if(removeID == 1){
                         imagePathOne = "";
                        Image1.setBackgroundResource(R.drawable.ic_camera_icon_one);
                        Images = false;


                    }
                    if(removeID == 2){
                         imagePathTwo = "";
                        Image2.setBackgroundResource(R.drawable.ic_camera_icon_one);
                        Images1 = false;
                    }
                    if(removeID == 3){
                        imagePathThree = "";
                        Image3.setBackgroundResource(R.drawable.ic_camera_icon_one);
                        Images2 = false;
                    }
                }

                sliderPagerAdapter.notifyDataSetChanged();

                alertDialog.dismiss();


            }
        });

    }


    public class SliderPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        Activity activity;
        ArrayList<Bitmap> image_arraylist;
        ArrayList<ImagesPhoto> imagespojjo;
        private AlertDialog alertDialog;
        private View dialogView;


        Context context;

        public SliderPagerAdapter(Activity activity, ArrayList<Bitmap> image_arraylist, ArrayList<ImagesPhoto> imagespojjo) {
            this.activity = activity;
            this.image_arraylist = image_arraylist;
            this.imagespojjo = imagespojjo;


        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
            ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);

            ImageView remove = (ImageView) view.findViewById(R.id.remove);


            Glide.with(activity.getApplicationContext())
                    .load(image_arraylist.get(position))
                    .placeholder(R.mipmap.ic_launcher) // optional
                    .error(R.mipmap.ic_launcher)         // optional
                    .into(im_slider);
            // Glide.with(activity.getApplicationContext()).load(image_arraylist.get(position).get.into(viewHolder.img);

            container.addView(view);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
                    ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
                    //((Activity)context).finish();


                    int removeID = imagePojjo.get(position).getId();

                    int pos =position;

                    ImageRemover(removeID,pos, slider_image_list1,imagePojjo, activity);


                    Log.e(TAG, "POS---"+imagePojjo.get(position).getId());


                }
            });

            return view;
        }

        @Override
        public int getCount() {
            return imagespojjo.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damage_check);
        context = this;

        slider_image_list1 = new ArrayList<Bitmap>();
        imagePojjo = new ArrayList<ImagesPhoto>();


        ImageView _btn_done = (ImageView) findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) findViewById(R.id.buttonOk);
        _btn_confirm.setOnClickListener(this);

        tv_bauletto = findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(DamageCheckActivity.this);
        tv_selia = findViewById(R.id.selia);
        tv_selia.setOnClickListener(DamageCheckActivity.this);
        tv_cavaletto = findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(DamageCheckActivity.this);
        tv_carena = findViewById(R.id.carena);
        tv_carena.setOnClickListener(DamageCheckActivity.this);
        tv_caschi = findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(DamageCheckActivity.this);
        tv_altro = findViewById(R.id.altro);
        tv_altro.setOnClickListener(DamageCheckActivity.this);
        tv_speech = findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(DamageCheckActivity.this);
        tv_levafreno = findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(DamageCheckActivity.this);
        tv_parafango = findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(DamageCheckActivity.this);
        tv_freece = findViewById(R.id.freece);
        tv_freece.setOnClickListener(DamageCheckActivity.this);
        tv_luci = findViewById(R.id.luci);
        tv_luci.setOnClickListener(DamageCheckActivity.this);
        tv_gomme = findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(DamageCheckActivity.this);
        Image1 = findViewById(R.id.image1);
        Image1.setOnClickListener(DamageCheckActivity.this);
        Image2 = findViewById(R.id.image2);
        Image2.setOnClickListener(DamageCheckActivity.this);
        Image3 = findViewById(R.id.image3);
        Image3.setOnClickListener(DamageCheckActivity.this);

         commentbox = findViewById(R.id.commentbox);
        commentbox.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bauletto:

                if (!buttonPressed) {
                    tv_bauletto.setBackgroundResource(R.drawable.round_corner);
                    tv_bauletto.setTextColor(Color.WHITE);

                    arrlist.add("1");

                  } else {
                    tv_bauletto.setBackgroundResource(R.drawable.round_green);
                    tv_bauletto.setTextColor(Color.BLACK);


                    if(arrlist.size()>0){
                        arrlist.remove("1");

                    }

                }
                buttonPressed = !buttonPressed;


                break;

            case R.id.selia:

                if (!buttonPressed) {
                    tv_selia.setBackgroundResource(R.drawable.round_corner);
                    tv_selia.setTextColor(Color.WHITE);

                    arrlist.add("2");

                } else {
                    tv_selia.setBackgroundResource(R.drawable.round_green);
                    tv_selia.setTextColor(Color.BLACK);


                    if(arrlist.size()>0){
                        arrlist.remove("2");

                    }

                }
                buttonPressed = !buttonPressed;


                break;
            case R.id.cavaletto:
                if (!buttonPressed) {
                    tv_cavaletto.setBackgroundResource(R.drawable.round_corner);
                    tv_cavaletto.setTextColor(Color.WHITE);

                    arrlist.add("3");


                } else {
                    tv_cavaletto.setBackgroundResource(R.drawable.round_green);
                    tv_cavaletto.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("3");

                    }

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.carena:
                if (!buttonPressed) {
                    tv_carena.setBackgroundResource(R.drawable.round_corner);
                    tv_carena.setTextColor(Color.WHITE);

                    arrlist.add("4");

                } else {
                    tv_carena.setBackgroundResource(R.drawable.round_green);
                    tv_carena.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("4");

                    }

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.caschi:

                if (!buttonPressed) {
                    tv_caschi.setBackgroundResource(R.drawable.round_corner);
                    tv_caschi.setTextColor(Color.WHITE);

                    arrlist.add("5");

                } else {
                    tv_caschi.setBackgroundResource(R.drawable.round_green);
                    tv_caschi.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("5");

                    }

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.altro:

                if (!buttonPressed) {
                    tv_altro.setBackgroundResource(R.drawable.round_corner);
                    tv_altro.setTextColor(Color.WHITE);

                    arrlist.add("6");

                } else {
                    tv_altro.setBackgroundResource(R.drawable.round_green);
                    tv_altro.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("6");

                    }

                }
                buttonPressed = !buttonPressed;
                break;


            case R.id.specchietti:

                if (!buttonPressed) {
                    tv_speech.setBackgroundResource(R.drawable.round_corner);
                    tv_speech.setTextColor(Color.WHITE);

                    arrlist.add("7");

                } else {
                    tv_speech.setBackgroundResource(R.drawable.round_green);
                    tv_speech.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("7");

                    }

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.levafreno:

                if (!buttonPressed) {
                    tv_levafreno.setBackgroundResource(R.drawable.round_corner);
                    tv_levafreno.setTextColor(Color.WHITE);

                    arrlist.add("8");

                } else {
                    tv_levafreno.setBackgroundResource(R.drawable.round_green);
                    tv_levafreno.setTextColor(Color.BLACK);


                    if(arrlist.size()>0){
                        arrlist.remove("8");

                    }

                }
                buttonPressed = !buttonPressed;
                break;
            case R.id.parafrango:

                if (!buttonPressed) {
                    tv_parafango.setBackgroundResource(R.drawable.round_corner);
                    tv_parafango.setTextColor(Color.WHITE);

                    arrlist.add("9");

                } else {
                    tv_parafango.setBackgroundResource(R.drawable.round_green);
                    tv_parafango.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("9");

                    }

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.freece:

                if (!buttonPressed) {
                    tv_freece.setBackgroundResource(R.drawable.round_corner);
                    tv_freece.setTextColor(Color.WHITE);

                    arrlist.add("10");

                } else {
                    tv_freece.setBackgroundResource(R.drawable.round_green);
                    tv_freece.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("10");

                    }
                }
                buttonPressed = !buttonPressed;
                break;
            case R.id.luci:

                if (!buttonPressed) {
                    tv_luci.setBackgroundResource(R.drawable.round_corner);
                    tv_luci.setTextColor(Color.WHITE);

                    arrlist.add("11");

                } else {
                    tv_luci.setBackgroundResource(R.drawable.round_green);
                    tv_luci.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("11");

                    }


                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.gomme:

                if (!buttonPressed) {
                    tv_gomme.setBackgroundResource(R.drawable.round_corner);
                    tv_gomme.setTextColor(Color.WHITE);

                    arrlist.add("12");

                } else {
                    tv_gomme.setBackgroundResource(R.drawable.round_green);
                    tv_gomme.setTextColor(Color.BLACK);

                    if(arrlist.size()>0){
                        arrlist.remove("12");

                    }


                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.buttonOk:

               for (String value : arrlist) {
                    System.out.println("Value = " + value);
                }

                PostData();
                break;

            case R.id.image1:
                if (Images == false) {
                    if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(DamageCheckActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(DamageCheckActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(DamageCheckActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
                        startActivityForResult(takePictureIntent, 1);
                    }

                } else {


                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    dialogView_image = LayoutInflater.from(DamageCheckActivity.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);


                    sliderPagerAdapter = new SliderPagerAdapter(DamageCheckActivity.this, slider_image_list1, imagePojjo);
                    vp_slider.setAdapter(sliderPagerAdapter);


                    vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            addBottomDots(position);
                        }

                        private void addBottomDots(int position) {

                            dots = new TextView[slider_image_list1.size()];

                            ll_dots.removeAllViews();
                            for (int i = 0; i < dots.length; i++) {
                                dots[i] = new TextView(DamageCheckActivity.this);
                                dots[i].setText(Html.fromHtml("&#8226;"));
                                dots[i].setTextSize(35);
                                dots[i].setTextColor(Color.parseColor("#000000"));
                                ll_dots.addView(dots[i]);
                            }

                            if (dots.length > 0)
                                dots[position].setTextColor(Color.parseColor("#FFFFFF"));
                        }


                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView_image);

                    //finally creating the alert dialog and displaying it
                    alertDialog_image= builder.create();

                    alertDialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog_image.setCancelable(true);
                    alertDialog_image.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


                    alertDialog_image.show();


                }

                break;
            case R.id.image2:

                if (Images1 == false) {
                    if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(DamageCheckActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(DamageCheckActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(DamageCheckActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
                        startActivityForResult(takePictureIntent, 2);
                    }

                } else {


                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    dialogView_image= LayoutInflater.from(DamageCheckActivity.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);

                    sliderPagerAdapter = new SliderPagerAdapter(DamageCheckActivity.this, slider_image_list1, imagePojjo);
                    vp_slider.setAdapter(sliderPagerAdapter);


                    vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            addBottomDots(position);
                        }

                        private void addBottomDots(int position) {

                            dots = new TextView[slider_image_list1.size()];

                            ll_dots.removeAllViews();
                            for (int i = 0; i < dots.length; i++) {
                                dots[i] = new TextView(DamageCheckActivity.this);
                                dots[i].setText(Html.fromHtml("&#8226;"));
                                dots[i].setTextSize(35);
                                dots[i].setTextColor(Color.parseColor("#000000"));
                                ll_dots.addView(dots[i]);
                            }

                            if (dots.length > 0)
                                dots[position].setTextColor(Color.parseColor("#FFFFFF"));
                        }


                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView_image);

                    //finally creating the alert dialog and displaying it
                    alertDialog_image = builder.create();

                    alertDialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog_image.setCancelable(true);
                    alertDialog_image.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


                    alertDialog_image.show();


                }

                break;
            case R.id.image3:

                if (Images2 == false) {
                    if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(DamageCheckActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(DamageCheckActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(DamageCheckActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
                        startActivityForResult(takePictureIntent, 3);
                    }

                } else {


                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    dialogView_image = LayoutInflater.from(DamageCheckActivity.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);


                    sliderPagerAdapter = new SliderPagerAdapter(DamageCheckActivity.this, slider_image_list1, imagePojjo);
                    vp_slider.setAdapter(sliderPagerAdapter);


                    vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            addBottomDots(position);
                        }

                        private void addBottomDots(int position) {

                            dots = new TextView[slider_image_list1.size()];

                            ll_dots.removeAllViews();
                            for (int i = 0; i < dots.length; i++) {
                                dots[i] = new TextView(DamageCheckActivity.this);
                                dots[i].setText(Html.fromHtml("&#8226;"));
                                dots[i].setTextSize(35);
                                dots[i].setTextColor(Color.parseColor("#000000"));
                                ll_dots.addView(dots[i]);
                            }

                            if (dots.length > 0)
                                dots[position].setTextColor(Color.parseColor("#FFFFFF"));
                        }


                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView_image);

                    //finally creating the alert dialog and displaying it
                    alertDialog_image = builder.create();

                    alertDialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog_image.setCancelable(true);
                    alertDialog_image.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


                    alertDialog_image.show();


                }


                break;
        }


    }

    private void UploadCameraOne() {
        if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(DamageCheckActivity.this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(DamageCheckActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DamageCheckActivity.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(DamageCheckActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {

            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                checkIfAlreadyhavePermission();
            }

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
            startActivityForResult(takePictureIntent, 1);
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Images = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image1.setImageBitmap(mphoto);

            ImagesPhoto ph1 = new ImagesPhoto();
            ph1.setPhoto(mphoto);
            ph1.setId(1);
            ph1.setRemoved(false);
            imagePojjo.add(ph1);


            slider_image_list1.add(mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
             imagePathOne = finalFile.getPath();
            System.out.println("ImagePath---"+imagePathOne);


            //=====For Service Call================

            //  ButtonOk.setVisibility(View.VISIBLE);
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            Images1 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image2.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            slider_image_list1.add(mphoto);

            ImagesPhoto ph2 = new ImagesPhoto();
            ph2.setPhoto(mphoto);
            ph2.setId(2);
            ph2.setRemoved(false);
            imagePojjo.add(ph2);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
             imagePathTwo = finalFile.getPath();
            System.out.println("ImagePath---"+imagePathTwo);


            //=====For Service Call================
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            Images2 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image3.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            slider_image_list1.add(mphoto);

            ImagesPhoto ph3 = new ImagesPhoto();
            ph3.setPhoto(mphoto);
            ph3.setId(3);
            ph3.setRemoved(false);
            imagePojjo.add(ph3);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
             imagePathThree = finalFile.getPath();
            System.out.println("ImagePath---"+imagePathThree);


            //=====For Service Call================
        }

    }

    private void UploadDamageFile_Api(final Context context, final String imagePath, int type) {

      //  showProgressbar();
        final ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);
        req.setType(type);

        Log.e(TAG, "type---"+type);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

       /* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",imagePath,requestFile);*/

        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetDamageMedia(Constants.TOKEN,bearer_authorization, multipartBody, 21, 1);
        resultRes.enqueue(new Callback<ImageselfieResponse>() {
            @Override
            public void onResponse(Call<ImageselfieResponse> call, Response<ImageselfieResponse> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {
                        if (response.code() != 500) {
                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                            LoginErrorResponse error = ErrorUtils.parseError(response);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", error.getError().getDescription());

                            // error_Alertbox(error.getError().getDescription());
                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                      String  _front_img = response.body().getData().getLocation();

                        Glide.with(context)
                                .load(_front_img) // or URI/path
                                .placeholder(R.drawable.ic_camera_icon_one)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.IMMEDIATE)
                                .error(R.drawable.ic_camera_icon_one)
                                .skipMemoryCache(false)
                                .into(Image1);

                    }


                }
                //dismissProgressbar();

            }

            @Override
            public void onFailure(Call<ImageselfieResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());

                //dismissProgressbar();

            }


        });
    }


    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(DamageCheckActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(DamageCheckActivity.this);

                    }
                });
        alertDialog.show();
    }
    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkIfAlreadyhavePermission() {
        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return true;
        } else

            return false;

    }
    private void PostData() {

        try{

            JSONObject jo = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray otherArray = new JSONArray();
            JSONObject jsonObj = null;

            ArrayList<Integer> intArray = new ArrayList<Integer>();

            for (String value : arrlist) {

               // System.out.println("Value = " + Integer.valueOf(value));

                intArray.add(Integer.valueOf(value));


            }

            for (int _value : intArray) {
                System.out.println("_value = " + _value);

                jsonObj = new JSONObject();
                jsonObj.put("code", _value);
                jsonObj.put("value", true);

                jsonArray.put(jsonObj);
            }

            try {

                if(arrlist.size()>0){
                    jo.put("instantSpotted",arrlist.size());
                }else{
                    jo.put("instantSpotted",0);
                }

                jo.put("parts",jsonArray);

                jo.put("description",commentbox.getText().toString());
                jo.put("other_damage",otherArray);

                Log.e("Suresh==--===", "req==="+jo.toString());


                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject)jsonParser.parse(jo.toString());

                damageServiceCall(gsonObject);


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }catch (JSONException je){

            je.printStackTrace();
        }


    }

    private void damageServiceCall(JsonObject requestData) {


        //showProgressbar();


        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<JsonObject> resultRes = register_details.GetPostDammageRes(Constants.TOKEN, bearer_authorization, requestData, 21);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

               // dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        JsonObject res = response.body();


                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                //dismissProgressbar();

            }


        });
    }
}
