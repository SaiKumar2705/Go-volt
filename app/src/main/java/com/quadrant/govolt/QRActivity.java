package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.security.Policy;
import java.util.List;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.Qrinterface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.LoginRequest;
import com.quadrant.request.QrRequest;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.LoginResonse;
import com.quadrant.response.QrResponse;

import java.security.Policy;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QRActivity extends AppCompatActivity implements Qrinterface, View.OnClickListener {
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    CircleImageView img, img_number;
    private Policy.Parameters parameter;
    private ImageButton flashLight;
    private Camera camera;

    private boolean deviceHasFlash;
    private boolean isFlashLightOn = false;
    private DecoratedBarcodeView qrView;

    private Context context;
    private AlertDialog alertDialog;

    private KProgressHUD progressbar_hud;

    private String TAG = "QRActivity";

    private TextView Qr_Label;

    private ImageView Img_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        context = this;
        Qr_Label = findViewById(R.id.toolbar_label);
        Img_cancel = findViewById(R.id.ic_cancel);
        Img_cancel.setOnClickListener(this);
        ShowFragment(new QR_Fragment());
    }

    @Override
    public void showVehicleNumberScreen() {
        Qr_Label.setText("Inserisci il codice");
        ShowFragment(new QR_NumberActivity());
    }

    @Override
    public void showQrcodeScreen() {
        Qr_Label.setText("Scansione");
        ShowFragment(new QR_Fragment());
    }

    @Override
    public void bookVehicle(String code) {
//        QrCodeApi(code);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void ShowFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment).commit();
    }

    private void QrCodeApi(String resultscan) {


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, this);
            return;
        }

        showProgressbar();
        String token = PreferenceUtil.getInstance().getString(context, Constants.REG_TOKEN, "");
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
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

                        }
                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        Log.e(TAG, "--Response code---" + response.code());
                        Log.e(TAG, "--Response ---" + response.body());

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();


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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ic_cancel:

//                Intent I = new Intent(this, HomeActivity.class);
//                startActivity(I);
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (f != null && f instanceof QR_NumberActivity)
            showQrcodeScreen();
        else
            super.onBackPressed();

    }
}
