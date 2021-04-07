package com.quadrant.govolt;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.APIinterface;
import com.quadrant.interfaces.Qrinterface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.QrRequest;
import com.quadrant.response.QrResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseQRFragment extends Fragment {
    private KProgressHUD progressbar_hud;
    private String TAG = "BaseQRFragment";

    protected void QrCodeApi(String resultscan, AlertDialog alertDialog, final APIinterface apIinterface) {


        boolean _isInternetAvailable = Constants.isInternetAvailable(getActivity());

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", getActivity(), alertDialog, getActivity());
            return;
        }

        showProgressbar();
        String token = PreferenceUtil.getInstance().getString(getActivity(), Constants.REG_TOKEN, "");
        String authorization = PreferenceUtil.getInstance().getString(getActivity(), Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;
        QrRequest req = new QrRequest();
        req.setCode(resultscan);

        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<QrResponse> resultRes = register_details.GetQrRes(Constants.TOKEN, bearer_authorization, req);
        resultRes.enqueue(new Callback<QrResponse>() {
            @Override
            public void onResponse(Call<QrResponse> call, Response<QrResponse> response) {

                dismissProgressbar();


                if (!response.isSuccessful()) {

                    if (response.code() != 200) {
                        if (response.code() != 500) {
                            if (apIinterface!=null){
                                apIinterface.onFailure();
                            }
                        }
                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        Log.e(TAG, "--Response code---" + response.code());
                        Log.e(TAG, "--Response ---" + response.body());
                        if (apIinterface!=null){
                            apIinterface.onSuccess();
                        }


                        //  Toast.makeText(context, "Api Working", Toast.LENGTH_SHORT).show();


                    }


                }
            }

            @Override
            public void onFailure(Call<QrResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });

    }

    private void dismissProgressbar() {

        progressbar_hud.dismiss();
    }

    private void showProgressbar() {

        progressbar_hud = KProgressHUD.create(getActivity())
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

    public void error_Alert(String error_message, Context context, Activity activity, final APIinterface apIinterface) {
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
        AlertDialog alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
                if (apIinterface!=null){
                    apIinterface.okClick();
                }

            }
        });
    }
}
