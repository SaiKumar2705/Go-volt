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
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
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
import com.quadrant.interfaces.EmailInformation;
import com.quadrant.interfaces.GeofencingInterface;
import com.quadrant.interfaces.GetSiteID_Interface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.StartRideSession;
import com.quadrant.interfaces.StopRideSession;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.locationtracker.LocationUpdatesService;
import com.quadrant.model.ImagesPhoto;
import com.quadrant.model.PublicPrivatePojo;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.EmailRequest;
import com.quadrant.response.EmailResponse;
import com.quadrant.response.GeoFenceResponse;
import com.quadrant.response.SiteIDResponse;
import com.quadrant.response.StartRideErrorResponse;
import com.quadrant.response.UserInfoResponse;
import com.quadrant.response.VehicleResponse;

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

public class BusinessActivity_Screen extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, View.OnClickListener {


    ArrayList<VehicleResponse.Data> vehicle_data_arrayone;


    ArrayList<VehicleResponse.Data> vehicle_data_arraytwo;


    private Context context;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    //  private FloatingActionMenu fab_menu;
    private ArrayList<LatLng> listMyLatLng;
    private ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();
    private ArrayList<MyLatLng> selected_loc_arrayList = new ArrayList<MyLatLng>();
    private Marker mSelectedMarker;
    private Marker myMarker;
    private static final String TAG = BusinessActivity_Screen.class.getSimpleName();

    Integer listvalues1;

    ArrayList<Integer> storelatlan = new ArrayList<Integer>();

    ArrayList<PublicPrivatePojo> storeintsiteid = new ArrayList<>();


    private BottomSheetBehavior mBottomSheetBehavior, mBottomSheetBehaviorGreen;

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

    int SiteId;

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";
    private RelativeLayout first_bikeLayout;
    private LinearLayout _play_layout;
    private RelativeLayout social_bike_layout_one;
    private RelativeLayout social_play_layout;
    private ImageView imageOne, _pinkhook, icCloseVp;
    TextView tv_bauletto, tv_selia, tv_cavaletto, tv_carena, tv_caschi, tv_altro, tv_speech, tv_levafreno, tv_parafango, tv_freece, tv_luci, tv_gomme;
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

    String Emaildata;

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

    private ImageView _stopRide;

    private boolean isUserStartSession = false;


    LinearLayout slideLayout, timer_buttons_Layout;
    private Marker my_marker;
    private ImageView call_bike_imageview, whatsUp_bike_imageview, call_play_imageview, whatsUp_play_imageview;
    SeekBar sb;
    //vr
    VehicleResponse response1;
    private boolean isGreenButtonVisible = false;
    private CountDownTimer yourCountDownTimer;
    String _type = "";
    private String progressPercentage;

    ArrayList<LatLng> markerPoints;
    private SimpleLocation location;

    private ImageView current_loc_button;
    private ImageView center_imageview;
    private double CURRENT_LATITUDE;
    private double CURRENT_LANGITUDE;
    TextView _time;
    TextView _distance;
    private String currentLocationID;
    int _percentageOfsb = 0;
    private int _vehicleID, validDocument;
    private String _vehicleStatus;
    private int userBookingCount = 0;
    private int licenseCount = 0;
    private int avatharCount = 0;
    private int documentCount = 0;
    private String walk_time = "";
    private String freewalkTime = "";
    //private OnLocationUpdatedListener locationListener;
    Runnable runnable;
    private Handler handler;
    LocationManager locationManager;
    String mprovider;
    // private boolean isVehiclePauseServiceCalled = false;
    private ToggleButton toogle_btn;
    private boolean isPublic;
    List<List<Double>> list_list_points = null;
    List<Double> double_list;
    List<Double> all_double_list;
    ArrayList<LatLng> firstLatLng = new ArrayList<LatLng>();
    int km = 0;
    String _startTime = "0";
    String _endTime = "0";
    int _durationMinutes = 0;
    int _distanceKm = 0;
    String price = "";
    private int siteIdApiIndex;
    private int siteIdByLocationAPIIndex;
    private CircleImageView image_recorda;
    private ImageView heart_recodra;
    private boolean isRecodraImage = false;
    private Bitmap recordo_mphoto;
    private String imagePath_recorda = "";
    private String imagePathOne;
    private String imagePathTwo;
    private String imagePathThree;
    private String imagePathFour;
    private String imagePathFive;
    private String imagePathSix;
    private int damageID;
    ArrayList<String> arrlist = new ArrayList<String>();
    private EditText commentbox;
    private ProgressBar progressbar;
    private String _vehicleRideStatus;
    double _vehicleLastLatitude;
    double _vehicleLastLongitude;
    ImageView _playPause;
    TextView _palypause_label;
    private String pastLocationID = "";
    private TextView timer_bottom;
    private Button green_btn;
    private String vehicleplate;
    private ImageView bikeHeart_break, playHeart_break;
    private String PriceEuro;
    private List<GeoFenceResponse.Data> datalist;
    private TextView tv_reserved;
    private View ivLeft, ivRight;
    private TextView autonomiakm;
    private static int SCOOTER_MAX_KM = 80;
    private Marker currentLocatinMarker, destinationMarker;
    private List<LatLng> points;
    private Polyline polyline;
    private boolean isPolyLoaded;
    public static final String MESSENGER_INTENT_KEY = "msg-intent-key";
    private IncomingMessageHandler mHandler;
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
        final AlertDialog alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_image.dismiss();
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
        damageList = new TreeMap<>();
        createGoogleApi();

        //   buisenessmenupopup();

		/*fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
		fab_menu.setVisibility(View.GONE);*/

