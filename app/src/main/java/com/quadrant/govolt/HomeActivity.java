package com.quadrant.govolt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
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
import android.widget.EditText;
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
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtilsStartRide;
import com.quadrant.govolt.Others.GeofenceTrasitionService;
import com.quadrant.govolt.Others.MyLatLng;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.ActivateSiteInterface;
import com.quadrant.interfaces.DeleteRideSession;
import com.quadrant.interfaces.GeofencingInterface;
import com.quadrant.interfaces.GetSiteID_Interface;
import com.quadrant.interfaces.GetVehicleInterface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.StartRideSession;
import com.quadrant.interfaces.StopRideSessionTwo;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.locationtracker.LocationUpdatesService;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.response.GeoFenceResponse;
import com.quadrant.response.SiteIDResponse;
import com.quadrant.response.StartRideErrorResponse;
import com.quadrant.response.UserInfoResponse;
import com.quadrant.response.VehicleResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.quadrant.govolt.UploadImageActivity.getFromPref;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, View.OnClickListener {
    private static final int CAMERAINTENTCUSTOM = 3210;
    private Context context;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FloatingActionMenu fab_menu;
    private ArrayList<LatLng> listMyLatLng;
    private ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();
    private ArrayList<MyLatLng> selected_loc_arrayList = new ArrayList<MyLatLng>();
    private Marker mSelectedMarker;
    private Marker myMarker;
    private static final String TAG = HomeActivity.class.getSimpleName();

    boolean Vehiclebooked;

    private TextView autonomiakm;

    private BottomSheetBehavior mBottomSheetBehavior;
    String Vehicleplate;
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
    private static final List<PatternItem> PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP);
    private List<Geofence> mGeofenceList;
    private GoogleApiClient mGoogleApiClient;
    private KProgressHUD progressbar_hud;
    private AlertDialog alertDialog, alertDialog_image;
    private View dialogView, dialogView_image;

    private List<VehicleResponse.Data> vehicle_data_array, vechicle_data;
    private TextView _bike_number_bottom;
    private CoordinatorLayout colayout;
    private View bottomSheet, bottomSheetkick;
    private ImageView img_kickcall;
    private String imagePath_recorda = "";
    private boolean isSlideViewClicked = false;
    private boolean _isDispalyedMarker = false;
    String PriceEuro;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private static int KICK_MAX_KM = 22;
    private static int SCOOTER_MAX_KM = 80;


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
    private LinearLayout _play_layout, _kick_bottomsheet;
    private RelativeLayout social_bike_layout_one;
    private RelativeLayout social_play_layout;
    private ImageView imageOne;
    TextView tv_bauletto, tv_selia, tv_cavaletto, tv_carena, tv_caschi, tv_altro, tv_speech, tv_levafreno, tv_parafango, tv_freece, tv_luci, tv_gomme, tvKickBattery;
    CircleImageView Image1, Image2, Image3, Image4, Image5, Image6;
    boolean buttonPressed = false;
    boolean buttonPressedcaraletto = false;
    boolean buttonPressedcarena = false;
    boolean buttonPressedbauletto = false;
    boolean buttonPressedselia = false;
    boolean buttonPressedcaschi = false;
    boolean buttonPressedaltro = false;
    boolean buttonPressedspecchietti = false;
    boolean buttonPressedlevafreno = false;
    boolean buttonPressedparafango = false;
    boolean buttonPressedfreece = false;
    boolean buttonPressedLuci = false;
    boolean buttonPressedgomme = false;


    private Polyline currentPolyline;

    ArrayList<String> arrlist = new ArrayList<String>();


    private static final int CAMERA_REQUEST = 1888;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    private TextView[] dots;
    int page_position = 0;
    boolean Images = false;
    boolean Images2 = false;
    boolean Images1 = false;
    boolean Images4 = false;
    boolean Images5 = false;
    boolean Images6 = false;
    private boolean isPlayPauseClicked = false;
    private ImageView _stopRide, img_ic_information, img_VpClose;
    private ImageView bikeHeart_break, playHeart_break, _playPause;
    TextView _palypause_label;


    private boolean isUserStartSession = false;

    private String _vehicleStatus = "";
    LinearLayout slideLayout, timer_buttons_Layout;
    private Marker my_marker;
    private ImageView call_bike_imageview, whatsUp_bike_imageview, call_play_imageview, whatsUp_play_imageview, Bike_Logo;

    SeekBar sb, sb_kick;
    private boolean isGreenButtonVisible = false;
    private CountDownTimer yourCountDownTimer;
    String _type = "";
    private String progressPercentage;

    ArrayList<LatLng> markerPoints;
    private SimpleLocation location;

    private ImageView current_loc_button;
    private double CURRENT_LATITUDE;
    private double CURRENT_LANGITUDE;
    TextView _time;
    TextView _distance;
    private String currentLocationID;
    private String pastLocationID = "";
    int _percentageOfsb = 0;
    private int _vehicleID, validDocument;
    private String _vehicleRideStatus;
    private int _vehiclePastID;

    double _vehicleLastLatitude;
    double _vehicleLastLongitude;

    private int userBookingCount = 0;
    private int licenseCount = 0;
    private int avatharCount = 0;
    private int documentCount = 0;
    private String walk_time = "";
    private String freewalkTime = "5";
    //private OnLocationUpdatedListener locationListener;
    Runnable runnable;

    LocationManager locationManager;
    String mprovider;
    int damageID;
    private String imagePathOne;
    private String imagePathTwo;
    private String imagePathThree;

    private String imagePathFour;
    private String imagePathFive;
    private String imagePathSix;

    private Bitmap recordo_mphoto;

    //======SessionRide DetailsFor Alert
    int km = 0;
    String _startTime = "0";
    String _endTime = "0";
    int _durationMinutes = 0;
    int _distanceKm = 0;
    String price = "";
    private EditText commentbox;
    private CircleImageView image_recorda;
    private ImageView heart_recodra;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final String MESSENGER_INTENT_KEY = "msg-intent-key";
    private IncomingMessageHandler mHandler;
    // private boolean isVehiclePauseServiceCalled = false;
    private double TRACK_USER_CURRENT_LATITUDE, TRACK_USER_CURRENT_LANGITUDE;
    List<List<Double>> list_list_points = null;
    boolean isRecodraImage = false;
    List<Double> double_list;
    List<Double> all_double_list;
    ArrayList<LatLng> firstLatLng = new ArrayList<LatLng>();
    private TextView timer_bottom;
    private Button green_btn;
    private ProgressBar progressbar;
    TextView tv_reserved;
    private String selectedScooterType;
    private SeekBar sbKickScooter;
    public static final int REQUEST_QR_CODE = 1234;
    private ImageView ic_type, _stopKick;
    private boolean isFromQrCodescreen;
    private int userRideStatus = 0; // 0 for free, 1 for in a booking, 2 while in a ride
    private View ivLeft, ivRight;
    private Marker currentLocatinMarker, destinationMarker;
    private List<LatLng> points;
    private Polyline polyline;
    private boolean isPolyLoaded;
    private TreeMap<Integer, Bitmap> damageList;

    public void ImageRemover(final int removeID, final int pos, Activity activity) {

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
        final Dialog alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);
        alertDialog.getWindow().setDimAmount(0.5f);
        alertDialog.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_image.dismiss();
                removeImage(removeID, pos);
                alertDialog.dismiss();


            }
        });


        tv_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_image.dismiss();
                alertDialog.dismiss();
                openCamera(removeID);

            }
        });

    }

    private void removeImage(final int removeID, final int pos) {
        if (damageList.size() > 0) {
            damageList.remove(removeID);

            Log.e(TAG, "removeID===" + removeID);
            if (Images == true) {
                if (removeID == 1) {
                    imagePathOne = "";
                    Image1.setImageResource(R.drawable.ic_camera_icon_one);
                    Images = false;


                }
            }

            if (Images1 == true) {
                if (removeID == 2) {
                    imagePathTwo = "";
                    Image2.setImageResource(R.drawable.ic_camera_icon_one);
                    Images1 = false;
                }
            }


            if (Images2 == true) {
                if (removeID == 3) {
                    imagePathThree = "";
                    Image3.setImageResource(R.drawable.ic_camera_icon_one);
                    Images2 = false;
                }
            }

            if (Images4 == true) {
                if (removeID == 1) {
                    imagePathFour = "";
                    Image4.setImageResource(R.drawable.ic_camera_icon_one);
                    Images4 = false;
                }
            }

            if (Images5 == true) {
                if (removeID == 2) {
                    imagePathFive = "";
                    Image5.setImageResource(R.drawable.ic_camera_icon_one);
                    Images5 = false;
                }
            }

            if (Images6 == true) {
                if (removeID == 3) {
                    imagePathSix = "";
                    Image6.setImageResource(R.drawable.ic_camera_icon_one);
                    Images6 = false;
                }
            }

        }

        sliderPagerAdapter.notifyDataSetChanged();
    }


    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }


   /* public static void ImageRemover(int pos, ArrayList<Bitmap> image_arraylist) {
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        context = this;


        if (Vehiclebooked == true) {

            Toast.makeText(context, "Vehicle booked.", Toast.LENGTH_SHORT).show();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        damageList = new TreeMap<>();
        createGoogleApi();


        LinearLayout menu_ll = (LinearLayout) findViewById(R.id.menu_layout);
        menu_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SideMenu.class);
                startActivity(i);
            }
        });


        FirstTimeCurrentLocations();


        if (savedInstanceState == null) {

            mGeofenceList = new ArrayList<Geofence>();

            int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (resp == ConnectionResult.SUCCESS) {

                initGoogleAPIClient();

            } else {
                Log.e(TAG, "Your Device doesn't support Google Play Services.");
            }


        }


//        LocationTarckerServiceStart();

        points = new ArrayList<LatLng>();
        setUpView();

        //handling menu status (open or close)
        // final TextView filter_header = findViewById(R.id.filter_header);
        final View fab_view = findViewById(R.id.fab_view);
        fab_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    // showToast("Menu is opened");
                    // filter_header.setVisibility(View.VISIBLE);
                    fab_view.setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.whiteTransparent));
                } else {
                    // showToast("Menu is closed");
                    // filter_header.setVisibility(View.GONE);
                    fab_view.setBackgroundColor(0);
                }
            }
        });

        findViewById(R.id.fab_scooter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScooterType = SCOOTERTYPE.SCOOTER.getType();
                fab_menu.close(true);
                sbKickScooter.setVisibility(View.GONE);
                refreshList();
            }
        });

        findViewById(R.id.fab_kick_scooter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScooterType = SCOOTERTYPE.KICKSCOOTER.getType();
                fab_menu.close(true);
                sbKickScooter.setVisibility(View.VISIBLE);
                refreshList();

            }
        });

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if (_isInternetAvailable) {


            UserInformation();

        } else {
            Toast.makeText(context, "Please connect internet.", Toast.LENGTH_SHORT).show();

        }

//        LocationTarckerServiceStart();

//saikk
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

                        if (userRideStatus != 2) {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            bottomSheet.setVisibility(View.GONE);
                        } else {
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            bottomSheet.setVisibility(View.VISIBLE);
                        }

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


        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TrunckOpenService();
            }
        });
        _playPause = (ImageView) colayout.findViewById(R.id.play_pause);
        _palypause_label = (TextView) colayout.findViewById(R.id.palypause_label);
        _distance = colayout.findViewById(R.id.distance);
        _distance.setOnClickListener(HomeActivity.this);


        _playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// play
                if (isPlayPauseClicked) {
                    userRideStatus = 2;
                    myMarker.setVisible(false);
                    if (currentPolyline != null) {
                        currentPolyline.setVisible(false);
                    }
                    _playPause.setImageResource(R.drawable.ic_pause);
                    isPlayPauseClicked = false;
                    _palypause_label.setText("PAUSA");

                    resumeVehicleServiceCall();
// pause
                } else {
                    userRideStatus = 1;
                    myMarker.setVisible(true);

                    requestDirection(new LatLng(_vehicleLastLatitude, _vehicleLastLongitude));

                    isPlayPauseClicked = true;
                    _playPause.setImageResource(R.drawable.ic_play_icon);
                    //Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_LONG).show();
                    _palypause_label.setText("CORSA");
                    pauseVehicleServiceCall();

                }
            }
        });

    }

    private void LocationTarckerServiceStart() {
        mHandler = new IncomingMessageHandler();
        Intent startServiceIntent = new Intent(this, LocationUpdatesService.class);
        Messenger messengerIncoming = new Messenger(mHandler);
        startServiceIntent.putExtra(MESSENGER_INTENT_KEY, messengerIncoming);
        startService(startServiceIntent);
    }

    private void locationStopService() {
        stopService(new Intent(HomeActivity.this, LocationUpdatesService.class));

    }

    private void TrunckOpenService() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);


        Call<JSONObject> resultRes = geo_details.GetTrunckOpen(Constants.TOKEN, bearer_authorization, _vehicleID);
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

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, HomeActivity.this);

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


    private void FirstTimeCurrentLocations() {

        boolean requireFineGranularity = false;
        boolean passiveMode = false;
        long updateIntervalInMilliseconds = 1 * 60 * 1000;
        location = new SimpleLocation(context, requireFineGranularity, passiveMode, updateIntervalInMilliseconds);


        //------here update interval every 5 seconds
        //location = new SimpleLocation(this, true, true, 5000);
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
        CURRENT_LATITUDE = location.getLatitude();
        CURRENT_LANGITUDE = location.getLongitude();

        Log.e(TAG, "LAT-LNG---" + CURRENT_LATITUDE + "-" + CURRENT_LANGITUDE);


       /*CURRENT_LATITUDE = 17.451652;
       CURRENT_LANGITUDE = 78.391780;*/

       /* CURRENT_LATITUDE = 45.464204;  // Milan
        CURRENT_LANGITUDE = 9.189982;*/


    }


    private void showDamagePopUp() {


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        if (damageList.size() > 0) {
            damageList.clear();
        }

        Images = false;
        Images1 = false;
        Images2 = false;
        imagePathOne = "";
        imagePathTwo = "";
        imagePathThree = "";

        buttonPressedcaraletto = false;
        buttonPressedcarena = false;
        buttonPressedbauletto = false;
        buttonPressedselia = false;
        buttonPressedcaschi = false;
        buttonPressedaltro = false;
        buttonPressedspecchietti = false;
        buttonPressedlevafreno = false;
        buttonPressedparafango = false;
        buttonPressedfreece = false;
        buttonPressedLuci = false;
        buttonPressedgomme = false;


        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.business_segnalazione, viewGroup, false);
        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.buttonOk);
        TextView damageplate = (TextView) dialogView.findViewById(R.id.damageplate);
        tv_bauletto = dialogView.findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(HomeActivity.this);
        tv_selia = dialogView.findViewById(R.id.selia);
        tv_selia.setOnClickListener(HomeActivity.this);
        tv_cavaletto = dialogView.findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(HomeActivity.this);
        tv_carena = dialogView.findViewById(R.id.carena);
        tv_carena.setOnClickListener(HomeActivity.this);
        tv_caschi = dialogView.findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(HomeActivity.this);
        tv_altro = dialogView.findViewById(R.id.altro);
        tv_altro.setOnClickListener(HomeActivity.this);
        tv_speech = dialogView.findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(HomeActivity.this);
        tv_levafreno = dialogView.findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(HomeActivity.this);
        tv_parafango = dialogView.findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(HomeActivity.this);
        tv_freece = dialogView.findViewById(R.id.freece);
        tv_freece.setOnClickListener(HomeActivity.this);
        tv_luci = dialogView.findViewById(R.id.luci);
        tv_luci.setOnClickListener(HomeActivity.this);
        tv_gomme = dialogView.findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(HomeActivity.this);
        Image1 = dialogView.findViewById(R.id.image1);
        Image1.setOnClickListener(HomeActivity.this);
        Image2 = dialogView.findViewById(R.id.image2);
        Image2.setOnClickListener(HomeActivity.this);
        Image3 = dialogView.findViewById(R.id.image3);
        Image3.setOnClickListener(HomeActivity.this);
        commentbox = dialogView.findViewById(R.id.commentbox);
        ic_type = dialogView.findViewById(R.id.ic_type);
        updateDamageView();


        damageplate.setText(Vehicleplate);

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


                //  ReFreash_onCreatemethod();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrlist.size() > 0) {

                    alertDialog.dismiss();


                /*for (String value : arrlist) {
                    System.out.println("Value = " + value);
                }*/

                    PostDamageData();
                } else {
                    Toast.makeText(HomeActivity.this, "Si prega di selezionare qualsiasi opzione", Toast.LENGTH_SHORT).show();

                }
            }


        });
    }


    private void showDamagePopUpAfetrSessionComplete() {

        if (damageList.size() > 0) {
            damageList.clear();
        }
        Images4 = false;
        Images5 = false;
        Images6 = false;

        buttonPressedcaraletto = false;
        buttonPressedcarena = false;
        buttonPressedbauletto = false;
        buttonPressedselia = false;
        buttonPressedcaschi = false;
        buttonPressedaltro = false;
        buttonPressedspecchietti = false;
        buttonPressedlevafreno = false;
        buttonPressedparafango = false;
        buttonPressedfreece = false;
        buttonPressedLuci = false;
        buttonPressedgomme = false;

        imagePathFour = "";
        imagePathFive = "";
        imagePathSix = "";

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.business_segnalazione_finish, viewGroup, false);
        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.buttonOk);
        TextView Tv_plate = (TextView) dialogView.findViewById(R.id.damageplate_finish);
        tv_bauletto = dialogView.findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(HomeActivity.this);
        tv_selia = dialogView.findViewById(R.id.selia);
        tv_selia.setOnClickListener(HomeActivity.this);
        tv_cavaletto = dialogView.findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(HomeActivity.this);
        tv_carena = dialogView.findViewById(R.id.carena);
        tv_carena.setOnClickListener(HomeActivity.this);
        tv_caschi = dialogView.findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(HomeActivity.this);
        tv_altro = dialogView.findViewById(R.id.altro);
        tv_altro.setOnClickListener(HomeActivity.this);
        tv_speech = dialogView.findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(HomeActivity.this);
        tv_levafreno = dialogView.findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(HomeActivity.this);
        tv_parafango = dialogView.findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(HomeActivity.this);
        tv_freece = dialogView.findViewById(R.id.freece);
        tv_freece.setOnClickListener(HomeActivity.this);
        tv_luci = dialogView.findViewById(R.id.luci);
        tv_luci.setOnClickListener(HomeActivity.this);
        tv_gomme = dialogView.findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(HomeActivity.this);
        Image4 = dialogView.findViewById(R.id.image4);
        Image4.setOnClickListener(HomeActivity.this);
        Image5 = dialogView.findViewById(R.id.image5);
        Image5.setOnClickListener(HomeActivity.this);
        Image6 = dialogView.findViewById(R.id.image6);
        Image6.setOnClickListener(HomeActivity.this);

        ic_type = dialogView.findViewById(R.id.ic_type);
        commentbox = dialogView.findViewById(R.id.commentbox);
        updateDamageView();

        Tv_plate.setText(Vehicleplate);


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

                if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
