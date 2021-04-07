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
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.MultipartUtility;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.ImageselfieRequest;
import com.quadrant.request.ProfileRequest;
import com.quadrant.response.ImageselfieResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.ProfileRequestResponse;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.*;
import static com.quadrant.govolt.Others.AppUtils.UploadImageFile_Api_Avatar;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView Img_Camera;
    private CardView Card_View;
    private String TAG = "UploadImageActivity";
    private Button ButtonOk;
    private Context con;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    private KProgressHUD progressbar_hud;

    Context context;

    String firstbaseimage;
    String imagePath = "";
    File finalFile;

    boolean FirstImage = false;


    private AlertDialog alertDialog;

    Bitmap mphoto;

    ImageView deletebtn;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_img_one);
        context = this;
        Img_Camera = (ImageView) findViewById(R.id.img);

        deletebtn = (ImageView) findViewById(R.id.delete);

        deletebtn.setVisibility(View.GONE);
        // Card_View = findViewById(R.id.card_view);

        ButtonOk = findViewById(R.id.buttonOk);

        ButtonOk.setVisibility(View.GONE);

        ButtonOk.setOnClickListener(this);
        // ButtonOk.setVisibility(View.GONE);

        cameraImgClick();

        //ButtonOkclick();


    }

    private void ButtonOkclick() {

        ButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    private void cameraImgClick() {

        Img_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirstImage == false) {

                    UploadImg();
                } else {


                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(UploadImageActivity.this).inflate(R.layout.imagepopup_upload, viewGroup, false);

                    ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

                    ImageView remove = (ImageView) dialogView.findViewById(R.id.remove);

                    ImageView right = (ImageView) dialogView.findViewById(R.id.right);

                    img.setImageBitmap(mphoto);


                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirstImage = false;
                            alertDialog.dismiss();

                            Img_Camera.setImageResource(R.drawable.selfie2);

                            Img_Camera.setTag(null);

                            deletebtn.setVisibility(View.GONE);

                            ButtonOk.setVisibility(View.GONE);


                        }
                    });

                    // TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

                    //  tv_error.setText("" + error_message);

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


            }
        });

    }


    private void UploadImg() {

        if (ContextCompat.checkSelfPermission(UploadImageActivity.this,
                permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(UploadImageActivity.this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(UploadImageActivity.this,
                    permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(UploadImageActivity.this,
                        permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(UploadImageActivity.this,
                            new String[]{permission.CAMERA},
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

        checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE);
        if ((checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            //show dialog to ask permission
            ActivityCompat.requestPermissions(this,
                    new String[]{permission.WRITE_EXTERNAL_STORAGE,
                            permission.WRITE_EXTERNAL_STORAGE},
                    1);
            return true;
        } else

            return false;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            mphoto = (Bitmap) data.getExtras().get("data");
//            Img_Camera.setImageBitmap(mphoto);
            mphoto = null;
            Bitmap compressedBitmap = null;
            try {
                mphoto = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mphoto.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                compressedBitmap = AppUtils.rotateImageIfRequired(this, compressedBitmap, imageUri);
                Img_Camera.setImageBitmap(compressedBitmap);
                mphoto = compressedBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Img_Camera.setTag(mphoto);

            deletebtn.setVisibility(View.VISIBLE);

//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            mphoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();

//            firstbaseimage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            System.out.println("ggggg:::" + firstbaseimage);
            ButtonOk.setVisibility(View.VISIBLE);

            FirstImage = true;

            ButtonOk.setVisibility(View.VISIBLE);

            Uri tempUri = getImageUri(getApplicationContext(), compressedBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

            imagePath = finalFile.getPath();

            AppUtils.deleteImage(imageUri, this);

            AppUtils.UploadImageFile_Api(context, imagePath);

            AppUtils.UploadDocumentFile_Api(context, imagePath, 0);
            AppUtils.UploadDocumentFile_Api(context, imagePath, 1);


            System.out.println("ImagePath---" + imagePath);
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
                        ActivityCompat.requestPermissions(UploadImageActivity.this,
                                new String[]{permission.CAMERA},
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
                        startInstalledAppDetailsActivity(UploadImageActivity.this);

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
                            saveToPreferences(UploadImageActivity.this, ALLOW_KEY, true);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonOk:


/*


                if (Img_Camera.getDrawable() == null) {

                    Toast toast=Toast.makeText(getApplicationContext(),"Please Upload the Image",Toast.LENGTH_SHORT);

                } else {

                   int statusCode =  AppUtils.UploadImageFile_Api_Avatar(context, imagePath);


                    Intent I = new Intent(UploadImageActivity.this, ProfilePaymentActivity.class);
                    startActivity(I);
*/


                if (FirstImage == false || Img_Camera.getTag() == null) {

                    Toast.makeText(getApplicationContext(), "Si prega di caricare l'immagine", Toast.LENGTH_SHORT).show();

                } else {


                    Intent I = new Intent(UploadImageActivity.this, ProfilePaymentActivity.class);
                    startActivity(I);
                }


                break;

        }

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
}
