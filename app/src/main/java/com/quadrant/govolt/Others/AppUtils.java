package com.quadrant.govolt.Others;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.quadrant.govolt.HomeActivity;
import com.quadrant.govolt.LoginActivity;
import com.quadrant.govolt.R;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.StartRideSession;
import com.quadrant.interfaces.StopRideSession;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.request.ImageselfieRequest;
import com.quadrant.response.AvatharResponse;
import com.quadrant.response.ImageselfieResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.StartRideErrorResponse;
import com.quadrant.response.UserInfoResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by ithelpdesk on 10/17/2015.
 */
public class AppUtils {
    private static String TAG = "AppUtils";
    private static int stautsCode;

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();


        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void error_Alert(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
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
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


        alertDialog.show();


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();

            }
        });
    }
    public static void Alert_information(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_info_alert, viewGroup, false);

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


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();

            }
        });
    }

    public static void Alert_Bookinginformation(String error_message, Context context, AlertDialog alertDialog, Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_info_alert, viewGroup, false);

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


        final AlertDialog finalAlertDialog = alertDialog;
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();

            }
        });
    }

    public static void SaveDataInSharePreference(Context context, UserInfoResponse userinfo) {
        PreferenceUtil.getInstance().saveString(context, Constants.USER_NAME, userinfo.getData().getUser().getUsername());
        PreferenceUtil.getInstance().saveString(context, Constants.NAME, userinfo.getData().getUser().getName());
        PreferenceUtil.getInstance().saveString(context, Constants.SUR_NAME, userinfo.getData().getUser().getSurname());
        PreferenceUtil.getInstance().saveString(context, Constants.ROLE, userinfo.getData().getUser().getRole());
        PreferenceUtil.getInstance().saveString(context, Constants.BIRTH, userinfo.getData().getUser().getBirth());
        PreferenceUtil.getInstance().saveString(context, Constants.SEX, userinfo.getData().getUser().getSex());
        PreferenceUtil.getInstance().saveString(context, Constants.USER_EMAIL, userinfo.getData().getUser().getEmail());
        PreferenceUtil.getInstance().saveString(context, Constants.USER_PWD, userinfo.getData().getUser().getPassword());
        PreferenceUtil.getInstance().saveString(context, Constants.TELEPHONE, userinfo.getData().getUser().getTel());
        PreferenceUtil.getInstance().saveString(context, Constants.RESIDENCY, userinfo.getData().getUser().getResidence());
        PreferenceUtil.getInstance().saveString(context, Constants.ZIPCODE_CAP, userinfo.getData().getUser().getCap());
        PreferenceUtil.getInstance().saveString(context, Constants.COUNTRY, userinfo.getData().getUser().getResidenceCountry());
        PreferenceUtil.getInstance().saveString(context, Constants.STATE, userinfo.getData().getUser().getResidenceState());
        PreferenceUtil.getInstance().saveString(context, Constants.COMPANY_NAME, userinfo.getData().getUser().getCompanyName());
        PreferenceUtil.getInstance().saveString(context, Constants.CREATED_AT, userinfo.getData().getUser().getCreatedAt());
        PreferenceUtil.getInstance().saveString(context, Constants.UPDATED_AT, userinfo.getData().getUser().getUpdatedAt());
        PreferenceUtil.getInstance().saveString(context, Constants.DELETED_AT, userinfo.getData().getUser().getUpdatedAt());

        PreferenceUtil.getInstance().saveString(context, Constants.CITY, userinfo.getData().getUser().getResidenceCity());

        PreferenceUtil.getInstance().saveString(context, Constants.TAXID_CF, userinfo.getData().getUser().getCf());


        List<UserInfoResponse.Avatar> avatar_list =  userinfo.getData().getUser().getAvatar();
        if(avatar_list.size()>0){
            PreferenceUtil.getInstance().saveString(context, Constants.AVATHAR_LOC_IMG, userinfo.getData().getUser().getAvatar().get(0).getLocation());

        }



        List<UserInfoResponse.LicensePlate> licencePlate = userinfo.getData().getUser().getLicensePlate();

        if(licencePlate!=null){
            for(int k=0; k<licencePlate.size(); k++){

                if(licencePlate.get(k).getType()!=null){
                    int type = licencePlate.get(k).getType();

                    if(type == 0){
                        String _location = licencePlate.get(k).getLocation();
                        Log.e(TAG, "Front--img--"+_location);
                        PreferenceUtil.getInstance().saveString(context, Constants.LICENCE_FRONT, _location);

                    }
                    if(type == 1){
                        String _location = licencePlate.get(k).getLocation();
                        PreferenceUtil.getInstance().saveString(context, Constants.LICENCE_BACK, _location);

                    }
                }

            }
        }



    }

    public static void UserStatusApiCall(final Context context) {


        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        UserInformation geo_details = retrofit.create(UserInformation.class);

        Call<UserInfoResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {



                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            UserInfoResponse userinfo = response.body();
                            //Log.e(TAG, "--ServiceID---"+userinfo.getData().getUser().getServiceId());
                            Integer _siteID = userinfo.getData().getUser().getSiteId();

                            Log.e(TAG, "--Update--");


                            AppUtils.SaveDataInSharePreference(context,userinfo);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }

    public static void UploadImageFile_Api(final Context context, String imagePath) {
        ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);


        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

       // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");


        File file = new File(imagePath);

       RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        /*File image = new File("" + (String) imagePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part fileImage = null;
        fileImage = MultipartBody.Part.createFormData("file", image.getName(), requestFile);*/


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<AvatharResponse> resultRes = register_details.GetAvtharResponse(Constants.TOKEN,bearer_authorization, multipartBody);
        resultRes.enqueue(new Callback<AvatharResponse>() {
            @Override
            public void onResponse(Call<AvatharResponse> call, Response<AvatharResponse> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {
                        if (response.code() != 500) {
                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                            LoginErrorResponse error = ErrorUtils.parseError(response);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", error.getError().getDescription());

                            // error_Alertbox(error.getError().getDescription());



                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        AvatharResponse res = response.body();
//                        String image = res.getData().get(0).getLocation();
                        String image = res.getData().getLocation();

                        PreferenceUtil.getInstance().saveString(context, Constants.AVATHAR_LOC_IMG, image);

                    }


                }


            }

            @Override
            public void onFailure(Call<AvatharResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }



    public static int UploadImageFile_Api_Avatar(final Context context, String imagePath) {
        ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);


        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");


        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        /*File image = new File("" + (String) imagePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part fileImage = null;
        fileImage = MultipartBody.Part.createFormData("file", image.getName(), requestFile);*/


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<AvatharResponse> resultRes = register_details.GetAvtharResponse(Constants.TOKEN,bearer_authorization, multipartBody);
        resultRes.enqueue(new Callback<AvatharResponse>() {
            @Override
            public void onResponse(Call<AvatharResponse> call, Response<AvatharResponse> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                        stautsCode = response.code();

                        if (response.code() != 500) {
                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                            LoginErrorResponse error = ErrorUtils.parseError(response);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", error.getError().getDescription());

                            // error_Alertbox(error.getError().getDescription());

                            stautsCode = response.code();


                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        AvatharResponse res = response.body();
//                        String image = res.getData().get(0).getLocation();
                        String image = res.getData().getLocation();

                        PreferenceUtil.getInstance().saveString(context, Constants.AVATHAR_LOC_IMG, image);


                       stautsCode = response.code();

                    }

                }



            }

            @Override
            public void onFailure(Call<AvatharResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
        return stautsCode;

    }

    public static void UploadLicenceFile_Api(final Context context, String imagePath, int type) {
        ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);
        req.setType(type);

        Log.e(TAG, "type---"+type);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

       /* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",imagePath,requestFile);*/

        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetLicenceResponse(Constants.TOKEN,bearer_authorization, multipartBody, type);
        resultRes.enqueue(new Callback<ImageselfieResponse>() {
            @Override
            public void onResponse(Call<ImageselfieResponse> call, Response<ImageselfieResponse> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {
                        if (response.code() != 500) {
                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                            LoginErrorResponse error = ErrorUtils.parseError(response);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", error.getError().getDescription());

                            // error_Alertbox(error.getError().getDescription());
                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        UserStatusApiCall(context);
                    }


                }


            }

            @Override
            public void onFailure(Call<ImageselfieResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        //final String PASSWORD_PATTERN = "(?=.*[A-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[*_%$@])(?!.*[pPoO])\\S{8,}";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    public static void UserProfileStatus(final Context context) {

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        UserInformation geo_details = retrofit.create(UserInformation.class);

        Call<UserInfoResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {

                   if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {


                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        try {
                            UserInfoResponse userinfo = response.body();
                            //Log.e(TAG, "--ServiceID---"+userinfo.getData().getUser().getServiceId());
                            Integer _siteID = userinfo.getData().getUser().getSiteId();

                            Log.e(TAG, "--Update--");

                            // saveDataInSharePref(userinfo);
                            AppUtils.SaveDataInSharePreference(context,userinfo);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }

    public static void callStartStopServiceForStopSession(final Context context, final int _vehicleID) {

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer "+authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StartRideSession geo_details = retrofit.create(StartRideSession.class);


        Call<JSONObject> resultRes=geo_details.GetResponse(Constants.TOKEN,bearer_authorization,_vehicleID);
        resultRes.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {



                if(!response.isSuccessful()){
                    Log.e(TAG, "--Response code---"+response.code());
                    Log.e(TAG, "--Response ---"+response.body());


                    if(response.code()!=200){

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());


                    }


                }else {
                    Log.e(TAG, "--Success---");


                    if(response.code() == 200){

                        stopRide_ServiceCall(context, _vehicleID);

                    }



                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }

    private static void stopRide_ServiceCall(Context context, int _vehicleID) {

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer "+authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        StopRideSession geo_details = retrofit.create(StopRideSession.class);


        Call<JsonObject> resultRes=geo_details.GetResponse(Constants.TOKEN,bearer_authorization,_vehicleID);
        resultRes.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if(!response.isSuccessful()){
                    Log.e(TAG, "--Response code---"+response.code());
                    Log.e(TAG, "--Response ---"+response.body());

                    if(response.code()!=200){

                        StartRideErrorResponse error = ErrorUtilsStartRide.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());



                    }


                }else {
                    Log.e(TAG, "--Success---");



                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }
    public static void UploadDamageFile_Api(final Context context, final String imagePath,int _vehicleID, int damageID) {

        //  showProgressbar();
        final ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);
        req.setType(damageID);

        Log.e(TAG, "type---"+damageID);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

       /* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",imagePath,requestFile);*/

        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetDamageMedia(Constants.TOKEN,bearer_authorization, multipartBody,_vehicleID, damageID);
        resultRes.enqueue(new Callback<ImageselfieResponse>() {
            @Override
            public void onResponse(Call<ImageselfieResponse> call, Response<ImageselfieResponse> response) {

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {
                        if (response.code() != 500) {
                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                            LoginErrorResponse error = ErrorUtils.parseError(response);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", error.getError().getDescription());

                            // error_Alertbox(error.getError().getDescription());
                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {


                    }


                }
                //dismissProgressbar();

            }

            @Override
            public void onFailure(Call<ImageselfieResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());

                //dismissProgressbar();

            }


        });
    }

    //document

    public static void UploadDocumentFile_Api(final Context context, String imagePath, int type) {
        ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);

        req.setType(type);


        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");


        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),body);


        /*File image = new File("" + (String) imagePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part fileImage = null;
        fileImage = MultipartBody.Part.createFormData("file", image.getName(), requestFile);*/


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetDocumentResponse(Constants.TOKEN,bearer_authorization, multipartBody,type);
        resultRes.enqueue(new Callback<ImageselfieResponse>() {
            @Override
            public void onResponse(Call<ImageselfieResponse> call, Response<ImageselfieResponse> response) {


                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {
                        if (response.code() != 500) {
                            //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                            LoginErrorResponse error = ErrorUtils.parseError(response);
                            // â€¦ and use it to show error information

                            // â€¦ or just log the issue like weâ€™re doing :)
                            Log.d("error message", error.getError().getDescription());

                            // error_Alertbox(error.getError().getDescription());



                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                      /*  AvatharResponse res = response.body();
                        String image = res.getData().get(0).getLocation();

                        PreferenceUtil.getInstance().saveString(context, Constants.AVATHAR_LOC_IMG, image);*/

                    }


                }


            }

            @Override
            public void onFailure(Call<ImageselfieResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }

    public static void deleteImage(Uri uri,Context context){
        try{
            File fdelete = new File(getRealPathFromURI(uri,context));
            if (fdelete.exists()) {
                fdelete.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void deleteImage(File target){
        try{
            if (target!=null && target.exists() && target.isFile() && target.canWrite()) {
                target.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



}
