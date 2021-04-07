/*
package com.quadrant.govolt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quadrant.govolt.Others.MyLatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.quadrant.govolt.UploadImageActivity.getFromPref;

public class HomeActivity_Four extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    String tag = "LifeCycleEvents";
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    HomeActivity.SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    ArrayList<Bitmap> slider_image_list1;
    private TextView[] dots;
    int page_position = 0;



    private Context context;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    //private FloatingActionMenu fab_menu;
    private ArrayList<LatLng> listMyLatLng;
    private ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();
    private Marker mSelectedMarker;
    private Marker myMarker;
    private static final String TAG = HomeActivity_Four.class.getSimpleName();
    private AlertDialog alertDialog;
    private View dialogView;

    TextView tv_bauletto, tv_selia, tv_cavaletto, tv_carena, tv_caschi, tv_altro, tv_speech, tv_levafreno, tv_parafango, tv_freece, tv_luci, tv_gomme;

    private BottomSheetBehavior mBottomSheetBehavior;

    boolean buttonPressed = false;

    private static final int CAMERA_REQUEST = 1888;

    CircleImageView Image1, Image2, Image3;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";


    boolean Images = false;
    boolean Images2 = false;

    boolean Images1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_four);
        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        slider_image_list1=new ArrayList<Bitmap>();

        //fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);

        ImageView menu = (ImageView) findViewById(R.id.balck_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity_Four.this, SideMenu.class);
                startActivity(i);
            }
        });


        //handling menu status (open or close)
        */
/*fab_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                   // showToast("Menu is opened");
                } else {
                    //showToast("Menu is closed");
                }
            }
        });*//*


        setData();

        //Find bottom Sheet ID
        final CoordinatorLayout colayout = (CoordinatorLayout) findViewById(R.id.maps_colayout);
        final View bottomSheet = colayout.findViewById(R.id.paly_layout);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


        final ImageView avali_btn = colayout.findViewById(R.id.image_one);
        avali_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*/
/*

                Images=false;

                slider_image_list1.clear();
*//*


                showPopUp();

            }
        });


        //By default set BottomSheet Behavior as Collapsed and Height 0
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);

        //If you want to handle callback of Sheet Behavior you can use below code
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d(TAG, "State Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d(TAG, "State Dragging");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.d(TAG, "State Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d(TAG, "State Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.d(TAG, "State Settling");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        openBoottomScreen();

    }
   */
/* @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


        Images=false;
        Images1=false;
        Images2=false;

        slider_image_list1.clear();

    }*//*



        public void onStart()
    {
        super.onStart();
        Log.d(tag, "In the onStart() event");
    }
    public void onRestart()
    {
        super.onRestart();
        Log.d(tag, "In the onRestart() event");
    }
    public void onResume()
    {
        super.onResume();
        Log.d(tag, "In the onResume() event");

       */
