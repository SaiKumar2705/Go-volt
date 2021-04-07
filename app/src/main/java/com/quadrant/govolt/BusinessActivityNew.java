package com.quadrant.govolt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtilsStartRide;
import com.quadrant.govolt.Others.GeofenceTrasitionService;
import com.quadrant.govolt.Others.MyLatLng;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.ActivateSiteInterface;
import com.quadrant.interfaces.GeofencingInterface;
import com.quadrant.interfaces.GetSiteID_Interface;
import com.quadrant.interfaces.GetVehicleInterface;
import com.quadrant.interfaces.StartRideSession;
import com.quadrant.interfaces.StopRideSession;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.model.ImagesPhoto;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.response.GeoFenceResponse;
import com.quadrant.response.SiteIDResponse;
import com.quadrant.response.StartRideErrorResponse;
import com.quadrant.response.UserInfoResponse;
import com.quadrant.response.VehicleResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.quadrant.govolt.UploadImageActivity.getFromPref;

public class BusinessActivityNew extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, View.OnClickListener {
    private Context context;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private ArrayList<LatLng> listMyLatLng;
    private ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();
    private ArrayList<MyLatLng> selected_loc_arrayList = new ArrayList<MyLatLng>();
    private Marker mSelectedMarker;
    private Marker myMarker;
    private static final String TAG = BusinessActivityNew.class.getSimpleName();

    private BottomSheetBehavior mBottomSheetBehavior;
    ProgressBar mProgressBar;

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    private KProgressHUD progressbar_hud;
    private AlertDialog alertDialog, alertDialog_image;
    private View dialogView, dialogView_image;

    private List<VehicleResponse.Data> vehicle_data_array;
    private TextView _bike_number_bottom;
    private CoordinatorLayout colayout;
    private View bottomSheet;

    private boolean isSlideViewClicked = false;
    private boolean _isDispalyedMarker = false;


    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters

    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    double LatGeo, LngGeo;
    private Circle geoFenceLimits;
    private Marker geoFenceMarker;

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";
    private RelativeLayout first_bikeLayout;
    private LinearLayout _play_layout;
    private RelativeLayout social_bike_layout_one;
    private RelativeLayout social_play_layout;
    private ImageView imageOne;
    TextView tv_bauletto, tv_selia, tv_cavaletto, tv_carena, tv_caschi, tv_altro, tv_speech, tv_levafreno, tv_parafango, tv_freece, tv_luci, tv_gomme;
    CircleImageView Image1, Image2, Image3;
    boolean buttonPressed = false;
    private Polyline currentPolyline;

    int i = 0;

    private static final int CAMERA_REQUEST = 1888;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    ArrayList<Bitmap> slider_image_list1;
    private TextView[] dots;
    int page_position = 0;

    boolean Images = false;
    boolean Images2 = false;
    boolean Images1 = false;
    private boolean isPlayPauseClicked = false;
    private int vehicleID;
    private ImageView _stopRide;

    private boolean isUserStartSession = false;


    LinearLayout slideLayout, timer_buttons_Layout;
    private Marker my_marker;
    private ImageView call_bike_imageview, whatsUp_bike_imageview, call_play_imageview, whatsUp_play_imageview;

    SeekBar sb;
    private boolean isGreenButtonVisible = false;
    private CountDownTimer yourCountDownTimer;

    ArrayList<ImagesPhoto> imagePojjo = new ArrayList<ImagesPhoto>();
    String _type = "";
    private String progressPercentage;

    ArrayList<LatLng> markerPoints;

