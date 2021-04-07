package com.quadrant.govolt;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.quadrant.interfaces.APIinterface;
import com.quadrant.interfaces.Qrinterface;
import com.quadrant.progressbar.KProgressHUD;

import java.security.Policy;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QR_Fragment extends BaseQRFragment implements View.OnClickListener {
    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    CircleImageView img, img_number;
    private Policy.Parameters parameter;
    private ImageButton flashLight;
    private Camera camera;
    ImageView img_cancel;
    private boolean deviceHasFlash;
    private boolean isFlashLightOn = false;
    private DecoratedBarcodeView qrView;
    private Context context;
    private AlertDialog alertDialog;

    private KProgressHUD progressbar_hud;


    private View view;


    private Qrinterface qrinterface;

    public QR_Fragment() {
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
        view = inflater.inflate(R.layout.activity_qr__frag, container, false);
        context = this.getActivity();


        //   TOKENID = getIntent().getStringExtra("tokenid");
        img = view.findViewById(R.id.image2);
        img_number = view.findViewById(R.id.ic_number);
        //img_cancel = view.findViewById(R.id.ic_cancel);
        // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        img_number.setOnClickListener(this);
        //  img_cancel.setOnClickListener(this);
        //setting the title
        //  toolbar.setTitle("Scansione");

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("Scansione");
        setSupportActionBar(toolbar);*/
        imgclick();
        qrView = view.findViewById(R.id.qr_scanner_view);
        CameraSettings s = new CameraSettings();
        s.setRequestedCameraId(0); // front/back/etc
        qrView.getBarcodeView().setCameraSettings(s);
        qrView.setStatusText("");
        qrView.resume();
        qrView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                final String resultscan = result.toString();
                qrView.pause();
                QrCodeApi(result.toString(), null, new APIinterface() {
                    @Override
                    public void onSuccess() {
                        if (qrinterface != null)
                            qrinterface.bookVehicle(resultscan);
                    }

                    @Override
                    public void onFailure() {
                        error_Alert("Not booked", getContext(), getActivity(), new APIinterface() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailure() {

                            }

                            @Override
                            public void okClick() {
                                qrView.resume();
                            }
                        });

                    }

                    @Override
                    public void okClick() {
                        //not required here
                    }
                });


            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        return view;
    }


    private void imgclick() {

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashLightOn) {
                    isFlashLightOn = false;
                    qrView.setTorchOff();
                } else {
                    isFlashLightOn = true;
                    qrView.setTorchOn();
                }


            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_number:

                if (qrinterface != null) {
                    qrinterface.showVehicleNumberScreen();
                }

                break;
        }

    }

}
