package com.quadrant.govolt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.FgtPwdRequest;
import com.quadrant.request.LoginRequest;
import com.quadrant.response.FgtPwdResponse;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.LoginResonse;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {
    private Button forgot_pwd_btn, ok_btn;
    private AlertDialog alertDialog;
    private ImageView btn_login;
    private EditText edit_email, edit_pwd;
    private String _email, _password;
    private String TAG = "LoginActivity";
    private Context context;
    private KProgressHUD progressbar_hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        context = this;
        Fabric.with(this, new Crashlytics());
        // edit_email.setText("Saikumar");
        edit_email = (EditText) findViewById(R.id.email);
        edit_pwd = (EditText) findViewById(R.id.password);
        btn_login = (ImageView) findViewById(R.id.login_btn);
        forgot_pwd_btn = (Button) findViewById(R.id.forgot_pwd);
        forgot_pwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* Intent I = new Intent(LoginActivity.this, DamageCheckActivity.class);
                startActivity(I);
*/
                // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                _email = edit_email.getText().toString();
                _password = edit_pwd.getText().toString();

                if (!validateEmail()) {
                    return;
                }

                if (!validatePassword()) {
                    return;
                }

                boolean _isInternetAvailable = Constants.isInternetAvailable(context);

                if (_isInternetAvailable) {
                    LoginSetviceCall(_email, _password);
                } else {

                    Toast.makeText(context, "Please connect internet.", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

 /*   public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }*/


    private void LoginSetviceCall(String email, String password) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, LoginActivity.this);
            return;
        }

        showProgressbar();

        LoginRequest req = new LoginRequest();
        req.setUsername(email);
        req.setPassword(password);

        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<LoginResonse> resultRes = register_details.GetResponse(Constants.TOKEN, req);
        resultRes.enqueue(new Callback<LoginResonse>() {
            @Override
            public void onResponse(Call<LoginResonse> call, Response<LoginResonse> response) {

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

                            error_Alertbox(error.getError().getDescription());
                        }
                    }


                } else {
                    Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        LoginResonse res = response.body();
                        Log.e(TAG, "--ID ---" + res.getData().getToken().getId());

                        PreferenceUtil.getInstance().saveInt(context, Constants.USER_ID, res.getData().getToken().getId());
                        PreferenceUtil.getInstance().saveString(context, Constants.USER_CODE, res.getData().getToken().getCode());


                        finish();

                        Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(it);

                    }


                }
            }

            @Override
            public void onFailure(Call<LoginResonse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });

    }

    private void error_Alertbox(String error_message) {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.custom_error_alert, viewGroup, false);

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


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.forgotpwd_alert, viewGroup, false);

        Button ok_btn = (Button) dialogView.findViewById(R.id.buttonOk_login);
        TextView textView = (TextView) dialogView.findViewById(R.id.textone);
        TextView title = (TextView) dialogView.findViewById(R.id.title);

        final EditText email_id = (EditText) dialogView.findViewById(R.id.email);

        // Button buttonOk = (Button) dialogView.findViewById(R.id.buttonOk);


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validateForgotEmail(email_id)) {
                    return;
                }

                getfgtpwdservice();

            }

            private void getfgtpwdservice() {

                boolean _isInternetAvailable = Constants.isInternetAvailable(context);

                if (!_isInternetAvailable) {


                    AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, LoginActivity.this);
                    return;
                }


                showProgressbar();

                FgtPwdRequest req_fgt = new FgtPwdRequest();
                req_fgt.setEmail(email_id.getText().toString());


                final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

                RequestInterface register_details = retrofit.create(RequestInterface.class);

                Call<FgtPwdResponse> resultRes = register_details.GetResponseFgtpwd(Constants.TOKEN, req_fgt);
                resultRes.enqueue(new Callback<FgtPwdResponse>() {

                    @Override
                    public void onResponse(Call<FgtPwdResponse> call, Response<FgtPwdResponse> response) {

                        dismissProgressbar();

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "--Response code---" + response.code());
                            Log.e(TAG, "--Response ---" + response.body());


                            if (response.code() != 200) {

                                //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                                //  LoginErrorResponse error = ErrorUtils.parseError(response);
                                // â€¦ and use it to show error information

                                // â€¦ or just log the issue like weâ€™re doing :)
                                //  Log.d("error message", error.getError().getDescription());

                                // error_Alertbox(error.getError().getDescription());

                            }


                        } else {
                            Log.e(TAG, "--Success---");


                            if (response.code() == 200) {
                                FgtPwdResponse res = response.body();
                                Log.e(TAG, "--ID ---" + res.getStatus());
                                Toast.makeText(getApplicationContext(), "La password è stata inviata all'indirizzo e-mail registrato", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();


/*
                                PreferenceUtil.getInstance().saveInt(context, Constants.USER_ID, res.getData().getToken().getId());
                                PreferenceUtil.getInstance().saveString(context, Constants.USER_CODE, res.getData().getToken().getCode());


                                finish();

                                Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(it);*/

                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<FgtPwdResponse> call, Throwable t) {
                        Log.e(TAG, "--Fail---" + t.getMessage());
                        dismissProgressbar();

                    }


                });


            }
        });


        String str = "Recupero" +
                "\n" +
                "password";
        textView.setText(str);

        String str1 = "Inserisci la tua email." +
                "\n" +
                "Ti invieremo un link per " + "\n" + "impostare nuovamente" + "\n" + "la password";
        title.setText(str1);

        title.setTextColor(getResources().getColor(R.color.white));

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);
        alertDialog.show();


    }

    private boolean validateForgotEmail(EditText email_id) {
        String email = email_id.getText().toString().trim();
        //inputLayoutEmail.setHintEnabled(true);
        if (email.isEmpty() || !isValidEmail(email)) {
            //  email_id.setError("Enter valid email.");
            requestFocus(email_id);
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        String email = edit_email.getText().toString().trim();
        //inputLayoutEmail.setHintEnabled(true);
        if (email.isEmpty() || !isValidEmail(email)) {
            //  edit_email.setError("Enter valid email.");

            AppUtils.error_Alert("Inserisci un indirizzo e-mail valido.", context, alertDialog, LoginActivity.this);

            edit_email.setBackgroundResource(R.drawable.red_input_feild);

            requestFocus(edit_email);
            return false;
        }
        edit_email.setBackgroundResource(R.drawable.ic_active_edittext);

        return true;

    }


    private boolean validatePassword() {
        //inputLayoutPassword.setHintEnabled(true);

        if (edit_pwd.getText().toString().trim().isEmpty()) {
            // edit_pwd.setError("Enter password.");
            AppUtils.error_Alert("Inserire la password", context, alertDialog, LoginActivity.this);
            edit_pwd.setBackgroundResource(R.drawable.red_input_feild);
            requestFocus(edit_pwd);
            return false;

        }
        edit_pwd.setBackgroundResource(R.drawable.ic_active_edittext);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