        LinearLayout menu_ll = (LinearLayout) findViewById(R.id.menu_layout);
        menu_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessActivity_Screen.this, SideMenu.class);
                startActivity(i);
            }
        });


        //handling menu status (open or close)
	   /* fab_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
			@Override
			public void onMenuToggle(boolean opened) {
				if (opened) {
				   // showToast("Menu is opened");
				} else {
					//showToast("Menu is closed");
				}
			}
		});*/
        points = new ArrayList<LatLng>();
        setUpView();
        ToggleButton1Colorchange();

        LocationsUpdate();


        CurrentLocations();

        // CallUserStatusApi();

        //siteidApi();

        if (savedInstanceState == null) {

            mGeofenceList = new ArrayList<Geofence>();

            int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (resp == ConnectionResult.SUCCESS) {

                initGoogleAPIClient();

            } else {
                Log.e(TAG, "Your Device doesn't support Google Play Services.");
            }


        }

        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDamagePopUp();
            }
        });
        sb = colayout.findViewById(R.id.normal_green_slide);
        _playPause = (ImageView) colayout.findViewById(R.id.play_pause);
        _palypause_label = (TextView) colayout.findViewById(R.id.palypause_label);
        _playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isPlayPauseClicked) {
                    if (isPublic)
                        _playPause.setImageResource(R.drawable.ic_pause);
                    else
                        _playPause.setImageResource(R.drawable.ic_pink_pause);
                    isPlayPauseClicked = false;
                    _palypause_label.setText("PAUSA");

                    resumeVehicleServiceCall();

                } else {
                    isPlayPauseClicked = true;
                    if (isPublic)
                        _playPause.setImageResource(R.drawable.ic_play_icon);
                    else
                        _playPause.setImageResource(R.drawable.ic_pink_play);
                    //Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_LONG).show();
                    _palypause_label.setText("CORSA");

                    pauseVehicleServiceCall();

                }
            }
        });

        setBottomSheet();


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if (_isInternetAvailable) {

            UserInformation();

        } else {
            Toast.makeText(context, "Please connect internet.", Toast.LENGTH_SHORT).show();

        }

    }

    private void LocationTarckerServiceStart() {
        mHandler = new IncomingMessageHandler();
        Intent startServiceIntent = new Intent(this, LocationUpdatesService.class);
        Messenger messengerIncoming = new Messenger(mHandler);
        startServiceIntent.putExtra(MESSENGER_INTENT_KEY, messengerIncoming);
        startService(startServiceIntent);
    }

    private void locationStopService() {
        stopService(new Intent(BusinessActivity_Screen.this, LocationUpdatesService.class));

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

                    CURRENT_LATITUDE = obj.getLatitude();
                    CURRENT_LANGITUDE = obj.getLongitude();

//					CURRENT_LANGITUDE = 78.3810;
//					CURRENT_LATITUDE = 17.4504;

                    if (currentLocatinMarker != null) {
                        LatLng latLng = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                        points.add(latLng);
//						mMap.clear();  //clears all Markers and Polylines
//
//						PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//						for (int i = 0; i < points.size(); i++) {
//							LatLng point = points.get(i);
//							options.add(point);
//						}
//						BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot_human);
//						if (isPublic) {
//							icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot);
//						}
//
//						if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISBUSINESS, false)) {
//							icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_location);
//							if (isPublic || PreferenceUtil.getInstance().getBoolean(context, Constants.ISPUBLIC, true)) {
//								icon = BitmapDescriptorFactory.fromResource(R.drawable.black_dot);
//							}
//						}
//						Marker marker = mMap.addMarker(new MarkerOptions()
//								.position(new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE))
//								.icon(icon));
//						marker.setTag("CurrentLocation");
//						currentLocatinMarker = marker;
//						 //add Marker in current position
//						polyline = mMap.addPolyline(options); //add Polyline
                        requestDirectionPast(latLng, new ArrayList<LatLng>(), false);

                    }

                    Log.e(TAG, "TRACKING LAT-LANG--" + CURRENT_LATITUDE + "--" + CURRENT_LATITUDE);

                    //Toast.makeText(context, "LAT :  " + obj.getLatitude() + "\nLNG : " + obj.getLongitude(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void setBottomSheet() {
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
        updateViews();

    }

    private void updateViews() {
        if (isPublic) {
            call_bike_imageview.setImageResource(R.drawable.ic_call);
            call_play_imageview.setImageResource(R.drawable.ic_call);
            whatsUp_bike_imageview.setImageResource(R.drawable.ic_whatapp);
            whatsUp_play_imageview.setImageResource(R.drawable.ic_whatapp);
            bikeHeart_break.setImageResource(R.drawable.ic_break);
            ((ImageView) findViewById(R.id.bike_logo)).setImageResource(R.drawable.ic_greenbike);
            /*  ((ImageView) findViewById(R.id.iv_petrol)).setImageResource(R.drawable.ic_petrol);*/
            ((ImageView) findViewById(R.id.iv_man)).setImageResource(R.drawable.ic_man);
            ((ImageView) findViewById(R.id.iv_euro)).setImageResource(R.drawable.ic_euro_one);
            ((ImageView) findViewById(R.id.iv_loc)).setImageResource(R.drawable.resize_loc);
            playHeart_break.setImageResource(R.drawable.ic_break);
            if (isPlayPauseClicked) {
                ((ImageView) findViewById(R.id.play_pause)).setImageResource(R.drawable.ic_play_icon);
            } else {
                ((ImageView) findViewById(R.id.play_pause)).setImageResource(R.drawable.ic_pause);
            }

            sb.setThumb(getResources().getDrawable(R.drawable.dots));
            sb.setBackgroundResource(R.drawable.slidefull);
            findViewById(R.id.sblocca).setBackgroundResource(R.drawable.round_corner);
            findViewById(R.id.btn_avela).setBackgroundResource(R.drawable.round_corner_whitearound);
            ((ImageView) findViewById(R.id.iv_charger)).setImageResource(R.drawable.ic_batteri);
            _stopRide.setImageResource(R.drawable.ic_start);
            imageOne.setImageResource(R.drawable.ic_box);
//            Drawable img = getResources().getDrawable(R.drawable.ic_round_timer);
//            ((TextView) findViewById(R.id.timer)).setCompoundDrawables(img, null, null, null);
            progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.circle));
        } else {
            call_bike_imageview.setImageResource(R.drawable.ic_pink_call);
            call_play_imageview.setImageResource(R.drawable.ic_pink_call);
            whatsUp_bike_imageview.setImageResource(R.drawable.ic_pink_whatsup);
            whatsUp_play_imageview.setImageResource(R.drawable.ic_pink_whatsup);
            bikeHeart_break.setImageResource(R.drawable.ic_pink_hartbreak);
            ((ImageView) findViewById(R.id.bike_logo)).setImageResource(R.drawable.ic_pinkbike);
            /* ((ImageView) findViewById(R.id.iv_petrol)).setImageResource(R.drawable.ic_pinkbunk);*/
            ((ImageView) findViewById(R.id.iv_man)).setImageResource(R.drawable.ic_human_pink);
            ((ImageView) findViewById(R.id.iv_euro)).setImageResource(R.drawable.ic_helmet_pink);
            ((ImageView) findViewById(R.id.iv_loc)).setImageResource(R.drawable.ic_pink_marker);
            playHeart_break.setImageResource(R.drawable.ic_pink_hartbreak);
            ((ImageView) findViewById(R.id.play_pause)).setImageResource(R.drawable.ic_pink_pause);
            if (isPlayPauseClicked) {
                ((ImageView) findViewById(R.id.play_pause)).setImageResource(R.drawable.ic_pink_play);
            } else {
                ((ImageView) findViewById(R.id.play_pause)).setImageResource(R.drawable.ic_pink_pause);
            }
            sb.setThumb(getResources().getDrawable(R.drawable.ic_sb_dot));
            sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
            findViewById(R.id.sblocca).setBackgroundResource(R.drawable.pink_round_corner);
            findViewById(R.id.btn_avela).setBackgroundResource(R.drawable.round_corner_pink);
            ((ImageView) findViewById(R.id.iv_charger)).setImageResource(R.drawable.ic_pink_charger);
            _stopRide.setImageResource(R.drawable.ic_pink_stop);
            imageOne.setImageResource(R.drawable.ic_pink_deck);
//            Drawable img = getResources().getDrawable(R.drawable.ic_pink_progress);
//            ((TextView) findViewById(R.id.timer)).setCompoundDrawables(img, null, null, null);
            progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_pink));

        }

    }

    private void CallUserStatusApi() {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }

        showProgressbar();
        siteIdApiIndex = 0;
        siteIdByLocationAPIIndex = 0;
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

	  /*  EmailRequest req_fgt = new EmailRequest();
		req_fgt.setEmail(Emaildata);*/
        UserInformation user_status_details = retrofit.create(UserInformation.class);
        Call<UserInfoResponse> resultRes = user_status_details.GetResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {

                if (!response.isSuccessful()) {
                    dismissProgressbar();
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "Response:::::" + response.body());

                    System.out.println("Response::::" + response.body());


                    if (response.code() != 200) {

                        //alertDialog.dismiss();

                    }


                } else {
                    Log.e(TAG, "--Success---");

                    int booking_id = 0;
                    if (response.code() == 200) {


//                        dismissProgressbar();

                        UserInfoResponse userInfoResponse = response.body();
                        // JSONArray ja_data = =response.body().getJSONArray("data");



					  /*  List<UserInfoResponseData.UserSite> objectList = userInfoResponse.getData().getUser().getUserSites();

						for(int n = 0; n < objectList.size(); n++) {

							int SiteId =objectList.get(n).getSiteId();

							System.out.println("ffffff::::"+SiteId);

						}*/
                        List<UserInfoResponse.UserSite> objectList = userInfoResponse.getData().getUser().getUserSites();

                        for (int n = 0; n < objectList.size(); n++) {
                            SiteId = objectList.get(n).getSiteId();
                            listvalues1 = new Integer(objectList.get(n).getSiteId());
                            System.out.println("dhdddjjdjjd" + SiteId);
                            storelatlan.add(listvalues1);
                        }


                        if (storelatlan.size() > 1) {

                            for (int j = 0; j < storelatlan.size(); j++) {

                                Integer LatitudesLong = storelatlan.get(j);

                                siteidApi(LatitudesLong);

                            }
                        } else {
                            dismissProgressbar();
                            buisenessmenupopup();

                        }



					   /* if(objectList.size()>1){

							siteidApi(SiteId);

						} else{



						}*/



					   /* List<Object> UserSites =userInfoResponse.getData().getUser().getUserSites();
						System.out.println("dhdddjjdjjd"+UserSites);
						if(UserSites.size()>1){




						} else{

						   // Openbenuventopopup();

							buisenessmenupopup();

						}*/
                        // Integer _siteID = userInfoResponse.getData().getUser().getSiteId();

                        //  System.out.println("gsgshshsh:::"+_siteID);


                        // Toast.makeText(getApplicationContext(), "User Api Sucess", Toast.LENGTH_SHORT).show();
                        //   Openbenuventopopup();


                    }


                }

//                dismissProgressbar();
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });


    }

    private void siteidApi(int siteId) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        final int id = siteId;

//        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);


        RequestInterface user_status_details = retrofit.create(RequestInterface.class);
        Call<JsonObject> resultRes = user_status_details.GetResponsesiteid(Constants.TOKEN, bearer_authorization, siteId);

        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                siteIdApiIndex += 1;
                if (response.code() != 200) {
                    dismissProgressbar();
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                } else {
                    if (response.code() == 200) {
//                        Toast.makeText(getApplicationContext(), "SiteId Api Sucess", Toast.LENGTH_SHORT).show();
                        JsonObject jsonObject = response.body();
                        System.out.println("bdjjbdbd::::" + jsonObject);
                        JsonObject data_details = jsonObject.getAsJsonObject("data");
                        String PrivateorPublic = data_details.get("private").getAsString();
                        System.out.println("privateorpublic:::" + PrivateorPublic);
                        int id = data_details.get("id").getAsInt();

                        storeintsiteid.add(new PublicPrivatePojo(id, PrivateorPublic));
                        if (siteIdApiIndex == storelatlan.size()) {
                            if (storeintsiteid.size() > 1) {
                                System.out.println("soosoosos:::" + storeintsiteid);
                                for (int j = 0; j < storeintsiteid.size(); j++) {

                                    Integer Int = storeintsiteid.get(j).getId();

                                    String puborpri = storeintsiteid.get(j).getPrivateorPublic();

                                    System.out.println("bcjnfjnff:::" + Int);

                                    boolean isLast = (j == storeintsiteid.size() - 1) ? true : false;
                                    SiteIdByLocationApi(Int, puborpri, isLast);
                                }

                            } else {
                                dismissProgressbar();

                            }
                        }



					   /* list.add(data_details.get("id").getAsInt());

						for(int j=0;j<list.size();j++){

							int SeriviceSiteid = list.get(id);

							System.out.println("SeriviceSiteid:::"+SeriviceSiteid);


						}
*/
					   /* int id = data_details.get("id").getAsInt();
						list.add(id);
						System.out.println("intlist::::"+list);*/
                        //saikk
                        //  CallVehicleService(_siteID);
                        System.out.println("storesiteid::" + storeintsiteid);
                        System.out.println("getid:::" + id);

                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissProgressbar();
                Log.d("MainActivity", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });
    }


    private void getStatusApi(int siteId, final String _vehicleRideStatus, final VehicleResponse.Data data1) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);


        RequestInterface user_status_details = retrofit.create(RequestInterface.class);
        Call<JsonObject> resultRes = user_status_details.GetResponsesiteid(Constants.TOKEN, bearer_authorization, siteId);

        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                dismissProgressbar();
                if (response.code() != 200) {
                    dismissProgressbar();
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                } else {
                    if (response.code() == 200) {
                        JsonObject jsonObject = response.body();
                        System.out.println("bdjjbdbd::::" + jsonObject);
                        JsonObject data_details = jsonObject.getAsJsonObject("data");
                        boolean isPrivate = data_details.get("private").getAsBoolean();
                        System.out.println("privateorpublic:::" + isPrivate);
                        if (isPrivate) {
                            isPublic = false;
                        } else {
                            isPublic = true;
                        }
                        isPublic = PreferenceUtil.getInstance().getBoolean(context, Constants.ISPUBLIC, true);
                        if (_vehicleRideStatus.equalsIgnoreCase("booked")) {
                            social_bike_layout_one.setVisibility(View.VISIBLE);
                        }
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        _play_layout.setVisibility(View.VISIBLE);
                        social_play_layout.setVisibility(View.VISIBLE);
                        bottomSheet.setVisibility(View.VISIBLE);
                        String autonomiascoot = String.valueOf(Math.round((data1.getTotalPercentage() * SCOOTER_MAX_KM / 100.)));
                        autonomiakm.setText(autonomiascoot + " Km");
                        setBatteryPercentage(data1);
                        updateViews();

                        Geofenceing_Service(1);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dismissProgressbar();
                Log.d("MainActivity", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });
    }

    private void SiteIdByLocationApi(final Integer anInt, String puborpri, final boolean isLast) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }

        final int siteidbyvehicle = anInt;

        final String IsprorPb = puborpri;

        System.out.println("IsprorPb::::" + IsprorPb);