/* Images=false;
        Images1=false;
        Images2=false;

        if(slider_image_list1!=null || slider_image_list1.size()>0){
            slider_image_list1.clear();
        }*//*

    }
    public void onPause()
    {
        super.onPause();
        Log.d(tag, "In the onPause() event");
    }
    public void onStop()
    {
        super.onStop();
        Log.d(tag, "In the onStop() event");
    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(tag, "In the onDestroy() event");
    }

        private void showPopUp() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.business_segnalazione, viewGroup, false);
        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.buttonOk);
        tv_bauletto = dialogView.findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(HomeActivity_Four.this);
        tv_selia = dialogView.findViewById(R.id.selia);
        tv_selia.setOnClickListener(HomeActivity_Four.this);
        tv_cavaletto = dialogView.findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(HomeActivity_Four.this);
        tv_carena = dialogView.findViewById(R.id.carena);
        tv_carena.setOnClickListener(HomeActivity_Four.this);
        tv_caschi = dialogView.findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(HomeActivity_Four.this);
        tv_altro = dialogView.findViewById(R.id.altro);
        tv_altro.setOnClickListener(HomeActivity_Four.this);
        tv_speech = dialogView.findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(HomeActivity_Four.this);
        tv_levafreno = dialogView.findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(HomeActivity_Four.this);
        tv_parafango = dialogView.findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(HomeActivity_Four.this);
        tv_freece = dialogView.findViewById(R.id.freece);
        tv_freece.setOnClickListener(HomeActivity_Four.this);
        tv_luci = dialogView.findViewById(R.id.luci);
        tv_luci.setOnClickListener(HomeActivity_Four.this);
        tv_gomme = dialogView.findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(HomeActivity_Four.this);
        Image1 = dialogView.findViewById(R.id.image1);
        Image1.setOnClickListener(HomeActivity_Four.this);
        Image2 = dialogView.findViewById(R.id.image2);
        Image2.setOnClickListener(HomeActivity_Four.this);
        Image3 = dialogView.findViewById(R.id.image3);
        Image3.setOnClickListener(HomeActivity_Four.this);









        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


        alertDialog.show();


        _btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                secondPopUp();
            }
        });

    }

    private void secondPopUp() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.business_recorda, viewGroup, false);

        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


        alertDialog.show();


        _btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                third_popUp();
            }
        });

    }

    private void third_popUp() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.buisness_err_alert, viewGroup, false);

        ImageView _ic_correct = (ImageView) dialogView.findViewById(R.id.ic_correct);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.done_btn);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


        alertDialog.show();


        _ic_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                fourth_PopUp();

            }
        });
    }

    private void fourth_PopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.buiseness_grazie, viewGroup, false);

        //ImageView _ic_correct = (ImageView) dialogView.findViewById(R.id.ic_correct);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.done_btn);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


        alertDialog.show();


        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });


    }

    private void openBoottomScreen() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            //If state is in collapse mode expand it
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        else
            //else if state is expanded collapse it
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void setData() {
        arrayList = new ArrayList<MyLatLng>();
        MyLatLng bean = new MyLatLng();
        bean.setTitle("Kukatpally");
        bean.setLat(17.48333);
        bean.setLng(78.41667);
        arrayList.add(bean);

        MyLatLng bean1 = new MyLatLng();
        bean1.setTitle("JNTU");
        bean1.setLat(17.495598);
        bean1.setLng(78.394966);
        arrayList.add(bean1);

        MyLatLng bean2 = new MyLatLng();
        bean2.setTitle("Miyapur");
        bean2.setLat(17.4968);
        bean2.setLng(78.3614);
        arrayList.add(bean2);

        MyLatLng bean3 = new MyLatLng();
        bean3.setTitle("madhapur");
        bean3.setLat(17.44056);
        bean3.setLng(78.39111);
        arrayList.add(bean3);

        MyLatLng bean4 = new MyLatLng();
        bean4.setTitle("Hitech city");
        bean4.setLat(17.44);
        bean4.setLng(78.3812);
        arrayList.add(bean4);

        MyLatLng bean5 = new MyLatLng();
        bean5.setTitle("Ayyapa sociaty");
        bean5.setLat(17.44056);
        bean5.setLng(78.39111);
        arrayList.add(bean5);

        MyLatLng bean6 = new MyLatLng();
        bean6.setTitle("Gachi bowli");
        bean6.setLat(17.4467);
        bean6.setLng(78.3571);
        arrayList.add(bean6);

        MyLatLng bean7 = new MyLatLng();
        bean7.setTitle("Kondapur");
        bean7.setLat(17.466614);
        bean7.setLng(78.368369);
        arrayList.add(bean7);


        MyLatLng bean8 = new MyLatLng();
        bean8.setTitle("Jublee hills");
        bean8.setLat(17.4334);
        bean8.setLng(78.4111);
        arrayList.add(bean8);

        // LoadingGoogleMap(arrayList);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.getUiSettings().setZoomControlsEnabled(true);
        // mMap.getUiSettings().setZoomGesturesEnabled(true);
        // mMap.getUiSettings().setCompassEnabled(true);

        */