//                    fourth_PopUp();
                    stopRide_ServiceCall();
                } else {
                    ReFreash_onCreatemethod();

                }


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrlist.size() > 0) {
                    alertDialog.dismiss();
                    if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                        stopRide_ServiceCall(true);
                    } else {
                        PostDamageDataAfetrSessionComplete();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Si prega di selezionare qualsiasi opzione", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void PostDamageData() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }

        showProgressbar();
        try {

            JSONObject jo = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray otherArray = new JSONArray();
            JSONObject jsonObj = null;

            final ArrayList<Integer> intArray = new ArrayList<Integer>();

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

                if (arrlist.size() > 0) {
                    jo.put("instantSpotted", arrlist.size());
                } else {
                    jo.put("instantSpotted", 0);
                }

                jo.put("parts", jsonArray);

                jo.put("description", commentbox.getText().toString());
                jo.put("other_damage", otherArray);

                Log.e("Suresh==--===", "req===" + jo.toString());


                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(jo.toString());

                String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
                String bearer_authorization = "Bearer " + authorization;


                final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                RequestInterface register_details = retrofit.create(RequestInterface.class);

                Call<JsonObject> resultRes = register_details.GetPostDammageRes(Constants.TOKEN, bearer_authorization, gsonObject, _vehicleID);
                resultRes.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                        if (!response.isSuccessful()) {
                            Log.e(TAG, "--Response code---" + response.code());
                            Log.e(TAG, "--Response ---" + response.body());


                            if (response.code() != 200) {

                            }


                        } else {
                            Log.e(TAG, "--Success---");


                            if (response.code() == 200) {
                                JsonObject jsonObject = response.body();
                                JsonObject damage_data = jsonObject.getAsJsonObject("data");
                                damageID = damage_data.get("id").getAsInt();

                                if (intArray.size() > 0) {

                                    if (imagePathOne != null) {
                                        if (!imagePathOne.equalsIgnoreCase("")) {
                                            AppUtils.UploadDamageFile_Api(context, imagePathOne, _vehicleID, damageID);

                                        }
                                    }


                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (imagePathTwo != null) {
                                                if (!imagePathTwo.equalsIgnoreCase("")) {
                                                    AppUtils.UploadDamageFile_Api(context, imagePathTwo, _vehicleID, damageID);


                                                }
                                            }
                                        }
                                    }, 2000);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (imagePathThree != null) {
                                                if (!imagePathThree.equalsIgnoreCase("")) {
                                                    AppUtils.UploadDamageFile_Api(context, imagePathThree, _vehicleID, damageID);


                                                }
                                            }
                                        }
                                    }, 4000);


                                }


                            }


                        }

                        try {
                            if (arrlist.size() > 0) {
                                arrlist.clear();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dismissProgressbar();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, "--Fail---" + t.getMessage());
                        dismissProgressbar();

                    }


                });


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (JSONException je) {

            je.printStackTrace();
        }


    }

    private void PostDamageDataAfetrSessionComplete() {
        try {
            showProgressbar();

            JSONObject jo = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray otherArray = new JSONArray();
            JSONObject jsonObj = null;

            final ArrayList<Integer> intArray = new ArrayList<Integer>();

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

                if (arrlist.size() > 0) {
                    jo.put("instantSpotted", arrlist.size());
                } else {
                    jo.put("instantSpotted", 0);
                }

                jo.put("parts", jsonArray);

                jo.put("description", commentbox.getText().toString());
                jo.put("other_damage", otherArray);

                Log.e("Suresh==--===", "req===" + jo.toString());


                JsonParser jsonParser = new JsonParser();
                JsonObject gsonObject = (JsonObject) jsonParser.parse(jo.toString());

                String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
                String bearer_authorization = "Bearer " + authorization;


                final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                RequestInterface register_details = retrofit.create(RequestInterface.class);

                Call<JsonObject> resultRes = register_details.GetPostDammageRes(Constants.TOKEN, bearer_authorization, gsonObject, _vehicleID);
                resultRes.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        // dismissProgressbar();

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "--Response code---" + response.code());
                            Log.e(TAG, "--Response ---" + response.body());
                            dismissProgressbar();

                            if (response.code() != 200) {

                            }


                        } else {
                            Log.e(TAG, "--Success---");


                            if (response.code() == 200) {
                                JsonObject res = response.body();

                                if (response.code() == 200) {
                                    JsonObject jsonObject = response.body();
                                    JsonObject damage_data = jsonObject.getAsJsonObject("data");
                                    damageID = damage_data.get("id").getAsInt();

                                    if (intArray.size() > 0) {
//                                        if (imagePathThree != null) {
//                                            if (!imagePathThree.equalsIgnoreCase("")) {
//                                                AppUtils.UploadDamageFile_Api(context, imagePathThree, _vehicleID, damageID);
//
//                                            }
//                                        }

                                        if (imagePathFour != null) {
                                            if (!imagePathFour.equalsIgnoreCase("")) {
                                                AppUtils.UploadDamageFile_Api(context, imagePathFour, _vehicleID, damageID);

                                            }
                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (imagePathFive != null) {
                                                    if (!imagePathFive.equalsIgnoreCase("")) {
                                                        AppUtils.UploadDamageFile_Api(context, imagePathFive, _vehicleID, damageID);

                                                    }
                                                }
                                            }
                                        }, 2000);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (imagePathSix != null) {
                                                    if (!imagePathSix.equalsIgnoreCase("")) {
                                                        AppUtils.UploadDamageFile_Api(context, imagePathSix, _vehicleID, damageID);

                                                    }
                                                }
                                            }
                                        }, 4000);
                                    }


                                }


                                try {
                                    if (arrlist.size() > 0) {
                                        arrlist.clear();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                dismissProgressbar();

                                if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                                    fourth_PopUp();
                                } else {
                                    ReFreash_onCreatemethod();

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


            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (JSONException je) {

            je.printStackTrace();
        }


    }

    private void closeRideAlert_popUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.buisness_err_alert, viewGroup, false);

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

                showDamagePopUpAfetrSessionComplete();
            }
        });
    }

    private void fourth_PopUp() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.buiseness_grazie, viewGroup, false);

        //ImageView _ic_correct = (ImageView) dialogView.findViewById(R.id.ic_correct);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.done_btn);


        TextView _starttime_label = (TextView) dialogView.findViewById(R.id.starttime_label);
        TextView _endtime_label = (TextView) dialogView.findViewById(R.id.endtime_label);
        TextView _minlabel = (TextView) dialogView.findViewById(R.id.minlabel);
        TextView _km_label = (TextView) dialogView.findViewById(R.id.km_label);

        //TextView _kg_label = (TextView)dialogView.findViewById(R.id.kg_label);
        TextView _price_label = (TextView) dialogView.findViewById(R.id.price_label);

        TextView _costo = (TextView) dialogView.findViewById(R.id.costo);

        TextView _controlla = (TextView) dialogView.findViewById(R.id.controlla);
        TextView _myrides = (TextView) dialogView.findViewById(R.id.myrides);


        if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
            _costo.setVisibility(viewGroup.GONE);
            _price_label.setTextSize(28);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _price_label.getLayoutParams();
            // View parent = (View)_price_label.getParent();
            // params.setMargins(parent.getMeasuredWidth() / 2 - _price_label.getMeasuredWidth() / 2, 0, 0, 0);
            params.setMargins(0, 0, 0, 0);
            _price_label.setLayoutParams(params);
            _controlla.setText("I dettagli in:");
            _myrides.setText("LE MIE CORSE");


        }

        // _kg_label.setText("");
        _km_label.setText("" + km + " metri");
        _starttime_label.setText("" + _startTime);
        _endtime_label.setText("" + _endTime);

        _minlabel.setText("" + _durationMinutes + " min");
        _price_label.setText("€ " + price);

/*
        // _kg_label.setText("");
        _km_label.setText(""+km);
        _starttime_label.setText(""+_startTime);
        _endtime_label.setText(""+_endTime);
        _minlabel.setText(""+_durationMinutes);
        _price_label.setText("€ "+price);*/


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

                //closeRideAlert_popUp();

                if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {


                    ReFreash_onCreatemethod();


                } else {

                    showDamagePopUpAfetrSessionComplete();
                }


            }
        });
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

        current_loc_button = (ImageView) findViewById(R.id.button3);

        current_loc_button.setOnClickListener(this);

        colayout = (CoordinatorLayout) findViewById(R.id.maps_colayout);
        bottomSheet = colayout.findViewById(R.id.bottom_sheet_bike);

        img_kickcall = (ImageView) findViewById(R.id.ic_kickcall);

        img_kickcall.setOnClickListener(this);


        img_ic_information = (ImageView) findViewById(R.id.info_kick);
        img_ic_information.setOnClickListener(this);


        bottomSheetkick = colayout.findViewById(R.id.bottom_sheet_bike_kick);
        bottomSheet.setVisibility(View.GONE);
        bottomSheetkick.setVisibility(View.GONE);

        first_bikeLayout = (RelativeLayout) colayout.findViewById(R.id.bike_first_layout);

        _kick_bottomsheet = (LinearLayout) colayout.findViewById(R.id.kick_bottomsheet);

        _kick_bottomsheet.setVisibility(View.GONE);

        tvKickBattery = colayout.findViewById(R.id.tvKickBattery);

//-------Bike buttons Layout-------
        social_bike_layout_one = (RelativeLayout) colayout.findViewById(R.id.social_layout_bike);
        social_bike_layout_one.setVisibility(View.GONE);

        call_bike_imageview = (ImageView) findViewById(R.id.call_img_bike);
        Bike_Logo = (ImageView) findViewById(R.id.bike_logo);
        whatsUp_bike_imageview = (ImageView) findViewById(R.id.whatsup_img_bike);
        bikeHeart_break = (ImageView) findViewById(R.id.bike_heart_break);

        bikeHeart_break.setOnClickListener(this);
        call_bike_imageview.setOnClickListener(this);
        whatsUp_bike_imageview.setOnClickListener(this);
        whatsUp_bike_imageview.setVisibility(View.GONE);