//        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);


        RequestInterface user_status_details = retrofit.create(RequestInterface.class);
        Call<VehicleResponse> resultRes = user_status_details.GetResponsebyvehiclesiteid(Constants.TOKEN, bearer_authorization, anInt);

        resultRes.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                siteIdByLocationAPIIndex += 1;
                if (response.code() != 200) {
                    dismissProgressbar();
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

                } else {
//                    dismissProgressbar();

                    if (response.code() == 200) {

                        VehicleResponse vehicleResponse = response.body();

                        if (IsprorPb.equals("true")) {


                            vehicle_data_arrayone = new ArrayList<VehicleResponse.Data>();

                            vehicle_data_arrayone = (ArrayList<VehicleResponse.Data>) vehicleResponse.getData();

                            System.out.println("getpins::::" + vehicle_data_arrayone.size());
                        } else if (IsprorPb.equals("false")) {

                            vehicle_data_arraytwo = new ArrayList<VehicleResponse.Data>();
                            ArrayList<VehicleResponse.Data> vehicle_data_array_two_temp = new ArrayList<VehicleResponse.Data>();
                            vehicle_data_array_two_temp = (ArrayList<VehicleResponse.Data>) vehicleResponse.getData();
                            for (VehicleResponse.Data vehicleData : vehicle_data_array_two_temp) {
                                if (vehicleData.getType() != null && !vehicleData.getType().equalsIgnoreCase("kick")) {
                                    vehicle_data_arraytwo.add(vehicleData);
                                }
                            }

                            System.out.println("getpins::::" + vehicle_data_arraytwo.size());
                        }
                        if (siteIdByLocationAPIIndex == storeintsiteid.size()) {
                            dismissProgressbar();
                            isPublic = true;
                            ShowMarkerVehiclesOnMap();
                            System.out.println("togglechnage1st:::" + vehicle_data_arrayone.size());
                            openBottomScreen();
                            toogle_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_default));
                            center_imageview.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_logo_one));
                            setUpCurrentLocation(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                            updateViews();
                        }





					   /* if(siteidbyvehicle==1) {

							 response1 =response.body();

							vehicle_data_array = new ArrayList<VehicleResponse.Data>();
							vehicle_data_array = response1.getData();

							System.out.println("firstarray:::"+vehicle_data_array.size());


							ShowMarkerVehiclesOnMap(vehicle_data_array);

							Toast.makeText(getApplicationContext(), "first Siteby vehicle Api sucess", Toast.LENGTH_SHORT).show();

						} *//*else if(siteidbyvehicle==2){



							response1 =response.body();

							vehicle_data_array = new ArrayList<VehicleResponse.Data>();
							vehicle_data_array = response1.getData();


							System.out.println("secondarray:::"+vehicle_data_array.size());



							ShowMarkerVehiclesOnMap(vehicle_data_array);


							Toast.makeText(getApplicationContext(), "Second Siteby vehicle Api sucess", Toast.LENGTH_SHORT).show();

						}*/


                    }


                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                dismissProgressbar();
                Log.d("MainActivity", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });

//        dismissProgressbar();


    }

    private void getvehiclesbysiteid() {


    }

  /*  private void siteidApi() {

   int     siteId=2;
		showProgressbar();

		String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
		String bearer_authorization = "Bearer " + authorization;

		Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

*//*
		RequestInterface user_status_details = retrofit.create(RequestInterface.class);
		Call<SiteIDResponse> resultRes = user_status_details.GetResponsesiteid(Constants.TOKEN, bearer_authorization,siteId);

		resultRes.enqueue(new Callback<SiteIDResponse>() {
			@Override
			public void onResponse(Call<SiteIDResponse> call, Response<SiteIDResponse> response) {

				dismissProgressbar();

				if (!response.isSuccessful()) {
					Log.e(TAG, "--Response code---" + response.code());
					Log.e(TAG, "--Response ---" + response.body());


					if (response.code() != 200) {

						alertDialog.dismiss();
					}


				} else {
					Log.e(TAG, "--Success---");

					int booking_id = 0;
					if (response.code() == 200) {


						Openbenuventopopup();


					}


				}
			}

			@Override
			public void onFailure(Call<SiteIDResponse> call, Throwable t) {
				Log.e(TAG, "--Fail---" + t.getMessage());
				dismissProgressbar();

			}


		});*//*


		RequestInterface user_status_details = retrofit.create(RequestInterface.class);
		Call<SiteIDResponse> resultRes = user_status_details.GetResponsesiteid(Constants.TOKEN, bearer_authorization,siteId);
		resultRes.enqueue(new Callback<SiteIDResponse>() {
			@Override
			public void onResponse(Call<SiteIDResponse> call, Response<SiteIDResponse> response) {
				dismissProgressbar();
				if (response.isSuccessful()) {

					Toast.makeText(getApplicationContext(), "SiteId Api Sucess", Toast.LENGTH_SHORT).show();

				}else {

				}
			}

			@Override
			public void onFailure(Call<SiteIDResponse> call, Throwable t) {
				dismissProgressbar();
				//LOGD(TAG, "onFailure: " + t.getMessage());
			}
		});

	}*/

    private void buisenessmenupopup() {
        toogle_btn.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.buisness_menu_alert, viewGroup, false);

        final EditText _email = (EditText) dialogView.findViewById(R.id.email);
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

                Emaildata = _email.getText().toString();

                if (Emaildata.length() > 0) {
                    alertDialog.dismiss();

                    CallemailApi();
                } else {

                    Toast.makeText(getApplicationContext(), "per favore inserisci email", Toast.LENGTH_SHORT).show();

                }

                //saik
            }
        });
    }

    private void CallemailApi() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }

        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        EmailRequest req_fgt = new EmailRequest();
        req_fgt.setEmail(Emaildata);
        EmailInformation register_details = retrofit.create(EmailInformation.class);
        Call<EmailResponse> resultRes = register_details.GetResponse(Constants.TOKEN, bearer_authorization, req_fgt);
        resultRes.enqueue(new Callback<EmailResponse>() {
            @Override
            public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                        alertDialog.dismiss();
                    }


                } else {
                    Log.e(TAG, "--Success---");

                    int booking_id = 0;
                    if (response.code() == 200) {


                        Openbenuventopopup();


                    }


                }
            }

            @Override
            public void onFailure(Call<EmailResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void Openbenuventopopup() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.buisness_benuveno, viewGroup, false);

        //  ImageView _ic_correct = (ImageView) dialogView.findViewById(R.id.ic_correct);
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

                //showDamagePopUpAfetrSessionComplete();
            }
        });

    }

    private void LocationsUpdate() {


    }


    private void CurrentLocations() {

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


	   /*CURRENT_LATITUDE = 45.478241;
		CURRENT_LANGITUDE = 9.18351;*/


    }


    private void showDamagePopUp() {
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
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.business_segnalazione, viewGroup, false);
        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.buttonOk);
        TextView damageplate = (TextView) dialogView.findViewById(R.id.damageplate);
        tv_bauletto = dialogView.findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(BusinessActivity_Screen.this);
        tv_selia = dialogView.findViewById(R.id.selia);
        tv_selia.setOnClickListener(BusinessActivity_Screen.this);
        tv_cavaletto = dialogView.findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(BusinessActivity_Screen.this);
        tv_carena = dialogView.findViewById(R.id.carena);
        tv_carena.setOnClickListener(BusinessActivity_Screen.this);
        tv_caschi = dialogView.findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(BusinessActivity_Screen.this);
        tv_altro = dialogView.findViewById(R.id.altro);
        tv_altro.setOnClickListener(BusinessActivity_Screen.this);
        tv_speech = dialogView.findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(BusinessActivity_Screen.this);
        tv_levafreno = dialogView.findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(BusinessActivity_Screen.this);
        tv_parafango = dialogView.findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(BusinessActivity_Screen.this);
        tv_freece = dialogView.findViewById(R.id.freece);
        tv_freece.setOnClickListener(BusinessActivity_Screen.this);
        tv_luci = dialogView.findViewById(R.id.luci);
        tv_luci.setOnClickListener(BusinessActivity_Screen.this);
        tv_gomme = dialogView.findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(BusinessActivity_Screen.this);
        Image1 = dialogView.findViewById(R.id.image1);
        Image1.setOnClickListener(BusinessActivity_Screen.this);
        Image2 = dialogView.findViewById(R.id.image2);
        Image2.setOnClickListener(BusinessActivity_Screen.this);
        Image3 = dialogView.findViewById(R.id.image3);
        Image3.setOnClickListener(BusinessActivity_Screen.this);
        commentbox = dialogView.findViewById(R.id.commentbox);
        damageplate.setText(vehicleplate);


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

			   /* if(isUserStartSession){
					alertDialog.dismiss();

				}else{
					alertDialog.dismiss();


				}*/
                if (arrlist.size() > 0) {
                    alertDialog.dismiss();
                    PostDamageData();
                } else {
                    Toast.makeText(BusinessActivity_Screen.this, "Si prega di selezionare qualsiasi opzione", Toast.LENGTH_SHORT).show();

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
        imagePathFour = "";
        imagePathFive = "";
        imagePathSix = "";
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
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.business_segnalazione_finish, viewGroup, false);
        ImageView _btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.buttonOk);
        TextView Tv_plate = (TextView) dialogView.findViewById(R.id.damageplate_finish);
        tv_bauletto = dialogView.findViewById(R.id.bauletto);
        tv_bauletto.setOnClickListener(BusinessActivity_Screen.this);
        tv_selia = dialogView.findViewById(R.id.selia);
        tv_selia.setOnClickListener(BusinessActivity_Screen.this);
        tv_cavaletto = dialogView.findViewById(R.id.cavaletto);
        tv_cavaletto.setOnClickListener(BusinessActivity_Screen.this);
        tv_carena = dialogView.findViewById(R.id.carena);
        tv_carena.setOnClickListener(BusinessActivity_Screen.this);
        tv_caschi = dialogView.findViewById(R.id.caschi);
        tv_caschi.setOnClickListener(BusinessActivity_Screen.this);
        tv_altro = dialogView.findViewById(R.id.altro);
        tv_altro.setOnClickListener(BusinessActivity_Screen.this);
        tv_speech = dialogView.findViewById(R.id.specchietti);
        tv_speech.setOnClickListener(BusinessActivity_Screen.this);
        tv_levafreno = dialogView.findViewById(R.id.levafreno);
        tv_levafreno.setOnClickListener(BusinessActivity_Screen.this);
        tv_parafango = dialogView.findViewById(R.id.parafrango);
        tv_parafango.setOnClickListener(BusinessActivity_Screen.this);
        tv_freece = dialogView.findViewById(R.id.freece);
        tv_freece.setOnClickListener(BusinessActivity_Screen.this);
        tv_luci = dialogView.findViewById(R.id.luci);
        tv_luci.setOnClickListener(BusinessActivity_Screen.this);
        tv_gomme = dialogView.findViewById(R.id.gomme);
        tv_gomme.setOnClickListener(BusinessActivity_Screen.this);
        Image4 = dialogView.findViewById(R.id.image4);
        Image4.setOnClickListener(BusinessActivity_Screen.this);
        Image5 = dialogView.findViewById(R.id.image5);
        Image5.setOnClickListener(BusinessActivity_Screen.this);
        Image6 = dialogView.findViewById(R.id.image6);
        Image6.setOnClickListener(BusinessActivity_Screen.this);
        commentbox = dialogView.findViewById(R.id.commentbox);

        Tv_plate.setText(vehicleplate);


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
                ReFreash_onCreatemethod();


            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrlist.size() > 0) {
                    alertDialog.dismiss();
                    PostDamageDataAfetrSessionComplete();
                } else {
                    Toast.makeText(BusinessActivity_Screen.this, "Si prega di selezionare qualsiasi opzione", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void PostDamageData() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
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

                                ReFreash_onCreatemethod();

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
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.buisness_err_alert, viewGroup, false);

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
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.buiseness_grazie, viewGroup, false);

        //ImageView _ic_correct = (ImageView) dialogView.findViewById(R.id.ic_correct);
        Button _btn_confirm = (Button) dialogView.findViewById(R.id.done_btn);


        TextView _starttime_label = (TextView) dialogView.findViewById(R.id.starttime_label);
        TextView _endtime_label = (TextView) dialogView.findViewById(R.id.endtime_label);
        TextView _minlabel = (TextView) dialogView.findViewById(R.id.minlabel);
        TextView _km_label = (TextView) dialogView.findViewById(R.id.km_label);

        //TextView _kg_label = (TextView)dialogView.findViewById(R.id.kg_label);
        TextView _price_label = (TextView) dialogView.findViewById(R.id.price_label);


        // _kg_label.setText("");
        _km_label.setText("" + km);
        _starttime_label.setText("" + _startTime);
        _endtime_label.setText("" + _endTime);
        _minlabel.setText("" + _durationMinutes);
        _price_label.setText("" + price);


        // _kg_label.setText("");
        _km_label.setText("" + km);
        _starttime_label.setText("" + _startTime);
        _endtime_label.setText("" + _endTime);
        _minlabel.setText("" + _durationMinutes);
        _price_label.setText("€ " + price);


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

//                closeRideAlert_popUp();
                showDamagePopUpAfetrSessionComplete();

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


    private void ToggleButton1Colorchange() {

        toogle_btn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            toogle_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_pink));

                            center_imageview.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_dentsu));
                            //  center_imageview.getLayoutParams().height = 100;
                            // center_imageview.getLayoutParams().height = 80;

						   /* vehicle_data_array = new ArrayList<VehicleResponse.Data>();
							vehicle_data_array = response1.getData();*/

                            isPublic = false;

//                            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//                            setBottomSheet();
                            updateViews();
                        } else {
							/*vehicle_data_array = new ArrayList<VehicleResponse.Data>();
							vehicle_data_array = response1.getData();



							ShowMarkerVehiclesOnMap(vehicle_data_array);

							System.out.println("togglechnage2nd:::"+vehicle_data_array.size());
*/

//                            ShowMarkerVehiclesOnMapgreen(vehicle_data_arrayone);
//                            System.out.println("togglechnage1st:::" + vehicle_data_arrayone.size());
//                            openBottomScreen();
                            isPublic = true;
                            toogle_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_default));
                            center_imageview.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_logo_one));