/*com.google.android.gms.maps.model.LatLng sydney = new com.google.android.gms.maps.model.LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );*//*



        LoadingGoogleMap(arrayList);

    }


    void LoadingGoogleMap(ArrayList<MyLatLng> arrayList) {
        if (mMap != null) {
            mMap.clear();
            //  mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //  mMap.setMyLocationEnabled(true);
            //   mMap.getUiSettings().setZoomControlsEnabled(true);

            if (arrayList.size() > 0) {
                try {
                    listMyLatLng = new ArrayList<LatLng>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        MyLatLng bean = arrayList.get(i);
                        if (bean.getLat() > 0 && bean.getLng() > 0) {
                            double _lat = bean.getLat();
                            double _lon = bean.getLng();

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green);

                            myMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(_lat, _lon))
                                    .title(bean.getTitle())
                                    .icon(icon));
                            //Set Zoom Level of Map pin
                            LatLng object = new LatLng(_lat, _lon);
                            listMyLatLng.add(object);


                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12));
                        }
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        // marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dot));

                        if (null != mSelectedMarker) {
                            mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green));
                        }
                        mSelectedMarker = marker;
                        mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dot));

                        openBottomScreen();

                        return false;
                    }
                });

               */
/* googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker position)
                    {
                        LatLngBean bean=hashMapMarker.get(position);
                        Toast.makeText(getApplicationContext(), bean.getTitle(),Toast.LENGTH_SHORT).show();

                    }
                });*//*

            }
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    private void openBottomScreen() {

        */
/*if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            //If state is in collapse mode expand it
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        else
            //else if state is expanded collapse it
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);*//*

    }


    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void configureBottomSheetBehavior(View contentView) {
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            // dismiss(); //if you want the modal to be dismissed when user drags the bottomsheet down
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.bauletto:

                if (!buttonPressed) {
                    tv_bauletto.setBackgroundResource(R.drawable.round_corner);
                    tv_bauletto.setTextColor(Color.WHITE);
                } else {
                    tv_bauletto.setBackgroundResource(R.drawable.round_green);
                    tv_bauletto.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;


                break;

            case R.id.selia:

                if (!buttonPressed) {
                    tv_selia.setBackgroundResource(R.drawable.round_corner);
                    tv_selia.setTextColor(Color.WHITE);
                } else {
                    tv_selia.setBackgroundResource(R.drawable.round_green);
                    tv_selia.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;


                break;
            case R.id.cavaletto:
                if (!buttonPressed) {
                    tv_cavaletto.setBackgroundResource(R.drawable.round_corner);
                    tv_cavaletto.setTextColor(Color.WHITE);
                } else {
                    tv_cavaletto.setBackgroundResource(R.drawable.round_green);
                    tv_cavaletto.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.carena:
                if (!buttonPressed) {
                    tv_carena.setBackgroundResource(R.drawable.round_corner);
                    tv_carena.setTextColor(Color.WHITE);
                } else {
                    tv_carena.setBackgroundResource(R.drawable.round_green);
                    tv_carena.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.caschi:

                if (!buttonPressed) {
                    tv_caschi.setBackgroundResource(R.drawable.round_corner);
                    tv_caschi.setTextColor(Color.WHITE);
                } else {
                    tv_caschi.setBackgroundResource(R.drawable.round_green);
                    tv_caschi.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.altro:

                if (!buttonPressed) {
                    tv_altro.setBackgroundResource(R.drawable.round_corner);
                    tv_altro.setTextColor(Color.WHITE);
                } else {
                    tv_altro.setBackgroundResource(R.drawable.round_green);
                    tv_altro.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;


            case R.id.specchietti:

                if (!buttonPressed) {
                    tv_speech.setBackgroundResource(R.drawable.round_corner);
                    tv_speech.setTextColor(Color.WHITE);
                } else {
                    tv_speech.setBackgroundResource(R.drawable.round_green);
                    tv_speech.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.levafreno:

                if (!buttonPressed) {
                    tv_levafreno.setBackgroundResource(R.drawable.round_corner);
                    tv_levafreno.setTextColor(Color.WHITE);
                } else {
                    tv_levafreno.setBackgroundResource(R.drawable.round_green);
                    tv_levafreno.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;
            case R.id.parafrango:

                if (!buttonPressed) {
                    tv_parafango.setBackgroundResource(R.drawable.round_corner);
                    tv_parafango.setTextColor(Color.WHITE);
                } else {
                    tv_parafango.setBackgroundResource(R.drawable.round_green);
                    tv_parafango.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.freece:

                if (!buttonPressed) {
                    tv_freece.setBackgroundResource(R.drawable.round_corner);
                    tv_freece.setTextColor(Color.WHITE);
                } else {
                    tv_freece.setBackgroundResource(R.drawable.round_green);
                    tv_freece.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;
            case R.id.luci:

                if (!buttonPressed) {
                    tv_luci.setBackgroundResource(R.drawable.round_corner);
                    tv_luci.setTextColor(Color.WHITE);
                } else {
                    tv_luci.setBackgroundResource(R.drawable.round_green);
                    tv_luci.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;

            case R.id.gomme:

                if (!buttonPressed) {
                    tv_gomme.setBackgroundResource(R.drawable.round_corner);
                    tv_gomme.setTextColor(Color.WHITE);
                } else {
                    tv_gomme.setBackgroundResource(R.drawable.round_green);
                    tv_gomme.setTextColor(Color.BLACK);

                }
                buttonPressed = !buttonPressed;
                break;


            case R.id.image1:
                if(Images==false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity_Four.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity_Four.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity_Four.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity_Four.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity_Four.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        startActivityForResult(takePictureIntent, 1);
                    }

                } else {





                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView.findViewById(R.id.ll_dots);


                  //  slider_image_list1 = new ArrayList<>();

                   */
/* slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/mountain_bongo_animal_mammal_220289.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/mountain_bongo_animal_mammal_220289.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");
                   *//*
 sliderPagerAdapter = new HomeActivity.SliderPagerAdapter(this, slider_image_list1);
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
                                dots[i] = new TextView(HomeActivity_Four.this);
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
                    builder.setView(dialogView);

                    //finally creating the alert dialog and displaying it
                    alertDialog = builder.create();

                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setCancelable(true);
                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


                    alertDialog.show();






                }


                break;
            case R.id.image2:

                if(Images1==false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity_Four.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity_Four.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity_Four.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity_Four.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity_Four.this,
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
                    dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView.findViewById(R.id.ll_dots);


                  */
/*  slider_image_list = new ArrayList<>();

                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/mountain_bongo_animal_mammal_220289.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/mountain_bongo_animal_mammal_220289.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");
                    *//*
sliderPagerAdapter = new SliderPagerAdapter(HomeActivity_Four.this, slider_image_list1);
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
                                dots[i] = new TextView(HomeActivity_Four.this);
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
                    builder.setView(dialogView);

                    //finally creating the alert dialog and displaying it
                    alertDialog = builder.create();

                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setCancelable(true);
                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


                    alertDialog.show();






                }

                break;
            case R.id.image3:

                if(Images2==false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity_Four.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity_Four.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity_Four.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity_Four.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity_Four.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 3);
                        startActivityForResult(takePictureIntent, 3);
                    }

                } else {





                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    dialogView = LayoutInflater.from(HomeActivity_Four.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView.findViewById(R.id.ll_dots);


                  */
/*  slider_image_list = new ArrayList<>();

                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/mountain_bongo_animal_mammal_220289.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/mountain_bongo_animal_mammal_220289.jpg");
                    slider_image_list.add("https://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");
                   *//*
 sliderPagerAdapter = new SliderPagerAdapter(HomeActivity_Four.this, slider_image_list1);
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
                                dots[i] = new TextView(HomeActivity_Four.this);
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
                    builder.setView(dialogView);

                    //finally creating the alert dialog and displaying it
                    alertDialog = builder.create();

                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setCancelable(true);
                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


                    alertDialog.show();






                }


                break;


        }


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
                        ActivityCompat.requestPermissions(HomeActivity_Four.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Images=true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image1.setImageBitmap(mphoto);

            slider_image_list1.add(mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh"+encoded);

            //  ButtonOk.setVisibility(View.VISIBLE);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Images1=true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image2.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            slider_image_list1.add(mphoto);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh"+encoded);
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            Images2=true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image3.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            slider_image_list1.add(mphoto);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh"+encoded);
        }

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
                        //    startInstalledAppDetailsActivity(UploadImageActivity.this);

                    }
                });
        alertDialog.show();
    }
}
*/