//-------Bike buttons Layout-------

        //-------Play buttons Layout-------
        social_play_layout = (RelativeLayout) colayout.findViewById(R.id.social_layout_playbuttons);
        _play_layout = (LinearLayout) colayout.findViewById(R.id.playlayout);
        _play_layout.setVisibility(View.GONE);

        imageOne = (ImageView) colayout.findViewById(R.id.image_one);

        autonomiakm = (TextView) colayout.findViewById(R.id.autonomiakm);

        _stopRide = (ImageView) colayout.findViewById(R.id.stop_ride);

        _stopKick = (ImageView) colayout.findViewById(R.id.ic_stop_kick);

        // _stopKick=(ImageView) colayout.findViewById(R.id.stop_ride);

        _stopRide.setOnClickListener(this);

        _stopKick.setOnClickListener(this);

        call_play_imageview = (ImageView) findViewById(R.id.call_img_play);
        whatsUp_play_imageview = (ImageView) findViewById(R.id.whatsup_img_play);
        playHeart_break = (ImageView) findViewById(R.id.play_heart_break);

        playHeart_break.setOnClickListener(this);
        call_play_imageview.setOnClickListener(this);
        whatsUp_play_imageview.setOnClickListener(this);
        whatsUp_play_imageview.setVisibility(View.GONE);

        progressbar = findViewById(R.id.progressbar);
        timer_bottom = (TextView) findViewById(R.id.timer);
        green_btn = (Button) findViewById(R.id.sblocca);

        slideLayout = colayout.findViewById(R.id.slide_layout);
        timer_buttons_Layout = colayout.findViewById(R.id.timeandbuttons);
        tv_reserved = (TextView) findViewById(R.id.reserved);

        fab_menu = findViewById(R.id.fab_menu);
        fab_menu.setVisibility(View.VISIBLE);

        sbKickScooter = findViewById(R.id.sbKickScooter);

        sb_kick = findViewById(R.id.normal_green_slide1);
        sbKickScooter.setVisibility(View.GONE);

        final int[] _percentageOfSbKick = new int[1];

        final int[] _percentageOfSbKickscooter = new int[1];


        sbKickScooter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                DecimalFormat decimalFormat = new DecimalFormat("0.00%");

                // Calculate progress value percentage.
                float progressPercentageFloat = (float) progress / (float) seekBar.getMax();
                String s = decimalFormat.format(progressPercentageFloat);

                if (s.contains(",")) {
                    String[] parts = s.split("\\,"); // escape .
                    String part1 = parts[0];

                    System.out.println("Percentage----" + part1);
                    double _progress = Double.parseDouble(part1);

                    _percentageOfSbKick[0] = (int) _progress;
                    Log.e(TAG, "--progress--" + _percentageOfsb);


                }


                if (s.contains(".")) {
                    String[] parts = s.split("\\."); // escape .
                    String part1 = parts[0];
                    System.out.println("Percentage----" + part1);
                    double _progress = Double.parseDouble(part1);

                    _percentageOfSbKick[0] = (int) _progress;
                    Log.e(TAG, "--progress--" + _percentageOfSbKick[0]);


                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (_percentageOfSbKick[0] < 70) {
                    sbKickScooter.setBackgroundResource(R.drawable.slidefull);
                    seekBar.setProgress(2);
                    sbKickScooter.getThumb().mutate().setAlpha(255);

                } else {
                    sbKickScooter.setBackgroundResource(R.drawable.ic_after_slide);
                    seekBar.setProgress(0);
                    sbKickScooter.getThumb().mutate().setAlpha(255);
                    validateDocument(null, sbKickScooter);
                }
            }
        });


        sb_kick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                DecimalFormat decimalFormat = new DecimalFormat("0.00%");

                // Calculate progress value percentage.
                float progressPercentageFloat = (float) progress / (float) seekBar.getMax();
                String s = decimalFormat.format(progressPercentageFloat);

                if (s.contains(",")) {
                    String[] parts = s.split("\\,"); // escape .
                    String part1 = parts[0];

                    System.out.println("Percentage----" + part1);
                    double _progress = Double.parseDouble(part1);

                    _percentageOfSbKickscooter[0] = (int) _progress;
                    Log.e(TAG, "--progress--" + _percentageOfsb);


                }


                if (s.contains(".")) {
                    String[] parts = s.split("\\."); // escape .
                    String part1 = parts[0];
                    System.out.println("Percentage----" + part1);
                    double _progress = Double.parseDouble(part1);

                    _percentageOfSbKickscooter[0] = (int) _progress;
                    Log.e(TAG, "--progress--" + _percentageOfSbKickscooter[0]);


                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (_percentageOfSbKickscooter[0] < 70) {
                    sb_kick.setBackgroundResource(R.drawable.slidefull);
                    seekBar.setProgress(2);
                    sb_kick.getThumb().mutate().setAlpha(255);

                } else {
                    sb_kick.setBackgroundResource(R.drawable.ic_after_slide);
                    seekBar.setProgress(0);
                    sb_kick.getThumb().mutate().setAlpha(255);
                    validateDocument(null, sb_kick);

                }
            }
        });
