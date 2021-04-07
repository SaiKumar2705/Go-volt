package com.quadrant.govolt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adyen.checkout.core.CheckoutException;
import com.adyen.checkout.core.PaymentMethodHandler;
import com.adyen.checkout.core.PaymentResult;
import com.adyen.checkout.core.StartPaymentParameters;
import com.adyen.checkout.core.handler.StartPaymentParametersHandler;
import com.adyen.checkout.ui.CheckoutController;
import com.adyen.checkout.ui.CheckoutSetupParameters;
import com.adyen.checkout.ui.CheckoutSetupParametersHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.GetAdditionalData;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.ImageselfieRequest;
import com.quadrant.request.PaymentSessionRequest;
import com.quadrant.request.PaymentVerifyRequest;
import com.quadrant.response.AvatharResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.PatentResponse;
import com.quadrant.response.PaymentSessionResponse;
import com.quadrant.response.PaymentVerifyResponse;
import com.quadrant.response.UserInfoResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileScreen_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView menu_img;
    private LinearLayout _first_layout, _second_layout, _third_layout, _fourth_layout;
    private TextView title, _profile_patent_secondText, _profile_patent_date;
    private String fullname, address, telephone, email, userfullname;
    private TextView _username, _userlocation, _telephone, _email, _usernamefull;
    private String TAG = "ProfileScreen_Activity";
    private KProgressHUD progressbar_hud;
    private ImageView Imageview_pro;
    private static final int CAMERA_REQUEST = 1888;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private CircleImageView _profileImg;
    private String imagePath;
    private String _driving_authorities, _driving_expiry_date;
    private  String _avathar_loc_img;
    private AlertDialog alertDialog;
    public static final int REQUEST_CODE_CHECKOUT = 101;
    private Uri imageUri;
    private boolean profileImageChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        setUpView();

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Il mio profilo");

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
                Intent i = new Intent(ProfileScreen_Activity.this, HomeActivity.class);
                startActivity(i);
            }
        });


        Log.e(TAG, "==onCreate--");


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e(TAG, "==onStart--");

    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.e(TAG, "==onResume--");
        if (!profileImageChanged)
            UserInformation();

    }


    @Override
    protected void onRestart() {
        super.onRestart();


        Log.e(TAG, "==onRestart--");
    }


    private void setValues() {
        String name = PreferenceUtil.getInstance().getString(context, Constants.NAME, "");
        String surname = PreferenceUtil.getInstance().getString(context, Constants.SUR_NAME, "");
        fullname =  surname+ " " + name;

        address = PreferenceUtil.getInstance().getString(context, Constants.RESIDENCY, "");
        telephone = PreferenceUtil.getInstance().getString(context, Constants.TELEPHONE, "");
        email = PreferenceUtil.getInstance().getString(context, Constants.USER_EMAIL, "");
        String emaillD;
        if (email.length() >= 13) {
            emaillD = email.substring(0, 13)+ "...";

        } else {

            emaillD = email;

        }
        userfullname = PreferenceUtil.getInstance().getString(context, Constants.NAME, "");

        _driving_authorities = PreferenceUtil.getInstance().getString(context, Constants.PATENT_RELICA_ISSUEDON, "");
        _driving_expiry_date = PreferenceUtil.getInstance().getString(context, Constants.PATENT_SCENDENA_EXP_DATE, "");


        Log.e("SURESH", "userNemme--" + fullname + "  email==" + email);

        if (fullname != null) {
            _username.setText("" + fullname);

        }
        if (address != null) {
            _userlocation.setText("" + address);
        }
        if (telephone != null) {
            _telephone.setText("" + telephone);
        }
        if (email != null) {
            _email.setText("" + emaillD);
        }
        _avathar_loc_img = PreferenceUtil.getInstance().getString(context, Constants.AVATHAR_LOC_IMG, "");
        Glide.with(context)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.menu_govolt_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.menu_govolt_icon)
                .skipMemoryCache(false)
                .into(_profileImg);

        CallAdditionalGetService();
        Log.e(TAG, "_driving_authorities--" + _driving_authorities);


    }

    private void setUpView() {
        // Imageview_pro = (ImageView) findViewById(R.id.profile_image);
        _first_layout = (LinearLayout) findViewById(R.id.first_layout);
        _second_layout = (LinearLayout) findViewById(R.id.second_layout);
        _third_layout = (LinearLayout) findViewById(R.id.third_layout);
        _fourth_layout = (LinearLayout) findViewById(R.id.fourth_layout);
        _profileImg = (CircleImageView) findViewById(R.id.profile_image);
        _profileImg.setOnClickListener(this);
        _profile_patent_secondText = (TextView) findViewById(R.id.patent_secondtext);
        _profile_patent_date = (TextView) findViewById(R.id.patent_date);


        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Modifica Profilo");

        _first_layout.setOnClickListener(this);
        _second_layout.setOnClickListener(this);
        _third_layout.setOnClickListener(this);
        _fourth_layout.setOnClickListener(this);

        _username = (TextView) findViewById(R.id.profi_user);
        _userlocation = (TextView) findViewById(R.id.userlocation);
        _telephone = (TextView) findViewById(R.id.telephone);
        _email = (TextView) findViewById(R.id.email_pro);
        _usernamefull = (TextView) findViewById(R.id.profi_full);
        findViewById(R.id.ic_camera_small).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.first_layout:

                Intent i = new Intent(context, ProfileEdit_Activity.class);
                startActivity(i);
               /* Intent i = new Intent(context, DamageCheckActivity.class);
                startActivity(i);*/
                break;
            case R.id.second_layout:
                Intent it = new Intent(context, Profile_Account.class);
                startActivity(it);
                break;

            case R.id.third_layout:
                Intent in = new Intent(context, Profile_Patent_Activity.class);
                startActivity(in);
                break;
            case R.id.fourth_layout:
              /*  Intent ni = new Intent(context, ProfileMasterCard.class);

                startActivity(ni);*/

                showProgressbar();
                CheckoutController.startPayment(ProfileScreen_Activity.this, new CheckoutSetupParametersHandler() {
                    @Override
                    public void onRequestPaymentSession(@NonNull CheckoutSetupParameters checkoutSetupParameters) {

                        // Forward to your own server and request the payment session from Adyen with the given CheckoutSetupParameters.
                        String returnUrl = checkoutSetupParameters.getReturnUrl();
                        String token = checkoutSetupParameters.getSdkToken();
                        String channel = "Android";

                        PaymentSessionRequest paymentSessionRequest = new PaymentSessionRequest();
                        paymentSessionRequest.setChannel(channel);
                        paymentSessionRequest.setToken(token);
                        paymentSessionRequest.setReturnUrl(returnUrl);


                        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                        RequestInterface register_details = retrofit.create(RequestInterface.class);
                        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
                        String bearer_authorization = "Bearer " + authorization;

                        Call<PaymentSessionResponse> resultRes = register_details.getPaymentSession(Constants.TOKEN, bearer_authorization, paymentSessionRequest);
                        resultRes.enqueue(new Callback<PaymentSessionResponse>() {
                            @Override
                            public void onResponse(Call<PaymentSessionResponse> call, Response<PaymentSessionResponse> response) {
                                dismissProgressbar();
                                if (!response.isSuccessful()) {
                                    Log.e(TAG, "--Response code---" + response.code());
                                    Log.e(TAG, "--Response ---" + response.body());
                                    if (response.code() != 200) {
                                        if (response.code() != 500) {
                                            Toast.makeText(getApplicationContext(), "C'è qualche errore", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "--Success---");
                                    if (response.code() == 200) {
                                        PaymentSessionResponse res = response.body();
                                        String encodedPaymentSession = null;
                                        if (res != null && res.getData() != null && res.getData().getPaymentSession() != null) {
                                            Log.e(TAG, "--Payment Session ---" + res.getData().getPaymentSession());
                                            encodedPaymentSession = res.getData().getPaymentSession();
                                        } else {
                                            Log.e(TAG, "--Payment Session --- Not received");
                                        }
                                        handlePaymentSessionResponse(encodedPaymentSession);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentSessionResponse> call, Throwable t) {
                                dismissProgressbar();
                                Toast.makeText(getApplicationContext(), "C'è qualche errore" + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        });
                    }

                    @Override
                    public void onError(@NonNull CheckoutException checkoutException) {
                        // TODO: Handle error.
                        dismissProgressbar();
                        Toast.makeText(getApplicationContext(), "checkOut Exception Occurred " + checkoutException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                break;
            case R.id.ic_camera_small:
                /*if(_avathar_loc_img.equalsIgnoreCase("")){
                    uploadImage();
                }else{
                    DeleteFrontImage();
                }*/

                uploadImage();
                break;

            case R.id.profile_image:
                if(_avathar_loc_img.equalsIgnoreCase("")){
                    uploadImage();
                }else{
                    DeleteFrontImage();
                }

        }
    }
    private void DeleteFrontImage() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ProfileScreen_Activity.this).inflate(R.layout.imagepopup, viewGroup, false);

        ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

        ImageView remove =(ImageView)dialogView.findViewById(R.id.remove);

        ImageView right =(ImageView)dialogView.findViewById(R.id.right);



        Glide.with(context)
                .load(_avathar_loc_img) // or URI/path
                .placeholder(R.drawable.menu_govolt_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.IMMEDIATE)
                .error(R.drawable.menu_govolt_icon)
                .skipMemoryCache(false)
                .into(img);



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

                _avathar_loc_img = "";

                PreferenceUtil.getInstance().saveString(context, Constants.AVATHAR_LOC_IMG, "");

                _profileImg.setImageResource(R.drawable.menu_govolt_icon);
            }
        });


    }

    private void uploadImage() {


        if (ContextCompat.checkSelfPermission(ProfileScreen_Activity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(ProfileScreen_Activity.this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(ProfileScreen_Activity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileScreen_Activity.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(ProfileScreen_Activity.this,
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
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(intent, CAMERA_REQUEST);

//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }


    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkIfAlreadyhavePermission() {

        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return true;
        } else

            return false;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
//            Imageview_pro.setImageBitmap(mphoto);
            // ButtonOk.setVisibility(View.VISIBLE);
            profileImageChanged = true;
            Bitmap mphoto = null;
            Bitmap compressedBitmap = null;
            try {
                mphoto = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mphoto.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                compressedBitmap = AppUtils.rotateImageIfRequired(this,compressedBitmap,imageUri);
                _profileImg.setImageBitmap(compressedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Uri tempUri = getImageUri(getApplicationContext(), mphoto);
            Uri tempUri = getImageUri(getApplicationContext(), compressedBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            imagePath = finalFile.getPath();

            System.out.println("ImagePath---" + imagePath);
            UploadImageFile_Api(context, imagePath);
            AppUtils.deleteImage(imageUri,this);


        } else  if (requestCode == REQUEST_CODE_CHECKOUT) {
            if (resultCode == PaymentMethodHandler.RESULT_CODE_OK) {
                showProgressbar();
                PaymentResult paymentResult = PaymentMethodHandler.Util.getPaymentResult(data);

                Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                RequestInterface register_details = retrofit.create(RequestInterface.class);
                String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
                String bearer_authorization = "Bearer " + authorization;

                PaymentVerifyRequest paymentVerifyRequest = new PaymentVerifyRequest();
                paymentVerifyRequest.setPayload(paymentResult.getPayload());
                // Handle PaymentResult.
                Call<PaymentVerifyResponse> resultRes = register_details.getPaymentVerification(Constants.TOKEN, bearer_authorization, paymentVerifyRequest);

                resultRes.enqueue(new Callback<PaymentVerifyResponse>() {
                    @Override
                    public void onResponse(Call<PaymentVerifyResponse> call, Response<PaymentVerifyResponse> response) {

                        dismissProgressbar();

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "--Response code---" + response.code());
                            Log.e(TAG, "--Response ---" + response.body());


                            if (response.code() != 200) {
                                if (response.code() != 500) {
                                    Toast.makeText(getApplicationContext(), "C'è qualche errore nei dettagli inseriti", Toast.LENGTH_SHORT).show();
                                }
                            }


                        } else {
                            Log.e(TAG, "--Success---");


                            if (response.code() == 200) {
                                PaymentVerifyResponse res = response.body();
                                if(res.getStatus())
                                    Toast.makeText(getApplicationContext(), "Pagamento verificato", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Pagamento non verificato", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Pagamento non verificato", Toast.LENGTH_SHORT).show();

                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<PaymentVerifyResponse> call, Throwable t) {
                        dismissProgressbar();
                        Toast.makeText(getApplicationContext(), "C'è qualche errore: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }


                });


            }
            else {
                dismissProgressbar();
                CheckoutException checkoutException = PaymentMethodHandler.Util.getCheckoutException(data);
                Toast.makeText(getApplicationContext(), "Pagamento fallito", Toast.LENGTH_SHORT).show();
                if (resultCode == PaymentMethodHandler.RESULT_CODE_CANCELED) {
                    // Handle cancellation and optional CheckoutException.
                } else {

                    // Handle CheckoutException.
                }
            }
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

    private void UserInformation() {
        // showProgressbar();

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, ProfileScreen_Activity.this);
            return;
        }


        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        UserInformation geo_details = retrofit.create(UserInformation.class);

        Call<UserInfoResponse> resultRes = geo_details.GetResponse(Constants.TOKEN, bearer_authorization);
        resultRes.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {

                // dismissProgressbar();

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
                            AppUtils.SaveDataInSharePreference(context, userinfo);

                            setValues();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                //  dismissProgressbar();

            }


        });
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

    private void dismissProgressbar() {
        progressbar_hud.dismiss();
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
                        ActivityCompat.requestPermissions(ProfileScreen_Activity.this,
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
                        startInstalledAppDetailsActivity(ProfileScreen_Activity.this);

                    }
                });
        alertDialog.show();
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
                            saveToPreferences(ProfileScreen_Activity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request

        }
    }

    private void CallAdditionalGetService() {
        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, ProfileScreen_Activity.this);
            return;
        }

        showProgressbar();

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
                        String _holder_name= res.getData().getDrivingLincenseHolderName();
                        String _nationality= res.getData().getDrivingLicenseNationality();

                        Log.e(TAG, "DATA--" + guy + ", " + driving_licence_no + "," + _driving_authorities);


                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_Tipo_Guy, guy);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_DRIVING_LICENCE_NO, driving_licence_no);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_RELICA_ISSUEDON, _driving_authorities);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_SCENDENA_EXP_DATE, _driving_expiry_date);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_RILICA_RELEASE, _driving_release_date);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_HOLDER_NAME, _holder_name);
                        PreferenceUtil.getInstance().saveString(context, Constants.PATENT_NATIONALITY, _nationality);



                        if (_driving_authorities != null) {
                            _profile_patent_secondText.setText("" + _driving_authorities);
                        }else{
                            _profile_patent_secondText.setText("");

                        }

                        if (_driving_expiry_date != null) {
                            String dateStr = _driving_expiry_date;
                           /* String dateStr = _driving_expiry_date;
                            System.out.println("ghhhhghg:::"+dateStr);
                            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                            Date date = null;
                            try {
                                date = (Date)formatter.parse(dateStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            System.out.println(date);

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                            System.out.println("formatedDate : " + formatedDate);
*/
                            _profile_patent_date.setText("" + dateStr);
                        }else{
                            _profile_patent_date.setText("");
                        }

                        if (driving_licence_no != null) {
                            _usernamefull.setText("" + driving_licence_no);

                        }else {
                            _usernamefull.setText("");
                        }

                    }


                }

                dismissProgressbar();

            }

            @Override
            public void onFailure(Call<PatentResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }
    private void handlePaymentSessionResponse(String encodedPaymentSession) {
        CheckoutController.handlePaymentSessionResponse(/*Activity*/ this, encodedPaymentSession, new StartPaymentParametersHandler() {

            @Override
            public void onError(@NonNull CheckoutException error) {
                Log.e(TAG, "--CheckoutException --- error" + error.getMessage());
            }

            @Override
            public void onPaymentInitialized(@NonNull StartPaymentParameters startPaymentParameters) {
                PaymentMethodHandler paymentMethodHandler = CheckoutController.getCheckoutHandler(startPaymentParameters);
                paymentMethodHandler.handlePaymentMethodDetails(/* Activity */ ProfileScreen_Activity.this, REQUEST_CODE_CHECKOUT);
            }
        });
    }

    public void UploadImageFile_Api(final Context context, String imagePath) {
        showProgressbar();
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
                dismissProgressbar();
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
                        String image = res.getData().getLocation();
                        PreferenceUtil.getInstance().saveString(context, Constants.AVATHAR_LOC_IMG, image);
                        _avathar_loc_img = PreferenceUtil.getInstance().getString(context, Constants.AVATHAR_LOC_IMG, "");
                        profileImageChanged =  false;
//                        Glide.with(context)
//                                .load(_avathar_loc_img) // or URI/path
//                                .placeholder(R.drawable.menu_govolt_icon)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .priority(Priority.IMMEDIATE)
//                                .error(R.drawable.menu_govolt_icon)
//                                .skipMemoryCache(false)
//                                .into(_profileImg);
                        onResume();
                    }

                }


            }

            @Override
            public void onFailure(Call<AvatharResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());


            }


        });
    }
}