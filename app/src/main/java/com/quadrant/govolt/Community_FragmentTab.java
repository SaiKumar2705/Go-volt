package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.response.CommunityResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Community_FragmentTab extends Fragment {
    private Context context;
    private View view;
    private AlertDialog alertDialog;
    private KProgressHUD progressbar_hud;

    private String TAG = "Community_FragmentTab";
    private String coupon_name;
    private int couponID;
    private TextView _code;
    public final static int QRcodeWidth = 500 ;
    private ImageView _qrCodeImg;
    private  Bitmap bitmap ;
    private ImageView _share_img;
    public Community_FragmentTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.community_frag, container, false);
        context = this.getActivity();

        _code = (TextView)view.findViewById(R.id.code);
        _qrCodeImg = (ImageView)view.findViewById(R.id.qrcode);
        _share_img = (ImageView)view.findViewById(R.id.share_icon);

        community_service();

        _share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ""+coupon_name);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        return  view;
    }

    private void community_service() {
        showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer "+authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<CommunityResponse> resultRes=geo_details.GetResponse(Constants.TOKEN,bearer_authorization);
        resultRes.enqueue(new Callback<CommunityResponse>() {
            @Override
            public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {



                if(!response.isSuccessful()){
                    Log.e(TAG, "--Response code---"+response.code());
                    Log.e(TAG, "--Response ---"+response.body());
                    dismissProgressbar();

                    if(response.code()!=200){


                    }


                }else {
                    Log.e(TAG, "--Success---");


                    if(response.code() == 200){
                        CommunityResponse myresponse = response.body();
                        List<CommunityResponse.Data> array = myresponse.getData();

                        Log.e(TAG, "--22---");
                        for (int k = 0; k < array.size(); k++) {

                            Log.e(TAG, "--333---");
                            CommunityResponse.Coupon coupons = array.get(k).getCoupon();
                            coupon_name = coupons.getName();
                             couponID = array.get(k).getCouponId();


                        }

                        _code.setText(""+coupon_name);

                        try {
                            bitmap = TextToImageEncode(""+coupon_name);

                            _qrCodeImg.setImageBitmap(bitmap);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        dismissProgressbar();
                    }



                }
            }

            @Override
            public void onFailure(Call<CommunityResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }
    private void showProgressbar() {
        progressbar_hud = KProgressHUD.create(this.getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(new DialogInterface.OnCancelListener()
                {
                    @Override public void onCancel(DialogInterface
                                                           dialogInterface)
                    {
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
    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}