//-------Play buttons Layout-------

       /* sb = colayout.findViewById(R.id.normal_green_slide);
        sb.setOnSeekBarChangeListener(this);*/
    }


    private void UserInformation() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
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

                if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISBUSINESS, false)) {
                    Alert_information("Hai già prenotato un veicolo in affari", context, alertDialog, HomeActivity.this);
                    return;
                }

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());

                    System.out.println("userstatusresponse:::" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");

                    int booking_id = 0;
                    if (response.code() == 200) {
                        try {

                            Log.e(TAG, "Res===" + response.body().toString());

                            UserInfoResponse userinfo = response.body();
                            //Log.e(TAG, "--ServiceID---"+userinfo.getData().getUser().getServiceId());
                            Integer _siteID = userinfo.getData().getUser().getSiteId();
                            Log.e(TAG, "--_siteID---" + _siteID);

                            //Log.e("TAG", "response 33: "+new Gson().toJson(response.body()) );

                            String obj = new Gson().toJson(response.body());

                            Log.e(TAG, "Res===" + obj);
                            JSONObject res = new JSONObject(obj);

                            JSONObject data = res.getJSONObject("data");
                            JSONObject bookingObj = data.getJSONObject("booking");
                            JSONObject userObj = data.getJSONObject("user");
                            validDocument = userObj.getInt("validDocument");
                            Log.e(TAG, "bookingObj-=-=-=-" + bookingObj.length());

                            userBookingCount = bookingObj.length();
                            getPricedetailsApiKick();

                            if (userBookingCount > 0) {
                                JSONObject _sharingObj = bookingObj.getJSONObject("sharing");

                                int id = _sharingObj.getInt("id");
                                Log.e(TAG, "id==" + id);
                                JSONObject _vehicleObj = _sharingObj.getJSONObject("vehicle");
                                _vehicleID = _vehicleObj.getInt("id");
                                double batteryPercentage = _vehicleObj.getDouble("total_percentage");
                                System.out.println("vehiclekickid::" + _vehicleID);
                                _vehicleRideStatus = _vehicleObj.getString("status");

                                userRideStatus = _vehicleRideStatus.equalsIgnoreCase("running") ? 2 : _vehicleRideStatus.equalsIgnoreCase("free") ? 0 : 1;

                                _vehicleLastLatitude = _vehicleObj.getDouble("latitude");
                                _vehicleLastLongitude = _vehicleObj.getDouble("longitude");
                                selectedScooterType = _vehicleObj.getString("type");
                                System.out.println("selectedScooterType:::" + selectedScooterType);
                                Log.e(TAG, "_vehicleStatus==" + _vehicleRideStatus);
                                Log.e(TAG, "_vehicleLatitude==" + _vehicleLastLatitude);
                                Log.e(TAG, "_vehicleLongitude==" + _vehicleLastLongitude);

                                String _latString = String.valueOf(_vehicleLastLatitude);
                                String _lngString = String.valueOf(_vehicleLastLongitude);

                                PreferenceUtil.getInstance().saveString(context, Constants.STATUS_OF_RIDE, _vehicleRideStatus);
                                PreferenceUtil.getInstance().saveString(context, Constants.RIDE_LATITUDE, _latString);
                                PreferenceUtil.getInstance().saveString(context, Constants.RIDE_LANGITUDE, _lngString);

                                if (_vehicleRideStatus.equalsIgnoreCase("running")) {


                                    if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {


                                        // Log.e(TAG, "isPlayPauseClicked==running==" + isPlayPauseClicked);

                                        bottomSheet.setVisibility(View.VISIBLE);
                                        // _play_layout.setVisibility(View.GONE);
                                        _kick_bottomsheet.setVisibility(View.VISIBLE);

                                        sbKickScooter.setVisibility(View.GONE);

                                        bottomSheetkick.setVisibility(View.GONE);

                                        first_bikeLayout.setVisibility(View.GONE);

                                        _play_layout.setVisibility(View.GONE);

                                        fab_menu.setVisibility(View.GONE);
                                        fab_menu.removeAllViews();

                                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


                                        ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();

                                        MyLatLng bean = new MyLatLng();
                                        bean.setTitle("");
                                        bean.setLat(CURRENT_LATITUDE);
                                        bean.setLng(CURRENT_LANGITUDE);
                                        bean.setLicense_palte("");
                                        arrayList.add(bean);


                                        MyLatLng bean1 = new MyLatLng();
                                        bean1.setTitle("");
                                        bean1.setLat(_vehicleLastLatitude);
                                        bean1.setLng(_vehicleLastLongitude);
                                        bean1.setLicense_palte("");
                                        arrayList.add(bean1);


                                        ArrayList<LatLng> array_loc = new ArrayList<LatLng>();
                                        LatLng postUserloc = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                                        LatLng postBikeloc = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                                        array_loc.add(postUserloc);
                                        array_loc.add(postBikeloc);

                                        if (mMap != null) {
                                            mMap.clear();
                                        }
                                        // DrawPolyLinePastLocation(array_loc);
                                        Marker marker = DrawPolyLinePastLocation(array_loc);
                                        VehicleResponse.Data data1 = new VehicleResponse.Data();
                                        data1 = new Gson().fromJson(_vehicleObj.toString(), VehicleResponse.Data.class);
                                        JSONObject serviceObj = data.getJSONObject("service");
                                        VehicleResponse.Service service = new VehicleResponse.Service();
                                        service.setPrice(serviceObj.getDouble("price"));
                                        data1.setService(service);
                                        requestDirectionFormarkerClicks(postUserloc);
                                        vehicle_data_array = new ArrayList<>();
                                        vehicle_data_array.add(data1);

                                        Vehicleplate = vehicle_data_array.get(0).getLicensePlate();
                                        String autonomiakick = String.valueOf(Math.round((vehicle_data_array.get(0).getTotalPercentage() * KICK_MAX_KM / 100.)));
                                        tvKickBattery.setText(autonomiakick + "km" + "/" + vehicle_data_array.get(0).getTotalPercentage() + "%");

                                        if (isFromQrCodescreen)
                                            showDamagePopUp();

                                        // updateBottomScreenData(0, marker, selectedScooterType);

                                        // Toast.makeText(context, "Status Running", Toast.LENGTH_SHORT).show();
                                    } else {

                                        // Toast.makeText(context, "Status Running", Toast.LENGTH_SHORT).show();
                                        isPlayPauseClicked = false;
                                        Log.e(TAG, "isPlayPauseClicked==running==" + isPlayPauseClicked);
                                        _playPause.setImageResource(R.drawable.ic_pause);
                                        _palypause_label.setText("PAUSA");
                                        bottomSheet.setVisibility(View.VISIBLE);
                                        _play_layout.setVisibility(View.VISIBLE);
                                        social_play_layout.setVisibility(View.VISIBLE);
                                        first_bikeLayout.setVisibility(View.GONE);
                                        fab_menu.setVisibility(View.GONE);
                                        fab_menu.removeAllViews();

                                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                               /* ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();

                                MyLatLng bean = new MyLatLng();
                                bean.setTitle("");
                                bean.setLat(CURRENT_LATITUDE);
                                bean.setLng(CURRENT_LANGITUDE);
                                bean.setLicense_palte("");
                                arrayList.add(bean);


                                MyLatLng bean1 = new MyLatLng();
                                bean1.setTitle("");
                                bean1.setLat(_vehicleLastLatitude);
                                bean1.setLng(_vehicleLastLongitude);
                                bean1.setLicense_palte("");
                                arrayList.add(bean1);*/


                                        ArrayList<LatLng> array_loc = new ArrayList<LatLng>();
                                        LatLng postUserloc = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                                        LatLng postBikeloc = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                                        array_loc.add(postUserloc);
                                        array_loc.add(postBikeloc);


                                        DrawPolyLinePastLocation(array_loc);
                                        VehicleResponse.Data data1 = new VehicleResponse.Data();
                                        data1 = new Gson().fromJson(_vehicleObj.toString(), VehicleResponse.Data.class);
                                        JSONObject serviceObj = data.getJSONObject("service");
                                        VehicleResponse.Service service = new VehicleResponse.Service();
                                        service.setPrice(serviceObj.getDouble("price"));
                                        data1.setService(service);
                                        requestDirectionFormarkerClicks(postUserloc);
                                        vehicle_data_array = new ArrayList<>();
                                        vehicle_data_array.add(data1);

                                        Vehicleplate = vehicle_data_array.get(0).getLicensePlate();
                                        String autonomiascoot = String.valueOf(Math.round((vehicle_data_array.get(0).getTotalPercentage() * SCOOTER_MAX_KM / 100.)));
                                        autonomiakm.setText(autonomiascoot + " Km");
                                        setBatteryPercentage(vehicle_data_array.get(0));


                                    }



                               /* if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                } else {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    bottomSheet.setVisibility(View.GONE);
                                }*/


                                } else if (_vehicleRideStatus.equalsIgnoreCase("parked")) {

                                    isPlayPauseClicked = true;

                                    Log.e(TAG, "isPlayPauseClicked==parked==" + isPlayPauseClicked);


                                    _playPause.setImageResource(R.drawable.ic_play_icon);
                                    _palypause_label.setText("CORSA");

                                    bottomSheet.setVisibility(View.VISIBLE);
                                    _play_layout.setVisibility(View.VISIBLE);
                                    social_play_layout.setVisibility(View.VISIBLE);
                                    first_bikeLayout.setVisibility(View.GONE);
                                    fab_menu.setVisibility(View.GONE);
                                    fab_menu.removeAllViews();

                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                    ArrayList<LatLng> array_loc = new ArrayList<LatLng>();
                                    LatLng postUserloc = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                                    LatLng postBikeloc = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                                    array_loc.add(postUserloc);
                                    array_loc.add(postBikeloc);


                                    DrawPolyLinePastLocation(array_loc);

                                    VehicleResponse.Data data1 = new VehicleResponse.Data();
                                    data1 = new Gson().fromJson(_vehicleObj.toString(), VehicleResponse.Data.class);
                                    JSONObject serviceObj = data.getJSONObject("service");
                                    VehicleResponse.Service service = new VehicleResponse.Service();
                                    service.setPrice(serviceObj.getDouble("price"));
                                    data1.setService(service);
                                    requestDirectionFormarkerClicks(postUserloc);
                                    vehicle_data_array = new ArrayList<>();
                                    vehicle_data_array.add(data1);

                                    Vehicleplate = vehicle_data_array.get(0).getLicensePlate();


                                } else if (_vehicleRideStatus.equalsIgnoreCase("booked")) {
                                    bottomSheet.setVisibility(View.VISIBLE);
                                    _play_layout.setVisibility(View.VISIBLE);
                                    social_play_layout.setVisibility(View.VISIBLE);
                                    first_bikeLayout.setVisibility(View.VISIBLE);
                                    progressbar.setVisibility(View.GONE);
                                    timer_bottom.setVisibility(View.GONE);
                                    isGreenButtonVisible = true;
                                    green_btn.setVisibility(View.VISIBLE);
                                    progressbar.setVisibility(View.GONE);
                                    slideLayout.setVisibility(View.GONE);
                                    tv_reserved.setVisibility(View.GONE);
                                    fab_menu.setVisibility(View.GONE);
                                    fab_menu.removeAllViews();
                                    isSlideViewClicked = true;
                                    social_bike_layout_one.setVisibility(View.VISIBLE);
                                    ArrayList<LatLng> array_loc = new ArrayList<>();
                                    LatLng postUserloc = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                                    LatLng postBikeloc = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                                    array_loc.add(postUserloc);
                                    array_loc.add(postBikeloc);
                                    Marker marker = DrawPolyLinePastLocation(array_loc);
                                    VehicleResponse.Data data1 = new VehicleResponse.Data();
                                    data1 = new Gson().fromJson(_vehicleObj.toString(), VehicleResponse.Data.class);
                                    JSONObject serviceObj = data.getJSONObject("service");
                                    VehicleResponse.Service service = new VehicleResponse.Service();
                                    service.setPrice(serviceObj.getDouble("price"));
                                    data1.setService(service);
                                    requestDirectionFormarkerClicks(postUserloc);
                                    vehicle_data_array = new ArrayList<>();
                                    vehicle_data_array.add(data1);


                                    updateBottomScreenData(0, marker, selectedScooterType);
                                    LocationTarckerServiceStart();
                                }

                            } else {


                                JSONObject user = data.getJSONObject("user");

                                JSONArray avathar_arrayObj = user.getJSONArray("Avatar");
                                Log.e(TAG, "avathar_arrayObj=--=-=-=" + avathar_arrayObj.length());
                                avatharCount = avathar_arrayObj.length();

                                JSONArray document_arrayObj = user.getJSONArray("Document");
                                Log.e(TAG, "document_arrayObj=--=-=-=" + document_arrayObj.length());
                                documentCount = document_arrayObj.length();


                                JSONArray licence_arrayObj = user.getJSONArray("LicensePlate");
                                Log.e(TAG, "licence_arrayObj=--=-=-=" + licence_arrayObj.length());

                                licenseCount = licence_arrayObj.length();

                                AppUtils.SaveDataInSharePreference(context, userinfo);


                                if (_siteID == null) {

                                    //get Site by passing Locations
                                    getSiteId_service();

                                } else {
                                    Log.e(TAG, "--_siteID--xx-" + _siteID);

                                    PreferenceUtil.getInstance().saveInt(context, Constants.SITE_ID, _siteID);

                                    PreferenceUtil.getInstance().saveBoolean(context, Constants.PAYMENT_CREDITED, userinfo.getData().getUser().getPaymentCreated());

                                    CallVehicleService(_siteID);



                                }
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

    private void setBatteryPercentage(VehicleResponse.Data data) {

        Resources res1 = getResources();
        Drawable drawable = res1.getDrawable(R.drawable.circular_horizontal);
        final ProgressBar mProgress = findViewById(R.id.circularProgressbarBat);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
        mProgress.setProgress(data.getTotalPercentage());
    }

    private void getSiteId_service() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        showProgressbar();

        Log.e(TAG, "For SiteID---" + CURRENT_LATITUDE + "-" + CURRENT_LANGITUDE);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        GetSiteID_Interface geo_details = retrofit.create(GetSiteID_Interface.class);

        Call<SiteIDResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, Constants.LATITUDE, Constants.LONGITUDE);

        //Call<SiteIDResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, CURRENT_LATITUDE, CURRENT_LANGITUDE);
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
                                PreferenceUtil.getInstance().saveInt(context, Constants.SITE_ID, siteID);
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

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
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

                        int _siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
                        CallVehicleService(_siteID);
                        // i Called Geofence service after Marker displayed

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
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
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
                        vechicle_data = new ArrayList<VehicleResponse.Data>();
                        vechicle_data.addAll(vehicle_data_array);


                        ShowMarkerVehiclesOnMap(vehicle_data_array);

                        setUpCurrentLocation(CURRENT_LATITUDE, CURRENT_LANGITUDE);


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


            Log.e(TAG, "isStatus---" + vehicle_data_array.get(i).getStatus());

            String _vehicleStatus = vehicle_data_array.get(i).getStatus();

            if (_vehicleStatus.equalsIgnoreCase("free")) {
                double _lat = vehicle_data_array.get(i).getLatitude();
                double _lng = vehicle_data_array.get(i).getLongitude();
                _type = vehicle_data_array.get(i).getType();
                String _licencePlate = vehicle_data_array.get(i).getLicensePlate();
                double _automoniakm = vehicle_data_array.get(i).getKm();

                Log.e(TAG, "LATLNG---" + _lat + " " + _lng);

                int vehicleID = vehicle_data_array.get(i).getId();

                MyLatLng bean = new MyLatLng();
                bean.setTitle(_type);
                bean.setLat(_lat);
                bean.setLng(_lng);
                bean.setLicense_palte(_licencePlate);
                autonomiakm.setText(String.valueOf(Math.round(vehicle_data_array.get(i).getTotalPercentage() * SCOOTER_MAX_KM / 100.)) + " Km");
                // autonomiakm.setText(String.valueOf(Math.round(_automoniakm)) + "Km");
                setBatteryPercentage(vehicle_data_array.get(i));
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
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
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
                    //List<List<Double>> list_points = null;
                    //List<GeoFenceResponse.Zona> _zona_list = null;
                    if (response.code() == 200) {
                        String responseRecieved = new Gson().toJson(response.body());

                        Log.e(TAG, "Geofence--" + response.body().getData().get(0).getZonas().get(0).getPoints().get(0));

                        int sizeCount = 0;
                        Log.e(TAG, "RES===" + responseRecieved);
                        firstLatLng = new ArrayList<LatLng>();
                        List<GeoFenceResponse.Zona> zonalsit = null;
                        List<Double> doubleList = null;
                        List<GeoFenceResponse.Data> datalist = response.body().getData();
                        for (int d = 0; d < datalist.size(); d++) {

                            if (!datalist.get(d).getActive()) {
                                continue;
                            }

                            zonalsit = datalist.get(d).getZonas();


                            for (int p = 0; p < zonalsit.size(); p++) {

                                boolean _isExclusive = zonalsit.get(p).getExclude();
//                                if (_isExclusive == false) {


                                list_list_points = zonalsit.get(p).getPoints();

                                Log.e(TAG, "==list_list_points==" + list_list_points.size());


                                sizeCount++;

                                if (sizeCount > 1) {
                                    double_list.clear();
                                    firstLatLng.clear();
                                }

                                for (int s = 0; s < list_list_points.size(); s++) {
                                    Log.e(TAG, "==list_list_points==11==" + list_list_points.get(s));

                                    double_list = list_list_points.get(s);

                                    double _lat = double_list.get(0).doubleValue();
                                    double _long = double_list.get(1).doubleValue();

                                    Log.e(TAG, "LAT-LNG=-=-=--=-" + _lat + "==" + _long);

                                    LatLng firstPolyLatlng = new LatLng(_lat, _long);
                                    firstLatLng.add(firstPolyLatlng);

                                }
                                if (_isExclusive == false) {
                                    mMap.addPolygon(new PolygonOptions()
                                            .addAll(firstLatLng)
                                            .strokeColor(Color.BLUE)
                                            .strokeWidth(2)
                                            .fillColor(Color.argb(20, 0, 255, 0)));
                                } else {
                                    mMap.addPolygon(new PolygonOptions()
                                            .addAll(firstLatLng)
                                            .strokeColor(Color.BLUE)
                                            .strokeWidth(2)
                                            .fillColor(Color.WHITE));
                                }

//                                    if (sizeCount == 1) {
//                                        for (int s = 0; s < list_list_points.size(); s++) {
//                                            Log.e(TAG, "==list_list_points==11==" + list_list_points.get(s));
//
//                                            double_list = list_list_points.get(s);
//
//                                            double _lat = double_list.get(0).doubleValue();
//                                            double _long = double_list.get(1).doubleValue();
//
//                                            Log.e(TAG, "LAT-LNG=-=-=--=-" + _lat + "==" + _long);
//
//                                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                                            firstLatLng.add(firstPolyLatlng);
//
//                                        }
//
//                                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                                .addAll(firstLatLng)
//                                                .strokeColor(Color.BLUE)
//                                                .strokeWidth(2)
//                                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                                    }
//
//
//                                    if (sizeCount == 2) {
//                                        double_list.clear();
//                                        firstLatLng.clear();
//                                        for (int s = 0; s < list_list_points.size(); s++) {
//                                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                                            double_list = list_list_points.get(s);
//
//                                            double _lat = double_list.get(0).doubleValue();
//                                            double _long = double_list.get(1).doubleValue();
//
//                                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                                            firstLatLng.add(firstPolyLatlng);
//
//                                        }
//
//                                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                                .addAll(firstLatLng)
//                                                .strokeColor(Color.BLUE)
//                                                .strokeWidth(2)
//                                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                                    }
//
//                                    if (sizeCount == 3) {
//                                        double_list.clear();
//                                        firstLatLng.clear();
//                                        for (int s = 0; s < list_list_points.size(); s++) {
//                                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                                            double_list = list_list_points.get(s);
//
//                                            double _lat = double_list.get(0).doubleValue();
//                                            double _long = double_list.get(1).doubleValue();
//
//                                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                                            firstLatLng.add(firstPolyLatlng);
//
//                                        }
//
//                                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                                .addAll(firstLatLng)
//                                                .strokeColor(Color.BLUE)
//                                                .strokeWidth(2)
//                                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                                    }
//                                    if (sizeCount == 4) {
//                                        double_list.clear();
//                                        firstLatLng.clear();
//                                        for (int s = 0; s < list_list_points.size(); s++) {
//                                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                                            double_list = list_list_points.get(s);
//
//                                            double _lat = double_list.get(0).doubleValue();
//                                            double _long = double_list.get(1).doubleValue();
//
//                                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                                            firstLatLng.add(firstPolyLatlng);
//
//                                        }
//
//                                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                                .addAll(firstLatLng)
//                                                .strokeColor(Color.BLUE)
//                                                .strokeWidth(2)
//                                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                                    }
//
//                                    if (sizeCount == 5) {
//                                        double_list.clear();
//                                        firstLatLng.clear();
//                                        for (int s = 0; s < list_list_points.size(); s++) {
//                                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                                            double_list = list_list_points.get(s);
//
//                                            double _lat = double_list.get(0).doubleValue();
//                                            double _long = double_list.get(1).doubleValue();
//
//                                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                                            firstLatLng.add(firstPolyLatlng);
//
//                                        }
//
//                                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                                .addAll(firstLatLng)
//                                                .strokeColor(Color.BLUE)
//                                                .strokeWidth(2)
//                                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                                    }
//
//                                    if (sizeCount == 6) {
//                                        double_list.clear();
//                                        firstLatLng.clear();
//                                        for (int s = 0; s < list_list_points.size(); s++) {
//                                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                                            double_list = list_list_points.get(s);
//
//                                            double _lat = double_list.get(0).doubleValue();
//                                            double _long = double_list.get(1).doubleValue();
//
//                                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                                            firstLatLng.add(firstPolyLatlng);
//
//                                        }
//
//                                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                                .addAll(firstLatLng)
//                                                .strokeColor(Color.BLUE)
//                                                .strokeWidth(2)
//                                                .fillColor(Color.argb(20, 0, 255, 0)));


//                                    }

//                                }


                            }


                        }

                        Log.e(TAG, "sizeCount===" + sizeCount);


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
                       /* Toast.makeText(context, "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();*/
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
    protected void onResume() {
        super.onResume();
        if (sb_kick != null && sbKickScooter != null) {
            sb_kick.setBackgroundResource(R.drawable.slidefull);
            sbKickScooter.setBackgroundResource(R.drawable.slidefull);
        }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler = null;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        //DC_FIX
        LatLng StartPosition = new LatLng(45.4654219, 9.1859243);
        float zoomlevel = (float) 12.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartPosition, zoomlevel));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Log.e(TAG, "==ssss===");
                if (userRideStatus != 2) {
                    if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheet.setVisibility(View.GONE);
                    }
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheet.setVisibility(View.VISIBLE);
                }


//                if (userBookingCount > 0) {
//                    // Toast.makeText(context, "markerClided", Toast.LENGTH_SHORT).show();
//
//                    if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        bottomSheet.setVisibility(View.GONE);
//                    } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        bottomSheet.setVisibility(View.VISIBLE);
//                    }
//                }


            }
        });


    }


    public void setUpCurrentLocation(double latitude, double longitude) {
        if (latitude == 0 || longitude == 0)
            return;
        LatLng latLng = new LatLng(latitude, longitude);
        // Log.e(TAG, "LAT-LANG--"+latitude+"-"+longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f);
        mMap.animateCamera(cameraUpdate);


        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot);
        if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISSESSION, false)) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot);
        }

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(icon));
        marker.setTag("CurrentLocation");
        myMarker = marker;
        currentLocationID = marker.getId();


    }

    //Showing free status Vehicles
    void LoadingGoogleMap(ArrayList<MyLatLng> arrayList) {
        if (mMap != null) {
            mMap.clear();
            mSelectedMarker = null;

            bottomSheet.setVisibility(View.GONE);
            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer.onFinish();

            }

            final ArrayList<MyLatLng> myLatLng = new ArrayList<MyLatLng>();

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
                                    .icon(icon));
                            myMarker.setTag(bean.getTitle());
                            //Set Zoom Level of Map pin
                            LatLng object = new LatLng(_lat, _lon);
                            listMyLatLng.add(object);

                            MyLatLng latlng = new MyLatLng();
                            latlng.setLat(_lat);
                            latlng.setLng(_lon);
                            myLatLng.add(latlng);


                         /*   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 17));*/


                        }


                    }


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        Log.i(TAG, marker.getId());
                        Log.i(TAG, myMarker.getId());

                        if (marker.getTag() != null && !marker.getTag().toString().equalsIgnoreCase("CurrentLocation")) { //==========IF Current Location Marker Clicks============


                            bottomSheet.setVisibility(View.VISIBLE);
                           /* first_bikeLayout.setVisibility(View.GONE);
                            _kick_bottomsheet.setVisibility(View.VISIBLE);*/
                            if (vehicle_data_array != null) {

                                if (null != mSelectedMarker) {
                                    if (_isDispalyedMarker == false) {
                                        mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green));

                                    }
                                }
                                mSelectedMarker = marker;
                                if (_isDispalyedMarker == false) {
                                    int id = R.drawable.ic_loc_small;
                                    if (mSelectedMarker.getTag().toString().equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                                        id = R.drawable.ic_kickloc;
                                    }
                                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(id));
                                    LatLng source = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                                    requestDirectionFormarkerClicks(source);


                                }
                                if (userRideStatus != 2) {
                                    showinMarkers(marker);
                                }
                            } else {

                                //==========IF Current Location Marker Clicks============
                                int icon = R.drawable.ic_dot;

                                if (_vehicleRideStatus != null && (_vehicleRideStatus.equalsIgnoreCase("booked") || _vehicleRideStatus.equalsIgnoreCase("running") || _vehicleRideStatus.equalsIgnoreCase("started"))) {
                                    icon = R.drawable.ic_empty;
                                }
                                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(icon));
                                bottomSheet.setVisibility(View.GONE);

                                //==========IF Current Location Marker Clicks============
                            }


                        }


                        return false;
                    }


                });


            }

            //  GeofenceFunction(myLatLng);

        } else {
            Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestDirectionFormarkerClicks(LatLng origin) {
        //  final LatLng destination = new LatLng(CURRENT_LATITUDE,  CURRENT_LANGITUDE);
        /*TRACK_USER_CURRENT_LATITUDE =45.4606138;
        TRACK_USER_CURRENT_LANGITUDE =9.2252721;*/
        final LatLng destination = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
        String serverKey = context.getResources().getString(R.string.google_maps_key);
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.WALKING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);


                            try {
                                // Log.e(TAG, "--Res---"+rawBody);

                                if (rawBody != null) {
                                    final JSONObject json = new JSONObject(rawBody);
                                    JSONArray routeArray = json.getJSONArray("routes");
                                    JSONObject routes = routeArray.getJSONObject(0);

                                    JSONArray newTempARr = routes.getJSONArray("legs");
                                    JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                                    JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                                    JSONObject timeOb = newDisTimeOb.getJSONObject("duration");

                                    String distance = distOb.getString("text");
                                    walk_time = timeOb.getString("text");

                                    Log.i("Distance :", distOb.getString("text"));
                                    Log.i("Time :", timeOb.getString("text"));


                                    if (distance != null) {
                                        _distance.setText("" + distance);

                                    }
                                    if (walk_time != null) {
                                        _time.setText("" + walk_time);

                                    }

                                    Log.e(TAG, "====walk_time==" + walk_time);
                                    String s = walk_time;
                                    String[] parts = s.split("\\s+"); // escape .
                                    freewalkTime = parts[0];
                                    String mins = parts[1];

                                    Log.e(TAG, "====part1==" + freewalkTime);
                                    Log.e(TAG, "====part2==" + mins);

                                    if (currentPolyline != null) {
                                        currentPolyline.remove();
                                    }
                                    if (userRideStatus != 2) {
                                        currentPolyline = mMap.addPolyline(DirectionConverter.createPolyline(context, route.getLegList().get(0).getDirectionPoint(), 5, ContextCompat.getColor(context, R.color.green_dark)));
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                        // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                    }
                });
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

            String VehicletpekickorE = vehicle_data_array.get(i).getType();
            if (marker.getPosition().latitude == vehicle_data_array.get(i).getLatitude() && marker.getPosition().longitude == vehicle_data_array.get(i).getLongitude()) {
                updateBottomScreenData(i, marker, VehicletpekickorE);

            }


        }

    }

    private void updateBottomScreenData(int i, final Marker marker, String vehicletpekickorE) {

        final int j = i;
        String VehicleDisplay = vehicletpekickorE;

        System.out.println("vehicletypekickoeE" + VehicleDisplay);

        if (VehicleDisplay.equals("kick")) {

            bottomSheetkick.setVisibility(View.VISIBLE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            first_bikeLayout.setVisibility(View.GONE);

            // Bike_Logo.setImageResource(R.drawable.ic_kick);


            // final TextView timer_bottom = (TextView) findViewById(R.id.timer1);
            TextView _bike_number_bottom = (TextView) findViewById(R.id.bike_number1);
            TextView _km = (TextView) findViewById(R.id.km1);
            TextView _price = (TextView) findViewById(R.id.price1);
            TextView _battery = (TextView) findViewById(R.id.battery1);
            TextView _model_type = (TextView) findViewById(R.id.model_name1);
            _time = (TextView) findViewById(R.id.google_time1);
            _distance = (TextView) findViewById(R.id.distance1);


            //By default set BottomSheet Behavior as Collapsed and Height 0
            //  mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            // mBottomSheetBehavior.setPeekHeight(0);
            //If you want to handle callback of Sheet Behavior you can use below code

            _bike_number_bottom.setText("" + vehicle_data_array.get(i).getLicensePlate());

            Vehicleplate = vehicle_data_array.get(i).getLicensePlate();
            System.out.println("Vehicleplatekick::" + Vehicleplate);

            double dCalculatedKm = (double) 0.0;
            double temp = (double) 0.0;
            if (vehicle_data_array.get(i).getType().equalsIgnoreCase("kick")) {
                dCalculatedKm = (double) (vehicle_data_array.get(i).getTotalPercentage() * KICK_MAX_KM / 100);

            }
            _km.setText("" + dCalculatedKm + "km");

//            PriceEuro = vehicle_data_array.get(i).getService().getPrice() != null ? "€" + vehicle_data_array.get(i).getService().getPrice() : "";
//            _price.setText(PriceEuro);

            double battery_percentage = vehicle_data_array.get(i).getTotalPercentage();

            // double battery_percentage = vehicle_data_array.get(i).getBattery().getPercentage();
            int battery = (int) battery_percentage;


            _battery.setText("" + battery + "%");

            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.circular);
            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
            mProgress.setProgress(0);   // Main Progress
            mProgress.setSecondaryProgress(100); // Secondary Progress
            mProgress.setMax(100); // Maximum Progress
            mProgress.setProgressDrawable(drawable);
            mProgress.setProgress(battery);

            //  _battery.setText("" + vehicle_data_array.get(i).getBattery().getAhTot() + "%");

            String address = StringUtils.substringBefore(getCompleteAddressString(vehicle_data_array.get(i).getLatitude(), vehicle_data_array.get(i).getLongitude()), ",");

            Log.e(TAG, "Address===" + vehicle_data_array.get(i).getAddress());
//        String address = vehicle_data_array.get(i).getAddress();

            if (address != null) {
                if (!address.equalsIgnoreCase("")) {
                    address = address.replaceAll(",", "\n");
                    System.out.println(address);

                    _model_type.setText("" + address);
                }
            }


            _vehicleStatus = vehicle_data_array.get(i).getStatus();
            _vehicleID = vehicle_data_array.get(i).getId();

            Log.e(TAG, "ID----" + _vehicleID);


        } else {

            //Bike_Logo.setImageResource(R.drawable.ic_greenbike);

            bottomSheet.setVisibility(View.VISIBLE);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            first_bikeLayout.setVisibility(View.VISIBLE);


            final TextView timer_bottom = (TextView) findViewById(R.id.timer);
            TextView _bike_number_bottom = (TextView) findViewById(R.id.bike_number);
            TextView _km = (TextView) findViewById(R.id.km);
            TextView _price = (TextView) findViewById(R.id.price);
            TextView _battery = (TextView) findViewById(R.id.battery);
            TextView _model_type = (TextView) findViewById(R.id.model_name);


            _time = (TextView) findViewById(R.id.google_time);
            _distance = (TextView) findViewById(R.id.distance);


            final Button avali_btn = colayout.findViewById(R.id.btn_avela);
            avali_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            green_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    autonomiakm.setText(String.valueOf(Math.round(vehicle_data_array.get(j).getTotalPercentage() * SCOOTER_MAX_KM / 100.)) + " Km");
                    setBatteryPercentage(vehicle_data_array.get(j));
                    SessionStart();


                }
            });
            final Button cancel_btn = (Button) findViewById(R.id.btn_avela);
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mMap.clear();
                    if (yourCountDownTimer != null) {
                        yourCountDownTimer.cancel();
                        yourCountDownTimer.onFinish();

                    }

                    //  callStartStopServiceForStopSession(context,_vehicleID);

                    callDeleteApi();