//                            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetGreen);
//                            setBottomSheet();
                            updateViews();

                            // center_imageview.getLayoutParams().height = 120;
                            // center_imageview.getLayoutParams().height = 120;
                        }

                        ShowMarkerVehiclesOnMap();
                        setUpCurrentLocation(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                        System.out.println("togglechnage1st:::" + vehicle_data_arraytwo.size());
                        openBottomScreen();
                    }
                });
    }

    private void setUpView() {
        toogle_btn = (ToggleButton) findViewById(R.id.toogle);
        center_imageview = (ImageView) findViewById(R.id.center_img);


        current_loc_button = (ImageView) findViewById(R.id.button3);
        current_loc_button.setOnClickListener(this);

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

        imageOne = (ImageView) colayout.findViewById(R.id.image_one);

        _stopRide = (ImageView) colayout.findViewById(R.id.stop_ride);
        _stopRide.setOnClickListener(this);

        call_play_imageview = (ImageView) findViewById(R.id.call_img_play);
        whatsUp_play_imageview = (ImageView) findViewById(R.id.whatsup_img_play);

        call_play_imageview.setOnClickListener(this);
        whatsUp_play_imageview.setOnClickListener(this);
        progressbar = findViewById(R.id.progressbar);
        timer_bottom = (TextView) findViewById(R.id.timer);
        green_btn = (Button) findViewById(R.id.sblocca);

        slideLayout = colayout.findViewById(R.id.slide_layout);
        timer_buttons_Layout = colayout.findViewById(R.id.timeandbuttons);
        playHeart_break = (ImageView) findViewById(R.id.play_heart_break);
        playHeart_break.setOnClickListener(this);
        bikeHeart_break = (ImageView) findViewById(R.id.bike_heart_break);
        bikeHeart_break.setOnClickListener(this);
        tv_reserved = (TextView) findViewById(R.id.reserved);
        autonomiakm = (TextView) colayout.findViewById(R.id.autonomiakm);

//-------Play buttons Layout-------

	   /* sb = colayout.findViewById(R.id.normal_green_slide);
		sb.setOnSeekBarChangeListener(this);*/
    }


    private void UserInformation() {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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

                if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISSESSION, false)) {
                    Alert_information("Hai già prenotato un veicolo in affari", context, alertDialog, BusinessActivity_Screen.this);
                    return;
                }

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
                            JSONObject bookingObj = data.getJSONObject("booking");
                            Log.e(TAG, "booking-=-=-=-" + bookingObj.length());
                            JSONObject userObj = data.getJSONObject("user");
                            validDocument = userObj.getInt("validDocument");

                            userBookingCount = bookingObj.length();
                            if (userBookingCount > 0) {
                                JSONObject _sharingObj = bookingObj.getJSONObject("sharing");

                                int id = _sharingObj.getInt("id");
                                Log.e(TAG, "id==" + id);


                                JSONObject _vehicleObj = _sharingObj.getJSONObject("vehicle");
                                _vehicleID = _vehicleObj.getInt("id");
                                _vehicleRideStatus = _vehicleObj.getString("status");
                                _vehicleLastLatitude = _vehicleObj.getDouble("latitude");
                                _vehicleLastLongitude = _vehicleObj.getDouble("longitude");

                                Log.e(TAG, "_vehicleStatus==" + _vehicleRideStatus);
                                Log.e(TAG, "_vehicleLatitude==" + _vehicleLastLatitude);
                                Log.e(TAG, "_vehicleLongitude==" + _vehicleLastLongitude);

                                String _latString = String.valueOf(_vehicleLastLatitude);
                                String _lngString = String.valueOf(_vehicleLastLongitude);

                                PreferenceUtil.getInstance().saveString(context, Constants.STATUS_OF_RIDE, _vehicleRideStatus);
                                PreferenceUtil.getInstance().saveString(context, Constants.RIDE_LATITUDE, _latString);
                                PreferenceUtil.getInstance().saveString(context, Constants.RIDE_LANGITUDE, _lngString);

                                if (_vehicleRideStatus.equalsIgnoreCase("running")) {

                                    isPlayPauseClicked = false;

                                    Log.e(TAG, "isPlayPauseClicked==running==" + isPlayPauseClicked);

                                    _playPause.setImageResource(R.drawable.ic_pause);
                                    _palypause_label.setText("PAUSA");


//                                    bottomSheet.setVisibility(View.VISIBLE);
//                                    _play_layout.setVisibility(View.VISIBLE);
//                                    social_play_layout.setVisibility(View.VISIBLE);
                                    first_bikeLayout.setVisibility(View.GONE);
                                    toogle_btn.setVisibility(View.GONE);
//                                    fab_menu.setVisibility(View.GONE);
//                                    fab_menu.removeAllViews();


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
                                    VehicleResponse.Data data1 = new VehicleResponse.Data();
                                    data1 = new Gson().fromJson(_vehicleObj.toString(), VehicleResponse.Data.class);
                                    JSONObject serviceObj = data.getJSONObject("service");
                                    VehicleResponse.Service service = new VehicleResponse.Service();
                                    service.setPrice(serviceObj.getDouble("price"));
                                    data1.setService(service);
                                    vehicleplate = data1.getLicensePlate();
                                    DrawPolyLinePastLocation(array_loc);

                                    JSONObject user = data.getJSONObject("user");
                                    int siteId = user.getInt("SiteId");
                                    getStatusApi(siteId, _vehicleRideStatus, data1);




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

//                                    bottomSheet.setVisibility(View.VISIBLE);
//                                    _play_layout.setVisibility(View.VISIBLE);
//                                    social_play_layout.setVisibility(View.VISIBLE);
                                    first_bikeLayout.setVisibility(View.GONE);
                                    toogle_btn.setVisibility(View.GONE);
//                                    fab_menu.setVisibility(View.GONE);
//                                    fab_menu.removeAllViews();

//                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                    ArrayList<LatLng> array_loc = new ArrayList<LatLng>();
                                    LatLng postUserloc = new LatLng(_vehicleLastLatitude, _vehicleLastLongitude);
                                    LatLng postBikeloc = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
                                    array_loc.add(postUserloc);
                                    array_loc.add(postBikeloc);

                                    VehicleResponse.Data data1 = new VehicleResponse.Data();
                                    data1 = new Gson().fromJson(_vehicleObj.toString(), VehicleResponse.Data.class);
                                    JSONObject serviceObj = data.getJSONObject("service");
                                    VehicleResponse.Service service = new VehicleResponse.Service();
                                    service.setPrice(serviceObj.getDouble("price"));
                                    data1.setService(service);
                                    vehicleplate = data1.getLicensePlate();
                                    DrawPolyLinePastLocation(array_loc);

                                    JSONObject user = data.getJSONObject("user");
                                    int siteId = user.getInt("SiteId");
                                    getStatusApi(siteId, _vehicleRideStatus, data1);


                                } else if (_vehicleRideStatus.equalsIgnoreCase("booked")) {
//                                    _play_layout.setVisibility(View.VISIBLE);
//                                    social_play_layout.setVisibility(View.VISIBLE);
                                    first_bikeLayout.setVisibility(View.VISIBLE);
                                    progressbar.setVisibility(View.GONE);
                                    timer_bottom.setVisibility(View.GONE);
                                    isGreenButtonVisible = true;
                                    green_btn.setVisibility(View.VISIBLE);
//                                    bottomSheet.setVisibility(View.VISIBLE);
                                    slideLayout.setVisibility(View.GONE);
                                    toogle_btn.setVisibility(View.GONE);
                                    isSlideViewClicked = true;
                                    tv_reserved.setVisibility(View.GONE);
//                                    isPublic = true;
//                                    updateViews();
                                    isPublic = PreferenceUtil.getInstance().getBoolean(context, Constants.ISPUBLIC, true);
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
                                    vehicleplate = data1.getLicensePlate();
                                    requestDirectionFormarkerClicks(postUserloc);
                                    JSONObject user = data.getJSONObject("user");
                                    int siteId = user.getInt("SiteId");
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    getStatusApi(siteId, _vehicleRideStatus, data1);
                                    updateBottomScreenData(data1, marker);
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
                                    CallUserStatusApi();
                                    Log.e(TAG, "--_siteID--xx-" + _siteID);

                                    PreferenceUtil.getInstance().saveInt(context, Constants.SITE_ID, _siteID);

                                    PreferenceUtil.getInstance().saveBoolean(context, Constants.PAYMENT_CREDITED, userinfo.getData().getUser().getPaymentCreated());

                                    //CallVehicleService(_siteID);
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
        ImageView ivHook = findViewById(R.id.ivHook);
        Drawable drawable = res1.getDrawable(R.drawable.circular_horizontal_pink);
        ivHook.setImageResource(R.drawable.ic_pink_hook);
        if (isPublic) {
            drawable = res1.getDrawable(R.drawable.circular_horizontal);
            ivHook.setImageResource(R.drawable.greenhook);
        }
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


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        showProgressbar();

        Log.e(TAG, "For SiteID---" + CURRENT_LATITUDE + "-" + CURRENT_LANGITUDE);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        GetSiteID_Interface geo_details = retrofit.create(GetSiteID_Interface.class);


        Call<SiteIDResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization, CURRENT_LATITUDE, CURRENT_LANGITUDE);
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


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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
                        //  CallVehicleService(_siteID);
                        // i Called Geofence service after Marker displayed
                        CallUserStatusApi();
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

  /*  private void CallVehicleService(final int siteID) {
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
	}*/

    private void ShowMarkerVehiclesOnMap() {
        arrayList = new ArrayList<MyLatLng>();

        ArrayList<VehicleResponse.Data> vehicle_data = getSelectedVehicleTypeList();
        for (int i = 0; i < vehicle_data.size(); i++) {


            Log.e(TAG, "isStatus---" + vehicle_data.get(i).getStatus());

            String _vehicleStatus = vehicle_data.get(i).getStatus();

            if (_vehicleStatus.equalsIgnoreCase("free")) {
                double _lat = vehicle_data.get(i).getLatitude();
                double _lng = vehicle_data.get(i).getLongitude();
                _type = vehicle_data.get(i).getType();
                String _licencePlate = vehicle_data.get(i).getLicensePlate();

                Log.e(TAG, "LATLNG---" + _lat + " " + _lng);

                int vehicleID = vehicle_data.get(i).getId();

                MyLatLng bean = new MyLatLng();
                bean.setTitle(_type);
                bean.setLat(_lat);
                bean.setLng(_lng);
                bean.setLicense_palte(_licencePlate);
                arrayList.add(bean);
            }


        }
        if (arrayList != null) {

            System.out.println("pinsnumber::::" + arrayList.size());

            LoadingGoogleMap(arrayList);
        }

        int siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
//        if (datalist!=null && datalist.size()>0){
//            showGeoFence();
//        }else{
        Geofenceing_Service(siteID);
//        }

    }

    private void ShowMarkerVehiclesOnMapgreen(List<VehicleResponse.Data> vehicle_data_array) {
//        arrayList = new ArrayList<MyLatLng>();


        for (int i = 0; i < vehicle_data_array.size(); i++) {


            Log.e(TAG, "isStatus---" + vehicle_data_array.get(i).getStatus());

            String _vehicleStatus = vehicle_data_array.get(i).getStatus();

            if (_vehicleStatus.equalsIgnoreCase("free")) {
                double _lat = vehicle_data_array.get(i).getLatitude();
                double _lng = vehicle_data_array.get(i).getLongitude();
                _type = vehicle_data_array.get(i).getType();
                String _licencePlate = vehicle_data_array.get(i).getLicensePlate();

                Log.e(TAG, "LATLNG---" + _lat + " " + _lng);

                int vehicleID = vehicle_data_array.get(i).getId();

                MyLatLng bean = new MyLatLng();
                bean.setTitle(_type);
                bean.setLat(_lat);
                bean.setLng(_lng);
                bean.setLicense_palte(_licencePlate);
                arrayList.add(bean);
            }


        }
        if (arrayList != null) {

            System.out.println("pinsnumber::::" + arrayList.size());

            LoadingGoogleMapgreen(arrayList);
        }

        int siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
        Geofenceing_Service(siteID);
    }

    private void Geofenceing_Service(int siteID) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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


                    if (response.code() == 200) {
                        String responseRecieved = new Gson().toJson(response.body());
                        Log.e(TAG, "Geofence--" + response.body().getData().get(0).getZonas().get(0).getPoints().get(0));
                        datalist = response.body().getData();
                        showGeoFence();
                        Log.e(TAG, "RES===" + responseRecieved);

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

    private void showGeoFence() {
        if (datalist == null) {
            return;
        }
        int sizeCount = 0;
        firstLatLng = new ArrayList<LatLng>();
        List<Double> doubleList = null;
        List<GeoFenceResponse.Zona> zonalsit = null;
        Log.e(TAG, "sizeCount===" + sizeCount);
        for (int d = 0; d < datalist.size(); d++) {

            if (!datalist.get(d).getActive()) {
                continue;
            }

            zonalsit = datalist.get(d).getZonas();


            for (int p = 0; p < zonalsit.size(); p++) {

                boolean _isExclusive = zonalsit.get(p).getExclude();
//                if (_isExclusive == false) {


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


//                    }


//                    if (sizeCount == 2) {
//                        double_list.clear();
//                        firstLatLng.clear();
//                        for (int s = 0; s < list_list_points.size(); s++) {
//                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                            double_list = list_list_points.get(s);
//
//                            double _lat = double_list.get(0).doubleValue();
//                            double _long = double_list.get(1).doubleValue();
//
//                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                            firstLatLng.add(firstPolyLatlng);
//
//                        }
//
//                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                .addAll(firstLatLng)
//                                .strokeColor(Color.BLUE)
//                                .strokeWidth(2)
//                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                    }
//
//                    if (sizeCount == 3) {
//                        double_list.clear();
//                        firstLatLng.clear();
//                        for (int s = 0; s < list_list_points.size(); s++) {
//                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                            double_list = list_list_points.get(s);
//
//                            double _lat = double_list.get(0).doubleValue();
//                            double _long = double_list.get(1).doubleValue();
//
//                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                            firstLatLng.add(firstPolyLatlng);
//
//                        }
//
//                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                .addAll(firstLatLng)
//                                .strokeColor(Color.BLUE)
//                                .strokeWidth(2)
//                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                    }
//                    if (sizeCount == 4) {
//                        double_list.clear();
//                        firstLatLng.clear();
//                        for (int s = 0; s < list_list_points.size(); s++) {
//                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                            double_list = list_list_points.get(s);
//
//                            double _lat = double_list.get(0).doubleValue();
//                            double _long = double_list.get(1).doubleValue();
//
//                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                            firstLatLng.add(firstPolyLatlng);
//
//                        }
//
//                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                .addAll(firstLatLng)
//                                .strokeColor(Color.BLUE)
//                                .strokeWidth(2)
//                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                    }
//
//                    if (sizeCount == 5) {
//                        double_list.clear();
//                        firstLatLng.clear();
//                        for (int s = 0; s < list_list_points.size(); s++) {
//                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                            double_list = list_list_points.get(s);
//
//                            double _lat = double_list.get(0).doubleValue();
//                            double _long = double_list.get(1).doubleValue();
//
//                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                            firstLatLng.add(firstPolyLatlng);
//
//                        }
//
//                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                .addAll(firstLatLng)
//                                .strokeColor(Color.BLUE)
//                                .strokeWidth(2)
//                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                    }
//
//                    if (sizeCount == 6) {
//                        double_list.clear();
//                        firstLatLng.clear();
//                        for (int s = 0; s < list_list_points.size(); s++) {
//                            Log.e(TAG, "==list_list_points==22==" + list_list_points.get(s));
//
//                            double_list = list_list_points.get(s);
//
//                            double _lat = double_list.get(0).doubleValue();
//                            double _long = double_list.get(1).doubleValue();
//
//                            Log.e(TAG, "LAT-LNG=-=-22=--=-" + _lat + "==" + _long);
//
//                            LatLng firstPolyLatlng = new LatLng(_lat, _long);
//                            firstLatLng.add(firstPolyLatlng);
//
//                        }
//
//                        Polygon polygon = mMap.addPolygon(new PolygonOptions()
//                                .addAll(firstLatLng)
//                                .strokeColor(Color.BLUE)
//                                .strokeWidth(2)
//                                .fillColor(Color.argb(20, 0, 255, 0)));
//
//
//                    }

//                }


            }


        }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
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


    public void setUpCurrentLocation(double latitude, double longitude) {
        if (latitude == 0 || longitude == 0)
            return;
        LatLng latLng = new LatLng(latitude, longitude);
        Log.e(TAG, "LAT-LANG--" + latitude + "-" + longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17.0f);
        mMap.animateCamera(cameraUpdate);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot_human);
        if (isPublic) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot);
        }

        if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISBUSINESS, false)) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_location);
            if (isPublic || PreferenceUtil.getInstance().getBoolean(context, Constants.ISPUBLIC, true)) {
                icon = BitmapDescriptorFactory.fromResource(R.drawable.black_dot);
            }
        }
        LatLng position = new LatLng(latitude, longitude);
        if (currentLocatinMarker == null) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(icon));
            marker.setTag("CurrentLocation");
            currentLocatinMarker = marker;
        } else {
            currentLocatinMarker.setPosition(position);
        }

        currentLocationID = currentLocatinMarker.getId();


    }

    //Showing free status Vehicles
    void LoadingGoogleMap(ArrayList<MyLatLng> arrayList) {
        if (mMap != null) {
            final ArrayList<VehicleResponse.Data> vehicle_data_array = getSelectedVehicleTypeList();
            mMap.clear();
            mSelectedMarker = null;

            bottomSheet.setVisibility(View.GONE);
            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer.onFinish();

            }

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
                            if (isPublic)
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green);

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


                        if (marker.getTag() == null) { //==========IF Current Location Marker Clicks============
                            bottomSheet.setVisibility(View.VISIBLE);


                            if (vehicle_data_array != null) {

                                if (null != mSelectedMarker) {
                                    if (_isDispalyedMarker == false) {
                                        mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_dot));
                                        if (isPublic)
                                            mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green));

                                    }
                                }
                                mSelectedMarker = marker;
                                if (_isDispalyedMarker == false) {
                                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_pink));
                                    if (isPublic)
                                        mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_small));

                                    LatLng source = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                                    requestDirectionFormarkerClicks(source);


                                }

                                showinMarkers(marker);
                            } else {

                                //==========IF Current Location Marker Clicks============
                                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dot));
                                bottomSheet.setVisibility(View.GONE);


                                //==========IF Current Location Marker Clicks============
                            }


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


    void LoadingGoogleMapgreen(ArrayList<MyLatLng> arrayList) {
        if (mMap != null) {
            mMap.clear();
            mSelectedMarker = null;

            bottomSheet.setVisibility(View.GONE);
            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer.onFinish();

            }

            ArrayList<MyLatLng> myLatLng = new ArrayList<MyLatLng>();

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


                        if (!marker.getId().equalsIgnoreCase(currentLocationID)) { //==========IF Current Location Marker Clicks============

                            bottomSheet.setVisibility(View.VISIBLE);


                            if (vehicle_data_arraytwo != null) {

                                if (null != mSelectedMarker) {
                                    if (_isDispalyedMarker == false) {
                                        mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_dot));

                                    }
                                }
                                mSelectedMarker = marker;
                                if (_isDispalyedMarker == false) {
                                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_pink));

                                    LatLng source = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                                    requestDirectionFormarkerClicks(source);


                                }

                                showinMarkers(marker);
                            } else {

                                //==========IF Current Location Marker Clicks============
                                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dot));
                                bottomSheet.setVisibility(View.GONE);

                                //==========IF Current Location Marker Clicks============
                            }


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

    private void requestDirectionFormarkerClicks(LatLng origin) {
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

                                    System.out.println("walktime:::" + walk_time);

                                    Log.i("Diatance :", distOb.getString("text"));
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

//        LatGeo = arrayList.get(0).getLat();
//        // System.out.println("kkkkk"+Lat);
//
//        LngGeo = arrayList.get(0).getLng();
//        // System.out.println("kkkkk"+Lng);
//        LatLng latLng = new LatLng(LatGeo, LngGeo);
//        markerForGeofence(latLng);
//
//        startGeofence(LatGeo, LngGeo);

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
        ArrayList<VehicleResponse.Data> vehicle_data_array = getSelectedVehicleTypeList();

        for (int i = 0; i < vehicle_data_array.size(); i++) {
            if (marker.getPosition().latitude == vehicle_data_array.get(i).getLatitude() && marker.getPosition().longitude == vehicle_data_array.get(i).getLongitude()) {
                updateBottomScreenData(vehicle_data_array.get(i), marker);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                openBottomScreen();
            }


        }

    }

    private void updateBottomScreenData(VehicleResponse.Data data, final Marker marker) {
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
        TextView _bike_number_bottom = (TextView) findViewById(R.id.bike_number);
        TextView _km = (TextView) findViewById(R.id.km);
        TextView _price = (TextView) findViewById(R.id.price);
        TextView _priceDesc = (TextView) findViewById(R.id.price_desc);
        TextView _battery = (TextView) findViewById(R.id.battery);
        TextView _model_type = (TextView) findViewById(R.id.model_name);


        _time = (TextView) findViewById(R.id.google_time);
        _distance = (TextView) findViewById(R.id.distance);

        _pinkhook = (ImageView) findViewById(R.id.pinkhook);


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


            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                if (yourCountDownTimer != null) {
                    yourCountDownTimer.cancel();
                    yourCountDownTimer.onFinish();

                }

//                        callStartStopServiceForStopSession(context, _vehicleID);
                callDeleteApi();

            }
        });

        //By default set BottomSheet Behavior as Collapsed and Height 0
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // mBottomSheetBehavior.setPeekHeight(0);
        //If you want to handle callback of Sheet Behavior you can use below code

        _bike_number_bottom.setText("" + data.getLicensePlate());
        vehicleplate = data.getLicensePlate();
        _km.setText("" + data.getKm() + "km");
