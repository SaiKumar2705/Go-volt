package com.quadrant.govolt.Others;

import android.content.Context;
import android.util.Log;

import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.response.UserInfoResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SessionRideServices {
    private static String TAG = "SessionRideServices";
    public static int statusCode;



    public static int BookVehicle(Context context) {

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface geo_details = retrofit.create(RequestInterface.class);

        Call<JSONObject> resultRes = geo_details.GetVehicleBookResponse(Constants.TOKEN, bearer_authorization,21);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());

                    if (response.code() != 200) {

                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            JSONObject res = response.body();

                            Log.e(TAG, "--Update--"+res.toString());



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }else if(response.code() == 404){

                    }


                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });

        return statusCode;
    }
}
