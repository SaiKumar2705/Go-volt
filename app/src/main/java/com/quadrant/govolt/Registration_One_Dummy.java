package com.quadrant.govolt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RegistrationInterface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.request.LoginRequest;
import com.quadrant.request.RegistrationRequest;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.LoginResonse;
import com.quadrant.response.RegistrationErrorResponse;
import com.quadrant.response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.quadrant.govolt.Others.AppUtils.isValidEmail;

public class Registration_One_Dummy extends AppCompatActivity implements View.OnClickListener {
    private TextView swith_text_one, swith_text_two, swith_text_three, swith_text_four;
    private Button button_ok;
    private Button btn_done;
    private ImageView Img_done;
    private TextView pwd_alert_title, TV_Leggi, TV_Leggi2;
    private AlertDialog alertDialog;
    private View dialogView;
    private EditText password_ed, ed_email;
    //private PrefixEditText phone_num;
    // private EditText contry_code,phone_num;
    private LinearLayout ll_mobile_layout;
    private Context context;
    EditText mEdtcountryCode, mEdtPhoneNumber, mConfirmPassword;
    View mView;
    ImageButton Info;
    LinearLayout mLinearLayout;
    private String _email, _password, _confirm_password;
    private ToggleButton ToggleButton1, ToggleButton2, ToggleButton3;

    boolean CheckedOrNot = false;
    boolean CheckedOrNotToggle2 = false;
    boolean CheckedOrNotToggle3 = false;

    private AwesomeValidation awesomeValidation;
    boolean checkortrue;