    public void ImageRemover(final int pos, ArrayList<Bitmap> slider_image_list1, final ArrayList<ImagesPhoto> pojjoImg, Activity activity) {

        slider_image_list1 = slider_image_list1;

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

                if (pojjoImg.size() > 0) {
                    pojjoImg.remove(pos);

                    //  pojjoImg.get(pos).setRemoved(true);


                }

               /*if(imagePojjo!=null){
                   for(int k=0; k<imagePojjo.size(); k++){
                       Log.e(TAG, "POS---"+imagePojjo.get(k).getId());
                   }
               }*/


                //  Image1.setImageDrawable(null);

                //Images=false;


                sliderPagerAdapter.notifyDataSetChanged();


                alertDialog.dismiss();


            }
        });

    }

   /* public static void ImageRemover(int pos, ArrayList<Bitmap> image_arraylist) {
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity);

        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        slider_image_list1 = new ArrayList<Bitmap>();
        createGoogleApi();


        // ImageView menu = (ImageView)findViewById(R.id.balck_menu);
        LinearLayout menu_ll = (LinearLayout) findViewById(R.id.menu_layout);
        menu_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessActivityNew.this, SideMenu.class);
                startActivity(i);
            }
        });


        if (savedInstanceState == null) {

            mGeofenceList = new ArrayList<Geofence>();

            int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (resp == ConnectionResult.SUCCESS) {

                initGoogleAPIClient();

                createGeofences(17.44056, 78.39111);

            } else {
                Log.e(TAG, "Your Device doesn't support Google Play Services.");
            }


        }


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if (_isInternetAvailable) {

            UserInformation();

        } else {
            Toast.makeText(context, "Please connect internet.", Toast.LENGTH_SHORT).show();

        }
        imagePojjo = new ArrayList<ImagesPhoto>();

        setUpView();


        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {


                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d(TAG, "State Collapsed");

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d(TAG, "State Dragging");

                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheet.setVisibility(View.GONE);

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

        getGeofencing();

        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDamagePopUp();
            }
        });

        final ImageView _playPause = (ImageView) colayout.findViewById(R.id.play_pause);
        _playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isPlayPauseClicked) {
                    _playPause.setImageResource(R.drawable.ic_pause);
                    isPlayPauseClicked = false;
                } else {
                    isPlayPauseClicked = true;
                    _playPause.setImageResource(R.drawable.ic_play_icon);
                    //Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showDamagePopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.business_segnalazione, viewGroup, false);
        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.buttonOk);
        tv_bauletto = dialogView.findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(BusinessActivityNew.this);
        tv_selia = dialogView.findViewById(R.id.selia);
        tv_selia.setOnClickListener(BusinessActivityNew.this);
        tv_cavaletto = dialogView.findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(BusinessActivityNew.this);
        tv_carena = dialogView.findViewById(R.id.carena);
        tv_carena.setOnClickListener(BusinessActivityNew.this);
        tv_caschi = dialogView.findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(BusinessActivityNew.this);
        tv_altro = dialogView.findViewById(R.id.altro);
        tv_altro.setOnClickListener(BusinessActivityNew.this);
        tv_speech = dialogView.findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(BusinessActivityNew.this);
        tv_levafreno = dialogView.findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(BusinessActivityNew.this);
        tv_parafango = dialogView.findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(BusinessActivityNew.this);
        tv_freece = dialogView.findViewById(R.id.freece);
        tv_freece.setOnClickListener(BusinessActivityNew.this);
        tv_luci = dialogView.findViewById(R.id.luci);
        tv_luci.setOnClickListener(BusinessActivityNew.this);
        tv_gomme = dialogView.findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(BusinessActivityNew.this);
        Image1 = dialogView.findViewById(R.id.image1);
        Image1.setOnClickListener(BusinessActivityNew.this);
        Image2 = dialogView.findViewById(R.id.image2);
        Image2.setOnClickListener(BusinessActivityNew.this);
        Image3 = dialogView.findViewById(R.id.image3);
        Image3.setOnClickListener(BusinessActivityNew.this);


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

                if (isUserStartSession) {
                    alertDialog.dismiss();

                } else {
                    alertDialog.dismiss();
                    ReFreash_onCreatemethod();
                }


            }
        });
    }


    private void closeRideAlert_popUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.buisness_err_alert, viewGroup, false);

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

                showDamagePopUp();
            }
        });
    }

    private void fourth_PopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.buiseness_grazie, viewGroup, false);

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

                closeRideAlert_popUp();

            }
        });
    }


    private void getGeofencing() {


        Geofence geofence = new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID) // Geofence ID
                .setCircularRegion(Constants.LATITUDE, Constants.LATITUDE, GEOFENCE_RADIUS) // defining fence region
                .setExpirationDuration(GEO_DURATION) // expiring date
                // Transition types that it should look for
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

    }

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Create a Geofence list
     */

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Log.e(TAG, "onConnectionFailed");
                }
            };


    private void setUpView() {
        colayout = (CoordinatorLayout) findViewById(R.id.maps_colayout);
        bottomSheet = colayout.findViewById(R.id.bottom_sheet_bike);
        bottomSheet.setVisibility(View.GONE);

        first_bikeLayout = (RelativeLayout) colayout.findViewById(R.id.bike_first_layout);

//-------Bike buttons Layout-------
        social_bike_layout_one = (RelativeLayout) colayout.findViewById(R.id.social_layout_bike);
        social_bike_layout_one.setVisibility(View.GONE);

        call_bike_imageview = (ImageView) findViewById(R.id.call_img_bike);
        whatsUp_bike_imageview = (ImageView) findViewById(R.id.whatsup_img_bike);

        call_bike_imageview.setOnClickListener(this);
        whatsUp_bike_imageview.setOnClickListener(this);

//-------Bike buttons Layout-------


        //-------Play buttons Layout-------
        social_play_layout = (RelativeLayout) colayout.findViewById(R.id.social_layout_playbuttons);
        _play_layout = (LinearLayout) colayout.findViewById(R.id.playlayout);
        _play_layout.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) colayout.findViewById(R.id.progressbar);
        mProgressBar.setProgress(i);

        imageOne = (ImageView) colayout.findViewById(R.id.image_one);

        _stopRide = (ImageView) colayout.findViewById(R.id.stop_ride);
        _stopRide.setOnClickListener(this);

        call_play_imageview = (ImageView) findViewById(R.id.call_img_play);
        whatsUp_play_imageview = (ImageView) findViewById(R.id.whatsup_img_play);

        call_play_imageview.setOnClickListener(this);
        whatsUp_play_imageview.setOnClickListener(this);