//                ReFreash_onCreatemethod();

                    //  alertDialog.dismiss();

                }
            });

            //By default set BottomSheet Behavior as Collapsed and Height 0

            // mBottomSheetBehavior.setPeekHeight(0);
            //If you want to handle callback of Sheet Behavior you can use below code


            _bike_number_bottom.setText("" + vehicle_data_array.get(i).getLicensePlate());

            Vehicleplate = vehicle_data_array.get(i).getLicensePlate();

            double dCalculatedKm = (double) 0.0;
            double temp = (double) 0.0;


            dCalculatedKm = Math.round(vehicle_data_array.get(i).getTotalPercentage() * SCOOTER_MAX_KM / 100.);

            _km.setText("" + dCalculatedKm + " km");

            PriceEuro = vehicle_data_array.get(i).getService().getPrice() != null ? "€" + vehicle_data_array.get(i).getService().getPrice() : "";
            _price.setText(PriceEuro);

            double battery_percentage = vehicle_data_array.get(i).getTotalPercentage();

            // double battery_percentage = vehicle_data_array.get(i).getBattery().getPercentage();
            int battery = (int) battery_percentage;


            _battery.setText("" + battery + "%");

            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.circular);
            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
            mProgress.setProgress(0);   // Main Progress
            mProgress.setSecondaryProgress(100); // Secondary Progress
            mProgress.setMax(100); // Maximum Progress
            mProgress.setProgressDrawable(drawable);
            mProgress.setProgress(battery);

            //  _battery.setText("" + vehicle_data_array.get(i).getBattery().getAhTot() + "%");


            String address = StringUtils.substringBefore(getCompleteAddressString(vehicle_data_array.get(i).getLatitude(), vehicle_data_array.get(i).getLongitude()), ",");
            Log.e(TAG, "Address===" + vehicle_data_array.get(i).getAddress());
//        String address = vehicle_data_array.get(i).getAddress();

            if (address != null) {
                if (!address.equalsIgnoreCase("")) {
                    address = address.replaceAll(",", "\n");
                    System.out.println(address);

                    _model_type.setText("" + address);
                }
            }


            _vehicleStatus = vehicle_data_array.get(i).getStatus();
            _vehicleID = vehicle_data_array.get(i).getId();

            Log.e(TAG, "ID----" + _vehicleID);

        }


        final Button cancel_btn = (Button) findViewById(R.id.btn_avela);


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


        sb = colayout.findViewById(R.id.normal_green_slide);
        sb.setMax(100);
        sb.setProgress(2);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                DecimalFormat decimalFormat = new DecimalFormat("0.00%");

                // Calculate progress value percentage.
                float progressPercentageFloat = (float) progress / (float) seekBar.getMax();
                progressPercentage = decimalFormat.format(progressPercentageFloat);

                String s = progressPercentage;


                if (s.contains(",")) {
                    String[] parts = s.split("\\,"); // escape .
                    String part1 = parts[0];

                    System.out.println("Percentage----" + part1);
                    double _progress = Double.parseDouble(part1);

                    _percentageOfsb = (int) _progress;
                    Log.e(TAG, "--progress--" + _percentageOfsb);


                }


                if (s.contains(".")) {
                    String[] parts = s.split("\\."); // escape .
                    String part1 = parts[0];
                    System.out.println("Percentage----" + part1);
                    double _progress = Double.parseDouble(part1);

                    _percentageOfsb = (int) _progress;
                    Log.e(TAG, "--progress--" + _percentageOfsb);


                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (_percentageOfsb < 70) {
                    sb.setBackgroundResource(R.drawable.slidefull);
                    seekBar.setProgress(2);

                    sb.getThumb().mutate().setAlpha(255);


                } else {

                    boolean isPaymentCredited = PreferenceUtil.getInstance().getBoolean(context, Constants.PAYMENT_CREDITED, false);

                    if (isPaymentCredited == true) {

                        if (avatharCount > 0) {

                            if (documentCount > 0) {

                                if (licenseCount > 0) {

                                    //If User is already booked userBookingCount will be 1, so we not allow for another ride, until its stoped.
                                    if (userBookingCount == 0) {

                                        seekBar.setProgress(100);
                                        sb.getThumb().mutate().setAlpha(0);
                                        sb.setBackgroundResource(R.drawable.ic_greenslideafter);
                                        seekBar.setProgress(2);

                                        validateDocument(marker, seekBar);

                                        // BookingTimeCheckUserInformation(_vehicleID,timer_bottom,green_btn,marker);


                                    } else {
                                        // Toast.makeText(context, "Sorry! your already booked vehicle.",Toast.LENGTH_SHORT).show();

                                        sb.setBackgroundResource(R.drawable.slidefull);
                                        seekBar.setProgress(2);
                                        sb.getThumb().mutate().setAlpha(255);

                                        Alert_Bookinginformation("Per favore completa il tuo profilo per prenotare una corsa!", context, alertDialog, HomeActivity.this);


                                        //paymentGateWay_Alert("Sorry! your already booked vehicle.");
                                    }
                                } else {
                                    //Toast.makeText(context, "Per favore completa il tuo profilo per prenotare una corsa!",Toast.LENGTH_SHORT).show();
                                    sb.setBackgroundResource(R.drawable.slidefull);
                                    seekBar.setProgress(2);
                                    sb.getThumb().mutate().setAlpha(255);

                                    paymentGateWay_Alert("Per favore completa il tuo profilo per prenotare una corsa!");
                                }

                            } else {
                                //Toast.makeText(context, "Please upload valid documents.",Toast.LENGTH_SHORT).show();
                                sb.setBackgroundResource(R.drawable.slidefull);
                                seekBar.setProgress(2);
                                sb.getThumb().mutate().setAlpha(255);

                                paymentGateWay_Alert("Per favore completa il tuo profilo per prenotare una corsa!");
                            }

                        } else {
                            // Toast.makeText(context, "Please upload Profile Pic.",Toast.LENGTH_SHORT).show();
                            sb.setBackgroundResource(R.drawable.slidefull);
                            seekBar.setProgress(2);
                            sb.getThumb().mutate().setAlpha(255);

                            paymentGateWay_Alert("Per favore completa il tuo profilo per prenotare una corsa!");
                        }

                    } else {
                        // Toast.makeText(context, "Non autorizzato, per favore completa il tuo profilo.", Toast.LENGTH_SHORT).show();

                        sb.setBackgroundResource(R.drawable.slidefull);
                        seekBar.setProgress(2);
                        sb.getThumb().mutate().setAlpha(255);

                        paymentGateWay_Alert("Per favore completa il tuo profilo per prenotare una corsa!");


                    }

                }


            }
        });

        openBottomScreen();
    }

    private void validateDocument(Marker marker, SeekBar seekBar) {
        switch (validDocument) {
            case 1:
                if (seekBar.getId() == sb_kick.getId() || seekBar.getId() == sbKickScooter.getId()) {
                    Intent intent = new Intent(HomeActivity.this, QRActivity.class);
                    startActivityForResult(intent, REQUEST_QR_CODE);
                } else {
                    bookVehicle_ServiceCall(_vehicleID, timer_bottom, green_btn, marker);
                }
                break;
            case -1:
                showImproperDocumentError(getString(R.string.waiting_for_approval), seekBar);
                break;
            case 0:
                showImproperDocumentError(getString(R.string.not_valid), seekBar);
                break;
            case 2:
                showImproperDocumentError(getString(R.string.rejected), seekBar);
                break;

        }

    }

    private void showImproperDocumentError(String errorMsg, SeekBar seekBar) {
        seekBar.setBackgroundResource(R.drawable.slidefull);
        seekBar.setProgress(0);
        seekBar.getThumb().mutate().setAlpha(255);
        AppUtils.error_Alert(errorMsg, context, null, HomeActivity.this);
    }

    private void callDeleteApi() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, false);

        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        DeleteRideSession geo_details = retrofit.create(DeleteRideSession.class);


        Call<JSONObject> resultRes = geo_details.GetDeleteResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                ReFreash_onCreatemethod();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                       /* StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        AppUtils.error_Alert(error.getError().getDescription(),context, alertDialog, HomeActivity.this);*/


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {


//                            showDamagePopUp();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }

                dismissProgressbar();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });


    }


    //=====This method call 2 times
    private void SessionStart() {
        _play_layout.setVisibility(View.VISIBLE);
        first_bikeLayout.setVisibility(View.GONE);
        _kick_bottomsheet.setVisibility(View.GONE);
        fab_menu.setVisibility(View.GONE);
        fab_menu.removeAllViews();

        StartRideSession();
    }

    private void showErrorPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.buisness_alert_attenzione, viewGroup, false);

        Button _btn_done = (Button) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);
        Button _cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        TextView tv_amount = (TextView) dialogView.findViewById(R.id.amount);


        // String amount ="";

        tv_amount.setText("Terminati I minuti, \n verrà addebitata la tariffa \n standard di " + PriceEuro + "/min");


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

               /* isUserStartSession = true;

                if(yourCountDownTimer!=null){
                    yourCountDownTimer.cancel();
                    yourCountDownTimer.onFinish();

                }

                SessionStart();*/


            }
        });
        _cancel.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           _alertDialog.dismiss();

                                           mMap.clear();


                                           if (yourCountDownTimer != null) {
                                               yourCountDownTimer.cancel();
                                               yourCountDownTimer.onFinish();

                                           }
                                           //---Note : For stop session before we need to startRide and later stop the ride service need to call
                                           // callStartStopServiceForStopSession(context,_vehicleID);

                                           //now

                                           //Note : Afer start and stop service called then ReFreash_onCreatemethod(); call
                                           _alertDialog.dismiss();
                                           callDeleteApi();

