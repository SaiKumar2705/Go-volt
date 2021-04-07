package com.quadrant.govolt;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.response.AvailableItemResponse;
import com.quadrant.responses.PachettiErrorResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WalletDiaplayActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout Rl;
    TextView PackageName, tv_reward_amt, tv_credit_amt, tv_label,tv_discount;
    Button Btn_confirm,Btn_cancel;
    ImageView iv_home_icon;
    private KProgressHUD progressbar_hud;
    private AlertDialog alertDialog;
    int AvailableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaccheti_alert);
        setUpview();
        getdata();
    }
    private void setUpview() {
        Rl = findViewById(R.id.rl);
        PackageName = findViewById(R.id.packagename);
        tv_reward_amt = findViewById(R.id.reward_amt);
        tv_credit_amt = findViewById(R.id.credit_amt);
        iv_home_icon = findViewById(R.id.home_icon);
        tv_label = findViewById(R.id.label);
        tv_discount = findViewById(R.id.discount);
        Btn_confirm = findViewById(R.id.btn_confirm);
        Btn_cancel= findViewById(R.id.btn_cancel);
        Btn_confirm.setOnClickListener(this);
        Btn_cancel.setOnClickListener(this);
        iv_home_icon.setOnClickListener(this);
    }

    private void getdata() {
        Intent intent = getIntent();
        String packagename = intent.getStringExtra("packagename");
        int creditamt = intent.getIntExtra("creditamt", 0);
        int rewardamt = intent.getIntExtra("rewardamt", 0);
        AvailableId = intent.getIntExtra("availableid", 0);
        System.out.println("fbhbfdbdb" + packagename);
        if (packagename.equals("FRIENDS")) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.ic_img_friends);
            Rl.setBackground(drawable);
            PackageName.setText(packagename);
            tv_reward_amt.setText(String.valueOf("€" + " " + rewardamt));
            tv_credit_amt.setText(String.valueOf("€" + " " + creditamt));
            PackageName.setTextColor(Color.parseColor("#f9be63"));
            tv_reward_amt.setTextColor(Color.parseColor("#f9be63"));
            //tv_credit_amt.setTextColor(Color.parseColor("#f9be63"));
            tv_label.setTextColor(Color.parseColor("#f9be63"));
            tv_discount.setTextColor(Color.parseColor("#f9be63"));
            tv_discount.setText("10% di sconto");
            tv_label.setText("Enjoy the ride!");
        } else if (packagename.equals("MATES")) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.ic_img_mates);
            Rl.setBackground(drawable);
            PackageName.setText(packagename);
            tv_reward_amt.setText(String.valueOf("€" + " " + rewardamt));
            tv_credit_amt.setText(String.valueOf("€" + " " + creditamt));
            PackageName.setTextColor(Color.parseColor("#e76f6f"));
            tv_reward_amt.setTextColor(Color.parseColor("#e76f6f"));
           // tv_credit_amt.setTextColor(Color.parseColor("#e76f6f"));
            tv_label.setTextColor(Color.parseColor("#e76f6f"));
            tv_label.setText("Share your energy!");
            tv_discount.setTextColor(Color.parseColor("#e76f6f"));
            tv_discount.setText("15% di sconto");
        } else if (packagename.equals("LOVERS")) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.ic_img_lovers);
            Rl.setBackground(drawable);
            PackageName.setText(packagename);
            tv_reward_amt.setText(String.valueOf("€" + " " + rewardamt));
            tv_credit_amt.setText(String.valueOf("€" + " " + creditamt));
            PackageName.setTextColor(Color.parseColor("#901814"));
            tv_reward_amt.setTextColor(Color.parseColor("#901814"));
         //   tv_credit_amt.setTextColor(Color.parseColor("#901814"));
            tv_label.setTextColor(Color.parseColor("#901814"));
            tv_label.setText("Live the future!");
            tv_discount.setTextColor(Color.parseColor("#901814"));
            tv_discount.setText("20% di sconto");
        } else if (packagename.equals("GOVOLTERS")) {
            Resources res = getResources();
            Drawable drawable = res.getDrawable(R.drawable.ic_img_govolters);
            Rl.setBackground(drawable);
            PackageName.setText(packagename);
            tv_reward_amt.setText(String.valueOf("€" + " " + rewardamt));
            tv_credit_amt.setText(String.valueOf("€" + " " + creditamt));
            PackageName.setTextColor(Color.parseColor("#3cad2b"));
            tv_reward_amt.setTextColor(Color.parseColor("#3cad2b"));
           // tv_credit_amt.setTextColor(Color.parseColor("#3cad2b"));
            tv_label.setTextColor(Color.parseColor("#3cad2b"));
            tv_label.setText("Be the revolution!");
            tv_discount.setTextColor(Color.parseColor("#901814"));
            tv_discount.setText("25% di sconto");
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_confirm:
                PostRequestApi(AvailableId);
                break;
            case R.id.btn_cancel:
               /*Intent I = new Intent(this,Wallet_Activity.class);
               startActivity(I);*/
                break;

            case R.id.home_icon:
               Intent I = new Intent(this,HomeActivity.class);
               startActivity(I);
                break;
        }

    }

    private void PostRequestApi(int availableId) {

        showProgressbar();
        String authorization = PreferenceUtil.getInstance().getString(this, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;
        int _siteID = PreferenceUtil.getInstance().getInt(this, Constants.SITE_ID, 0);
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

    private void confirmPopup() {


        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.complimenti_alert, viewGroup, false);
        Button _done = (Button) dialogView.findViewById(R.id.buttonOk);
        //TextView textView = (TextView)dialogView.findViewById(R.id.title);
//Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
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

    private void error_Alertbox(String error_message) {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_error_alert, viewGroup, false);

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

        tv_error.setText("" + error_message);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

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
}