//-------Play buttons Layout-------

       /* sb = colayout.findViewById(R.id.normal_green_slide);
        sb.setOnSeekBarChangeListener(this);*/
    }


    private void UserInformation() {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        UserInformation geo_details = retrofit.create(UserInformation.class);

        Call<UserInfoResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            UserInfoResponse userinfo = response.body();
                            //Log.e(TAG, "--ServiceID---"+userinfo.getData().getUser().getServiceId());
                            Integer _siteID = userinfo.getData().getUser().getSiteId();
                            Log.e(TAG, "--_siteID---" + _siteID);

                            PreferenceUtil.getInstance().saveInt(context, Constants.SITE_ID, _siteID);
                            if (_siteID == null) {

                                getSiteId_service();

                            } else {
                                Log.e(TAG, "--_siteID--xx-" + _siteID);
                                CallVehicleService(_siteID);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void getSiteId_service() {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        GetSiteID_Interface geo_details = retrofit.create(GetSiteID_Interface.class);

        Call<SiteIDResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, Constants.LATITUDE, Constants.LONGITUDE);
        resultRes.enqueue(new Callback<SiteIDResponse>() {
            @Override
            public void onResponse(Call<SiteIDResponse> call, Response<SiteIDResponse> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            int siteID = 0;
                            SiteIDResponse res = response.body();
                            List<SiteIDResponse.Data> data_array = res.getData();
                            for (int k = 0; k < data_array.size(); k++) {
                                siteID = data_array.get(k).getServiceId();
                            }

                            Log.e(TAG, "SITE ID-=-==--" + siteID);
                            if (siteID != 0) {
                                Call_ActivatUserSite(siteID);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }
            }

            @Override
            public void onFailure(Call<SiteIDResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void Call_ActivatUserSite(int _siteID) {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        ActivateSiteInterface geo_details = retrofit.create(ActivateSiteInterface.class);

        Call<JSONObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, _siteID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                    }


                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void CallVehicleService(final int siteID) {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        GetVehicleInterface geo_details = retrofit.create(GetVehicleInterface.class);

        Call<VehicleResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, siteID);
        resultRes.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {


                        VehicleResponse res = response.body();
                        vehicle_data_array = new ArrayList<VehicleResponse.Data>();
                        vehicle_data_array = res.getData();


                        ShowMarkerVehiclesOnMap(vehicle_data_array);


                       /* for (int i = 0; i < vehicle_data_array.size(); i++) {



                             vehicleID = vehicle_data_array.get(i).getId();

                            Log.e(TAG, "isAvailable---"+vehicle_data_array.get(i).getExternalStatus().getAvailable());

                            boolean _vehicleStatus = vehicle_data_array.get(i).getExternalStatus().getAvailable();

                            if (_vehicleStatus) {
                                double _lat = vehicle_data_array.get(i).getLatitude();
                                double _lng = vehicle_data_array.get(i).getLongitude();
                                _type = vehicle_data_array.get(i).getType();
                                String _licencePlate = vehicle_data_array.get(i).getLicensePlate();

                                Log.e(TAG, "LATLNG---" + _lat + " " + _lng);

                                MyLatLng bean = new MyLatLng();
                                bean.setTitle(_type);
                                bean.setLat(_lat);
                                bean.setLng(_lng);
                                bean.setLicense_palte(_licencePlate);
                                arrayList.add(bean);
                            }


                        }
                        if (arrayList != null) {
                            LoadingGoogleMap(arrayList);



                        }

                        Geofenceing_Service(siteID);*/

                    }


                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void ShowMarkerVehiclesOnMap(List<VehicleResponse.Data> vehicle_data_array) {
        arrayList = new ArrayList<MyLatLng>();


        for (int i = 0; i < vehicle_data_array.size(); i++) {

            vehicleID = vehicle_data_array.get(i).getId();

            Log.e(TAG, "isAvailable---" + vehicle_data_array.get(i).getExternalStatus().getAvailable());

            boolean _vehicleStatus = vehicle_data_array.get(i).getExternalStatus().getAvailable();

            if (_vehicleStatus) {
                double _lat = vehicle_data_array.get(i).getLatitude();
                double _lng = vehicle_data_array.get(i).getLongitude();
                _type = vehicle_data_array.get(i).getType();
                String _licencePlate = vehicle_data_array.get(i).getLicensePlate();

                Log.e(TAG, "LATLNG---" + _lat + " " + _lng);

                MyLatLng bean = new MyLatLng();
                bean.setTitle(_type);
                bean.setLat(_lat);
                bean.setLng(_lng);
                bean.setLicense_palte(_licencePlate);
                arrayList.add(bean);
            }


        }
        if (arrayList != null) {
            LoadingGoogleMap(arrayList);
        }

        int siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
        Geofenceing_Service(siteID);
    }

    private void Geofenceing_Service(int siteID) {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        GeofencingInterface geo_details = retrofit.create(GeofencingInterface.class);

        Call<GeoFenceResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, siteID);
        resultRes.enqueue(new Callback<GeoFenceResponse>() {
            @Override
            public void onResponse(Call<GeoFenceResponse> call, Response<GeoFenceResponse> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                    }


                }
            }

            @Override
            public void onFailure(Call<GeoFenceResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void showProgressbar() {
        progressbar_hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface
                                                 dialogInterface) {
                        Toast.makeText(context, "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();
                    }
                });
        progressbar_hud.show();


    }

    private void dismissProgressbar() {
        progressbar_hud.dismiss();
    }

    public void initGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * Create a Geofence list
     */


    private GeofencingRequest getGeofencingRequest() {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();

    }

    public void createGeofences(double latitude, double longitude) {

        String id = UUID.randomUUID().toString();
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, 200) // Try changing your radius
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Log.e(TAG, "==ssss===");

                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomSheet.setVisibility(View.GONE);
                }


            }
        });

    }


    void LoadingGoogleMap(ArrayList<MyLatLng> arrayList) {
        if (mMap != null) {
            mMap.clear();
            mSelectedMarker = null;

            bottomSheet.setVisibility(View.GONE);
            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer.onFinish();

            }

            //  mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //  mMap.setMyLocationEnabled(true);
            //   mMap.getUiSettings().setZoomControlsEnabled(true);

            ArrayList<MyLatLng> myLatLng = new ArrayList<MyLatLng>();

            if (arrayList.size() > 0) {
                try {
                    listMyLatLng = new ArrayList<LatLng>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        MyLatLng bean = arrayList.get(i);
                        if (bean.getLat() > 0 && bean.getLng() > 0) {
                            double _lat = bean.getLat();
                            double _lon = bean.getLng();

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_dot);

                            myMarker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(_lat, _lon))
                                    .icon(icon));
                            //Set Zoom Level of Map pin
                            LatLng object = new LatLng(_lat, _lon);
                            listMyLatLng.add(object);

                            MyLatLng latlng = new MyLatLng();
                            latlng.setLat(_lat);
                            latlng.setLng(_lon);
                            myLatLng.add(latlng);


                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 17));


                        }


                    }


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        bottomSheet.setVisibility(View.VISIBLE);

                        if (vehicle_data_array != null) {

                            if (null != mSelectedMarker) {
                                if (_isDispalyedMarker == false) {
                                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_dot));

                                }
                            }
                            mSelectedMarker = marker;
                            if (_isDispalyedMarker == false) {
                                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_pink));

                            }

                           /* if (null != mSelectedMarker) {
                                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green));
                            }
                            mSelectedMarker = marker;

                                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_small));*/


                            showinMarkers(marker);
                        }

                        return false;
                    }


                });


            }

            GeofenceFunction(myLatLng);

        } else {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    private void GeofenceFunction(ArrayList<MyLatLng> arrayList) {

        LatGeo = arrayList.get(0).getLat();
        // System.out.println("kkkkk"+Lat);

        LngGeo = arrayList.get(0).getLng();
        // System.out.println("kkkkk"+Lng);
        LatLng latLng = new LatLng(LatGeo, LngGeo);
        markerForGeofence(latLng);

        startGeofence(LatGeo, LngGeo);
    }

    private void startGeofence(double latGeo, double lngGeo) {

        Log.i(TAG, "startGeofence()");
        if (geoFenceMarker != null) {
            Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest);
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    private void addGeofence(GeofencingRequest geofenceRequest) {

        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    geofenceRequest,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    private PendingIntent createGeofencePendingIntent() {

        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private boolean checkPermission() {

        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private Geofence createGeofence(LatLng position, float geofenceRadius) {

        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(LatGeo, LngGeo, GEOFENCE_RADIUS)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    private void showinMarkers(final Marker marker) {


        for (int i = 0; i < vehicle_data_array.size(); i++) {
            if (marker.getPosition().latitude == vehicle_data_array.get(i).getLatitude()) {
                slideLayout = colayout.findViewById(R.id.slide_layout);
                timer_buttons_Layout = colayout.findViewById(R.id.timeandbuttons);


                final Button green_btn = (Button) findViewById(R.id.sblocca);
                final Button continue_btn = (Button) findViewById(R.id.btn_avela);

                if (isGreenButtonVisible) {
                    green_btn.setVisibility(View.VISIBLE);

                } else {
                    green_btn.setVisibility(View.GONE);

                }

                if (isSlideViewClicked) {
                    timer_buttons_Layout.setVisibility(View.VISIBLE);

                } else {
                    timer_buttons_Layout.setVisibility(View.GONE);

                }


                final TextView timer_bottom = (TextView) findViewById(R.id.timer);
                TextView _bike_number_bottom = (TextView) findViewById(R.id.bike_number);
                TextView _km = (TextView) findViewById(R.id.km);
                TextView _price = (TextView) findViewById(R.id.price);
                TextView _battery = (TextView) findViewById(R.id.battery);
                TextView _model_type = (TextView) findViewById(R.id.model_name);

                final Button avali_btn = colayout.findViewById(R.id.btn_avela);
                avali_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });

                green_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SessionStart();

                        showDamagePopUp();


                    }
                });

                continue_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showErrorPopUp();

                    }
                });

                //By default set BottomSheet Behavior as Collapsed and Height 0
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                // mBottomSheetBehavior.setPeekHeight(0);
                //If you want to handle callback of Sheet Behavior you can use below code

                _bike_number_bottom.setText("" + vehicle_data_array.get(i).getLicensePlate());
                _km.setText("" + vehicle_data_array.get(i).getKm() + "km");
                _price.setText("€" + vehicle_data_array.get(i).getService().getPrice());
                _battery.setText("" + vehicle_data_array.get(i).getBattery().getAhTot() + "%");

                String address = getCompleteAddressString(vehicle_data_array.get(i).getLatitude(), vehicle_data_array.get(i).getLongitude());

                Log.e(TAG, "Address===" + address);


                _model_type.setText("" + address);


                sb = colayout.findViewById(R.id.normal_pink_slide);

                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        DecimalFormat decimalFormat = new DecimalFormat("0.00%");

                        // Calculate progress value percentage.
                        float progressPercentageFloat = (float) progress / (float) seekBar.getMax();
                        progressPercentage = decimalFormat.format(progressPercentageFloat);

                        Log.e(TAG, "Percentage----" + progressPercentage);






                       /* Double _progress = Double.parseDouble(progressPercentage);
                        if(_progress<=50){
                            sb.setBackgroundResource(R.drawable.slidefull);
                            sb.getThumb().mutate().setAlpha(255);
                            seekBar.setProgress(0);
                        }else{
                            seekBar.setProgress(100);
                            sb.getThumb().mutate().setAlpha(0);
                            sb.setBackgroundResource(R.drawable.ic_greenslideafter);
                            isSlideViewClicked = true;


                            seekBar.setProgress(0);
                        }*/

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {


                        if (seekBar.getProgress() < 80) {
                            sb.setBackgroundResource(R.drawable.ic_pink_slide_one);


                            sb.getThumb().mutate().setAlpha(255);


                        } else {
                            seekBar.setProgress(100);
                            sb.getThumb().mutate().setAlpha(0);
                            sb.setBackgroundResource(R.drawable.ic_pink_slider_two);
                            isSlideViewClicked = true;


                            seekBar.setProgress(0);
                        }
                        isSlideViewClicked = true;

                        slideLayout.setVisibility(View.GONE);
                        timer_buttons_Layout.setVisibility(View.VISIBLE);


                        Log.e(TAG, "show loc--LATLNG---" + marker.getPosition().latitude + "--" + marker.getPosition().longitude);
                        social_bike_layout_one.setVisibility(View.VISIBLE);


                        Log.e(TAG, "show loc--LATLNG---" + marker.getPosition().latitude + "--" + marker.getPosition().longitude);

                        MyLatLng select_lat = new MyLatLng();
                        select_lat.setLat(marker.getPosition().latitude);
                        select_lat.setLng(marker.getPosition().longitude);

                        selected_loc_arrayList.add(select_lat);

                        //---After draging the bottom  slide view button
                        DisplaySelectedMarker(selected_loc_arrayList, marker);

                        startFreeTime(timer_bottom, green_btn);

                    }
                });


                openBottomScreen();

            }


        }

    }


    //=====This method call 2 times
    private void SessionStart() {
        _play_layout.setVisibility(View.VISIBLE);
        first_bikeLayout.setVisibility(View.GONE);


        StartRideSession();
    }

    private void showErrorPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.buisness_alert_attenzione, viewGroup, false);

        Button _btn_done = (Button) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);
        Button _cancel = (Button) dialogView.findViewById(R.id.btn_cancel);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog _alertDialog = builder.create();

        _alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        _alertDialog.setCancelable(true);
        _alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);


        _alertDialog.show();


        _btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _alertDialog.dismiss();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _alertDialog.dismiss();

                isUserStartSession = true;

                if (yourCountDownTimer != null) {
                    yourCountDownTimer.cancel();
                    yourCountDownTimer.onFinish();

                }


                SessionStart();


                showDamagePopUp();


            }
        });
        _cancel.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           _alertDialog.dismiss();

                                           mMap.clear();

                                           ReFreash_onCreatemethod();


                                       }
                                   }


        );

    }

    private void ReFreash_onCreatemethod() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