//                                           ReFreash_onCreatemethod();

                                       }
                                   }


        );

    }

    private void ReFreash_onCreatemethod() {

       /*if (android.os.Build.VERSION.SDK_INT >= 11){
            //Code for recreate
            recreate();

        }else{
            //Code for Intent
            Intent intent = getIntent();
           intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
           finish();
            startActivity(intent);
        }*/

        Intent i = new Intent(HomeActivity.this, HomeActivity_Refersh.class);
        startActivity(i);
        finish();
        if (yourCountDownTimer != null) {
            yourCountDownTimer.cancel();
            yourCountDownTimer.onFinish();

        }

        //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

       /* isGreenButtonVisible = false;
        isSlideViewClicked = false;

        UserInformation();


        if(yourCountDownTimer!=null){
            yourCountDownTimer.cancel();
            yourCountDownTimer.onFinish();

        }*/


    }


    private void DisplaySelectedMarker(ArrayList<MyLatLng> selected_loc_arrayList, Marker marker) {
        arrayList.clear();
        mMap.clear();

        if (marker != null) {
            mSelectedMarker = marker;
        }
        _isDispalyedMarker = true;

        if (selected_loc_arrayList != null) {
            try {
                listMyLatLng = new ArrayList<LatLng>();
                for (int i = 0; i < selected_loc_arrayList.size(); i++) {
                    MyLatLng bean = selected_loc_arrayList.get(i);
                    if (bean.getLat() > 0 && bean.getLng() > 0) {
                        double _lat = bean.getLat();
                        double _lon = bean.getLng();

                        int id = R.drawable.ic_loc_small;
                        if (mSelectedMarker.getTag() != null && mSelectedMarker.getTag().toString().equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                            id = R.drawable.ic_kickloc;
                        }
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(id);

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(_lat, _lon))
                                .icon(icon));
                        //Set Zoom Level of Map pin
                        LatLng object = new LatLng(_lat, _lon);
                        _vehicleLastLatitude = _lat;
                        _vehicleLastLongitude = _lon;


                        listMyLatLng.add(object);

                      /*  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12.0f));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 17));*/


                    }

                }

                //  GeofenceFunction(selected_loc_arrayList);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            DrawPolyLine(listMyLatLng);
        }


    }

    private void DrawPolyLine(ArrayList<LatLng> polyline_array) {
        Log.e(TAG, "Size---" + polyline_array.size());

        LatLng source = new LatLng(polyline_array.get(0).latitude, polyline_array.get(0).longitude);


        requestDirection(source);

    }

    private Marker DrawPolyLinePastLocation(ArrayList<LatLng> polyline_array) {
        Log.e(TAG, "DrawPolyLinePastLocation--Size---" + polyline_array.size());

        LatLng source = new LatLng(polyline_array.get(0).latitude, polyline_array.get(0).longitude);


        return requestDirectionPast(source, polyline_array, true);

    }

    public Marker requestDirectionPast(final LatLng origin, final ArrayList<LatLng> polyline_array, final boolean isZoomRequired) {

        // final LatLng destination = new LatLng(CURRENT_LATITUDE,  CURRENT_LANGITUDE);
//        final LatLng destination = new LatLng(TRACK_USER_CURRENT_LATITUDE, TRACK_USER_CURRENT_LANGITUDE);
        final LatLng destination = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
        final Marker[] marker = new Marker[1];

        String serverKey = context.getResources().getString(R.string.google_maps_key);
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.WALKING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Geofenceing_Service(1);
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            // mMap.addMarker(new MarkerOptions().position(origin));
                            //  mMap.addMarker(new MarkerOptions().position(destination));

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot);
                            int id = R.drawable.ic_loc_small;
                            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                                id = R.drawable.ic_kickloc;
                            }
                            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(id);


                            Marker localMarker;
                            if (currentLocatinMarker == null) {
                                if (userRideStatus != 2) {
                                    localMarker = mMap.addMarker(new MarkerOptions()
                                            .position(destination)
                                            .icon(icon));
                                } else {
                                    localMarker = mMap.addMarker(new MarkerOptions()
                                            .position(destination)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_empty)));
                                }

                                localMarker.setTag("CurrentLocation");
                                currentLocationID = localMarker.getId();
                                myMarker = localMarker;
                            } else {
                                currentLocatinMarker.setPosition(destination);
                            }

                            if (destinationMarker == null) {
                                marker[0] = mMap.addMarker(new MarkerOptions()
                                        .position(origin)
                                        .icon(icon1));
                                pastLocationID = marker[0].getId();
                                destinationMarker = marker[0];
                            } else {
                                destinationMarker.setPosition(origin);
                            }
                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            if (isPolyLoaded == false) {
                                if (userRideStatus != 2) {
                                    polyline = mMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, R.color.green_dark)));
                                    isPolyLoaded = true;
                                }
                            } else {
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, R.color.green_dark));
                                polyline.setPoints(polylineOptions.getPoints());
                            }

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(origin, 17.0f);
                            if (isZoomRequired) {
                                mMap.animateCamera(cameraUpdate);
                            }
                            // setCameraWithCoordinationBounds(route);
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    if (marker.getTag() == null) {
                                        if (userRideStatus != 2) {
                                            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                                bottomSheet.setVisibility(View.GONE);
                                            } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                                bottomSheet.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                    return false;
                                }
                            });


                        } else {

                            // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                        // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                    }
                });
        return marker[0];
    }
    // Fetches data from url passed

    /**
     * A method to download json data from url
     */

    public void requestDirection(LatLng origin) {

        final LatLng destination = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
//        final LatLng destination = new LatLng(TRACK_USER_CURRENT_LATITUDE, TRACK_USER_CURRENT_LANGITUDE);

        // final LatLng destination = new LatLng(TRACK_USER_CURRENT_LATITUDE,  TRACK_USER_CURRENT_LANGITUDE);
        String serverKey = context.getResources().getString(R.string.google_maps_key);
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.WALKING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            // mMap.addMarker(new MarkerOptions().position(origin));
                            //  mMap.addMarker(new MarkerOptions().position(destination));

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot);

                            Marker localMarker = mMap.addMarker(new MarkerOptions()
                                    .position(destination)
                                    .icon(icon));
                            localMarker.setTag("Current Location");
                            currentLocationID = localMarker.getId();
                            myMarker = localMarker;

                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();

                            if (userRideStatus != 2) {
                                if (currentPolyline != null) {
                                    currentPolyline.remove();
                                }
                                polyline = mMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, R.color.green_dark)));
                                // setCameraWithCoordinationBounds(route);
                                points = polyline.getPoints();
                            }

                            try {
                                // Log.e(TAG, "--Res---"+rawBody);

                                final JSONObject json = new JSONObject(rawBody);
                                JSONArray routeArray = json.getJSONArray("routes");
                                JSONObject routes = routeArray.getJSONObject(0);

                                JSONArray newTempARr = routes.getJSONArray("legs");
                                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                                JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                                JSONObject timeOb = newDisTimeOb.getJSONObject("duration");

                                String distance = distOb.getString("text");
                                String time = timeOb.getString("text");

                                Log.i("Diatance :", distOb.getString("text"));
                                Log.i("Time :", timeOb.getString("text"));


                                _distance.setText("" + distance);
                                _time.setText("" + time);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                        // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                    }
                });
    }


    private void startFreeTime(final TextView _timerTV, final Button _greenButton) {

        //Log.e(TAG, "distanceTime--1-"+freewalkTime);

        long distanceTime = Long.parseLong(freewalkTime);

        long time = distanceTime * 5 * 1000;  //Testing
        progressbar.setMax((int) time);
        yourCountDownTimer = new CountDownTimer(time, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                System.out.println("format-------" + f.format(millisUntilFinished));
                System.out.println("format111--- " + String.format("%1$02d", millisUntilFinished));
                long milliSecs = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                progressbar.setProgress((int) (millisUntilFinished));
                long milliMins = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);

                String convMilliMins = milliMins < 10 ? "0" + String.valueOf(milliMins) : String.valueOf(milliMins);

                Long milliSecsNew = milliSecs -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                String milliSecStr = milliSecsNew < 10 ? "0" + String.valueOf(milliSecsNew) : String.valueOf(milliSecsNew);

                _timerTV.setText("" + String.format("%S:%S min",
                        convMilliMins,
                        milliSecStr));

                String TimerCheck = "" + String.format("%d:%d min",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                System.out.println("bbdbhdbhbdtime:::" + TimerCheck);


                if (millisUntilFinished / 1000 == 59) {
                    try {
                        showErrorPopUp();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            public void onFinish() {
                // _tv.setText("START");
                isGreenButtonVisible = true;
                _timerTV.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);
                _greenButton.setVisibility(View.VISIBLE);
                tv_reserved.setVisibility(View.GONE);
                LocationTarckerServiceStart();
            }

        }.start();


    }


    private void openBottomScreen() {
        if (userRideStatus != 2) {
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bottomSheet.setVisibility(View.GONE);
            }
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheet.setVisibility(View.VISIBLE);
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

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Toast.makeText(context, "LAT-LNG"+location.getLatitude()+"-"+location.getLongitude(), Toast.LENGTH_SHORT).show();

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
                .strokeColor(getResources().getColor(R.color.geofence_green))
                .fillColor(getResources().getColor(R.color.geofence_green))
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


            //Click the current Location on Top
            case R.id.button3:
                // setUpCurrentLocation(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                setUpCurrentLocation(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                break;

            case R.id.bauletto:

                if (!buttonPressedbauletto) {
                    tv_bauletto.setBackgroundResource(R.drawable.round_corner);
                    tv_bauletto.setTextColor(Color.WHITE);

                    arrlist.add("1");

                } else {
                    tv_bauletto.setBackgroundResource(R.drawable.round_green);
                    tv_bauletto.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("1");

                    }

                }
                buttonPressedbauletto = !buttonPressedbauletto;


                break;

            case R.id.selia:

                if (!buttonPressedselia) {
                    tv_selia.setBackgroundResource(R.drawable.round_corner);
                    tv_selia.setTextColor(Color.WHITE);

                    arrlist.add("2");

                } else {
                    tv_selia.setBackgroundResource(R.drawable.round_green);
                    tv_selia.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("2");

                    }

                }
                buttonPressedselia = !buttonPressedselia;


                break;
            case R.id.cavaletto:
                if (buttonPressedcaraletto) {

                    tv_cavaletto.setBackgroundResource(R.drawable.round_green);
                    tv_cavaletto.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("3");

                    }
                } else {

                    tv_cavaletto.setBackgroundResource(R.drawable.round_corner);
                    tv_cavaletto.setTextColor(Color.WHITE);

                    arrlist.add("3");


                }
                buttonPressedcaraletto = !buttonPressedcaraletto;
                break;

            case R.id.carena:
                if (buttonPressedcarena) {


                    tv_carena.setBackgroundResource(R.drawable.round_green);


                    tv_carena.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("4");

                    }


//saikk
                } else {

                    tv_carena.setBackgroundResource(R.drawable.round_corner);
                    tv_carena.setTextColor(Color.WHITE);
                    arrlist.add("4");
                }
                buttonPressedcarena = !buttonPressedcarena;
                break;

            case R.id.caschi:

                if (!buttonPressedcaschi) {
                    tv_caschi.setBackgroundResource(R.drawable.round_corner);
                    tv_caschi.setTextColor(Color.WHITE);
                    arrlist.add("5");

                } else {
                    tv_caschi.setBackgroundResource(R.drawable.round_green);
                    tv_caschi.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("5");

                    }

                }
                buttonPressedcaschi = !buttonPressedcaschi;
                break;

            case R.id.altro:

                if (!buttonPressedaltro) {
                    tv_altro.setBackgroundResource(R.drawable.round_corner);
                    tv_altro.setTextColor(Color.WHITE);

                    arrlist.add("6");
                } else {
                    tv_altro.setBackgroundResource(R.drawable.round_green);
                    tv_altro.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("6");

                    }

                }
                buttonPressedaltro = !buttonPressedaltro;
                break;


            case R.id.specchietti:

                if (!buttonPressedspecchietti) {
                    tv_speech.setBackgroundResource(R.drawable.round_corner);
                    tv_speech.setTextColor(Color.WHITE);

                    arrlist.add("7");

                } else {
                    tv_speech.setBackgroundResource(R.drawable.round_green);
                    tv_speech.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("7");

                    }

                }
                buttonPressedspecchietti = !buttonPressedspecchietti;
                break;

            case R.id.levafreno:

                if (!buttonPressedlevafreno) {
                    tv_levafreno.setBackgroundResource(R.drawable.round_corner);
                    tv_levafreno.setTextColor(Color.WHITE);
                    arrlist.add("8");

                } else {
                    tv_levafreno.setBackgroundResource(R.drawable.round_green);
                    tv_levafreno.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("8");

                    }

                }
                buttonPressedlevafreno = !buttonPressedlevafreno;
                break;
            case R.id.parafrango:

                if (!buttonPressedparafango) {
                    tv_parafango.setBackgroundResource(R.drawable.round_corner);
                    tv_parafango.setTextColor(Color.WHITE);

                    arrlist.add("9");
                } else {
                    tv_parafango.setBackgroundResource(R.drawable.round_green);
                    tv_parafango.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("9");

                    }

                }
                buttonPressedparafango = !buttonPressedparafango;
                break;

            case R.id.freece:

                if (!buttonPressedfreece) {
                    tv_freece.setBackgroundResource(R.drawable.round_corner);
                    tv_freece.setTextColor(Color.WHITE);
                    arrlist.add("10");
                } else {
                    tv_freece.setBackgroundResource(R.drawable.round_green);
                    tv_freece.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("10");

                    }

                }
                buttonPressedfreece = !buttonPressedfreece;
                break;
            case R.id.luci:

                if (!buttonPressedLuci) {
                    tv_luci.setBackgroundResource(R.drawable.round_corner);
                    tv_luci.setTextColor(Color.WHITE);
                    arrlist.add("11");

                } else {
                    tv_luci.setBackgroundResource(R.drawable.round_green);
                    tv_luci.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("11");

                    }

                }
                buttonPressedLuci = !buttonPressedLuci;
                break;

            case R.id.gomme:

                if (!buttonPressedgomme) {
                    tv_gomme.setBackgroundResource(R.drawable.round_corner);
                    tv_gomme.setTextColor(Color.WHITE);

                    arrlist.add("12");

                } else {
                    tv_gomme.setBackgroundResource(R.drawable.round_green);
                    tv_gomme.setTextColor(Color.BLACK);

                    if (arrlist.size() > 0) {
                        arrlist.remove("12");

                    }

                }
                buttonPressedgomme = !buttonPressedgomme;
                break;


            case R.id.image1:
                if (Images == false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        openCamera(1);

                    }

                } else {
                    showDamageImages(1);
                }


                break;
            case R.id.image2:

                if (Images1 == false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
//                        if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
//                            Intent customcam = new Intent(this, CameraFragmentMainActivity.class);
//                            startActivityForResult(customcam, 2);
//                        } else {
//                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
//                            startActivityForResult(takePictureIntent, 2);
//                        }
                        openCamera(2);
                    }

                } else {

                    showDamageImages(2);


                }

                break;
            case R.id.image3:

                if (Images2 == false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
//                        if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
//                            Intent customcam = new Intent(this, CameraFragmentMainActivity.class);
//                            startActivityForResult(customcam, 3);
//                        } else {
//                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
//                            startActivityForResult(takePictureIntent, 3);
//                        }
                        openCamera(3);
                    }

                } else {


                    showDamageImages(3);


                }


                break;


            case R.id.image4:
                if (Images4 == false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        openCamera(4);
                    }

                } else {


                    showDamageImages(1);

                }


                break;
            case R.id.image5:

                if (Images5 == false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        openCamera(5);
                    }

                } else {

                    showDamageImages(2);


                }

                break;
            case R.id.image6:

                if (Images6 == false) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        openCamera(6);
                    }

                } else {

                    showDamageImages(3);

                }


                break;


            //========For Stop Ride========
            case R.id.stop_ride:


                if (isPlayPauseClicked) {
                    /*_playPause.setImageResource(R.drawable.ic_pause);
                    isPlayPauseClicked = false;
                    _palypause_label.setText("Pausa");*/


                    endRideService();

//skumar
                    //ShowInfoAlert();


                    //   resumeVehicleServiceCall(                              );

                } else {
                   /* isPlayPauseClicked = true;
                    _playPause.setImageResource(R.drawable.ic_play_icon);
                    //Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_LONG).show();
                    _palypause_label.setText("Curriculum vitae");
*/


                    stopRide_ServiceCall();
                    // pauseVehicleServiceCall();

                }
                break;
            case R.id.ic_stop_kick:

                Intent customcam = new Intent(this, CameraFragmentMainActivity.class);
                customcam.putExtra("isFrom", "kick");
                startActivityForResult(customcam, CAMERAINTENTCUSTOM);

