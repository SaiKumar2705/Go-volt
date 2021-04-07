package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.quadrant.adapters.RideListAdapter;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RecyclerItemClickListener;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.model.TripModel;
import com.quadrant.progressbar.KProgressHUD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LieMieCourse_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView menu;
    private RecyclerView recyclerView;
    RideListAdapter mAdapter;
    ArrayList<TripModel> arrayList = new ArrayList<TripModel>();
    private Context context;
    private CircleImageView _profileImg;

    int uniqueId;
    private KProgressHUD progressbar_hud;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liemi_course);
        context = this;
        setUpView();

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Le mie corse");

        ImageView back = (ImageView) findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ImageView _homeIcon = (ImageView) findViewById(R.id.home_icon);
        _homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LieMieCourse_Activity.this, HomeActivity.class);
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

        RideListAdapter mAdapter = new RideListAdapter(arrayList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        int id = arrayList.get(position).getId();

                        Intent i = new Intent(context, CourseDetails_Activity.class);
                        i.putExtra("intVariableName", id);
                        i.putExtra("startlat",arrayList.get(position).getLattitudestartpos());
                        i.putExtra("startlan",arrayList.get(position).getLongitudestartpos());
                        i.putExtra("finishlat",arrayList.get(position).getLattitudefinishpos());
                        i.putExtra("finishlan",arrayList.get(position).getLongitudefinishpos());


                        //  i.putExtra("arraylist",)
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }

    private void setUpView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        _profileImg = (CircleImageView) findViewById(R.id.profile_image);
        LoadJson();
        //  prepareMovieData();
    }

    private void LoadJson() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, LieMieCourse_Activity.this);
            return;
        }
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JsonObject> resultRes = geo_details.GetAllTrips(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (!response.isSuccessful()) {
                 /*   Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());*/

                    if (response.code() != 200) {


                    }


                } else {
                    //Log.e(TAG, "--Success---");
                    if (response.code() == 200) {
                        try {
                            JsonObject jsonObject = response.body();
                            System.out.println("hdhdhdhhd:::" + jsonObject);
                            JsonArray trip_data = jsonObject.getAsJsonArray("data");
                            System.out.println("hdhdhdhhdKNDNDN:::" + trip_data);
                            int size = trip_data.size();
                            for (int ii = 0; ii < size; ii++) {
                                JsonObject json = trip_data.get(ii).getAsJsonObject();

                                try {
                                    uniqueId = json.get("id").getAsInt();
                                    System.out.println("huihisds:::" + json);
                                    JsonObject booking = json.getAsJsonObject("Booking");
                                    System.out.println("aaaaaaaaa" + booking);
                                    JsonObject sp = json.getAsJsonObject("StartPosition");
                                    System.out.println("bbbbbbbb" + sp);
                                    JsonObject fp = json.getAsJsonObject("FinishPosition");
                                    System.out.println("ccccccccc" + fp);
                                    JsonObject veh = json.getAsJsonObject("Vehicle");
                                    if (veh.isJsonNull())
                                        continue;
                                    System.out.println("ddddddddd" + veh);
                                    String created_at = booking.get("created_at").getAsString();
                                    System.out.println("eeeeeeeee" + created_at);
                                    double price = booking.get("price").getAsDouble();
                                    System.out.println("fffffffff" + price);
                                    String licenseplate = veh.get("license_plate").getAsString();
                                    System.out.println("gggggggggg" + licenseplate);
                                    String address_finish = fp.get("address").getAsString();
                                    double lattitudefinishpos = fp.get("latitude").getAsDouble();
                                    double longitudefinishpos = fp.get("longitude").getAsDouble();
                                    System.out.println("hhhhhhhhhh" + address_finish);
                                    String address_start = sp.get("address").getAsString();
                                    String scootertype =sp.get("type").getAsString();
                                    System.out.println("fbfbhfbhbf:::"+scootertype);
                                    double lattitudestartpos = sp.get("latitude").getAsDouble();
                                    double longitudestartpos = sp.get("longitude").getAsDouble();

                                System.out.println("iiiiiiiiii" + address_start);


                                long starttime = json.get("start").getAsLong();

                                    System.out.println("datee...."+starttime);

                                Long timel = starttime;
                                Date date = new Date(timel);

                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                                String tempdate = dateFormat.format(date);
                                DateFormat timeFormat = new SimpleDateFormat("HH.mm");
                                timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                                // String tempdate=dateFormat.format(date);
                                String temptime = timeFormat.format(date);


                                System.out.println("jjjjjjjjj" + temptime);

                                long finish_time = json.get("end").getAsLong();
    
                                Long time2 = finish_time;
                                Date date2 = new Date(time2);
                                DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy");
                                dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                                String tempdate2 = dateFormat2.format(date2);
                                DateFormat timeFormat2 = new SimpleDateFormat("HH.mm");
                                timeFormat2.setTimeZone(TimeZone.getTimeZone("GMT+2"));

                                String temptime2=timeFormat2.format(date2);

                                System.out.println("kkkkkkkkkk" + temptime2);

                             /*   long difference = date2.getTime() - date.getTime();
                                long diffMs = date2.getTime() - date.getTime();
                                long diffSec = diffMs / 1000;
                                long min = diffSec / 60;
*/
                                    //System.out.println("The difference is "+min+" minutes and "+sec+" seconds.");
                                    // System.out.println("sssssssss::::"+difference);
                                  //  if ( price > 2)
                                    {
                                        TripModel tm = new TripModel(uniqueId, created_at, price, licenseplate, address_finish, address_start, temptime, temptime2, tempdate, lattitudestartpos, longitudestartpos, lattitudefinishpos, longitudefinishpos,scootertype);
                                        arrayList.add(tm);
                                        Collections.reverse(arrayList);
                                    }
                                    // if (ii==110)
                                  //  System.out.println("*******************************UNIQUEID    " + uniqueId + " i " + ii + "  SIZE CORRENTE " + trip_data.size() + "  SIZE INIZIALE " + size);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        RideListAdapter mAdapter = new RideListAdapter(arrayList);
                        recyclerView.setAdapter(mAdapter);
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

    private void dismissProgressbar() {
        progressbar_hud.dismiss();
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


   /* private void prepareMovieData() {

        RideListItems item = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item);
        RideListItems item1 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item1);
        RideListItems item2 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item2);
        RideListItems item3 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item3);
        RideListItems item4 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item4);
        RideListItems item5 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item5);
        RideListItems item6 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item6);
        RideListItems item7 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item7);
        RideListItems item8 = new RideListItems("via carlo poma2-via Than De Revel 3", "DBLX8X", "21min - 4.5 km", "3/12/2018", "€ 0,60");
        arrayList.add(item8);

        // mAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void onClick(View v) {

    }
}
