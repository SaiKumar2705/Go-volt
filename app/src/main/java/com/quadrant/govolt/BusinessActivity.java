package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
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

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.quadrant.bottomsheetfragment;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.MyLatLng;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.ActivateSiteInterface;
import com.quadrant.interfaces.GetSiteID_Interface;
import com.quadrant.interfaces.GetVehicleInterface;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.response.SiteIDResponse;
import com.quadrant.response.UserInfoResponse;
import com.quadrant.response.VehicleResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BusinessActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private Context context;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ToggleButton toogle_btn;
    private ImageView menu, center_imageview;
    private static final String TAG = BusinessActivity.class.getSimpleName();
    private BottomSheetBehavior mBottomSheetBehavior;
    private AlertDialog alertDialog;
    private View dialogView;
    private KProgressHUD progressbar_hud;
    private List<VehicleResponse.Data> vehicle_data_array;
    private int vehicleID;
    private ArrayList<LatLng> listMyLatLng;
    private  ArrayList<MyLatLng> arrayList = new ArrayList<MyLatLng>();
    private  ArrayList<MyLatLng> selected_loc_arrayList = new ArrayList<MyLatLng>();
    String _type = "";
    CoordinatorLayout colayout;
    View bottomSheet;
    private Marker mSelectedMarker;
    private Marker myMarker;
    private boolean _isDispalyedMarker = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity);
        context  = this;

        String sai ="programmer";
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpView();
        ToggleButton1Colorchange();
        showBottomSheet();
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if(_isInternetAvailable){
            UserInformation();
        }else{
            Toast.makeText(context, "Please connect internet.", Toast.LENGTH_SHORT).show();

        }


    }

    private void setUpView() {
        menu = (ImageView)findViewById(R.id.balck_menu);
        toogle_btn = (ToggleButton)findViewById(R.id.toogle);
        center_imageview = (ImageView)findViewById(R.id.center_img);

        menu.setOnClickListener(this);
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

        Call<JSONObject> resultRes=geo_details.GetResponse(Constants.TOKEN,bearer_authorization, _siteID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                }else {
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

        int siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
       // Geofenceing_Service(siteID);
    }

   void LoadingGoogleMap(ArrayList<MyLatLng> arrayList) {
        if (mMap != null) {
            mMap.clear();
            mSelectedMarker = null;

            bottomSheet.setVisibility(View.GONE);
           /* if(yourCountDownTimer!=null){
                yourCountDownTimer.cancel();
                yourCountDownTimer.onFinish();

            }*/


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

                            MyLatLng latlng =new  MyLatLng();
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
                                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_green));

                                }
                            }
                            mSelectedMarker = marker;
                            if (_isDispalyedMarker == false) {
                                mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_small));

                            }


                            //showinMarkers(marker);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.balck_menu:
               finish();
                break;
          /*  case R.id.ll1:
                firstPopUp();
                break;
            case R.id.ll2:
                secondPopUp();
                break;*/

        }
    }

    private void secondPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivity.this).inflate(R.layout.buisness_benuveno, viewGroup, false);
        Button Img_btn=(Button) dialogView.findViewById(R.id.done_btn);

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


        Img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });

    }

    private void firstPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(BusinessActivity.this).inflate(R.layout.buisness_menu_alert, viewGroup, false);
        Button Img_done=(Button) dialogView.findViewById(R.id.done_btn);

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


        Img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

             }
        });

    }

    private void ToggleButton1Colorchange() {

        toogle_btn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            toogle_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_pink));

                            center_imageview.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_dentsu));
                          //  center_imageview.getLayoutParams().height = 100;
                           // center_imageview.getLayoutParams().height = 80;
                            openBottomScreen();

                        } else {

                            toogle_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_default));
                            center_imageview.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_logo_one));

                           // center_imageview.getLayoutParams().height = 120;
                           // center_imageview.getLayoutParams().height = 120;
                        }
                    }
                });
    }

    private void showBottomSheet() {

         colayout = (CoordinatorLayout) findViewById(R.id.maps_colayout);
         bottomSheet = colayout.findViewById(R.id.bottom_sheet_bike);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


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

    }
    private void openBottomScreen() {

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            //If state is in collapse mode expand it
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        else
            //else if state is expanded collapse it
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

   /* private void ToggleButton1Colorchange() {

        toogle_btn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            bottomsheetfragment addPhotoBottomDialogFragment =
                                    bottomsheetfragment.newInstance();
                            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                                    "add_photo_dialog_fragment");


                        } else {

                            toogle_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_default));
                            center_imageview.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_logo_one));
                        }
                    }
                });
    }*/

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
}