//                _price.setText("€" + vehicle_data_array.get(i).getService().getPrice());


        double battery_percentage = data.getTotalPercentage();

        // double battery_percentage = vehicle_data_array.get(i).getBattery().getPercentage();
        int battery = (int) battery_percentage;


        _battery.setText("" + battery + "%");
	   /* Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.pinkcircular);
		final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
		mProgress.setProgress(0);   // Main Progress
		mProgress.setSecondaryProgress(100); // Secondary Progress
		mProgress.setMax(100); // Maximum Progress
		mProgress.setProgressDrawable(drawable);
		mProgress.setProgress(55);*/
        PriceEuro = data.getService().getPrice() != null ? "€" + data.getService().getPrice() : "";
        if (isPublic) {
            _price.setText("" + PriceEuro);
            _priceDesc.setText("al minuto");

            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.circular);
            _pinkhook.setImageResource(R.drawable.greenhook);
            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
            mProgress.setProgress(0);   // Main Progress
            mProgress.setSecondaryProgress(100); // Secondary Progress
            mProgress.setMax(100); // Maximum Progress
            mProgress.setProgressDrawable(drawable);
            mProgress.setProgress(battery);
        } else {
            _price.setText("2 caschi");
            _priceDesc.setText("nel bauletto");
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.pinkcircular);
            _pinkhook.setImageResource(R.drawable.ic_pink_hook
            );
            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
            mProgress.setProgress(0);   // Main Progress
            mProgress.setSecondaryProgress(100); // Secondary Progress
            mProgress.setMax(100); // Maximum Progress
            mProgress.setProgressDrawable(drawable);
            mProgress.setProgress(battery);
        }


        //  _battery.setText("" + vehicle_data_array.get(i).getBattery().getAhTot() + "%");

        String address = getCompleteAddressString(data.getLatitude(), data.getLongitude());

        Log.e(TAG, "Address===" + data.getAddress());