//Code for recreate
            recreate();

        } else {
//Code for Intent
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }


        if (yourCountDownTimer != null) {
            yourCountDownTimer.cancel();
            yourCountDownTimer.onFinish();

        }


    }


    private void DisplaySelectedMarker(ArrayList<MyLatLng> selected_loc_arrayList, Marker marker) {
        arrayList.clear();
        mMap.clear();

        mSelectedMarker = marker;
        _isDispalyedMarker = true;

        if (selected_loc_arrayList != null) {
            try {
                listMyLatLng = new ArrayList<LatLng>();
                for (int i = 0; i < selected_loc_arrayList.size(); i++) {
                    MyLatLng bean = selected_loc_arrayList.get(i);
                    if (bean.getLat() > 0 && bean.getLng() > 0) {
                        double _lat = bean.getLat();
                        double _lon = bean.getLng();

                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_pink);

                        myMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(_lat, _lon))
                                .icon(icon));
                        //Set Zoom Level of Map pin
                        LatLng object = new LatLng(_lat, _lon);


                        listMyLatLng.add(object);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12.0f));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 17));


                    }

                }

                GeofenceFunction(selected_loc_arrayList);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            DrawPolyLine(listMyLatLng);
        }


    }

    private void DrawPolyLine(ArrayList<LatLng> polyline_array) {
        Log.e(TAG, "Size---" + polyline_array.size());

        // LatLng source = new LatLng(polyline_array.get(0).latitude, polyline_array.get(0).longitude);
        LatLng destination = new LatLng(45.49010086, 9.1693449);
        LatLng source = new LatLng(45.482089, 9.181560);
        LatLng ll2 = new LatLng(45.478241, 9.173645);
        LatLng ll3 = new LatLng(45.479264, 9.183511);

        polyline_array.add(destination);
        polyline_array.add(source);
        polyline_array.add(ll2);
        polyline_array.add(ll3);


        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.pink));
        polylineOptions.width(5);

        polylineOptions.addAll(polyline_array);
        mMap.addPolyline(polylineOptions);


        // new GoogleMapsPath(HomeActivity.this, mMap, source, destination);


        //----------Need to Update with Current Location------------------
        //polyline_array.add(la);
        //polyline_array.add(laa);

        /*if (polyline_array.size() >= 2) {
            LatLng origin = polyline_array.get(0);
            LatLng dest = polyline_array.get(1);

            Log.e(TAG, "origin" + origin);
            Log.e(TAG, "dest" + origin);

            MarkerOptions markerOptions = new MarkerOptions();


            markerOptions.position(polyline_array.get(1));

            // Setting titile of the infowindow of the marker
            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_small);
            markerOptions.icon(icon1);

            PolylineOptions polylineOptions = new PolylineOptions();


            polylineOptions.color(Color.GREEN);

            polylineOptions.width(3);


            polyline_array.add(polyline_array.get(0));

            polylineOptions.addAll(polyline_array);

            mMap.addPolyline(polylineOptions);


            mMap.addMarker(markerOptions);

        }*/

    }
    // Fetches data from url passed

    /**
     * A method to download json data from url
     */

    public void requestDirection() {
        final LatLng destination = new LatLng(45.49010086, 9.1693449);
        final LatLng origin = new LatLng(45.482089, 9.181560);
        String serverKey = context.getResources().getString(R.string.google_maps_key);
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            mMap.addMarker(new MarkerOptions().position(origin));
                            mMap.addMarker(new MarkerOptions().position(destination));

                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            mMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, Color.RED));
                            // setCameraWithCoordinationBounds(route);


                        } else {
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }


    private void startFreeTime(final TextView _timerTV, final Button _greenButton) {
        yourCountDownTimer = new CountDownTimer(Constants.FIVFTEEN_MINUTES, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                _timerTV.setText("" + String.format("%d:%d min",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                i++;
                mProgressBar.setProgress((int) i * 100 / (5000 / 1000));

                //1 min 55 seconds
                if ((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) == Constants.FOURTEEN) &&
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) == Constants.FIFTY_NINE) {

                    showErrorPopUp();


                }

            }

            public void onFinish() {
                // _tv.setText("START");

                i++;
                mProgressBar.setProgress(100);

                isGreenButtonVisible = true;
                _timerTV.setVisibility(View.GONE);
                _greenButton.setVisibility(View.VISIBLE);
            }

        }.start();
    }


    private void openBottomScreen() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheet.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPolygonClick(Polygon polygon) {

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onResult(@NonNull Status status) {

        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            saveGeofence();
            drawGeofence();
        } else {
            // inform about fail
        }


    }

    private void markerForGeofence(LatLng latLng) {

        //Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if (mMap != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mMap.addMarker(markerOptions);

        }

        geoFenceMarker.remove();

    }

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");
        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(getResources().getColor(R.color.geofence_pink))
                .fillColor(getResources().getColor(R.color.geofence_pink))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = mMap.addCircle(circleOptions);
    }


    private void saveGeofence() {

        Log.d(TAG, "saveGeofence()");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(KEY_GEOFENCE_LAT, Double.doubleToRawLongBits(geoFenceMarker.getPosition().latitude));
        editor.putLong(KEY_GEOFENCE_LON, Double.doubleToRawLongBits(geoFenceMarker.getPosition().longitude));
        editor.apply();

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
                if (Images == false) {
                    if (ContextCompat.checkSelfPermission(BusinessActivityNew.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivityNew.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivityNew.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivityNew.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivityNew.this,
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
                    dialogView_image = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);


                    sliderPagerAdapter = new SliderPagerAdapter(BusinessActivityNew.this, slider_image_list1, imagePojjo);
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
                                dots[i] = new TextView(BusinessActivityNew.this);
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
            case R.id.image2:

                if (Images1 == false) {
                    if (ContextCompat.checkSelfPermission(BusinessActivityNew.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivityNew.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivityNew.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivityNew.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivityNew.this,
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
                    dialogView_image = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);

                    sliderPagerAdapter = new SliderPagerAdapter(BusinessActivityNew.this, slider_image_list1, imagePojjo);
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
                                dots[i] = new TextView(BusinessActivityNew.this);
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
                    if (ContextCompat.checkSelfPermission(BusinessActivityNew.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivityNew.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivityNew.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivityNew.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivityNew.this,
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
                    dialogView_image = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.viewpager, viewGroup, false);


                    vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
                    ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);


                    sliderPagerAdapter = new SliderPagerAdapter(BusinessActivityNew.this, slider_image_list1, imagePojjo);
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
                                dots[i] = new TextView(BusinessActivityNew.this);
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


            //========For Stop Ride========
            case R.id.stop_ride:
                stopRide_ServiceCall();
                break;


            case R.id.call_img_bike:
                phoneCall_method();
                break;
            case R.id.call_img_play:
                phoneCall_method();
                break;
            case R.id.whatsup_img_bike:
                whatsUpCall_method();
                break;
            case R.id.whatsup_img_play:
                whatsUpCall_method();
                break;


        }


    }

    private void whatsUpCall_method() {

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "HI Govolt");
        try {

            startActivity(whatsappIntent);

        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(getApplicationContext(), "Whatsap not installed", Toast.LENGTH_SHORT).show();

        }
    }

    private void phoneCall_method() {
        String phone = "0283432432";
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(phoneIntent);

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
                        ActivityCompat.requestPermissions(BusinessActivityNew.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
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


                    int pos = position;

                    ImageRemover(pos, slider_image_list1, imagePojjo, activity);


                    Log.e(TAG, "POS---" + imagePojjo.get(position).getId());


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


    private void StartRideSession() {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StartRideSession geo_details = retrofit.create(StartRideSession.class);


        Call<JSONObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, vehicleID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, BusinessActivityNew.this);
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void stopRide_ServiceCall() {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StopRideSession geo_details = retrofit.create(StopRideSession.class);


        Call<JsonObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, vehicleID);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());

                    isUserStartSession = false;
                    if (response.code() != 200) {

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, BusinessActivityNew.this);

                        ShowInfoAlert();
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            ShowInfoAlert();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void ShowInfoAlert() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivityNew.this).inflate(R.layout.business_recorda, viewGroup, false);

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
                fourth_PopUp();
            }
        });

    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");


               /*for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");

                }
               strAdd = strReturnedAddress.toString();*/
                //Log.w("loction address", strReturnedAddress.toString());

                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    strAdd = city;
                }

            } else {
                Log.w("loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("loction address", "Canont get Address!");
        }
        return strAdd;
    }
}