//                stopRide_ServiceCall();
                // pauseVehicleServiceCall()

                // stopRide_ServiceCall();
            /*    if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
                    startActivityForResult(takePictureIntent, 9);
                }*/

                break;

            case R.id.info_kick:

                showDamagePopUp();

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

            case R.id.bike_heart_break:
                showDamagePopUp();
                break;

            case R.id.play_heart_break:
                showDamagePopUp();
                break;


            case R.id.ic_kickcall:
                phoneCall_method();
                break;
        }


    }

    private void showDamageImages(int index) {
        int position = 0;
        for (Map.Entry<Integer, Bitmap> entry : damageList.entrySet()) {
            if (entry.getKey() == index) {
                break;
            }
            position++;
        }
//        if (position!=0 && !damageList.containsKey(position-1))
////        if (position > damageList.size() - 1)
//            position = 0;

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView_image = LayoutInflater.from(HomeActivity.this).inflate(R.layout.viewpager, viewGroup, false);


        vp_slider = dialogView_image.findViewById(R.id.vp_slider);
        ll_dots = dialogView_image.findViewById(R.id.ll_dots);
        ivLeft = dialogView_image.findViewById(R.id.ivLeft);
        ivRight = dialogView_image.findViewById(R.id.ivRight);
        img_VpClose = dialogView_image.findViewById(R.id.vpclose);
        sliderPagerAdapter = new SliderPagerAdapter(HomeActivity.this);
        vp_slider.setAdapter(sliderPagerAdapter);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = vp_slider.getCurrentItem();
                if (currentPosition > 0) {
                    vp_slider.setCurrentItem(currentPosition - 1);
                }
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = vp_slider.getCurrentItem();
                if (currentPosition < damageList.size() - 1) {
                    vp_slider.setCurrentItem(currentPosition + 1);
                }
            }
        });
        vp_slider.setCurrentItem(position);
        handleArrowImages(position);
        addBottomDots(position);
        if (damageList.size() == 1) {
            ivLeft.setVisibility(View.GONE);
            ivRight.setVisibility(View.GONE);
        }
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handleArrowImages(position);
                addBottomDots(position);


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


        img_VpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_image.dismiss();
            }
        });
    }

    private void handleArrowImages(int position){
        if (position == 0) {
            ivLeft.setVisibility(View.GONE);
            ivRight.setVisibility(View.VISIBLE);
        } else if (position == damageList.size() - 1) {
            ivRight.setVisibility(View.GONE);
            ivLeft.setVisibility(View.VISIBLE);
        } else {
            ivRight.setVisibility(View.VISIBLE);
            ivLeft.setVisibility(View.VISIBLE);
        }
    }

    private void openCamera(int requestCode) {
        if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
            Intent customcam = new Intent(this, CameraFragmentMainActivity.class);
            startActivityForResult(customcam, requestCode);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    private void addBottomDots(int position) {

        dots = new TextView[damageList.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(HomeActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[position].setTextColor(Color.parseColor("#FFFFFF"));
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
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap mphoto;
            File imgFile = null;
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                imagePathOne = data.getStringExtra("imagecustompath");
                imgFile = new File(imagePathOne);
                mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                mphoto = (Bitmap) data.getExtras().get("data");
            }
            Images = true;
            Image1.setImageBitmap(mphoto);

            damageList.put(1, mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathOne = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathOne);
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                AppUtils.deleteImage(imgFile);
            }


            //=====For Service Call================

            //  ButtonOk.setVisibility(View.VISIBLE);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bitmap mphoto2;
            File imgFile = null;
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                imagePathTwo = data.getStringExtra("imagecustompath");
                imgFile = new File(imagePathTwo);
                mphoto2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                mphoto2 = (Bitmap) data.getExtras().get("data");
            }
            Images1 = true;
            Image2.setImageBitmap(mphoto2);
            //  ButtonOk.setVisibility(View.VISIBLE);

            damageList.put(2, mphoto2);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto2);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathTwo = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathTwo);
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                AppUtils.deleteImage(imgFile);
            }

            //=====For Service Call================
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bitmap mphoto3;
            File imgFile = null;
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                imagePathThree = data.getStringExtra("imagecustompath");
                imgFile = new File(imagePathThree);
                mphoto3 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                mphoto3 = (Bitmap) data.getExtras().get("data");
            }
            Images2 = true;
            Image3.setImageBitmap(mphoto3);
            //  ButtonOk.setVisibility(View.VISIBLE);

            damageList.put(3, mphoto3);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto3.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto3);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathThree = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathThree);
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                AppUtils.deleteImage(imgFile);
            }

            //=====For Service Call================
        }

        if (requestCode == 4 && resultCode == RESULT_OK) {
            Bitmap mphoto;
            File imgFile = null;
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                imagePathFour = data.getStringExtra("imagecustompath");
                imgFile = new File(imagePathFour);
                mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                mphoto = (Bitmap) data.getExtras().get("data");
            }
            Images4 = true;
            Image4.setImageBitmap(mphoto);
            damageList.put(1, mphoto);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathFour = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathFour);
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                AppUtils.deleteImage(imgFile);
            }

            //=====For Service Call================

            //  ButtonOk.setVisibility(View.VISIBLE);
        }
        if (requestCode == 5 && resultCode == RESULT_OK) {
            Bitmap mphoto;
            File imgFile = null;
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                imagePathFive = data.getStringExtra("imagecustompath");
                imgFile = new File(imagePathFive);
                mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                mphoto = (Bitmap) data.getExtras().get("data");
            }
            Images5 = true;
            Image5.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            damageList.put(2, mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathFive = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathFive);
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                AppUtils.deleteImage(imgFile);
            }

            //=====For Service Call================
        }

        if (requestCode == 6 && resultCode == RESULT_OK) {
            Bitmap mphoto;
            File imgFile = null;
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                imagePathSix = data.getStringExtra("imagecustompath");
                imgFile = new File(imagePathSix);
                mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            } else {
                mphoto = (Bitmap) data.getExtras().get("data");
            }
            Images6 = true;
            Image6.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);


            damageList.put(3, mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathSix = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathSix);
            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                AppUtils.deleteImage(imgFile);
            }

            //=====For Service Call================
        }


        if (requestCode == 7 && resultCode == RESULT_OK) {


            isRecodraImage = true;
            recordo_mphoto = (Bitmap) data.getExtras().get("data");
            image_recorda.setImageBitmap(recordo_mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            recordo_mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), recordo_mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePath_recorda = finalFile.getPath();
            System.out.println("ImagePath---" + imagePath_recorda);


            //=====For Service Call================
        }

        if (requestCode == REQUEST_QR_CODE && resultCode == RESULT_OK) {
           /* bottomSheet.setVisibility(View.VISIBLE);
            PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, true);
            _play_layout.setVisibility(View.VISIBLE);
            first_bikeLayout.setVisibility(View.GONE);
            fab_menu.setVisibility(View.GONE);
            fab_menu.removeAllViews();
            openBottomScreen();*/
            PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, true);
            UserInformation();
            selectedScooterType = "kick";
            isFromQrCodescreen = true;
        }

        if (requestCode == 9 && resultCode == RESULT_OK) {
            Bitmap recordo_mphoto = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            recordo_mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            //=====For Service Call================
            Uri tempUri = getImageUri(getApplicationContext(), recordo_mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            String imagePath = finalFile.getPath();
            System.out.println("ImagePath---" + imagePath);
            PostDamageDataOnKickStop(imagePath);

            //=====For Service Call================
        }

        if (requestCode == CAMERAINTENTCUSTOM && resultCode == RESULT_OK) {

            String imagePath = data.getStringExtra("imagecustompath");
            System.out.println("ImagePath---" + imagePath);
            PostDamageDataOnKickStop(imagePath);

            //=====For Service Call================
        }

    }

    /* private void UploadDamageFile_Api(final Context context, final String imagePath, int type) {

         //  showProgressbar();
         final ImageselfieRequest req = new ImageselfieRequest();

         // req.setFile(firstbaseimage);
         req.setFile(imagePath);
         req.setType(type);

         Log.e(TAG, "type---"+type);

         String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
         String bearer_authorization = "Bearer " + authorization;

        *//* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",imagePath,requestFile);*//*

        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetDamageMedia(Constants.TOKEN,bearer_authorization, multipartBody, type, damageID, type);
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
*/
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
        ArrayList<Integer> imagespojjo;
        private AlertDialog alertDialog;
        private View dialogView;


        Context context;

        public SliderPagerAdapter(Activity activity) {
            this.activity = activity;
            this.imagespojjo = new ArrayList<>();
            this.image_arraylist = new ArrayList<>();

            for (Map.Entry<Integer, Bitmap> entry : damageList.entrySet()) {
                this.image_arraylist.add(entry.getValue());
                this.imagespojjo.add(entry.getKey());
            }
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
                    int removeID = imagespojjo.get(position);
                    int pos = position;
                    Log.e(TAG, "POS---" + imagespojjo.get(position));
                    if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
                        alertDialog_image.dismiss();
                        removeImage(removeID, pos);
                    } else {
                        ImageRemover(removeID, pos, activity);
                    }
                }
            });

            return view;
        }

        @Override
        public int getCount() {
            return damageList.size();
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
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StartRideSession geo_details = retrofit.create(StartRideSession.class);


        Call<JSONObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, HomeActivity.this);

                    }


                } else {
                    Log.e(TAG, "---Success---");


                    if (response.code() == 200) {
                        try {
                            userRideStatus = 2;

                            myMarker.setVisible(false);
                            if (currentPolyline != null) {
                                currentPolyline.setVisible(false);
                            }

                            showDamagePopUp();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }

                dismissProgressbar();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void stopRide_ServiceCall() {
        stopRide_ServiceCall(false);
    }

    private void stopRide_ServiceCall(final boolean isCallPostDamageAPI) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, false);
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StopRideSessionTwo geo_details = retrofit.create(StopRideSessionTwo.class);

        System.out.println("_vehicleIDkick::::" + _vehicleID);


        Call<JsonObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
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

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, HomeActivity.this);


                        // ReFreash_onCreatemethod();
                        //ShowInfoAlert();

                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            userRideStatus = 0;

                            JsonObject jsonObject = response.body();

                            JsonObject data = jsonObject.getAsJsonObject("data");

                            System.out.println("hdhdhdhhd:::" + jsonObject);

                            _distanceKm = data.get("distance").getAsInt();
                            _durationMinutes = data.get("duration").getAsInt();
                            long startTime = data.get("started").getAsLong();
                            long endTime = data.get("setupEnd").getAsLong();


                            Date date = new Date(startTime);
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            _startTime = dateFormat.format(date);

                            Date date1 = new Date(endTime);
                            DateFormat dateFormat1 = new SimpleDateFormat("hh:mm");
                            _endTime = dateFormat1.format(date1);
                            price = data.get("price").getAsString();

                            Log.e(TAG, "==_startTime=====" + _startTime);

                            Log.e(TAG, "==_endTime=====" + _endTime);

                            JsonObject vehicleData = data.getAsJsonObject("vehicle");
                            km = vehicleData.get("km").getAsInt();

                            if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