    String IDTOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_one);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        context = this;
        setUpView();
        Awesomevalidations();
        ToggleValidations();
        Infobtnclick();
        onFocusChange();
        Textchnagers();
        Drawableclick_method();
        ColorinbetweenStringMethod();
        InfoImageTextWatch();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _email = ed_email.getText().toString();
                _password = password_ed.getText().toString();
                _confirm_password = mConfirmPassword.getText().toString();


                if (awesomeValidation.validate()) {
                    //  Toast.makeText(Registration_One.this, "Validation Successfull", Toast.LENGTH_LONG).show();
                    ed_email.setBackgroundResource(R.drawable.red_input_feild);
                    ed_email.setError("Enter valid email");
                    password_ed.setBackgroundResource(R.drawable.red_input_feild);
                    password_ed.setError("Password should be 8 charecters");
                    mConfirmPassword.setBackgroundResource(R.drawable.red_input_feild);
                    mConfirmPassword.setError("Passwords Not matching");
                    mLinearLayout.setBackgroundResource(R.drawable.red_regi_input);
                    mEdtPhoneNumber.setError("Enter Valid Phonenumber");
                    checkortrue = true;
                    //  m.setBackgroundResource(R.drawable.red_input_feild);

                    //process the data further
                } else {

                    awesomeValidation.clear();
                    checkortrue = true;

                    if (ed_email.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        validateEmail();
                        password_ed.setFocusable(true);
                    }
                    if (password_ed.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        validatePassword();
                        mConfirmPassword.setFocusable(true);
                    }
                    if (mConfirmPassword.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        validateConfirmpassword();
                        mEdtPhoneNumber.setFocusable(true);
                    }
                    if (mEdtPhoneNumber.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        validatePhoneNumber();
                    }
                    if (checkortrue == true) {


                        awesomeValidation.clear();

                       /* if (!validatePhoneNumber()) {


                            return;
                        }
*/

                        if (CheckedOrNot == false || CheckedOrNotToggle2 == false || CheckedOrNotToggle3 == false) {


                            ViewGroup viewGroup = findViewById(android.R.id.content);

                            //then we will inflate the custom alert dialog xml that we created
                            dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.error_alert, viewGroup, false);

                            btn_done = (Button) dialogView.findViewById(R.id.done_btn);

                            Img_done = (ImageView) dialogView.findViewById(R.id.img_done);

                            pwd_alert_title = (TextView) dialogView.findViewById(R.id.forgot_pwd_title);

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


                            Img_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();

                                    if (CheckedOrNot == false) {

                                        ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_border_switch));
                                        ToggleButtononeColorChange();

                                    } else if (CheckedOrNotToggle2 == false) {


                                        ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_border_switch));
                                        ToggleButton2ColorChange();
                                        ;
                                    } else if (CheckedOrNotToggle3 == false) {


                                        ToggleButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_border_switch));
                                        ToggleButton3ColorChange();
                                        ;


                                    }


                                    //launchPesonalDetails();

                                }
                            });


                        } else {


                            postdata();


                        }

                    }
                }


            }
        });


    }

    private void InfoImageTextWatch() {

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
                String answerString = password_ed.getText().toString();

                if (answerString.length() >= 8) {

                    Info.setImageResource(R.drawable.ic_right_small);

                } else {

                    Info.setImageResource(R.drawable.ic_i_icon);
                }

            }
        };
        password_ed.addTextChangedListener(textWatcher);


    }

    private void ColorinbetweenStringMethod() {
        String text1 = "Accetto i <font color='#37B12B'>Termini &amp; Condizioni</font> generali di contratto di scooter sharing e il regolamento";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            swith_text_one.setText(Html.fromHtml(text1, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            swith_text_one.setText(Html.fromHtml(text1), TextView.BufferType.SPANNABLE);
        }

        String text2 = "Ai sensi degli <font color='#37B12B'>art. 1341 e 1342 c.c.</font> accetto specificamente le seguenti clausole dei termini e condizioni: Art 2.5, Art 3,4.2, 4.5, 5.5,7.6, 8.9, 13.3, 16, 18.2";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  swith_text_two.setText(Html.fromHtml(text2, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            //swith_text_two.setText(Html.fromHtml(text2), TextView.BufferType.SPANNABLE);
        }

        String text4 = "<font color='#37B12B'>Newsletter</font> per ricevere promozioni, informazioni e notizie su GoVolt";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            swith_text_four.setText(Html.fromHtml(text4, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            swith_text_four.setText(Html.fromHtml(text4), TextView.BufferType.SPANNABLE);
        }


    }

    private void Textchnagers() {
        mEdtcountryCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    mLinearLayout.setBackgroundResource(R.drawable.reg_input_mble);
                } else {
                    mLinearLayout.setBackgroundResource(R.drawable.edittext_border);
                }
            }
        });
        mEdtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mLinearLayout.setBackgroundResource(R.drawable.reg_input_mble);

                } else {
                    mLinearLayout.setBackgroundResource(R.drawable.edittext_border);


                }
            }
        });
        mEdtcountryCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = mEdtcountryCode.getText().length();

                if (textlength1 >= 3) {
                    mEdtPhoneNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void ToggleValidations() {

        ToggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mEdtPhoneNumber.clearFocus();


                if (ToggleButton3.isChecked()) {

                    CheckedOrNotToggle3 = true;
                    //   Toast.makeText(Registration_One.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                } else {

                    CheckedOrNotToggle3 = false;
                    //Toast.makeText(Registration_One.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }


            }
        });
        ToggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // mEdtPhoneNumber.clearFocus();


                if (ToggleButton2.isChecked()) {

                    CheckedOrNotToggle2 = true;
                    //   Toast.makeText(Registration_One.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                } else {

                    CheckedOrNotToggle2 = false;
                    //Toast.makeText(Registration_One.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }
            }
        });
        ToggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEdtPhoneNumber.clearFocus();


                if (ToggleButton1.isChecked()) {

                    CheckedOrNot = true;
                    //   Toast.makeText(Registration_One.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                } else {

                    CheckedOrNot = false;
                    //Toast.makeText(Registration_One.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void Awesomevalidations() {
        awesomeValidation.addValidation(Registration_One_Dummy.this, R.id.et_email, "", R.string.nameerror);
        awesomeValidation.addValidation(Registration_One_Dummy.this, R.id.et_password, "", R.string.emailerror);
        awesomeValidation.addValidation(Registration_One_Dummy.this, R.id.et_confirmpassword, "", R.string.nameerror);
        awesomeValidation.addValidation(Registration_One_Dummy.this, R.id.et_phonenumber, "", R.string.nameerror);

    }

    private void postdata() {

        RegistrationRequest req = new RegistrationRequest();

        req.setEmail(ed_email.getText().toString());

        req.setPassword(password_ed.getText().toString());
        req.setTel(mEdtPhoneNumber.getText().toString());


        //Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();


        //   showProgressbar();

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + authorization;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RegistrationInterface geo_details = retrofit.create(RegistrationInterface.class);
        Call<RegistrationResponse> resultRes = geo_details.GetResponseReg(Constants.TOKEN, req);
        resultRes.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {


                if (!response.isSuccessful()) {
                   /* Log.e(TAG, "--Response code---"+response.code());
                    Log.e(TAG, "--Response ---"+response.body());
                    dismissProgressbar();
*/
                    if (response.code() != 200) {

                        //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                        RegistrationErrorResponse error = ErrorUtils.parseErrorReg(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        error_Alertbox(error.getError().getDescription());


                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        getLoginApi();

                        //Toast.makeText(getApplicationContext(), "Hello Javatpoint", Toast.LENGTH_SHORT).show();

                        launchPesonalDetails();

                    /*    CommunityResponse myresponse = response.body();
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

                        dismissProgressbar();*/
                    }


                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                //  dismissProgressbar();

            }


        });

    }

    private void getLoginApi() {


        //showProgressbar();

        LoginRequest req = new LoginRequest();
        req.setUsername(ed_email.getText().toString());
        req.setPassword(password_ed.getText().toString());

        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        Call<LoginResonse> resultRes = register_details.GetResponse(Constants.TOKEN, req);
        resultRes.enqueue(new Callback<LoginResonse>() {
            @Override
            public void onResponse(Call<LoginResonse> call, Response<LoginResonse> response) {

                //  dismissProgressbar();

                if (!response.isSuccessful()) {
                   /* Log.e(TAG, "--Response code---" + response.code());
                    Log.e(TAG, "--Response ---" + response.body());
*/

                    if (response.code() != 200) {

                        //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                        LoginErrorResponse error = ErrorUtils.parseError(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        Log.d("error message", error.getError().getDescription());

                        error_Alertbox(error.getError().getDescription());

                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        LoginResonse res = response.body();
                        // Log.e(TAG, "--ID ---" + res.getData().getToken().getId());

                     /*   PreferenceUtil.getInstance().getInt(context, Constants.USER_ID, 0);
                        PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");*/

                        IDTOKEN = res.getData().getToken().getCode();

                        Log.e("SURESH", "IDTOKEN===" + IDTOKEN);

                        PreferenceUtil.getInstance().saveString(context, Constants.REG_TOKEN, IDTOKEN);

                        //  System.out.println("hdhdhhd"+ID);
                      /*  PreferenceUtil.getInstance().saveInt(context, Constants.USER_ID, res.getData().getToken().getId());
                        PreferenceUtil.getInstance().saveString(context, Constants.USER_CODE, res.getData().getToken().getCode());
*/

                        /*  finish();*/
/*

                        Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(it);
*/

                    }


                }
            }

            @Override
            public void onFailure(Call<LoginResonse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                //dismissProgressbar();

            }


        });

    }

    private void error_Alertbox(String description) {

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.custom_error_alert, viewGroup, false);

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

        TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

        tv_error.setText("" + description);

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


    private void onFocusChange() {

        ed_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    validateEmail();
                    password_ed.setFocusable(true);
                } else {
                    // Toast.makeText(Registration_One.this, "Get Focus", Toast.LENGTH_SHORT).show();
                }


            }
        });

        password_ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus) {
                    validatePassword();
                } else {
                    // Toast.makeText(Registration_One.this, "Get Focus", Toast.LENGTH_SHORT).show();
                }


            }
        });


        mConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus) {
                    validateConfirmpassword();
                } else {
                    // Toast.makeText(Registration_One.this, "Get Focus", Toast.LENGTH_SHORT).show();
                }


            }
        });


        mEdtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus) {
                    validatePhoneNumber();
                } else {
                    // Toast.makeText(Registration_One.this, "Get Focus", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private boolean validateEmail() {
        String email = ed_email.getText().toString().trim();
        //inputLayoutEmail.setHintEnabled(true);
        if (email.isEmpty() || !isValidEmail(email)) {
            ed_email.setError("Enter valid email.");
            ed_email.setBackgroundResource(R.drawable.red_input_feild);
            // requestFocus(ed_email);
            return false;
        } else {


            ed_email.setBackgroundResource(R.drawable.edittext_image_change);
            return true;

        }


    }

    private boolean validatePassword() {
        String pwd = password_ed.getText().toString().trim();

        if (TextUtils.isEmpty(pwd) || pwd.length() < 8) {
            password_ed.setError("You must have 8 characters in your password");
            password_ed.setBackgroundResource(R.drawable.red_input_feild);
            return false;
        } else {
            password_ed.setBackgroundResource(R.drawable.edittext_image_change);
            return true;

        }


    }


    private boolean validateConfirmpassword() {


       /* if (!_password.equals(_confirm_password)) {

            mConfirmPassword.setError("Password Not matching.");
            return;
        }*/
        String pwd = password_ed.getText().toString().trim();
        String conpwd = mConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(conpwd) || !pwd.equals(conpwd)) {

            mConfirmPassword.setError("Password Not matching.");
            mConfirmPassword.setBackgroundResource(R.drawable.red_input_feild);

            ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.confirmpasswordalert, viewGroup, false);

            ImageView btn_done = (ImageView) dialogView.findViewById(R.id.img_done);

            //TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

            // tv_error.setText("" + error_message);

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
            return false;
        } else {
            mConfirmPassword.setBackgroundResource(R.drawable.edittext_image_change);
            return true;

        }


    }


    private boolean validatePhoneNumber() {
        String phno = mEdtPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(phno) || phno.length() != 10) {
            mEdtPhoneNumber.setError("Enter valid phone number");
            mLinearLayout.setBackgroundResource(R.drawable.red_regi_input);
            return false;
        } else {
            mLinearLayout.setBackgroundResource(R.drawable.edittext_image_change);
            return true;
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void SecondpopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.termandcondialert, viewGroup, false);

        btn_done = (Button) dialogView.findViewById(R.id.confirm);

        TV_Leggi = (TextView) dialogView.findViewById(R.id.leggi);

        TV_Leggi2 = (TextView) dialogView.findViewById(R.id.leggi2);


        SpannableString content = new SpannableString(TV_Leggi.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, TV_Leggi.getText().toString().length(), 0);
        TV_Leggi.setText(content);


        SpannableString content1 = new SpannableString(TV_Leggi2.getText().toString());
        content1.setSpan(new UnderlineSpan(), 0, TV_Leggi2.getText().toString().length(), 0);
        TV_Leggi2.setText(content);

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
        TV_Leggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://govolt.it/termini-e-condizioni/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TV_Leggi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://govolt.it/privacy-cookie-policy"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


            }
        });
    }

    private void thirdPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.manifesto, viewGroup, false);

        btn_done = (Button) dialogView.findViewById(R.id.confirm);

        //Img_done=(ImageView) dialogView.findViewById(R.id.img_done);

        // pwd_alert_title = (TextView) dialogView.findViewById(R.id.forgot_pwd_title);

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

    private void ToggleButtononeColorChange() {


        ToggleButton1.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                        } else {
                            //ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                        }
                    }
                });

    }

    private void ToggleButton2ColorChange() {


        ToggleButton2.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                        } else {
                            //  ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                        }
                    }
                });

    }

    private void ToggleButton3ColorChange() {


        ToggleButton3.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ToggleButton3.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                        } else {
                            //  ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                        }
                    }
                });

    }

    private void ToggleButton1Colorchange() {

        ToggleButton1.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector_red));
                            ViewGroup viewGroup = findViewById(android.R.id.content);

                            //then we will inflate the custom alert dialog xml that we created
                            dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.error_alert, viewGroup, false);

                            Img_done = (ImageView) dialogView.findViewById(R.id.img_done);

                          /*  pwd_alert_title = (TextView) dialogView.findViewById(R.id.forgot_pwd_title);

                            String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";
                            pwd_alert_title.setText(str);*/

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


                            Img_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();

                                }
                            });

                        } else {

                            ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                            /*Toast.makeText(Registration_One.this,
                                    "Switch Off", Toast.LENGTH_SHORT).show();*/
                        }
                    }
                });
    }

    private void Infobtnclick() {

        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                dialogView = LayoutInflater.from(Registration_One_Dummy.this).inflate(R.layout.info_alert, viewGroup, false);

                //  btn_done = (Button)dialogView.findViewById(R.id.done_btn);

                Img_done = (ImageView) dialogView.findViewById(R.id.done_btn);


                // pwd_alert_title = (TextView)dialogView.findViewById(R.id.forgot_pwd_title);

                String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";
                //pwd_alert_title.setText(str);

                AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.blackTransparent);


                alertDialog.show();


                Img_donebtnclick();


            }

            private void Img_donebtnclick() {

                Img_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });


            }
        });
    }

    private void Drawableclick_method() {


    }

    private void launchPesonalDetails() {
        /*startActivity(new Intent(Registration_One.this, PersonalDetails.class));*/


        Intent I = new Intent(Registration_One_Dummy.this, PersonalDetails.class);
        I.putExtra("tokenid", IDTOKEN);
        System.out.println("saikumar::::" + IDTOKEN);
        startActivity(I);


    }


    private void setUpView() {
        Info = (ImageButton) findViewById(R.id.info);
        swith_text_one = (TextView) findViewById(R.id.switch_label_one);
        //swith_text_two = (TextView) findViewById(R.id.switch_label_two);
        swith_text_three = (TextView) findViewById(R.id.switch_label_three);
        swith_text_four = (TextView) findViewById(R.id.switch_label_four);
        button_ok = (Button) findViewById(R.id.buttonOk);
        ed_email = (EditText) findViewById(R.id.email);
        password_ed = (EditText) findViewById(R.id.et_password);
        swith_text_one.setOnClickListener(this);
        swith_text_three.setOnClickListener(this);

        ed_email = (EditText) findViewById(R.id.et_email);
        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);
        mEdtPhoneNumber = (EditText) findViewById(R.id.et_phonenumber);
        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);
        mEdtcountryCode = (EditText) findViewById(R.id.et_countrycode);
        mConfirmPassword = (EditText) findViewById(R.id.et_confirmpassword);
        ToggleButton1 = (ToggleButton) findViewById(R.id.togglebutton_term);
        ToggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        ToggleButton3 = (ToggleButton) findViewById(R.id.switch4);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_label_one:

                SecondpopUp();


                break;

            case R.id.switch_label_three:


                thirdPopUp();

                break;


        }
    }


}