//        String address = data.getAddress();

        if (address != null) {
            if (!address.equalsIgnoreCase("")) {
                address = address.replaceAll(",", "\n");
                System.out.println(address);

                _model_type.setText("" + address);
            }
        } else {
            _model_type.setText("");
        }


        _vehicleStatus = data.getStatus();
        _vehicleID = data.getId();
        _vehicleLastLatitude = data.getLatitude();
        _vehicleLastLongitude = data.getLongitude();
        destinationMarker = marker;

        Log.e(TAG, "ID----" + _vehicleID);


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
                    sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
                    if (isPublic)
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
                                        sb.setBackgroundResource(R.drawable.ic_pink_slider_two);
                                        if (isPublic)
                                            sb.setBackgroundResource(R.drawable.ic_greenslideafter);
                                        seekBar.setProgress(2);


													 /*  isSlideViewClicked = true;
													   slideLayout.setVisibility(View.GONE);
													   timer_buttons_Layout.setVisibility(View.VISIBLE);

													   Log.e(TAG, "show loc--LATLNG---" + marker.getPosition().latitude + "--" + marker.getPosition().longitude);
													   social_bike_layout_one.setVisibility(View.VISIBLE);


													   Log.e(TAG, "show loc--LATLNG---" + marker.getPosition().latitude + "--" + marker.getPosition().longitude);

													   MyLatLng select_lat = new MyLatLng();
													   select_lat.setLat(marker.getPosition().latitude);
													   select_lat.setLng(marker.getPosition().longitude);

													   selected_loc_arrayList.add(select_lat);*/

                                        //---After draging the bottom  slide view button
                                        //DisplaySelectedMarker(selected_loc_arrayList, marker);

                                        //After Vehicle Book, we need to StartFreeTime

//                                        bookVehicle_ServiceCall(_vehicleID, timer_bottom, green_btn, marker);
                                        // BookingTimeCheckUserInformation(_vehicleID,timer_bottom,green_btn,marker);
                                        validateDocument(marker, seekBar);


                                    } else {
                                        // Toast.makeText(context, "Sorry! your already booked vehicle.",Toast.LENGTH_SHORT).show();

                                        sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
                                        if (isPublic)
                                            sb.setBackgroundResource(R.drawable.slidefull);
                                        seekBar.setProgress(2);
                                        sb.getThumb().mutate().setAlpha(255);

                                        Alert_Bookinginformation("Sorry! your already booked vehicle.", context, alertDialog, BusinessActivity_Screen.this);


                                        //paymentGateWay_Alert("Sorry! your already booked vehicle.");
                                    }
                                } else {
                                    Toast.makeText(context, "Please upload valid driving license.", Toast.LENGTH_SHORT).show();
                                    sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
                                    if (isPublic)
                                        sb.setBackgroundResource(R.drawable.slidefull);
                                    seekBar.setProgress(2);
                                    sb.getThumb().mutate().setAlpha(255);

                                    paymentGateWay_Alert("Please upload valid driving license.");
                                }

                            } else {
                                //Toast.makeText(context, "Please upload valid documents.",Toast.LENGTH_SHORT).show();
                                sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
                                if (isPublic)
                                    sb.setBackgroundResource(R.drawable.slidefull);
                                seekBar.setProgress(2);
                                sb.getThumb().mutate().setAlpha(255);

                                paymentGateWay_Alert("Please upload valid documents.");
                            }

                        } else {
                            // Toast.makeText(context, "Please upload Profile Pic.",Toast.LENGTH_SHORT).show();
                            sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
                            if (isPublic)
                                sb.setBackgroundResource(R.drawable.slidefull);
                            seekBar.setProgress(2);
                            sb.getThumb().mutate().setAlpha(255);

                            paymentGateWay_Alert("Please upload Profile Pic.");
                        }

                    } else {
                        // Toast.makeText(context, "Non autorizzato, per favore completa il tuo profilo.", Toast.LENGTH_SHORT).show();

                        sb.setBackgroundResource(R.drawable.ic_pink_slide_one);
                        if (isPublic)
                            sb.setBackgroundResource(R.drawable.slidefull);
                        seekBar.setProgress(2);
                        sb.getThumb().mutate().setAlpha(255);

                        paymentGateWay_Alert("Non autorizzato, per favore completa il tuo profilo.");


                    }

                }


            }
        });

//        openBottomScreen();
    }


    private void validateDocument(Marker marker, SeekBar seekBar) {
        switch (validDocument) {
            case 1:
                bookVehicle_ServiceCall(_vehicleID, timer_bottom, green_btn, marker);
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
        sb.setBackgroundResource(R.drawable.slidefull);
        seekBar.setProgress(2);
        sb.getThumb().mutate().setAlpha(255);
        AppUtils.error_Alert(errorMsg, context, null, BusinessActivity_Screen.this);
    }


    private void callDeleteApi() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }

        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISBUSINESS, false);
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        DeleteRideSession geo_details = retrofit.create(DeleteRideSession.class);


        Call<JSONObject> resultRes = geo_details.GetDeleteResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                dismissProgressbar();
                ReFreash_onCreatemethod();
                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());
                    if (response.code() != 200) {

					   /* StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
						// â€¦ and use it to show error information

						// â€¦ or just log the issue like weâ€™re doing :)
						Log.d("error message", error.getError().getDescription());

						AppUtils.error_Alert(error.getError().getDescription(),context, alertDialog, BusinessActivity_Screen.this);*/


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
		/*fab_menu.setVisibility(View.GONE);
		fab_menu.removeAllViews();*/

        StartRideSession();
    }

    private void showErrorPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.pink_buisness_alert_attenzione, viewGroup, false);
        if (isPublic)
            dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.buisness_alert_attenzione, viewGroup, false);

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

//                isUserStartSession = true;
//
//                if (yourCountDownTimer != null) {
//                    yourCountDownTimer.cancel();
//                    yourCountDownTimer.onFinish();
//
//                }

