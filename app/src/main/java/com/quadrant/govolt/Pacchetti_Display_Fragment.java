package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.response.AvailableItemResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.responses.PachettiErrorResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Pacchetti_Display_Fragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private AlertDialog alertDialog;
    private KProgressHUD progressbar_hud;
    private LinearLayout ll_one, ll_two, ll_three, ll_four;
    private TextView FirstCreditAmt, FirstRewardAmt, FirstValidTime, SecondCreditAmt, SecondRewardAmt, SecondValidTime, ThirdCreditAmt, ThirdRewardAmt, ThirdValidTime, FourthCreditAmt, FourthRewardAmt, FourthValidTime;
    private int AvailableId;
    private TextView[] creditAmtTxtArray, rewardAmtTxtArray, validTimeArray;
    private LinearLayout[] linearLayouts;
    private TextView HeaderText_One, HeaderText_Two, HeaderText_Three, HeaderText_Four;
    int reward_price;

    public Pacchetti_Display_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pacchetti, container, false);
        context = this.getActivity();
        Setupview();
        getavailableItemsApi();
        return view;
    }

    private void Setupview() {
        ll_one = view.findViewById(R.id.ll1);
        ll_two = view.findViewById(R.id.ll2);
        ll_three = view.findViewById(R.id.ll3);
        ll_four = view.findViewById(R.id.ll4);
        ll_one.setVisibility(View.INVISIBLE);
        ll_two.setVisibility(View.INVISIBLE);
        ll_three.setVisibility(View.INVISIBLE);
        ll_four.setVisibility(View.INVISIBLE);
        ll_one.setOnClickListener(this);
        ll_two.setOnClickListener(this);
        ll_three.setOnClickListener(this);
        ll_four.setOnClickListener(this);
        FirstCreditAmt = view.findViewById(R.id.firstcreditamt);
        FirstRewardAmt = view.findViewById(R.id.firstrewardamt);
        FirstValidTime = view.findViewById(R.id.firstvalidtime);
        SecondCreditAmt = view.findViewById(R.id.secondcreditamt);
        SecondRewardAmt = view.findViewById(R.id.secondrewardamt);
        SecondValidTime = view.findViewById(R.id.secondvalidtime);
        ThirdCreditAmt = view.findViewById(R.id.thirdcreditamt);
        ThirdRewardAmt = view.findViewById(R.id.thirdrewardamt);
        ThirdValidTime = view.findViewById(R.id.thirdvalidtime);
        FourthCreditAmt = view.findViewById(R.id.fourthcreditamt);
        FourthRewardAmt = view.findViewById(R.id.fourthrewardamt);
        FourthValidTime = view.findViewById(R.id.fourthvalidtime);
        creditAmtTxtArray = new TextView[]{FirstCreditAmt, SecondCreditAmt, ThirdCreditAmt, FourthCreditAmt};
        rewardAmtTxtArray = new TextView[]{FirstRewardAmt, SecondRewardAmt, ThirdRewardAmt, FourthRewardAmt};
        //   validTimeArray = new TextView[]{FirstValidTime, SecondValidTime, ThirdValidTime, FourthValidTime};
        linearLayouts = new LinearLayout[]{ll_one, ll_two, ll_three, ll_four};
        HeaderText_One = view.findViewById(R.id.headertext1);
        HeaderText_Two = view.findViewById(R.id.headertext2);
        HeaderText_Three = view.findViewById(R.id.headertext3);
        HeaderText_Four = view.findViewById(R.id.headertext4);

    }

    private void getavailableItemsApi() {
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
        Call<JsonObject> resultRes = geo_details.GetResponsebyavailableitems(Constants.TOKEN, bearer_authorization, _siteID);
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
                            JsonArray getavailableItems = jsonObject.getAsJsonArray("data");
                            int noofAvaiItems = getavailableItems.size();
                            if (getavailableItems != null && getavailableItems.size() > 0) {
                                databinding(getavailableItems);
                            }
                            System.out.println("noofAvaiItems:::" + noofAvaiItems);

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

    private void databinding(JsonArray getavailableItems) {
        for (int i = 0; i < getavailableItems.size(); i++) {
            JsonObject jo = getavailableItems.get(i).getAsJsonObject();
            linearLayouts[i].setVisibility(View.VISIBLE);
            JsonObject getprice = jo.get("price").getAsJsonObject();
            int firstcreditvalue = getprice.get("value").getAsInt();
            AvailableId = jo.get("id").getAsInt();
            rewardAmtTxtArray[i].setText("a €" + String.valueOf(firstcreditvalue / 100));
            JsonObject getbonustemplate = jo.get("BonusTemplate").getAsJsonObject();
            JsonObject limits = getbonustemplate.get("limits").getAsJsonObject();
            JsonObject getpricereward = limits.get("price").getAsJsonObject();
            int rewardprice = getpricereward.get("value").getAsInt();
            reward_price = rewardprice / 100;
            creditAmtTxtArray[i].setText(String.valueOf(rewardprice / 100));
            JsonObject getconstraints = getbonustemplate.get("constraints").getAsJsonObject();
            JsonArray getvaliditytime = getconstraints.get("validityTime").getAsJsonArray();
            JsonObject jofromtime = new JsonObject();
            JsonObject jotilltime = new JsonObject();
            for (int j = 0; j < getvaliditytime.size(); j++) {
                jofromtime = getvaliditytime.get(0).getAsJsonObject();
                jotilltime = getvaliditytime.get(1).getAsJsonObject();
            }
            String Fromtime = jofromtime.get("type").getAsString();
            String Tilltime = jotilltime.get("type").getAsString();
            /*if (Fromtime.equals("UNBOUNDED") && Tilltime.equals("UNBOUNDED")) {
                validTimeArray[i].setText("Valido fino" + " Tutta la vita");
            } else {
                validTimeArray[i].setText("Valido fino" + Fromtime + "-" + Tilltime);
            }*/
        }

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

    private void showCustomDialog(final int pos, String header) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = (ViewGroup) view.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.wallpaccheti_alert, viewGroup, false);

       /* Button confirm_btn = (Button) dialogView.findViewById(R.id.confirm);
        TextView creditamtpopup = (TextView) dialogView.findViewById(R.id.popupcreditamt);
        TextView rewardamtpopup = (TextView) dialogView.findViewById(R.id.rewardpopuptime);
        TextView Validtimepopup = (TextView) dialogView.findViewById(R.id.validtimepopup);
        TextView Header_tv = (TextView) dialogView.findViewById(R.id.header_popup);
        Header_tv.setText(header);
        LinearLayout backnav = (LinearLayout) dialogView.findViewById(R.id.backll);

        if (pos == 1) {
            creditamtpopup.setText(FirstCreditAmt.getText().toString());
            rewardamtpopup.setText(FirstRewardAmt.getText().toString());
            Validtimepopup.setHint(FirstValidTime.getText().toString());
        } else if (pos == 2) {
            creditamtpopup.setText(SecondCreditAmt.getText().toString());
            rewardamtpopup.setText(SecondRewardAmt.getText().toString());
            Validtimepopup.setHint(SecondValidTime.getText().toString());
        } else if (pos == 3) {
            creditamtpopup.setText(ThirdCreditAmt.getText().toString());
            rewardamtpopup.setText(ThirdRewardAmt.getText().toString());
            Validtimepopup.setHint(ThirdValidTime.getText().toString());
        } else if (pos == 4) {
            creditamtpopup.setText(FourthCreditAmt.getText().toString());
            rewardamtpopup.setText(FourthRewardAmt.getText().toString());
            Validtimepopup.setHint(FourthValidTime.getText().toString());
        }*/

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        /*alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.whiteTransparent);*/
        alertDialog.show();

       /* confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                if (pos == 1) {
                    AvailableId = 1;
                } else if (pos == 2) {
                    AvailableId = 2;
                } else if (pos == 3) {
                    AvailableId = 3;
                } else if (pos == 4) {
                    AvailableId = 4;
                }

                PostRequestApi(AvailableId);

                //  confirmPopup();
            }
        });*/

        /*backnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });*/
    }

    private void PostRequestApi(int availableId) {
        showProgressbar();
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;
        int _siteID = PreferenceUtil.getInstance().getInt(context, Constants.SITE_ID, 0);
        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);
        RequestInterface register_details = retrofit.create(RequestInterface.class);
        Call<AvailableItemResponse> resultRes = register_details.GetAvailableData(Constants.TOKEN, bearer_authorization, availableId, _siteID);
        resultRes.enqueue(new Callback<AvailableItemResponse>() {
            @Override
            public void onResponse(Call<AvailableItemResponse> call, Response<AvailableItemResponse> response) {
                if (!response.isSuccessful()) {
                  /*  Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Respo
                    nse ---" + response.body());


*/
                    int rescode = response.code();
                    System.out.println("bdhbhfbbfbbf:::" + rescode);
                    if (response.code() != 200) {
                        if (response.code() != 500) {

                            PachettiErrorResponse error = ErrorUtils.parseErrorPac(response);

                            String errorres = error.getError().getDescription();

                            if (errorres.equals("Payment's method is not found")) {

                                errorres = "Il metodo di pagamento non è stato trovato";
                            }

                            System.out.println("hfvhfhf:::" + errorres);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", errorres);

                            error_Alertbox(errorres);

                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    // Log.e(TAG, "--Success---");
                    if (response.code() == 200) {
                        Wallet_Display_Fragment.getInstance().updateWallet();
                        confirmPopup();

                    }
                }
                dismissProgressbar();

            }

            @Override
            public void onFailure(Call<AvailableItemResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());

                //dismissProgressbar();

            }


        });

    }


    private void error_Alertbox(String error_message) {
        ViewGroup viewGroup = view.findViewById(android.R.id.content);

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


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
    }

    private void confirmPopup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = (ViewGroup) view.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.complimenti_alert, viewGroup, false);
        Button _done = (Button) dialogView.findViewById(R.id.buttonOk);
        //TextView textView = (TextView)dialogView.findViewById(R.id.title);
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
        _done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll1:
                Intent intent = new Intent(getActivity(), WalletDiaplayActivity.class);
                intent.putExtra("packagename", "FRIENDS");
                String fca = FirstCreditAmt.getText().toString();
                String fra = FirstRewardAmt.getText().toString();
                String newString = fra.replace("a €", "");
                int fcamt = Integer.parseInt(fca);
                int framt = Integer.parseInt(newString);
                intent.putExtra("creditamt",framt);
                intent.putExtra("rewardamt", fcamt);
                intent.putExtra("availableid", 1);
                startActivity(intent);
                //showCustomDialog(1, HeaderText_One.getText().toString());
                break;
            case R.id.ll2:
                Intent intent1 = new Intent(getActivity(), WalletDiaplayActivity.class);
                intent1.putExtra("packagename", "MATES");
                String fca1 = SecondCreditAmt.getText().toString();
                String fra1 = SecondRewardAmt.getText().toString();
                String newString1 = fra1.replace("a €", "");
                int fcamt1 = Integer.parseInt(fca1);
                int framt1 = Integer.parseInt(newString1);
                intent1.putExtra("creditamt",framt1 );
                intent1.putExtra("rewardamt", fcamt1);
                intent1.putExtra("availableid", 2);
                startActivity(intent1);
                // showCustomDialog(2, HeaderText_Two.getText().toString());
                break;
            case R.id.ll3:
                Intent intent2 = new Intent(getActivity(), WalletDiaplayActivity.class);
                intent2.putExtra("packagename", "LOVERS");
                String fca2 = ThirdCreditAmt.getText().toString();
                String fra2 = ThirdRewardAmt.getText().toString();
                String newString2 = fra2.replace("a €", "");
                int fcamt2 = Integer.parseInt(fca2);
                int framt2 = Integer.parseInt(newString2);
                intent2.putExtra("creditamt",framt2 );
                intent2.putExtra("rewardamt", fcamt2);
                intent2.putExtra("availableid", 3);
                startActivity(intent2);
                //showCustomDialog(3, HeaderText_Three.getText().toString());
                break;
            case R.id.ll4:
                Intent intent3 = new Intent(getActivity(), WalletDiaplayActivity.class);
                intent3.putExtra("packagename", "GOVOLTERS");
                String fca3 = FourthCreditAmt.getText().toString();
                String fra3 = FourthRewardAmt.getText().toString();
                String newString3 = fra3.replace("a €", "");
                int fcamt3 = Integer.parseInt(fca3);
                int framt3 = Integer.parseInt(newString3);
                intent3.putExtra("creditamt",framt3 );
                intent3.putExtra("rewardamt", fcamt3);
                intent3.putExtra("availableid", 4);
                startActivity(intent3);
                // showCustomDialog(4, HeaderText_Four.getText().toString());
                break;

        }
    }

}