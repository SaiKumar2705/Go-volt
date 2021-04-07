package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.load.engine.Resource;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
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

import java.io.IOException;
import java.security.Policy;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QR_NumberActivity extends BaseQRFragment implements View.OnClickListener, Qrinterface {
//    private Camera camera;
    private Context context;
    private View view;
    private boolean isFlashLightOn = false;
    private AlertDialog alertDialog;
    private DecoratedBarcodeView qrView;
    CircleImageView img_Qr;

    private Policy.Parameters parameter;
    private Qrinterface qrinterface;

    EditText et_one, et_two, et_three, et_four, et_five;

    TextView error_text;
    private KProgressHUD progressbar_hud;

    ImageView img_torch;
    private EditText et_numbers;
//    Camera.Parameters p;


//    SurfaceTexture mPreviewTexture;


    public QR_NumberActivity() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        qrinterface = (Qrinterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        qrinterface = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_qr__number, container, false);
        context = this.getActivity();


        et_one = view.findViewById(R.id.text1);
        et_two = view.findViewById(R.id.text2);
        et_three = view.findViewById(R.id.text3);
        et_four = view.findViewById(R.id.text4);
        et_five = view.findViewById(R.id.text5);
        et_numbers = view.findViewById(R.id.et_numbers);
        error_text = view.findViewById(R.id.error_text);
        error_text.setVisibility(View.GONE);

        et_one.setOnClickListener(this);
        et_two.setOnClickListener(this);
        et_three.setOnClickListener(this);
        et_four.setOnClickListener(this);
        et_five.setOnClickListener(this);

        img_torch = view.findViewById(R.id.image2);
        img_Qr = view.findViewById(R.id.ic_qr);
        img_Qr.setOnClickListener(this);

//        camera = Camera.open();
//        p = camera.getParameters();

        qrView = view.findViewById(R.id.qr_scanner_view);
        CameraSettings s = new CameraSettings();
        s.setRequestedCameraId(0); // front/back/etc
        qrView.getBarcodeView().setCameraSettings(s);
        qrView.setStatusText("");
        qrView.resume();

        OnKeyListner();

        img_torchclick();

        return view;
    }

    private void img_torchclick() {


        img_torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashLightOn) {
//
//                    //  Toast.makeText(context,"Off",Toast.LENGTH_SHORT).show();
                    isFlashLightOn = false;
//                    camera = Camera.open();
//                    Camera.Parameters p = camera.getParameters();
//                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                    camera.setParameters(p);
//                    mPreviewTexture = new SurfaceTexture(0);
//                    try {
//                        camera.setPreviewTexture(mPreviewTexture);
//                    } catch (IOException ex) {
//                        // Ignore
//                    }
//                    camera.startPreview();

                     qrView.setTorchOff();
                } else {
                    //Toast.makeText(context,"On",Toast.LENGTH_SHORT).show();


                    isFlashLightOn = true;
                    qrView.setTorchOn();
//                    camera = Camera.open();
//                    Camera.Parameters p = camera.getParameters();
//                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                    camera.setParameters(p);
//                    mPreviewTexture = new SurfaceTexture(0);
//                    try {
//                        camera.setPreviewTexture(mPreviewTexture);
//                    } catch (IOException ex) {
//                        // Ignore
//                    }
//                    camera.startPreview();
                }


            }
        });
    }

    private void OnKeyListner() {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        et_numbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = et_numbers.getText().toString();
                char[] array = text.toCharArray();
                if (array.length==0){
                    et_one.setText("");
                }
                else if (array.length==1){
                    if (et_one.getText().toString().length()==0){
                        et_one.setText(""+array[0]);
                    }else{
                        et_two.setText("");
                    }
                }else if(array.length==2){
                    if (et_two.getText().toString().length()==0){
                        et_two.setText(""+array[1]);
                    }else{
                        et_three.setText("");
                    }

                }else if(array.length==3){
                    if (et_three.getText().toString().length()==0){
                        et_three.setText(""+array[2]);
                    }else{
                        et_four.setText("");
                    }
                }else if(array.length==4){
                    if (et_four.getText().toString().length()==0){
                        et_four.setText(""+array[3]);
                    }else{
                        et_five.setText("");
                    }
                }else if(array.length==5){
                    et_five.setText(""+array[4]);
                    final String resultscan = et_one.getText().toString() + et_two.getText().toString() + et_three.getText().toString() + et_four.getText().toString() + et_five.getText().toString();
//                    if (qrinterface != null)
//                        qrinterface.bookVehicle(resultscan);
                    error_text.setVisibility(View.GONE);
                    QrCodeApi(resultscan.toString(), null, new APIinterface() {
                        @Override
                        public void onSuccess() {
                            if (qrinterface != null)
                                qrinterface.bookVehicle(resultscan);
                        }

                        @Override
                        public void onFailure() {
                            error_text.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void okClick() {
                            //not required here
                        }
                    });

                }
            }

        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ic_qr:

                if (qrinterface != null) {

                    qrinterface.showQrcodeScreen();
                }
                Toast.makeText(getActivity(),"In click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text1:
            case R.id.text2:
            case R.id.text3:
            case R.id.text4:
            case R.id.text5:
                et_numbers.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_numbers,InputMethodManager.SHOW_FORCED);
                break;


        }

    }

    private void QrCodeApi(String resultscan) {


        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, this.getActivity());
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
                            Toast.makeText(context, "Api Not Working", Toast.LENGTH_SHORT).show();

                            error_text.setVisibility(View.VISIBLE);


                            // error_Alertbox(error.getError().getDescription());
                        }
                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        error_text.setVisibility(View.GONE);
                        //Toast.makeText(context, "Api Working", Toast.LENGTH_SHORT).show();


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
    public void showVehicleNumberScreen() {

    }

    @Override
    public void showQrcodeScreen() {

    }

    @Override
    public void bookVehicle(String code) {

    }

    @Override
    public void onResume() {
        super.onResume();



    }
}