//                                showDamagePopUpAfetrSessionComplete();
                                if (isCallPostDamageAPI) {
                                    PostDamageDataAfetrSessionComplete();
                                } else {
                                    fourth_PopUp();
                                }


                            } else {
                                ShowInfoAlert();
                            }


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

    private void stop_error_Alert(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_error_alert, viewGroup, false);

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

        tv_error.setText("" + error_message);

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


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
                ReFreash_onCreatemethod();
            }
        });
    }

    private void ShowInfoAlert() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.business_recorda, viewGroup, false);

        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);

        final EditText _comments = (EditText) dialogView.findViewById(R.id.comment_reicorda);

        image_recorda = (CircleImageView) dialogView.findViewById(R.id.recorda_img);


        heart_recodra = (ImageView) dialogView.findViewById(R.id.recodra_heart);


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
//saikk
                fourth_PopUp();
            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                String _commentsRecorda = _comments.getText().toString();
                ServiceCallForRecorda(_commentsRecorda);

                // fourth_PopUp();

            }
        });

        heart_recodra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDamagePopUp();


            }
        });
        image_recorda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMethod();
            }
        });

    }

    private void callMethod() {
        if (isRecodraImage == false) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (getFromPref(HomeActivity.this, ALLOW_KEY)) {

                    showSettingsAlert();

                } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.CAMERA)) {
                        showAlert();
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
                startActivityForResult(takePictureIntent, 7);
            }
        } else {

            ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.imagepopup_upload, viewGroup, false);

            ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

            ImageView remove = (ImageView) dialogView.findViewById(R.id.remove);

            ImageView right = (ImageView) dialogView.findViewById(R.id.right);

            img.setImageBitmap(recordo_mphoto);

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isRecodraImage = false;
                    alertDialog.dismiss();

                    image_recorda.setImageResource(R.drawable.ic_cir_camera);

                }
            });

            // TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

            //  tv_error.setText("" + error_message);

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
        }
    }

    private void ServiceCallForRecorda(String commentsRecorda) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }

        showProgressbar();
        try {

            JSONObject jo = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONArray otherArray = new JSONArray();
            JSONObject jsonObj = null;

            ArrayList<String> dummyList = new ArrayList<String>();

            final ArrayList<Integer> intArray = new ArrayList<Integer>();

            jsonObj = new JSONObject();
            jsonObj.put("code", 0);
            jsonObj.put("value", false);

            jsonArray.put(jsonObj);


            jo.put("instantSpotted", 0);
            jo.put("parts", jsonArray);

            jo.put("description", commentsRecorda);
            jo.put("other_damage", otherArray);

            Log.e("REs==--===", "req===" + jo.toString());


            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jo.toString());

            String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
            String bearer_authorization = "Bearer " + authorization;


            final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

            RequestInterface register_details = retrofit.create(RequestInterface.class);

            Call<JsonObject> resultRes = register_details.GetPostDammageRes(Constants.TOKEN, bearer_authorization, gsonObject, _vehicleID);
            resultRes.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                    if (!response.isSuccessful()) {
                        Log.e(TAG, "--Response code---" + response.code());
                        Log.e(TAG, "--Response ---" + response.body());


                        if (response.code() != 200) {

                        }


                    } else {
                        Log.e(TAG, "--Success---");


                        if (response.code() == 200) {
                            JsonObject jsonObject = response.body();
                            JsonObject damage_data = jsonObject.getAsJsonObject("data");
                            damageID = damage_data.get("id").getAsInt();


                            if (imagePath_recorda != null) {
                                if (!imagePath_recorda.equalsIgnoreCase("")) {
                                    AppUtils.UploadDamageFile_Api(context, imagePath_recorda, _vehicleID, damageID);

                                }
                            }


                        }


                    }

                    dismissProgressbar();

                    fourth_PopUp();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "--Fail---" + t.getMessage());
                    dismissProgressbar();

                    fourth_PopUp();


                }


            });


        } catch (JSONException je) {

            je.printStackTrace();
        }


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
                    strReturnedAddress.append(address).append(",").append(city).append(" ").append(postalCode).append(",").append(country);
//                    strAdd = city;
                    strAdd = strReturnedAddress.toString();
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


    private void bookVehicle_ServiceCall(int _vehicleID, final TextView timer_bottom, final Button green_btn, final Marker marker) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JSONObject> resultRes = geo_details.GetVehicleBookResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());

                    if (response.code() != 200) {

                        //For testing Only. need to remove
                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        error_Alert(error.getError().getDescription(), context, alertDialog, HomeActivity.this);


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            userRideStatus = 2;

                            JSONObject res = response.body();

                            Log.e(TAG, "--Update--" + res.toString());


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
                            PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, true);

                            //---After draging the bottom  slide view button
                            DisplaySelectedMarker(selected_loc_arrayList, marker);
                            startFreeTime(timer_bottom, green_btn);
                            Geofenceing_Service(1);

                            Vehiclebooked = true;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });

    }

    private void error_Alert(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_error_alert, viewGroup, false);

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

        tv_error.setText("" + error_message);

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


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
                ReFreash_onCreatemethod();
            }
        });
    }

    private void pauseVehicleServiceCall() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }

        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JSONObject> resultRes = geo_details.GetVehiclePauseResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
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
                        try {
                            JSONObject res = response.body();

                            Log.e(TAG, "--Update--" + res.toString());


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

    private void resumeVehicleServiceCall() {


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JSONObject> resultRes = geo_details.GetVehicleResumeResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
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
                        try {
                            JSONObject res = response.body();

                            Log.e(TAG, "--Update--" + res.toString());


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

    private void endRideService() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, false);
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JsonObject> resultRes = geo_details.GetVehicleEndResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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


                            JsonObject jsonObject = response.body();

                            JsonObject data = jsonObject.getAsJsonObject("data");

                            System.out.println("hdhdhdhhd:::" + jsonObject);

                            _distanceKm = data.get("distance").getAsInt();
                            _durationMinutes = data.get("duration").getAsInt();
                            long startTime = data.get("started").getAsLong();
                            long endTime = data.get("setupEnd").getAsLong();


                            Date date = new Date(startTime);
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            _startTime = dateFormat.format(date);

                            Date date1 = new Date(endTime);
                            DateFormat dateFormat1 = new SimpleDateFormat("hh:mm");
                            _endTime = dateFormat1.format(date1);


                            price = data.get("price").getAsString();

                            Log.e(TAG, "==_startTime=====" + _startTime);

                            Log.e(TAG, "==_endTime=====" + _endTime);

                            JsonObject vehicleData = data.getAsJsonObject("vehicle");
                            km = vehicleData.get("km").getAsInt();


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

    private void callStartStopServiceForStopSession(final Context context, final int _vehicleID) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, false);
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StartRideSession geo_details = retrofit.create(StartRideSession.class);


        Call<JSONObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        stopRide_ServiceCallAndRefresh(context, _vehicleID);

                    }


                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());

                dismissProgressbar();

                ReFreash_onCreatemethod();
            }


        });
    }

    private void stopRide_ServiceCallAndRefresh(Context context, int _vehicleID) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, HomeActivity.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISSESSION, false);
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StopRideSessionTwo geo_details = retrofit.create(StopRideSessionTwo.class);


        Call<JsonObject> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, _vehicleID);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());

                    if (response.code() != 200) {

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());


                    }


                } else {
                    Log.e(TAG, "--Success---");

                    ReFreash_onCreatemethod();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

                ReFreash_onCreatemethod();

            }


        });
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);



        /*AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();*/

    }

    private void paymentGateWay_Alert(String message) {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.paymentgateway_alert, viewGroup, false);

        TextView tv_message = (TextView) dialogView.findViewById(R.id.text_message);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.btn_confirm);
        Button _btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        tv_message.setText("" + message);

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


        _btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent i = new Intent(HomeActivity.this, ProfileScreen_Activity.class);
                startActivity(i);


            }
        });

    }

    private void BookingTimeCheckUserInformation(final int _vehicleID, final TextView timer_bottom, final Button green_btn, final Marker marker) {
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

                    int booking_id = 0;
                    if (response.code() == 200) {
                        try {
                            UserInfoResponse userinfo = response.body();
                            //Log.e(TAG, "--ServiceID---"+userinfo.getData().getUser().getServiceId());
                            Integer _siteID = userinfo.getData().getUser().getSiteId();
                            Log.e(TAG, "--_siteID---" + _siteID);

                            //Log.e("TAG", "response 33: "+new Gson().toJson(response.body()) );

                            String obj = new Gson().toJson(response.body());
                            JSONObject res = new JSONObject(obj);
                            JSONObject data = res.getJSONObject("data");
                            JSONObject booking = data.getJSONObject("booking");
                            Log.e(TAG, "booking-=-=-=-" + booking.length());

                            userBookingCount = booking.length();


                            JSONObject user = data.getJSONObject("user");

                            JSONArray avathar_arrayObj = user.getJSONArray("Avatar");
                            Log.e(TAG, "avathar_arrayObj=--=-=-=" + avathar_arrayObj.length());
                            avatharCount = avathar_arrayObj.length();

                            JSONArray document_arrayObj = user.getJSONArray("Document");
                            Log.e(TAG, "document_arrayObj=--=-=-=" + document_arrayObj.length());
                            documentCount = document_arrayObj.length();


                            JSONArray licence_arrayObj = user.getJSONArray("LicensePlate");
                            Log.e(TAG, "licence_arrayObj=--=-=-=" + licence_arrayObj.length());

                            licenseCount = licence_arrayObj.length();

                            AppUtils.SaveDataInSharePreference(context, userinfo);


                            PreferenceUtil.getInstance().saveInt(context, Constants.SITE_ID, _siteID);

                            PreferenceUtil.getInstance().saveBoolean(context, Constants.PAYMENT_CREDITED, userinfo.getData().getUser().getPaymentCreated());

                            Log.e(TAG, "=-=---76676=-=-==");
                            bookVehicle_ServiceCall(_vehicleID, timer_bottom, green_btn, marker);


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

    private void Alert_Bookinginformation(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_info_alert, viewGroup, false);

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

        tv_error.setText("" + error_message);

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


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();

                ReFreash_onCreatemethod();

            }
        });
    }

    class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage..." + msg.toString());

            super.handleMessage(msg);

            switch (msg.what) {
                case LocationUpdatesService.LOCATION_MESSAGE:
                    Location obj = (Location) msg.obj;
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                   /* TRACK_USER_CURRENT_LATITUDE = obj.getLatitude();
                    TRACK_USER_CURRENT_LANGITUDE = obj.getLongitude();*/

//                    TRACK_USER_CURRENT_LATITUDE = obj.getLatitude();
//                    TRACK_USER_CURRENT_LANGITUDE = obj.getLongitude();

                    CURRENT_LATITUDE = obj.getLatitude();
                    CURRENT_LANGITUDE = obj.getLongitude();
                    if (currentLocatinMarker != null) {
                        LatLng latLng = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                        points.add(latLng);
                        requestDirectionPast(latLng, new ArrayList<LatLng>(), false);
                    }

                    Log.e(TAG, "TRACKING LAT-LANG--" + CURRENT_LATITUDE + "--" + CURRENT_LANGITUDE);

                    //Toast.makeText(context, "LAT :  " + obj.getLatitude() + "\nLNG : " + obj.getLongitude(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void Alert_information(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_info_alert, viewGroup, false);

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

        tv_error.setText("" + error_message);

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


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_six = new Intent(HomeActivity.this, BusinessActivity_Screen.class);
                startActivity(intent_six);

            }
        });
    }

    private void refreshList() {
        if (vehicle_data_array != null) {
            vehicle_data_array.clear();
        }
        vehicle_data_array = getVehicleTypeList();
        ShowMarkerVehiclesOnMap(vehicle_data_array);
        setUpCurrentLocation(CURRENT_LATITUDE, CURRENT_LANGITUDE);
    }


    private void updateDamageView() {
        if (selectedScooterType != null && selectedScooterType.equalsIgnoreCase(SCOOTERTYPE.KICKSCOOTER.getType())) {
            tv_gomme.setVisibility(View.GONE);
            tv_altro.setVisibility(View.GONE);
            ic_type.setImageResource(R.drawable.ic_cover);
            tv_bauletto.setText("Manubrio");
            tv_selia.setText("Cavalletto");
            tv_cavaletto.setText("Ruota posteriore");
            tv_carena.setText("Ruota anteriore");
            tv_caschi.setText("Altro");
            tv_speech.setText("Acceleratore");
            tv_levafreno.setText("Freno");
            tv_parafango.setText("Parafango");
            tv_freece.setText("Campanello");
            tv_luci.setText("Luce");


/*
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 50, 0, 0);
            commentbox.setLayoutParams(lp);*/

          /*  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(ViewGroup.LayoutParams.WRAP_CONTENT, 50, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            commentbox.setLayoutParams(params);*/


        } else {
            tv_gomme.setVisibility(View.VISIBLE);
            tv_altro.setVisibility(View.VISIBLE);
            ic_type.setImageResource(R.drawable.ic_sessride);
        }

    }

    private List<VehicleResponse.Data> getVehicleTypeList() {
        List<VehicleResponse.Data> data = new ArrayList<>();
        if (vechicle_data != null && vechicle_data.size() > 0) {
            for (VehicleResponse.Data vehicle : vechicle_data) {
                try {
                    if (vehicle.getType().equalsIgnoreCase(selectedScooterType)) {
                        data.add(vehicle);
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "UN VEICOLO HA DATO TIPO NULL");
                }
            }
        }
        return data;
    }

    enum SCOOTERTYPE {
        SCOOTER("scooter"), KICKSCOOTER("kick");

        private String type;

        SCOOTERTYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private void PostDamageDataOnKickStop(final String imagePath) {
        showProgressbar();

        JSONObject jo = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray otherArray = new JSONArray();
        JSONObject jsonObj = null;

        try {
            jo.put("instantSpotted", 0);
            jo.put("parts", jsonArray);

            jo.put("description", "Parked");
            jo.put("other_damage", otherArray);

            Log.e("Suresh==--===", "req===" + jo.toString());


            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jo.toString());

            String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
            String bearer_authorization = "Bearer " + authorization;


            final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

            RequestInterface register_details = retrofit.create(RequestInterface.class);

            Call<JsonObject> resultRes = register_details.GetPostDammageRes(Constants.TOKEN, bearer_authorization, gsonObject, _vehicleID);
            resultRes.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    // dismissProgressbar();

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "--Response code---" + response.code());
                        Log.e(TAG, "--Response ---" + response.body());
                        dismissProgressbar();

                        if (response.code() != 200) {

                        }


                    } else {
                        Log.e(TAG, "--Success---");
                        if (response.code() == 200) {
                            JsonObject res = response.body();
                            if (response.code() == 200) {
                                JsonObject jsonObject = response.body();
                                JsonObject damage_data = jsonObject.getAsJsonObject("data");
                                damageID = damage_data.get("id").getAsInt();

                                if (imagePath != null) {
                                    if (!imagePath.equalsIgnoreCase("")) {
                                        AppUtils.UploadDamageFile_Api(context, imagePath, _vehicleID, damageID);

                                    }
                                }
                            }

                            dismissProgressbar();
                            showDamagePopUpAfetrSessionComplete();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "--Fail---" + t.getMessage());
                    dismissProgressbar();

                }


            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPricedetailsApiKick() {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if (!_isInternetAvailable) {
            return;
        }

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JsonObject> resultRes = geo_details.GetResponsepricedetails(Constants.TOKEN, bearer_authorization, 1);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    if (response.code() != 200) {

                    }

                } else {
                    //Log.e(TAG, "--Success---");
                    if (response.code() == 200) {
                        try {
                            JsonObject jsonObject = response.body();
                            System.out.println("hdhdhdhhd:::" + jsonObject);
                            JsonObject json = jsonObject.getAsJsonObject("data");
                            JsonObject booking = json.getAsJsonObject("movo");
                            JsonObject pricing = booking.getAsJsonObject("pricing");
                            JsonObject multi_vehicle = pricing.getAsJsonObject("multi_vehicle");
                            JsonObject time_thresholds = multi_vehicle.getAsJsonObject("time_thresholds");
                            JsonObject kick_scooter = time_thresholds.getAsJsonObject("kick");
                            JsonArray timethresholds = kick_scooter.getAsJsonArray("thresholds");
                            TextView _price = findViewById(R.id.price1);
                            TextView _price1 = findViewById(R.id.price2);
                            for (int i = 0; i < timethresholds.size(); i++) {
                                JsonObject json_price_data = timethresholds.get(i).getAsJsonObject();
                                double price_kick = json_price_data.get("price").getAsDouble();
                                if (i == 0) {
                                    PriceEuro = "" + price_kick;
                                    _price.setText("€ " + PriceEuro + " sblocca");
                                } else {
                                    _price1.setText("€ " + price_kick + " / min");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
                dismissProgressbar();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }
}
