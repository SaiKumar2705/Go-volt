package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.quadrant.adapters.OrderListAdapter;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.IUpdateWallet;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.model.OrderModel;
import com.quadrant.progressbar.KProgressHUD;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Wallet_Display_Fragment extends Fragment implements IUpdateWallet {
    private Context context;
    private View view;
    private AlertDialog alertDialog;
    private KProgressHUD progressbar_hud;
    RecyclerView Order_list;
    ArrayList<OrderModel> arrayList = new ArrayList<OrderModel>();
    LinearLayout LL;
    ImageView Image1;
    TextView Count,tv_FinalCount;
    private static IUpdateWallet iUpdateWallet;

    public static IUpdateWallet getInstance(){

        return iUpdateWallet;
    }


    private CountDownTimer yourCountDownTimer;

    public Wallet_Display_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iUpdateWallet = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet_dis, container, false);
        context = this.getActivity();

       // ReFreash_onCreatemethod();

        getbonusdetails();
        getorderlist();
        ImageView donebtn = (ImageView) view.findViewById(R.id.done);
        Order_list = view.findViewById(R.id.orderlist);
      //  Order_list.setVisibility(View.GONE);
        LL = view.findViewById(R.id.ll);
        Count = view.findViewById(R.id.count);
        tv_FinalCount= view.findViewById(R.id.finalcount);
       // Image1 = view.findViewById(R.id.image);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        Order_list.setLayoutManager(mLayoutManager);
        Order_list.setItemAnimator(new DefaultItemAnimator());
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

   /*     LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Order_list.isShown()) {
                    //

                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            0,                 // fromYDelta
                            Order_list.getHeight()); // toYDelta
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    Order_list.startAnimation(animate);

                    Order_list.setVisibility(View.GONE);

                    Image1.setImageResource(R.drawable.ic_right_button);
                } else {


                    // Order_list.setAlpha(0.0f);

                    TranslateAnimation animate = new TranslateAnimation(
                            0,                 // fromXDelta
                            0,                 // toXDelta
                            Order_list.getHeight(),  // fromYDelta
                            0);                // toYDelta
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    Order_list.startAnimation(animate);
                    Order_list.setVisibility(View.VISIBLE);


                    Image1.setImageResource(R.drawable.ic_down_button);
                }
            }
        });*/
        return view;
    }





    private void getorderlist() {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if (!_isInternetAvailable) {

            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, getActivity());
            return;
        }
        //  showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        int _siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
        Call<JsonObject> resultRes = geo_details.GetOrderResponse(Constants.TOKEN, bearer_authorization);
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
                        arrayList= new ArrayList<>();
                        JsonObject jsonObject = response.body();
                        JsonObject getOrderItems = jsonObject.getAsJsonObject("data");
                        JsonArray getfinallist = getOrderItems.getAsJsonArray("data");

                        for (int i = 0; i < getfinallist.size(); i++) {

                            JsonObject jo = getfinallist.get(i).getAsJsonObject();
                            String Status = jo.get("status").getAsString();
                            String datatime = jo.get("created_at").getAsString();
                            System.out.println("njfnnfnf:::" + datatime);
                            JsonObject shopitem = jo.get("ShopItem").getAsJsonObject();
                            JsonObject price = shopitem.get("price").getAsJsonObject();
                            String creditprice = price.get("value").getAsString();
                            JsonObject bonustemplate = shopitem.get("BonusTemplate").getAsJsonObject();
                            JsonObject limits = bonustemplate.get("limits").getAsJsonObject();
                            JsonObject rewardprice = limits.get("price").getAsJsonObject();
                            String rewardpriceval = rewardprice.get("value").getAsString();
                            JsonObject constrains = bonustemplate.get("constraints").getAsJsonObject();
                            JsonArray validitytime = constrains.get("validityTime").getAsJsonArray();


                            JsonObject jofromtime = new JsonObject();
                            JsonObject jotilltime = new JsonObject();
                            for (int j = 0; j < validitytime.size(); j++) {
                                jofromtime = validitytime.get(0).getAsJsonObject();
                                jotilltime = validitytime.get(1).getAsJsonObject();
                            }

                            String Fromtime = jofromtime.get("type").getAsString();
                            String Tilltime = jotilltime.get("type").getAsString();

                            OrderModel tm = new OrderModel(Status, datatime, creditprice, rewardpriceval, Fromtime, Tilltime);
                            arrayList.add(tm);
                        }


                        //System.out.println("nnjfnnfn:::"+res);

                    }
                    OrderListAdapter mAdapter = new OrderListAdapter(arrayList);
                    Order_list.setAdapter(mAdapter);

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

    private void getbonusdetails() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);
        if (!_isInternetAvailable) {

            AppUtils.error_Alert("Per favore connettiti a internet", context, alertDialog, getActivity());
            return;
        }
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        int _siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
        Call<JsonObject> resultRes = geo_details.GetBonusResponse(Constants.TOKEN, bearer_authorization);
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


                        JsonObject jsonObject = response.body();

                        JsonObject jo = jsonObject.get("data").getAsJsonObject();

                        JsonArray jA =jo.get("data").getAsJsonArray();

                     int   nTotalSum= 0;
                        for(int i=0;i<jA.size();i++){
                            JsonObject json = jA.get(i).getAsJsonObject();
                            JsonObject bonustemp_jo  =json.get("BonusTemplate").getAsJsonObject();
                            JsonObject limits_jo  =bonustemp_jo.get("limits").getAsJsonObject();
                            JsonObject price_jo  =limits_jo.get("price").getAsJsonObject();
                            int finalprice =price_jo.get("value").getAsInt();
                            nTotalSum += Integer.parseInt(String.valueOf(finalprice));

                        }
                        tv_FinalCount.setText(String.valueOf("€"+" "+nTotalSum/100));

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
        progressbar_hud = KProgressHUD.create(context)
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

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = (ViewGroup) view.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.wow_alert, viewGroup, false);

        Button ok_btn = (Button) dialogView.findViewById(R.id.done_btn);
        TextView textView = (TextView) dialogView.findViewById(R.id.title);

        String text1 = "Abbiamo aggiunto" + "<br />" + "<font color='#37B12B'>xxx minuti</font>" + "<br />" + "al tuo wallet!";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text1, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            textView.setText(Html.fromHtml(text1), TextView.BufferType.SPANNABLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);
        alertDialog.show();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void updateWallet() {
        getorderlist();
    }
}