//                SessionStart();


                //showDamagePopUp();


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
                                           callStartStopServiceForStopSession(context, _vehicleID);

                                           //Note : Afer start and stop service called then ReFreash_onCreatemethod(); call


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

        Intent i = new Intent(BusinessActivity_Screen.this, BusinessActivity_Screen.class);
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
                        if (isPublic)
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_small);

                        myMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(_lat, _lon))
                                .icon(icon));
                        //Set Zoom Level of Map pin
                        LatLng object = new LatLng(_lat, _lon);


                        listMyLatLng.add(object);

					  /*  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 12.0f));
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listMyLatLng.get(0), 17));*/


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

        LatLng source = new LatLng(polyline_array.get(0).latitude, polyline_array.get(0).longitude);


        requestDirection(source);

    }
    // Fetches data from url passed

    /**
     * A method to download json data from url
     */

    public void requestDirection(LatLng origin) {
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
                            // mMap.addMarker(new MarkerOptions().position(origin));
                            //  mMap.addMarker(new MarkerOptions().position(destination));

                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pink_location);
                            if (isPublic)
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.black_dot);

                            Marker localMarker = mMap.addMarker(new MarkerOptions()
                                    .position(destination)
                                    .icon(icon));
                            localMarker.setTag("Current Location");
                            currentLocationID = localMarker.getId();
                            currentLocatinMarker = localMarker;

                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            int color = R.color.pink;
                            if (isPublic)
                                color = R.color.green;
                            polyline = mMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, color)));
                            // setCameraWithCoordinationBounds(route);
                            points = polyline.getPoints();
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
                                System.out.println("timecheck::::" + time);

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
        //Log.e(TAG, "distanceTime---"+distanceTime);
        long time = distanceTime * 60 * 1000;
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
                _greenButton.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                tv_reserved.setVisibility(View.GONE);
                LocationTarckerServiceStart();
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

        Toast.makeText(context, "LAT-LNG" + location.getLatitude() + "-" + location.getLongitude(), Toast.LENGTH_SHORT).show();

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
        int color = R.color.geofence_pink;
        if (isPublic)
            color = R.color.geofence_green;
        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(getResources().getColor(color))
                .fillColor(getResources().getColor(color))
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
                if (!buttonPressedcaraletto) {
                    tv_cavaletto.setBackgroundResource(R.drawable.round_corner);
                    tv_cavaletto.setTextColor(Color.WHITE);
                    arrlist.add("3");
                } else {
                    tv_cavaletto.setBackgroundResource(R.drawable.round_green);
                    tv_cavaletto.setTextColor(Color.BLACK);
                    if (arrlist.size() > 0) {
                        arrlist.remove("3");

                    }

                }
                buttonPressedcaraletto = !buttonPressedcaraletto;
                break;

            case R.id.carena:
                if (!buttonPressedcarena) {
                    tv_carena.setBackgroundResource(R.drawable.round_corner);
                    tv_carena.setTextColor(Color.WHITE);
                    arrlist.add("4");
                } else {
                    tv_carena.setBackgroundResource(R.drawable.round_green);
                    tv_carena.setTextColor(Color.BLACK);
                    if (arrlist.size() > 0) {
                        arrlist.remove("4");

                    }

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
                    if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
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
                    if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        openCamera(2);
                    }

                } else {
                    showDamageImages(2);
                }

                break;
            case R.id.image3:

                if (Images2 == false) {
                    if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        }
                    } else {
                        openCamera(3);
                    }

                } else {
                    showDamageImages(3);
                }


                break;

            case R.id.image4:
                if (Images4 == false) {
                    if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
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
                    if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
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
                    if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                            showSettingsAlert();

                        } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Should we show an explanation?
                            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                                    Manifest.permission.CAMERA)) {
                                showAlert();
                            } else {
                                // No explanation needed, we can request the permission.
                                ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
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
                    endRideService();
                } else {
                    stopRide_ServiceCall();
                }

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
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView_image = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.viewpager, viewGroup, false);


        vp_slider = (ViewPager) dialogView_image.findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) dialogView_image.findViewById(R.id.ll_dots);
        ivLeft = dialogView_image.findViewById(R.id.ivLeft);
        ivRight = dialogView_image.findViewById(R.id.ivRight);
        icCloseVp = dialogView_image.findViewById(R.id.ivRight);
        sliderPagerAdapter = new SliderPagerAdapter(BusinessActivity_Screen.this);
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

        icCloseVp.setOnClickListener(new View.OnClickListener() {
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

    private void addBottomDots(int position) {

        dots = new TextView[damageList.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(BusinessActivity_Screen.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[position].setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void openCamera(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
        startActivityForResult(takePictureIntent, requestCode);
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
                        ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
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

            damageList.put(1, mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathOne = finalFile.getPath();

            //  ButtonOk.setVisibility(View.VISIBLE);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Images1 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image2.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            damageList.put(2, mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathTwo = finalFile.getPath();
        }

        if (requestCode == 3 && resultCode == RESULT_OK) {
            Images2 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            Image3.setImageBitmap(mphoto);
            //  ButtonOk.setVisibility(View.VISIBLE);

            damageList.put(3, mphoto);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println("hhhhh" + encoded);

            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imagePathThree = finalFile.getPath();
            System.out.println("ImagePath---" + imagePathThree);
        }
        if (requestCode == 4 && resultCode == RESULT_OK) {
            Images4 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
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


            //=====For Service Call================

            //  ButtonOk.setVisibility(View.VISIBLE);
        }
        if (requestCode == 5 && resultCode == RESULT_OK) {
            Images5 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
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


            //=====For Service Call================
        }

        if (requestCode == 6 && resultCode == RESULT_OK) {
            Images6 = true;
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
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
                    ImageRemover(removeID, pos, activity);
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

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, BusinessActivity_Screen.this);

                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {

                            locationStopService();
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
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISBUSINESS, false);
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StopRideSession geo_details = retrofit.create(StopRideSession.class);


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

                        AppUtils.error_Alert(error.getError().getDescription(), context, alertDialog, BusinessActivity_Screen.this);

//                        ShowInfoAlert();
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
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                            _startTime = dateFormat.format(date);

                            Date date1 = new Date(endTime);
                            DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aa");
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


    private void endRideService() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISBUSINESS, false);
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
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                            _startTime = dateFormat.format(date);

                            Date date1 = new Date(endTime);
                            DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aa");
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

    private void ShowInfoAlert() {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.business_recorda, viewGroup, false);

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
                fourth_PopUp();

            }
        });

        _btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
//                fourth_PopUp();
                String _commentsRecorda = _comments.getText().toString();
                ServiceCallForRecorda(_commentsRecorda);
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

    private void ServiceCallForRecorda(String commentsRecorda) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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

    private void callMethod() {
        if (isRecodraImage == false) {
            if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (getFromPref(BusinessActivity_Screen.this, ALLOW_KEY)) {

                    showSettingsAlert();

                } else if (ContextCompat.checkSelfPermission(BusinessActivity_Screen.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessActivity_Screen.this,
                            Manifest.permission.CAMERA)) {
                        showAlert();
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(BusinessActivity_Screen.this,
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
            View dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.imagepopup_upload, viewGroup, false);

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


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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

                        error_Alert(error.getError().getDescription(), context, alertDialog, BusinessActivity_Screen.this);


                    }


                } else {
                    Log.e(TAG, "--Success---");

                    toogle_btn.setVisibility(View.GONE);
                    if (response.code() == 200) {
                        try {
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

                            //---After draging the bottom  slide view button
                            PreferenceUtil.getInstance().saveBoolean(context, Constants.ISPUBLIC, isPublic);
                            PreferenceUtil.getInstance().saveBoolean(context, Constants.ISBUSINESS, true);
                            DisplaySelectedMarker(selected_loc_arrayList, marker);
                            startFreeTime(timer_bottom, green_btn);
                            Geofenceing_Service(1);

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


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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

    /*private void endRideService() {

		showProgressbar();

		String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
		String bearer_authorization = "Bearer " + authorization;

		Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

		RequestInterface geo_details = retrofit.create(RequestInterface.class);

		Call<JSONObject> resultRes = geo_details.GetVehicleEndResponse(Constants.TOKEN, bearer_authorization,_vehicleID);
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

							Log.e(TAG, "--Update--"+res.toString());

							ReFreash_onCreatemethod();


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
	}*/
    private void callStartStopServiceForStopSession(final Context context, final int _vehicleID) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISBUSINESS, false);
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


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
            return;
        }
        PreferenceUtil.getInstance().saveBoolean(context, Constants.ISBUSINESS, false);
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StopRideSession geo_details = retrofit.create(StopRideSession.class);


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



		/*AlertDialog.Builder builder = new AlertDialog.Builder(BusinessActivity_Screen.this);
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
        dialogView = LayoutInflater.from(BusinessActivity_Screen.this).inflate(R.layout.paymentgateway_alert, viewGroup, false);

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

                Intent i = new Intent(BusinessActivity_Screen.this, ProfileScreen_Activity.class);
                startActivity(i);


            }
        });

    }

    private void BookingTimeCheckUserInformation(final int _vehicleID, final TextView timer_bottom, final Button green_btn, final Marker marker) {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, BusinessActivity_Screen.this);
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


            }
        });
    }

    private ArrayList<VehicleResponse.Data> getSelectedVehicleTypeList() {
        if (isPublic)
            return vehicle_data_arraytwo;
        else
            return vehicle_data_arrayone;
    }

    private Marker DrawPolyLinePastLocation(ArrayList<LatLng> polyline_array) {
        Log.e(TAG, "DrawPolyLinePastLocation--Size---" + polyline_array.size());

        LatLng source = new LatLng(polyline_array.get(0).latitude, polyline_array.get(0).longitude);


        return requestDirectionPast(source, polyline_array, true);

    }

    public Marker requestDirectionPast(final LatLng origin, final ArrayList<LatLng> polyline_array, final boolean isZoomRequired) {

        // final LatLng destination = new LatLng(CURRENT_LATITUDE,  CURRENT_LANGITUDE);
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
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            // mMap.addMarker(new MarkerOptions().position(origin));
                            //  mMap.addMarker(new MarkerOptions().position(destination));
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_dot_pink);
                            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_pink);
                            if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISPUBLIC, true)) {
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.black_dot);
                                icon1 = BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_small);
                            }


                            if (currentLocatinMarker == null) {
                                Marker localMarker = mMap.addMarker(new MarkerOptions()
                                        .position(destination)
                                        .icon(icon));
                                localMarker.setTag("CurrentLocation");
                                currentLocationID = localMarker.getId();
                                currentLocatinMarker = localMarker;
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
                            int color = R.color.pink;
                            if (PreferenceUtil.getInstance().getBoolean(context, Constants.ISPUBLIC, true))
                                color = R.color.green;

                            if (isPolyLoaded == false) {
                                polyline = mMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, color)));
                                isPolyLoaded = true;
                            } else {
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, color));
                                polyline.setPoints(polylineOptions.getPoints());
                            }


                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(origin, 17.0f);
                            if (isZoomRequired) {
                                mMap.animateCamera(cameraUpdate);
                            }
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    if (marker.getTag() == null) {
                                        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                            bottomSheet.setVisibility(View.GONE);
                                        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                            bottomSheet.setVisibility(View.VISIBLE);
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
                Intent intent_six = new Intent(BusinessActivity_Screen.this, HomeActivity.class);
                startActivity(intent_six);

            }
        });
    }

}
