package com.quadrant.govolt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.BlurView;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.GetAdditionalData;
import com.quadrant.interfaces.RegistrationDocumentInterface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.ImageselfieRequest;
import com.quadrant.request.ProfilePatentRequest;
import com.quadrant.response.ImageselfieResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.PatentResponse;
import com.quadrant.response.ProfilePatentPostResponse;
import com.quadrant.response.RegistrationDocumentResponse;
import com.quadrant.response.UserInfoResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Environment;
import java.util.Locale;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class Profile_Patent_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private TextView date_picker_one, date_picker_two;
    private AlertDialog alertDialog;
    private View dialogView;
    private ImageView btn_done, img1, img2;
    private DatePicker dpDate;
    private EditText first_editbox, second_editbox, third_editbox, holder_name, nationality_et;
    private LinearLayout first_ll, second_ll, third_ll;
    private Context context;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView Imageview_One, ImageView_Two;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private CircleImageView _profileImg;
    private ImageView ok_btn;
    private String TAG = "Profile_Patent_Activity";
    private File finalFile;
    private String imagePath;
    private KProgressHUD progressbar_hud;
    private String _front_img = "";
    private String _back_img = "";
    String FirstDate, FirstDate_Another, SecondDate, SecondDateAnother;
    private int myear;
    private int mmonth;
    private int mday;
    private Uri imageUri1,imageUri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_patent);
        context = this;
        setUpView();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Patente");
        ImageView back = (ImageView) findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView _homeIcon = (ImageView) findViewById(R.id.home_icon);
        _homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile_Patent_Activity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        firstEditboxClicked();
        secondEditboxClicked();
        thirdEditboxClicked();
        HoldernameEditboxClicked();
        NationalityEditboxclicked();

        //Note: ShowSaved is always on Top of CallAdditionalGetService
        try {

            ShowSavedData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CallAdditionalGetService();

        Log.e("Suresh", "===OnCreate--");


        //--------If Using this Camera Captured image is no displaying

        showFirstImage();

        showsecondImage();


    }

    private void NationalityEditboxclicked() {
        nationality_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    nationality_et.setBackgroundResource(R.drawable.ic_text_feild_blur);


                } else {


                    nationality_et.setBackgroundResource(R.drawable.ic_text_feild_singleline);


                }
            }
        });
    }


    private void showFirstImage() {
        _front_img = PreferenceUtil.getInstance().getString(context, Constants.LICENCE_FRONT, "");
        Log.e("SURESH", "_front_img--" + _front_img);
        if (_front_img != null || !_front_img.equalsIgnoreCase("")) {
            Glide.with(context)
                    .load(_front_img) // or URI/path
                    .placeholder(R.drawable.ic_camera_icon_one)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.IMMEDIATE)
                    .error(R.drawable.ic_camera_icon_one)
                    .skipMemoryCache(false)
                    .into(Imageview_One);
        }

    }

    private void showsecondImage() {
        _back_img = PreferenceUtil.getInstance().getString(context, Constants.LICENCE_BACK, "");
        if (_back_img != null || !_back_img.equalsIgnoreCase("")) {
            Glide.with(context)
                    .load(_back_img) // or URI/path
                    .placeholder(R.drawable.ic_camera_icon_one)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.IMMEDIATE)
                    .error(R.drawable.ic_camera_icon_one)
                    .skipMemoryCache(false)
                    .into(ImageView_Two);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String _avathar_loc_img = PreferenceUtil.getInstance().getString(context, Constants.AVATHAR_LOC_IMG, "");
        Glide.with(context)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.ic_navigation_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_navigation_icon)
                .skipMemoryCache(false)
                .into(_profileImg);


    }

    private void CallAdditionalGetService() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Profile_Patent_Activity.this);
            return;
        }

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        // String bearer_authorization = "Bearer " + TokenId;
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        GetAdditionalData geo_details = retrofit.create(GetAdditionalData.class);
        Call<PatentResponse> resultRes = geo_details.GetResponseAddingReg(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<PatentResponse>() {
            @Override
            public void onResponse(Call<PatentResponse> call, Response<PatentResponse> response) {


                if (!response.isSuccessful()) {

                    if (response.code() != 200) {


                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {


                        PatentResponse res = response.body();
                        String guy = res.getData().getGuy();
                        String driving_licence_no = res.getData().getDrivingLicenseNumber();

                        String _driving_release_date = res.getData().getDrivingReleaseDate();
                        String _driving_expiry_date = res.getData().getDrivingExpiryDate();
                        String _driving_authorities = res.getData().getDrivingAuthorities();

                        Log.e("SURESH", "DATA--" + guy + ", " + driving_licence_no + "," + _driving_authorities);


                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_Tipo_Guy, guy);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_DRIVING_LICENCE_NO, driving_licence_no);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_RELICA_ISSUEDON, _driving_authorities);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_SCENDENA_EXP_DATE, _driving_expiry_date);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_RILICA_RELEASE, _driving_release_date);


                        try {
                            ShowSavedData();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                }


            }

            @Override
            public void onFailure(Call<PatentResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                //  dismissProgressbar();

            }


        });
    }

    private void ShowSavedData() throws ParseException {

        //---It is saved in Patent_Registration.java class

        String guy = PreferenceUtil.getInstance().getString(context, Constants.PATENT_Tipo_Guy, "");
        String _driving_licence_no = PreferenceUtil.getInstance().getString(context, Constants.PATENT_DRIVING_LICENCE_NO, "");
        String _driving_authorities = PreferenceUtil.getInstance().getString(context, Constants.PATENT_RELICA_ISSUEDON, "");
        SecondDate = PreferenceUtil.getInstance().getString(context, Constants.PATENT_SCENDENA_EXP_DATE, "");


        FirstDate = PreferenceUtil.getInstance().getString(context, Constants.PATENT_RILICA_RELEASE, "");
        System.out.println("ddhdhdhhdh:::"+FirstDate);
        // NEW

        String holder_names = PreferenceUtil.getInstance().getString(context, Constants.PATENT_HOLDER_NAME, "");
        String nationality = PreferenceUtil.getInstance().getString(context, Constants.PATENT_NATIONALITY, "");
        //PreferenceUtil.getInstance().saveString(context, Constants.PATENT_NATIONALITY, "");


        Log.e("SURESH", "_driving_authorities===" + _driving_authorities);
        Log.e("SURESH", "_driving_release_date===" + FirstDate);

        if (guy != null) {
            first_editbox.setText("" + guy);
        }

        if (holder_names != null) {
            holder_name.setText("" + holder_names);
        }

        if (nationality != null) {
            nationality_et.setText("" + nationality);
        }
        if (_driving_licence_no != null) {
            second_editbox.setText("" + _driving_licence_no);
        }

        if (FirstDate != null) {
/*

            String dateStr = FirstDate;
            System.out.println("ghhhhghg:::" + dateStr);
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = (Date) formatter.parse(dateStr);
            System.out.println(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            System.out.println("formatedDate : " + formatedDate);
*/

            date_picker_one.setText("" + FirstDate);
        }
        if (SecondDate != null) {

           /* String dateStr = SecondDate;
            System.out.println("ghhhhghg:::" + dateStr);
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = (Date) formatter.parse(dateStr);
            System.out.println(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            System.out.println("formatedDate : " + formatedDate);*/

            date_picker_two.setText("" + SecondDate);
        }

        if (_driving_authorities != null) {
            third_editbox.setText("" + _driving_authorities);
        }


    }


    private void firstEditboxClicked() {
        first_editbox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    first_ll.setBackgroundResource(R.drawable.ic_text_feild_blur);


                } else {


                    first_ll.setBackgroundResource(R.drawable.ic_text_feild_singleline);


                }
            }
        });
    }

    private void HoldernameEditboxClicked() {
        holder_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    holder_name.setBackgroundResource(R.drawable.ic_text_feild_blur);


                } else {


                    holder_name.setBackgroundResource(R.drawable.ic_text_feild_singleline);


                }
            }
        });
    }

    private void secondEditboxClicked() {
        second_editbox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    second_ll.setBackgroundResource(R.drawable.ic_text_feild_blur);


                } else {


                    second_ll.setBackgroundResource(R.drawable.ic_text_feild_singleline);


                }
            }
        });
    }

    private void thirdEditboxClicked() {
        third_editbox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    third_ll.setBackgroundResource(R.drawable.ic_text_feild_blur);


                } else {


                    third_ll.setBackgroundResource(R.drawable.ic_text_feild_singleline);


                }
            }
        });
    }

    private void setUpView() {
        Imageview_One = (ImageView) findViewById(R.id.ic_cameraone);
        Imageview_One.setOnClickListener(this);
        ImageView_Two = (ImageView) findViewById(R.id.ic_cameratwo);
        ImageView_Two.setOnClickListener(this);

        back = (ImageView) findViewById(R.id.back_img);
        date_picker_one = (TextView) findViewById(R.id.date_tv_one);
        date_picker_two = (TextView) findViewById(R.id.date_tv_two);
        first_editbox = (EditText) findViewById(R.id.first_edit);
        holder_name = (EditText) findViewById(R.id.holdername);
        nationality_et = (EditText) findViewById(R.id.nationality);
        second_editbox = (EditText) findViewById(R.id.second_edit);
        third_editbox = (EditText) findViewById(R.id.third_edit);

        ok_btn = (ImageView) findViewById(R.id.ok_img);
        ok_btn.setOnClickListener(this);

        img1 = (ImageView) findViewById(R.id.imageone);
        img2 = (ImageView) findViewById(R.id.imagetwo);
        _profileImg = (CircleImageView) findViewById(R.id.profile_image);


        first_ll = (LinearLayout) findViewById(R.id.first_layout);
        second_ll = (LinearLayout) findViewById(R.id.second_layout);
        third_ll = (LinearLayout) findViewById(R.id.third_layout);

        date_picker_one.setOnClickListener(this);
        date_picker_two.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.imageone:
                DatePickerOne();
                break;
            case R.id.imagetwo:
                DatePickerTwo();
                break;
            case R.id.ic_cameraone:

                if (_front_img.equalsIgnoreCase("")) {
                    UploadCameraOne();
                } else {
                    DeleteFrontImage();
                }

                break;
            case R.id.ic_cameratwo:


                if (_back_img.equalsIgnoreCase("")) {
                    UploadCameraTwo();
                } else {
                    DeleteBackImage();
                }

                break;
            case R.id.ok_img:
                String guy = first_editbox.getText().toString();
                String driving_number = second_editbox.getText().toString();
                String driving_release_date = date_picker_one.getText().toString();
                String driving_expire_date = date_picker_two.getText().toString();
                String authority = third_editbox.getText().toString();
                String holdernames = holder_name.getText().toString();
                String nationality = nationality_et.getText().toString();
                if (guy.length() > 0) {
                    if (holdernames.length() > 0) {
                        if (nationality.length() > 0) {
                            if (driving_number.length() > 0) {
                                if (driving_release_date.length() > 0) {
                                    if (driving_expire_date.length() > 0) {
                                        if (authority.length() > 0) {

                                            ServiceCall();
                                        } else {
                                            AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                                        }
                                    } else {
                                        AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                                    }
                                } else {
                                    AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                                }
                            } else {
                                AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                            }
                        } else {
                            AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                        }

                    } else {
                        AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                    }

                } else {
                    AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, Profile_Patent_Activity.this);

                }
                break;
        }
    }

    private void ServiceCall() {
        String _guy = first_editbox.getText().toString();
        String _driving_number = second_editbox.getText().toString();
        //saik


        String _driving_release_date = FirstDate_Another;
        String _driving_expire_date = SecondDateAnother;
        String _authority = third_editbox.getText().toString();
        String _holdername = holder_name.getText().toString();
        String _nationality = nationality_et.getText().toString();

        try {
            PostData(_holdername, _nationality, _guy, _driving_number, _driving_release_date, _driving_expire_date, _authority);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void PostData(String holdername, String nationality, String guy, String driving_number, String driving_release_date, String driving_expire_date, String authority) throws ParseException {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Profile_Patent_Activity.this);
            return;
        }
        showProgressbar();
        ProfilePatentRequest request = new ProfilePatentRequest();
        request.setDriving_lincense_holder_name(holdername);
        request.setDriving_license_nationality(nationality);
        request.setGuy(guy);
        request.setDriving_license_number(driving_number);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (driving_release_date != null) {
           /* Date date = (Date) formatter.parse(driving_release_date);
            System.out.println("Todayis...." + date.getTime());*/
            request.setDriving_release_date(driving_release_date);
        }/* else {

            request.setDriving_release_date(FirstDate_Another);

            driving_release_date = date_picker_one.getText().toString();
        }*/

        if (driving_expire_date != null) {
           /* DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = (Date) formatter1.parse(driving_expire_date);
            System.out.println("Todayis...." + date1.getTime());*/
            request.setDriving_expiry_date(driving_expire_date);

        } /*else {

            request.setDriving_expiry_date(SecondDateAnother);

            driving_expire_date = date_picker_two.getText().toString();

        }*/
        request.setDriving_authorities(authority);
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        // String bearer_authorization = "Bearer " + TokenId;
        String bearer_authorization = "Bearer " + authorization;
        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);
        RequestInterface geo_details = retrofit.create(RequestInterface.class);
        Call<ProfilePatentPostResponse> resultRes = geo_details.GetResponsProfilePatentPost(Constants.TOKEN, bearer_authorization, request);
        resultRes.enqueue(new Callback<ProfilePatentPostResponse>() {
            @Override
            public void onResponse(Call<ProfilePatentPostResponse> call, Response<ProfilePatentPostResponse> response) {
                if (!response.isSuccessful()) {

                    if (response.code() != 200) {

                    }

                } else {
                    // Log.e(TAG, "--Success---");
                    if (response.code() == 200) {


                        //RefreshOnCreate();
                    }


                }
                dismissProgressbar();


            }

            @Override
            public void onFailure(Call<ProfilePatentPostResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }


    private void UploadCameraTwo() {
        if (ContextCompat.checkSelfPermission(Profile_Patent_Activity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(Profile_Patent_Activity.this, ALLOW_KEY)) {
                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(Profile_Patent_Activity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Profile_Patent_Activity.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Profile_Patent_Activity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {

            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                checkIfAlreadyhavePermission();
            }

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Back Photo");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri2 = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
            startActivityForResult(intent, 2);


//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
//            startActivityForResult(takePictureIntent, 2);
        }


    }

    private void UploadCameraOne() {


        if (ContextCompat.checkSelfPermission(Profile_Patent_Activity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(Profile_Patent_Activity.this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(Profile_Patent_Activity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Profile_Patent_Activity.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Profile_Patent_Activity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {

            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                checkIfAlreadyhavePermission();
            }

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Front Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri1 = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
            startActivityForResult(intent, 1);

//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//            startActivityForResult(takePictureIntent, 1);
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bitmap mphoto1 = (Bitmap) data.getExtras().get("data");
//            Log.e("SURESH", "FRONT--" + mphoto1.toString());
//            Imageview_One.setImageBitmap(mphoto1);
            Bitmap mphoto1 = null;
            Bitmap compressedBitmap = null;
            try {
                mphoto1 = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri1);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mphoto1.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                compressedBitmap = rotateImageIfRequired(this,compressedBitmap,imageUri1);
                Imageview_One.setImageBitmap(compressedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            finalFile = new File(getRealPathFromURI(imageUri1));

            Uri tempUri = getImageUri(getApplicationContext(), compressedBitmap);
//
//            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

            imagePath = finalFile.getPath();

            System.out.println("ImagePath---" + imagePath);


            int type = 0;

            AppUtils.deleteImage(imageUri1,this);

            UploadLicenceFrentFile_Api(context, imagePath, type);
            // UserStatusApiCallForFirstImg(context);


        }
        if (requestCode == 2 && resultCode == RESULT_OK) {

//            Bitmap mphoto2 = (Bitmap) data.getExtras().get("data");
//            Log.e("SURESH", "BACK--" + mphoto2.toString());
//
//            ImageView_Two.setImageBitmap(mphoto2);
//
//            Uri tempUri = getImageUri(getApplicationContext(), mphoto2);

            Bitmap mphoto1 = null;
            Bitmap compressedBitmap = null;
            try {
                mphoto1 = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri2);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mphoto1.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                compressedBitmap = rotateImageIfRequired(this,compressedBitmap,imageUri2);
                ImageView_Two.setImageBitmap(compressedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri tempUri = getImageUri(getApplicationContext(), compressedBitmap);
//            finalFile = new File(getRealPathFromURI(imageUri2));
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

            imagePath = finalFile.getPath();

            System.out.println("ImagePath---" + imagePath);


            int type = 1;

            AppUtils.deleteImage(imageUri2,this);

            UploadLicenceBackFile_Api(context, imagePath, type);

            // UserStatusApiCallForSecond(context);

        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkIfAlreadyhavePermission() {
        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return true;
        } else

            return false;

    }

    private void DatePickerOne() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(Profile_Patent_Activity.this).inflate(R.layout.dp_profile_one, viewGroup, false);

        dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);
        // init
       //    dpDate.setMaxDate(System.currentTimeMillis());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        dpDate.setMaxDate(calendar.getTimeInMillis());


        btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);


        String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";
        // pwd_alert_title.setText(str);

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


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                FirstDate_Another = (dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());

                SimpleDateFormat dfs = new SimpleDateFormat("d/M/yyyy");
                String formattedDate = dfs.format(c.getTime());

                System.out.println("firstdate:::" + FirstDate);
                System.out.println("currentDate:::" + formattedDate);

                Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                DateFormat df = new SimpleDateFormat("MM/yy");
                FirstDate = df.format(cal.getTime());
                date_picker_one.setText(FirstDate_Another);

                /*if(SecondDate!=null){
                    if(!SecondDate.equalsIgnoreCase("")){

                        if(FirstDate.equalsIgnoreCase(SecondDate)){
                            date_picker_one.setText("");
                        }
                    }
                }*/



              /*  if(SecondDate!=null){
                    if (SecondDate != null) {
                        if (!FirstDate.equalsIgnoreCase("")) {


                            if (FirstDate.compareTo(SecondDate) > 0) {
                                //Log.i("app", "Date1 is after Date2");



                                Toast.makeText(getApplicationContext(),"release date should be befor the expiry date",Toast.LENGTH_SHORT).show();
                                Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                                DateFormat df = new SimpleDateFormat("MM/yy");
                                String date = df.format(cal.getTime());
                                date_picker_one.setText(date);



                            } else if (SecondDate.compareTo(FirstDate) < 0) {
                                //Log.i("app", "Date1 is before Date2");

                                date_picker_one.setText("");

                            } else if (SecondDate.equalsIgnoreCase(FirstDate)) {
                                date_picker_one.setText("");

                            }
                        }
                    } else {
                        // Valida_fino.setBackgroundResource(R.drawable.ic_active_edittext);

                    }

                }  else{

                  //  Toast.makeText(getApplicationContext(),"Please select the release date first",Toast.LENGTH_SHORT).show();
                    date_picker_two.setText("");

                }
*/


             /*   Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                DateFormat df = new SimpleDateFormat("MM/yy");
                String date = df.format(cal.getTime());
                date_picker_one.setText(date);

                date_picker_one.setTextColor(Color.BLACK);*/
                //ServiceCall();

                //  Toast.makeText(getApplicationContext(),"release date should be befor the expiry date",Toast.LENGTH_SHORT).show();

                // date_picker_two.setText("");

            }
        });


    }

    private void DatePickerTwo() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(Profile_Patent_Activity.this).inflate(R.layout.dp_profile_two, viewGroup, false);

        dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);
        // init
        dpDate.setMinDate(System.currentTimeMillis());

        btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);


        String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";
        // pwd_alert_title.setText(str);

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


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                SecondDateAnother = (dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear();

                Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                DateFormat df = new SimpleDateFormat("MM/yy");
                SecondDate = df.format(cal.getTime());
                date_picker_two.setText(SecondDateAnother);

                /*if(FirstDate!=null){
                    if(!FirstDate.equalsIgnoreCase("")){

                        if(SecondDate.equalsIgnoreCase(FirstDate)){
                            date_picker_two.setText("");
                        }
                    }
                }*/

/*
                Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                DateFormat df = new SimpleDateFormat("MM/yy");
                String date = df.format(cal.getTime());
                date_picker_two.setText(date);


                date_picker_two.setTextColor(Color.BLACK);*/

                // ServiceCall();

            }
        });

    }

    private void DeleteFrontImage() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(Profile_Patent_Activity.this).inflate(R.layout.imagepopup, viewGroup, false);

        ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

        ImageView remove = (ImageView) dialogView.findViewById(R.id.remove);

        ImageView right = (ImageView) dialogView.findViewById(R.id.right);

        Glide.with(context)
                .load(_front_img) // or URI/path
                .placeholder(R.drawable.ic_camera_icon_one)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_camera_icon_one)
                .skipMemoryCache(false)
                .into(img);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                _front_img = "";

                PreferenceUtil.getInstance().saveString(context, Constants.LICENCE_FRONT, _front_img);

                Imageview_One.setImageResource(R.drawable.ic_camera_icon_one);

            }
        });


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

    }

    private void

    DeleteBackImage() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(Profile_Patent_Activity.this).inflate(R.layout.imagepopup, viewGroup, false);

        ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

        ImageView remove = (ImageView) dialogView.findViewById(R.id.remove);

        ImageView right = (ImageView) dialogView.findViewById(R.id.right);

        Glide.with(context)
                .load(_back_img) // or URI/path
                .placeholder(R.drawable.ic_camera_icon_one)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.ic_camera_icon_one)
                .skipMemoryCache(false)
                .into(img);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                _back_img = "";

                PreferenceUtil.getInstance().saveString(context, Constants.LICENCE_BACK, _back_img);

                ImageView_Two.setImageResource(R.drawable.ic_camera_icon_one);

            }
        });


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

    }

    public static void saveToPreferences(Context context, String key,
                                         Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(Profile_Patent_Activity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(Profile_Patent_Activity.this);

                    }
                });
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult
            (int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale
                                        (this, permission);
                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(Profile_Patent_Activity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request

        }
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
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
                        Toast.makeText(context, "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();
                    }
                });
        progressbar_hud.show();
    }


    private void UploadLicenceFrentFile_Api(final Context context, String imagePath, int type) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Profile_Patent_Activity.this);
            return;
        }

        showProgressbar();
        final ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);
        req.setType(type);

        Log.e(TAG, "type---" + type);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

       /* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",imagePath,requestFile);*/

        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), body);


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetLicenceResponse(Constants.TOKEN, bearer_authorization, multipartBody, type);
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

                        _front_img = response.body().getData().getLocation();

                        PreferenceUtil.getInstance().saveString(context, Constants.LICENCE_FRONT, _front_img);

//                        showFirstImage();

                    }


                }
                dismissProgressbar();

            }

            @Override
            public void onFailure(Call<ImageselfieResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());

                dismissProgressbar();

            }


        });
    }

    private void UploadLicenceBackFile_Api(final Context context, String imagePath, int type) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Profile_Patent_Activity.this);
            return;
        }
        showProgressbar();

        final ImageselfieRequest req = new ImageselfieRequest();

        // req.setFile(firstbaseimage);
        req.setFile(imagePath);
        req.setType(type);

        Log.e(TAG, "type---" + type);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

       /* RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),"");

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",imagePath,requestFile);*/

        File file = new File(imagePath);

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), body);


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<ImageselfieResponse> resultRes = register_details.GetLicenceResponse(Constants.TOKEN, bearer_authorization, multipartBody, type);
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

                        _back_img = response.body().getData().getLocation();

                        PreferenceUtil.getInstance().saveString(context, Constants.LICENCE_BACK, _back_img);

//                        showsecondImage();

                    }


                }

                dismissProgressbar();


            }

            @Override
            public void onFailure(Call<ImageselfieResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

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


}
