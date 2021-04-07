package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.FgtPwdRequest;
import com.quadrant.request.LoginRequest;
import com.quadrant.request.PasswordResetRequest;
import com.quadrant.response.FgtPwdResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.LoginResonse;
import com.quadrant.response.PasswordResetResponse;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Profile_Account extends AppCompatActivity implements View.OnClickListener{
    private Button ok_btn;
    private AlertDialog alertDialog;
    private Context context;
    private CircleImageView _profileImg;
    private TextView pwd_tv;
    private ImageView _submit;
    private  EditText password, con_pwd,ed_email,ed_password;
    private KProgressHUD progressbar_hud;
    private String TAG = "Profile_Account";
    private String pwd, conPwd;
    private ImageButton first_green_mark, second_green_mark;
    private TextView forgot_pwd_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_account);
        context = this;

        setUpView();


        pwd_tv.setOnClickListener(this);

        ImageView back  = (ImageView)findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Account");
        ImageView _homeIcon = (ImageView)findViewById(R.id.home_icon);
        _homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile_Account.this, HomeActivity.class);
                startActivity(i);
            }
        });


        //ed_password.setText("* * * * * * * * *");

        String email = PreferenceUtil.getInstance().getString(context, Constants.USER_EMAIL, "");
       if(email!=null){
           ed_email.setText(""+email);
       }
        _profileImg = (CircleImageView)findViewById(R.id.profile_image);

        passwordTextWatcher();
        confirmPasswordTextWatcher();

    }

    private void setUpView() {
         pwd_tv = (TextView)findViewById(R.id.password_tv);

        _submit = (ImageView)findViewById(R.id.submit);
        password = (EditText)findViewById(R.id.password);
        con_pwd = (EditText)findViewById(R.id.password_confirm);

        ed_email = (EditText)findViewById(R.id.email_label_one);
        ed_email.setEnabled(false);
        ed_password = (EditText)findViewById(R.id.password_label_one);

        first_green_mark = (ImageButton)findViewById(R.id.green_right_mark_one);
        second_green_mark = (ImageButton)findViewById(R.id.green_right_mark_two);

        first_green_mark.setVisibility(View.GONE);
        second_green_mark.setVisibility(View.GONE);

        _submit.setOnClickListener(this);
        pwd_tv.setOnClickListener(this);




    }



    private void passwordTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //here, after we introduced something in the EditText we get the string from it
                String answerString = password.getText().toString();

                if (answerString.length() >= 8) {

                    first_green_mark.setVisibility(View.VISIBLE);
                    first_green_mark.setImageResource(R.drawable.ic_right_small);

                } else {

                    //first_green_mark.setImageResource(R.drawable.ic_i_icon);
                }

            }
        };
        password.addTextChangedListener(textWatcher);
    }

    private void confirmPasswordTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //here, after we introduced something in the EditText we get the string from it
                String answerString = con_pwd.getText().toString();

                if (answerString.length() >= 8) {

                    second_green_mark.setVisibility(View.VISIBLE);
                    second_green_mark.setImageResource(R.drawable.ic_right_small);

                } else {

                    //first_green_mark.setImageResource(R.drawable.ic_i_icon);
                }

            }
        };
        con_pwd.addTextChangedListener(textWatcher);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.password_tv:
                showCustomDialog();
                break;
            case R.id.submit:
                 pwd = password.getText().toString();
                 conPwd = con_pwd.getText().toString();



                String previousPwd = ed_password.getText().toString();
                if(!previousPwd.equalsIgnoreCase("")){

                    CheckPasswordValidation(previousPwd, pwd, conPwd);
                }else{

                    Toast.makeText(context, "per favore inserisci la password attuale", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void CheckPasswordValidation(String previousPwd, String pwd, String conPwd) {
       // if(pwd.length()<8 &&!AppUtils.isValidPassword(conPwd)){
        if(pwd.length()<8 &&!AppUtils.isValidPassword(conPwd)){
            System.out.println("Not Valid");


        }else{
            System.out.println("Valid");


            Password_ResetServiceCall(previousPwd, conPwd);

        }
    }

    private void Password_ResetServiceCall(String previousPwd, String newPassword) {
        showProgressbar();

        PasswordResetRequest req = new PasswordResetRequest();
        req.setPrevious(previousPwd);
        req.setNext(newPassword);
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;


        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<PasswordResetResponse> resultRes = register_details.GetResponse(Constants.TOKEN,bearer_authorization, req);
        resultRes.enqueue(new Callback<PasswordResetResponse>() {
            @Override
            public void onResponse(Call<PasswordResetResponse> call, Response<PasswordResetResponse> response) {

                dismissProgressbar();

                if (!response.isSuccessful()) {
                    Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());


                    if (response.code() != 200) {

                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        PasswordResetResponse res = response.body();
                        Toast.makeText(context, "La tua password è stata resettata con successo!", Toast.LENGTH_SHORT).show();

                        PreferenceUtil.clearSharedPreferences(context);
                        Intent intent  = new Intent(Profile_Account.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }


                }
            }

            @Override
            public void onFailure(Call<PasswordResetResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.forgotpwd_alert, viewGroup, false);

        Button ok_btn = (Button) dialogView.findViewById(R.id.buttonOk_login);
        TextView textView = (TextView) dialogView.findViewById(R.id.textone);
        TextView title = (TextView) dialogView.findViewById(R.id.title);

        final EditText email_id=  (EditText) dialogView.findViewById(R.id.email);

        // Button buttonOk = (Button) dialogView.findViewById(R.id.buttonOk);

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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validateForgotEmail(email_id)) {
                    return;
                }

                getfgtpwdservice(email_id);
            }
        });
    }
    private boolean validateForgotEmail(EditText email_id) {
        String email = email_id.getText().toString().trim();
        //inputLayoutEmail.setHintEnabled(true);
        if (email.isEmpty() || !isValidEmail(email)) {
            email_id.setError("Enter valid email.");
            requestFocus(email_id);
            return false;
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void getfgtpwdservice(EditText email_id) {

             showProgressbar();

            FgtPwdRequest req_fgt = new FgtPwdRequest();
            req_fgt.setEmail(email_id.getText().toString());


            final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

            RequestInterface register_details = retrofit.create(RequestInterface.class);

            Call<FgtPwdResponse> resultRes = register_details.GetResponseFgtpwd(Constants.TOKEN, req_fgt);
            resultRes.enqueue(new Callback<FgtPwdResponse>(){

                @Override
                public void onResponse(Call<FgtPwdResponse> call, Response<FgtPwdResponse> response) {

                    dismissProgressbar();
                    alertDialog.dismiss();

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "--Response code---" + response.code());
                        Log.e(TAG, "--Response ---" + response.body());


                        if (response.code() != 200) {



                        }


                    } else {
                        Log.e(TAG, "--Success---");


                        if (response.code() == 200) {
                            FgtPwdResponse res = response.body();
                            Toast.makeText(getApplicationContext(),"La password è stata inviata all'indirizzo e-mail registrato",Toast.LENGTH_SHORT).show();


                            Log.e(TAG, "--ID ---" + res.getStatus());



                        }


                    }

                }

                @Override
                public void onFailure(Call<FgtPwdResponse> call, Throwable t) {
                    Log.e(TAG, "--Fail---" + t.getMessage());
                    dismissProgressbar();
                    alertDialog.dismiss();

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


}
