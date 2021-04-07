package com.quadrant.govolt;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.WriterException;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.govolt.R;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.TripSingleResponse;
import com.quadrant.response.CommunityResponse;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


import com.google.android.gms.maps.model.LatLngBounds;

public class CourseDetails_Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {
    private ImageView back_img;
    private String TAG = "CourseDetails_Activity";
    private Context context;
    JSONObject Data_trip;
    private KProgressHUD progressbar_hud;
    private TextView Trip_Date, Trip_LicensePlate, Trip_Amount, To_time, From_Time, Trip_Minute, Trip_kg, TripFrom_Dist, TripTo_Dist;
    private int _singletripID;
    private View bottomSheet;
    JSONObject finalJo;
    String cylinder_capacity, licenseid;
    ArrayList<LatLng> storelatlan = new ArrayList<LatLng>();
    LatLng listvalues1, LatitudesLong, LatitudesLong1;
    LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private BottomSheetBehavior mBottomSheetBehavior;
    Integer intValue;
    StringBuffer finalString, finalString1;
    TextView start_Time, end_Time, Corsa, Pausa, Minuti, Prenotazione, Totale, Vat;
    Button Img_Done;
    ImageView HOme, Img_bike;
    double source_lat, source_lan, dist_lat, dist_lan;
    int difhours, difmin;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corse_second);
        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.imageView3);
        mapFragment.getMapAsync(this);
        //   slider_image_list1 = new ArrayList<Bitmap>();
        createGoogleApi();
        Inilializer();
        getData();
        TripSingleApi();
        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImgDone_Click();
        Homebtnclick();
    }

    /*   @Override
       public void onMapReady(GoogleMap googleMap) {
           // Add a polyline to the map.
           Polyline polyline1 = googleMap.addPolyline((new PolylineOptions())
                   .clickable(true)
                   .add(new LatLng(-35.016, 143.321),
                           new LatLng(-34.747, 145.592),
                           new LatLng(-34.364, 147.891),
                           new LatLng(-33.501, 150.217),
                           new LatLng(-32.306, 149.248),
                           new LatLng(-32.491, 147.309)));

           // Set listeners for click events.
           googleMap.setOnPolylineClickListener(this);
           googleMap.setOnPolygonClickListener(this);
       }
   */
    private void Homebtnclick() {

        HOme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Int = new Intent(CourseDetails_Activity.this, HomeActivity.class);
                startActivity(Int);
            }
        });
    }

    private void ImgDone_Click() {

        Img_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // Intent i = new Intent(CourseDetails_Activity.this, LieMieCourse_Activity.class);
                // startActivity(i);

            }
        });
    }


    private void getData() {
        Intent mIntent = getIntent();
        intValue = mIntent.getIntExtra("intVariableName", 0);
        source_lat = mIntent.getDoubleExtra("startlat", 0);
        System.out.println("vlatcheck::::" + source_lat);
        source_lan = mIntent.getDoubleExtra("startlan", 0);
        dist_lat = mIntent.getDoubleExtra("finishlat", 0);
        dist_lan = mIntent.getDoubleExtra("finishlan", 0);


        System.out.println("vdgdbbhdINT" + intValue);
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


    private void PolyLineOnMap() {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, CourseDetails_Activity.this);
            return;
        }

        _singletripID = intValue;
        //   showProgressbar();

        //ghouseid needs to be changed
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JsonObject> resultRes = geo_details.GetSingleTripResPosition(Constants.TOKEN, bearer_authorization, _singletripID);
        resultRes.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                System.out.println("Saujnddjd:::" + response.body());

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());
                    dismissProgressbar();

                    if (response.code() != 200) {
                    }

                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {


                        //  Toast.makeText(getApplicationContext(), "Hello Javatpoint", Toast.LENGTH_SHORT).show();
                        storelatlan = new ArrayList<LatLng>();
                        JsonObject jsonObject = response.body();
                        JsonObject polyline_data = jsonObject.getAsJsonObject("data");
                        JsonArray raw_data = polyline_data.getAsJsonArray("raw");
                        if (raw_data != null) {
                            for (int i = 0; i < raw_data.size(); i++) {
                                JsonObject json = raw_data.get(i).getAsJsonObject();
                                double longitude = json.get("longitude").getAsDouble();
                                double latitude = json.get("latitude").getAsDouble();
                                System.out.println("dggdfdhjdjd1:::" + longitude);
                                System.out.println("dggdfdhjdjd1:::" + latitude);
                                listvalues1 = new LatLng(raw_data.get(i).getAsJsonObject().get("latitude").getAsDouble(), raw_data.get(i).getAsJsonObject().get("longitude").getAsDouble());
                                System.out.println("latlng::::");
                                storelatlan.add(listvalues1);
                                for (int j = 0; j < storelatlan.size(); j++) {

                                }
                            }
                            LatitudesLong = storelatlan.get(0);
                            LatitudesLong1 = storelatlan.get(1);
                            System.out.println("vfhvjhbdhbhkbd:::" + LatitudesLong);
                            System.out.println("vfhvjhbdhbhkbdddd:::" + LatitudesLong1);
                            requestDirection(storelatlan);

                        }


                        //dismissProgressbar();

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


    private void requestDirection(ArrayList<LatLng> latlanlist) {
        System.out.println("deghdgdgdg" + latlanlist);
        LatLng origin = null;
        LatLng destination = null;
        origin = new LatLng(source_lat, source_lan);
        System.out.println("dhdhjjdjdjd" + origin);
        destination = new LatLng(dist_lat, dist_lan);
        System.out.println("dhdhjjdjdjdddddd" + destination);


        final PolylineOptions options = new PolylineOptions().width(20).color(Color.GREEN).geodesic(true);

        for (int k = 0; k < latlanlist.size(); k++) {

            // 18.1433904,78.0793135


            source_lat = latlanlist.get(k).latitude;
            source_lan = latlanlist.get(k).longitude;
            ;
            //   17.4514176,78.3900672
            dist_lat = latlanlist.get(k).latitude;
            dist_lan = latlanlist.get(k).longitude;


            LatLng point = latlanlist.get(k);
            options.add(point);


        }

        /*GoogleMap map;
        // ... get a map.
        // Add a thin red line from London to New York.
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(source_lat, source_lan), new LatLng(dist_lat, dist_lan))
                .width(5)
                .color(Color.RED));*/
        // final LatLng destination = new LatLng(45.478241,  9.18351);
        String serverKey = context.getResources().getString(R.string.google_maps_key);
        final LatLng finalDestination = destination;
        final LatLng finalOrigin = origin;
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


                            LatLngBounds.Builder builder = LatLngBounds.builder();

                            for (LatLng latLng : options.getPoints()) {
                                builder.include(latLng);
                            }
                            final LatLngBounds bounds = builder.build();

                            mMap.addMarker(new MarkerOptions().position(finalDestination));
                            mMap.addMarker(new MarkerOptions().position(finalOrigin));
                            //DC_FIX
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(finalDestination, 17.0f);
                            //   mMap.animateCamera(cameraUpdate);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                            //ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            // mMap.addPolyline(DirectionConverter.createPolyline(context, directionPositionList, 5, ContextCompat.getColor(context, R.color.green_dark)));
                            // setCameraWithCoordinationBounds(route);
                            mMap.addPolyline(options);


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


                             /*   _distance.setText(""+distance);
                                _time.setText(""+time);*/

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


    private void TripSingleApi() {


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, CourseDetails_Activity.this);
            return;
        }

        //  String url = "https://www.buildersmart.in/api/categoryresultsoptimised.php/?store_id=1&category_id=241&start=0&end=9&nocache=123456";

        _singletripID = intValue;
        showProgressbar();

        //ghouseid needs to be changed
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;
        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);
        RequestInterface geo_details = retrofit.create(RequestInterface.class);
        Call<JsonObject> resultRes = geo_details.GetSingleTripResponse(Constants.TOKEN, bearer_authorization, _singletripID);
        resultRes.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                System.out.println("Saujnddjd:::" + response.body());

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());
                    dismissProgressbar();

                    if (response.code() != 200) {

                    }

                } else {
                    Log.e(TAG, "--Success---");

                    if (response.code() == 200) {
                        // Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                        try {
                            JsonObject jsonObject = response.body();
                            System.out.println("hdhdhdhhd:::" + jsonObject);
                            JsonObject trip_data = jsonObject.getAsJsonObject("data");
                            System.out.println("hfhfhhfhf:::" + trip_data);
                            JsonObject tripdata_booking = trip_data.getAsJsonObject("Booking");
                            System.out.println("saiiiiii:::" + tripdata_booking);
                            JsonObject detailedprice = tripdata_booking.getAsJsonObject("detailedPrice");
                            System.out.println("detailedprice::::" + detailedprice);
                            double corsa = detailedprice.get("running").getAsDouble();
                            double pausa = detailedprice.get("parked").getAsDouble();
                            String minuti_gratis = detailedprice.get("free").getAsString();
                            String prenotazione = detailedprice.get("prepaid").getAsString();
                            double vat = detailedprice.get("vat").getAsDouble();
                            String created_at = tripdata_booking.get("created_at").getAsString();
                            //    long tempcal = tripdata_booking.get("created_at").getAsLong();
                            System.out.println("dhhdjhdjhdhhjdvhhvhbdbdb::" + created_at);
                            double price = tripdata_booking.get("price").getAsDouble();
                            long start_time = trip_data.get("start").getAsLong();
                            Date date = new Date(start_time);
                            DateFormat dateFormatstart = new SimpleDateFormat("dd/MM/yyyy");
                            String tempdatestart = dateFormatstart.format(date);
                            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                            String printdae = dateFormat.format(date);
                            System.out.println("jdbsjijsnjkns:::" + printdae);
                            long finish_time = trip_data.get("end").getAsLong();
                            int duration = tripdata_booking.get("duration").getAsInt();
                            JsonObject trip_data_startposition = trip_data.getAsJsonObject("StartPosition");
                            String scootertype = trip_data_startposition.get("type").getAsString();
                            if (scootertype.equals("kick")) {
                                Img_bike.setImageResource(R.drawable.ic_kick_ride);

                            } else {

                                Img_bike.setImageResource(R.drawable.ic_scooter);
                            }
                            String address = trip_data_startposition.get("address").getAsString();
                            JsonObject trip_data_finishposition = trip_data.getAsJsonObject("FinishPosition");
                            String address_finish = trip_data_finishposition.get("address").getAsString();
                            JsonArray statuschanges = trip_data.getAsJsonArray("StatusChanges");
                            long timel = start_time;
                            Date date3 = new Date(timel);
                            DateFormat timeFormat = new SimpleDateFormat("hh:mm");
                            timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                            // String tempdate=dateFormat.format(date);
                            String tempdatetime = timeFormat.format(date3);
                            Date date2 = new Date(timel);
                            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                            String tempdate = dateFormat2.format(date2);
                            String strDate = tempdate.substring(3, 7);
                            System.out.println("ndjdnjndjndn::" + strDate);
                            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN);
                        /*    String output= WordUtils.capitalize(outputFormat.toString());

                            System.out.println("saikkk:::"+output);*/
                            DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date date1 = inputFormat.parse(tempdate);
                            String outputText = outputFormat.format(date1);
                            System.out.println("AAA========::" + outputText);
                            long time2 = finish_time;
                            Date date4 = new Date(time2);
                            DateFormat timeFormat4 = new SimpleDateFormat("hh:mm");
                            timeFormat4.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                            String tempdatetimefinish = timeFormat4.format(date4);

                            //  getDateDiff();

                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm");
                                Date datestart = simpleDateFormat.parse(tempdatestart + " " + tempdatetime);
                                // Date datestart = simpleDateFormat.parse("20/05/2019 15:30");

                                Date datefinish = simpleDateFormat.parse(tempdate + " " + tempdatetimefinish);

                                long difference = datefinish.getTime() - datestart.getTime();
                                int days = (int) (difference / (1000 * 60 * 60 * 24));
                                difhours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                                difmin = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * difmin)) / (1000 * 60);
                                difhours = (difhours < 0 ? -difhours : difhours);
                                Log.i("======= Hours", " :: " + difhours);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            if (statuschanges != null) {
                                for (int i = 0; i < statuschanges.size(); i++) {
                                    JsonObject json = statuschanges.get(i).getAsJsonObject();
                                    cylinder_capacity = json.get("cylinder_capacity").getAsString();
                                    System.out.println("dggdfdhjdjd:::" + cylinder_capacity);
                                    licenseid = json.get("license_plate").getAsString();
                                    System.out.println("dggdfdhjdjd1:::" + licenseid);
                                }
                            }
                            //Trip_Amount.setText(String.valueOf(price));
                            DecimalFormat form = new DecimalFormat("0.00");
                            // Trip_Amount.setText(String.valueOf(price));
                            Trip_Amount.setText(form.format(price));
                            Trip_Minute.setText(String.valueOf(difhours + " " + "hr" + " " + " " + difmin + " " + "min"));
                            Trip_kg.setText(cylinder_capacity);
                            Trip_LicensePlate.setText(licenseid);
                            int index = 0;
                            finalString = new StringBuffer();
                            while (index < address.length()) {
                                Log.i(TAG, "test = " + address.substring(index, Math.min(index + 27, address.length())));
                                finalString.append(address.substring(index, Math.min(index + 27, address.length())) + "\n");
                                index += 27;
                            }
                            int index1 = 0;
                            finalString1 = new StringBuffer();
                            while (index1 < address_finish.length()) {
                                Log.i(TAG, "test = " + address_finish.substring(index1, Math.min(index1 + 27, address_finish.length())));
                                finalString1.append(address_finish.substring(index1, Math.min(index1 + 27, address_finish.length())) + "\n");
                                index1 += 27;
                            }
                            TripFrom_Dist.setText(finalString);
                            //  TripFrom_Dist.setText(address);
                            TripTo_Dist.setText(finalString1);
                            From_Time.setText(tempdatetime);
                            To_time.setText(tempdatetimefinish);
                            Trip_Date.setText(outputText);
                            start_Time.setText(tempdatetime);
                            end_Time.setText(tempdatetimefinish);

                            //Corsa.setText(String.valueOf(corsa));
                            Corsa.setText(form.format(corsa));
                            /*if(pausa.equals("0")){
                                pausa="0.00";
                            }*/
                            //Pausa.setText(String.valueOf(pausa));
                            Pausa.setText(form.format(pausa));
                            Minuti.setText(String.valueOf(minuti_gratis));
                            Prenotazione.setText(String.valueOf(prenotazione));
                            //  Totale.setText(String.valueOf(price));
                            Totale.setText(form.format(price));
                            //Vat.setText(String.valueOf(vat));
                            Vat.setText(form.format(vat));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                      /*  for(int i=0;i<trip_data.size();i++){


                            String created_at =trip_data.get("created_at").getAsString();

                            System.out.println("dhhdhdhhdhdhd:::"+created_at);
                        }
*/


                        // TripSingleResponse res = response.body();
                        /*String created_at = finalJo.getData().getBooking().getCreatedAt();
                        System.out.println("bhbjkshjsjh:::"+created_at);
                        int price = res.getData().getBooking().getPrice();
                        double start_time = res.getData().getBooking().getStarted();
                        int finish_time = res.getData().getBooking().getSetupEnd();
                        int duration = res.getData().getBooking().getDuration();
                        int capacity = res.getData().getVehicle().getCylinderCapacity();
                        String license_plate = res.getData().getVehicle().getLicensePlate();
                        String start_position = res.getData().getStartPosition().getAddress();
                        String finish_position = res.getData().getFinishPosition().getAddress();*/
                    }
                }
                dismissProgressbar();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();
            }
        });
    }

    private void getDateDiff() {


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

    private void Inilializer() {
        Trip_Date = (TextView) findViewById(R.id.textView2);
        Trip_Amount = (TextView) findViewById(R.id.textView3);
        From_Time = (TextView) findViewById(R.id.from_to_time);
        To_time = (TextView) findViewById(R.id.to_time);
        Trip_Minute = (TextView) findViewById(R.id.trip_minute);
        Trip_kg = (TextView) findViewById(R.id.trip_kg);
        TripFrom_Dist = (TextView) findViewById(R.id.tripfromdist);
        TripTo_Dist = (TextView) findViewById(R.id.triptodist);
        Trip_LicensePlate = (TextView) findViewById(R.id.license_plate);
        start_Time = (TextView) findViewById(R.id.tv_starttime);
        end_Time = (TextView) findViewById(R.id.tv_endtime);
        Corsa = (TextView) findViewById(R.id.corsa);
        Pausa = (TextView) findViewById(R.id.pausa);
        Minuti = (TextView) findViewById(R.id.minuti);
        Prenotazione = (TextView) findViewById(R.id.vat);
        Totale = (TextView) findViewById(R.id.totale);
        Vat = (TextView) findViewById(R.id.vat);
        Img_Done = (Button) findViewById(R.id.img_done);
        HOme = (ImageView) findViewById(R.id.imageView5);

        Img_bike = findViewById(R.id.img_bike);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
       /* mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //DC_FIX
        LatLng StartPosition = new LatLng(45.4654219, 9.1859243);
        float zoomlevel = (float) 12.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartPosition, zoomlevel));
        PolyLineOnMap();


        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);


        //   defaultGoogleMapShowLocation();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }
        });


    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
/*
    private void defaultGoogleMapShowLocation() {

        LatLng latlong = new LatLng(CURRENT_LATITUDE, CURRENT_LANGITUDE);
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(latlong, 15);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);

    }*/
}
