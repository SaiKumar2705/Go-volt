package com.quadrant.govolt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RegistrationInterface;
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.progressbar.KProgressHUD;
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

public class Registration_One extends AppCompatActivity implements View.OnClickListener {
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
    ImageButton Info, Tick;
    LinearLayout mLinearLayout;
    private String _email, _password, _confirm_password;
    private ToggleButton ToggleButton1, ToggleButton2, ToggleButton3;
    boolean CheckedOrNot = false;
    boolean CheckedOrNotToggle2 = false;
    boolean CheckedOrNotToggle3 = false;
    private AwesomeValidation awesomeValidation;
    boolean checkortrue, Negative_First_term, Negative_Second_Term, Negative_Second_Manifesto, Negative_Four_Manifesto, Negative_Six_Manifesto;
    String IDTOKEN;
    private RadioGroup radioSexGroup, RadioTerm, RadioTermTwo, RadioManifest_One, RadioManifest_Two, RadioManifest_Three;
    private RadioButton radiomFourButton, radiomTwoButton, RadiobtntermTwo, radiomsixButton, radiofourTermButton, radioSexButton, radioOneButton, radioTwoButton, radiomfiveButton, radiomOneButton, radiomThreeButton;
    private Button btnDisplay;
    private boolean isValidPh = false;
    private boolean isValidCp = false;
    private boolean isValidP = false;
    private boolean isValidE = false;
    boolean NegativeScenarioTerm, NegativeScenarioTermtwo, NegativePositve_term, threeradiopositiveterm, fourradionegativeterm;
    boolean Rb_one, Rb_two, Rb_three, Rb_four, Rb_five, Rb_six;

    private KProgressHUD progressbar_hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_one);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        context = this;
        setUpView();
        Tick.setVisibility(View.GONE);
        Awesomevalidations();
        System.out.println("dggdggdgd:::" + CheckedOrNot);
        Infobtnclick();
        onFocusChange();
        Textchnagers();
        Drawableclick_method();
        ColorinbetweenStringMethod();
        InfoImageTextWatch();
        CheckedOrNot = ToggleValidations();
        CheckedOrNotToggle2 = Toggleval();
        TextWatchers();


        //   TickImageTextWatch();
        //  ButtonColorChange();
        button_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (CheckedOrNot == false) {
                    ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));


                    CheckedOrNot = false;
                    NegativePositve_term = false;
                    threeradiopositiveterm = false;
                    Negative_First_term = true;
                    fourradionegativeterm = true;
                    ToggleButtononeColorChange();


                }

                if (CheckedOrNotToggle2 == false) {
                    ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                    CheckedOrNotToggle2 = false;
                    Rb_one = false;
                    Rb_two = true;
                    Rb_three = false;
                    Rb_four = true;
                    Rb_five = false;
                    Rb_six = true;

                    ToggleButton2ColorChange();
                }


                CheckedOrNot = ToggleValidations();

                System.out.println("dfggsdsghsgghs" + CheckedOrNot);

                CheckedOrNotToggle2 = Toggleval();
                System.out.println("sssfggsdsghsgghs" + CheckedOrNotToggle2);
                _email = ed_email.getText().toString();
                _password = password_ed.getText().toString();
                _confirm_password = mConfirmPassword.getText().toString();
                if (awesomeValidation.validate()) {
                    //  Toast.makeText(Registration_One.this, "Validation Successfull", Toast.LENGTH_LONG).show();
                    ed_email.setBackgroundResource(R.drawable.registration_input_red);
                    // ed_email.setError("Enter valid email");
                    password_ed.setBackgroundResource(R.drawable.registration_input_red);
                    // password_ed.setError("Password should be 8 charecters");
                    mConfirmPassword.setBackgroundResource(R.drawable.registration_input_red);
                    // mConfirmPassword.setError("Passwords Not matching");
                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);
                    //  mEdtPhoneNumber.setError("Enter Valid Phonenumber");
                    checkortrue = true;
                    //  m.setBackgroundResource(R.drawable.red_input_feild);

                    //process the data further
                } else if (checkortrue = true) {

                    awesomeValidation.clear();
                    checkortrue = true;

                    if (ed_email.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        isValidE = validateEmail();
                        password_ed.requestFocus();
                    }
                    if (password_ed.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        isValidP = validatePassword();
                        mConfirmPassword.requestFocus();
                    }
                    if (mConfirmPassword.getText().toString().matches("")) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                    } else {

                        isValidCp = validateConfirmpassword();
                        mEdtPhoneNumber.requestFocus();

                        //  Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

                    }
                    if (mEdtcountryCode.getText().toString().isEmpty()) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                        mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);
                        AppUtils.error_Alert("Si prega di inserire il prefisso internazionale", context, alertDialog, Registration_One.this);


                    } else if (mEdtPhoneNumber.getText().toString().isEmpty()) {
                       /* Toast.makeText(Registration_One.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;*/
                        mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);
                        AppUtils.error_Alert("Inserisci un numero di telefono valido", context, alertDialog, Registration_One.this);

                    } else {

                        isValidPh = validatePhoneNumber();
                        //String MobileNumber= mEdtPhoneNumber.getText().toString().length()==10;

                        String MbValue = mEdtPhoneNumber.getText().toString().trim();

                        int Saikk = MbValue.length();
                        System.out.println("ddddddgdggdggdg" + isValidPh);

                        if (isValidPh == false) {
                            mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);

                        }


                        if (isValidPh == true && isValidP && isValidE && isValidCp) {
                            // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                            //  AppUtils.error_Alert("Enter valid phone number", context, alertDialog, Registration_One.this);


                            if (!CheckedOrNot || !CheckedOrNotToggle2) {


                                //     Toast toast = Toast.makeText(getApplicationContext(), "Hello Javatpoint", Toast.LENGTH_SHORT);
                                ViewGroup viewGroup = findViewById(android.R.id.content);

                                //then we will inflate the custom alert dialog xml that we created
                                dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.toggle_alert, viewGroup, false);

                                btn_done = (Button) dialogView.findViewById(R.id.done_btn);

                                Img_done = (ImageView) dialogView.findViewById(R.id.img_done_dialogup);

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
                                        if (CheckedOrNot == false) {

                                            ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
                                            ToggleButtononeColorChange();

                                        } else if (CheckedOrNotToggle2 == false) {


                                            ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
                                            ToggleButton2ColorChange();
                                            ;
                                        }

                                        alertDialog.dismiss();
                                    }


                                });


                            }
                        }

                        // button_ok.setBackgroundResource(R.drawable.border_confirm);

                        if (CheckedOrNot == true && CheckedOrNotToggle2 == true) {
                            button_ok.setTextColor(Color.parseColor("#FFFFFF"));
                            button_ok.setBackgroundResource(R.drawable.border_confirm);


                        }


                    }

                    if (!NegativePositve_term && !threeradiopositiveterm) {
                        // CheckedOrNot = true;
                        CheckedOrNot = false;

                        // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                        ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                        ToggleButtononeColorChange();

                    }

                    if (!Rb_one && !Rb_three && !Rb_five) {
                        // CheckedOrNot = true;
                        CheckedOrNotToggle2 = false;

                        // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                        ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                        ToggleButton2ColorChange();
                        ;

                    }


                    /* if (!NegativePositve_term && !threeradiopositiveterm) {
                     *//*  // CheckedOrNot = true;
                        CheckedOrNot = false;
*//*
                         Toast.makeText(getApplicationContext(), "positive not checked ", Toast.LENGTH_SHORT).show();

                     *//*   ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                        ToggleButtononeColorChange();
*//*
                    }*/


                /*    if (!NegativePositve_term && !threeradiopositiveterm) {
                        // CheckedOrNot = true;
                        CheckedOrNot = false;

                        // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                        ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                        ToggleButtononeColorChange();

                    }


                    if (!Rb_one && !Rb_three && !Rb_five) {
                        // CheckedOrNot = true;
                        CheckedOrNot = false;

                        // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                        ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                        ToggleButton2ColorChange();
                        ;

                    }
*/

                    if (isValidCp == true && isValidE == true && isValidP == true && isValidPh == true && CheckedOrNot == true && CheckedOrNotToggle2 == true) {


                        //Toast toast = Toast.makeText(getApplicationContext(), "CheckValidPH", Toast.LENGTH_SHORT);


                        try {
                            postdata();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        // Toast.makeText(getApplicationContext(), "its false", Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });


    }

    private void TextWatchers() {

        ed_email.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = ed_email.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    ed_email.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    ed_email.setBackgroundResource(R.drawable.red_input_feild);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = ed_email.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    ed_email.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    ed_email.setBackgroundResource(R.drawable.red_input_feild);
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = ed_email.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    ed_email.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    ed_email.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        password_ed.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = password_ed.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    password_ed.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    password_ed.setBackgroundResource(R.drawable.red_input_feild);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = password_ed.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    password_ed.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    password_ed.setBackgroundResource(R.drawable.red_input_feild);
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = password_ed.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    password_ed.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    password_ed.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        mConfirmPassword.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = mConfirmPassword.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    mConfirmPassword.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    mConfirmPassword.setBackgroundResource(R.drawable.red_input_feild);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = mConfirmPassword.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    mConfirmPassword.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    mConfirmPassword.setBackgroundResource(R.drawable.red_input_feild);
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = mConfirmPassword.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    mConfirmPassword.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    mConfirmPassword.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        mEdtPhoneNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = mEdtPhoneNumber.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 < 10) {
                    //red
                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);

                } else {
                    //green
                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_green);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });




       /* mEdtPhoneNumber.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = mEdtPhoneNumber.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    mEdtPhoneNumber.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    mEdtPhoneNumber.setBackgroundResource(R.drawable.red_input_feild);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = mEdtPhoneNumber.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    mEdtPhoneNumber.setBackgroundResource(R.drawable.ic_active_edittext);



                } else {
                    //RED

                    mEdtPhoneNumber.setBackgroundResource(R.drawable.red_input_feild);
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = mEdtPhoneNumber.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    mEdtPhoneNumber.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    mEdtPhoneNumber.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });*/


    }

    private void checkFieldsForEmptyValues() {

        String s1 = ed_email.getText().toString();
        String s2 = password_ed.getText().toString();
        String s3 = mConfirmPassword.getText().toString();
        String s4 = mEdtPhoneNumber.getText().toString();

        if (s1.length() > 0 && s2.length() > 0 && s4.length() > 0 && s3.length() > 0 && CheckedOrNot == true && CheckedOrNotToggle2 == true) {

            button_ok.setTextColor(Color.parseColor("#FFFFFF"));
            button_ok.setBackgroundResource(R.drawable.border_confirm);
        } else {


            button_ok.setBackgroundResource(R.drawable.round_corner_black);
        }
    }

    private void ButtonColorChange() {
        if (isValidCp && isValidE && isValidP && isValidPh) {
            if (CheckedOrNot == true && CheckedOrNotToggle2 == true) {
                button_ok.setTextColor(Color.parseColor("#FFFFFF"));
                button_ok.setBackgroundResource(R.drawable.border_confirm);

            }


        }
    }

    private boolean Toggleval() {

        ToggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mEdtPhoneNumber.clearFocus();


                if (ToggleButton2.isChecked()) {

                    CheckedOrNotToggle2 = true;

                    Rb_one = true;
                    Rb_two = false;
                    Rb_three = true;
                    Rb_four = false;
                    Rb_five = true;
                    Rb_six = false;
                    checkFieldsForEmptyValues();
                    //   Toast.makeText(Registration_One.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                } else {

                    CheckedOrNotToggle2 = false;
                    Rb_one = false;
                    Rb_two = true;
                    Rb_three = false;
                    Rb_four = true;
                    Rb_five = false;
                    Rb_six = true;
                    checkFieldsForEmptyValues();
                    //Toast.makeText(Registration_One.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }

            }
        });
        return CheckedOrNotToggle2;

    }

    /* private void TickImageTextWatch() {
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
                 //  String answerString = password_ed.getText().toString();
 *//*
                if (answerString.length() >= 8) {

                    Tick.setImageResource(R.drawable.ic_right_small);

                } else {

                    Tick.setImageResource(R.drawable.ic_i_icon);
                }*//*

                String pass = password_ed.getText().toString();
                String cpass = mConfirmPassword.getText().toString();
                if (pass.equals(cpass)) {
                    Tick.setVisibility(View.VISIBLE);
                    Tick.setImageResource(R.drawable.ic_right_small);

                    mEdtPhoneNumber.setFocusable(true);
                    //   Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


                } else {

                    Tick.setVisibility(View.GONE);
                }
            }
        };
        password_ed.addTextChangedListener(textWatcher);


    }

    private void TickChange() {

    }
*/
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

                    //Info.setImageResource(R.drawable.ic_right_small);

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

                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);
                } else {
                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_green);
                }
            }
        });
        mEdtPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);

                } else {
                    mLinearLayout.setBackgroundResource(R.drawable.registration_phone_green);


                }
            }
        });
        mEdtcountryCode.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = mEdtcountryCode.getText().length();

                // mEdtcountryCode.setText("+39");

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

    private boolean ToggleValidations() {

       /* ToggleButton3.setOnClickListener(new View.OnClickListener() {
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
        });*/
        ToggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEdtPhoneNumber.clearFocus();


                if (ToggleButton1.isChecked()) {

                    CheckedOrNot = true;

                    NegativePositve_term = true;


                    threeradiopositiveterm = true;
                    // System.out.println("gdgdvdcbbdb:::" + threeradiopositiveterm);

                    //Toast toast = Toast.makeText(getApplicationContext(), "Hello checked!!", Toast.LENGTH_SHORT);

                    NegativePositve_term = true;
                    threeradiopositiveterm = true;
                    Negative_First_term = false;
                    fourradionegativeterm = false;
                    checkFieldsForEmptyValues();

                    //   Toast.makeText(Registration_One.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                } else {

                    CheckedOrNot = false;
                    NegativePositve_term = false;
                    threeradiopositiveterm = false;
                    Negative_First_term = true;
                    fourradionegativeterm = true;
                    checkFieldsForEmptyValues();
                    //Toast.makeText(Registration_One.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }

            }
        });
        return CheckedOrNot;

    }

    private void Awesomevalidations() {
        awesomeValidation.addValidation(Registration_One.this, R.id.et_email, "", R.string.nameerror);
        awesomeValidation.addValidation(Registration_One.this, R.id.et_password, "", R.string.emailerror);
        awesomeValidation.addValidation(Registration_One.this, R.id.et_confirmpassword, "", R.string.nameerror);
        awesomeValidation.addValidation(Registration_One.this, R.id.et_phonenumber, "", R.string.nameerror);

    }

    private void postdata() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Registration_One.this);
            return;
        }


        showProgressbar();
        String prefixPhone = "000";
        if (mEdtcountryCode.getText().toString().length() > 0) {
            prefixPhone = mEdtcountryCode.getText().toString();
        }

        String phno = mEdtPhoneNumber.getText().toString();

        String fullPhone_number = prefixPhone;
        fullPhone_number = fullPhone_number.concat(phno);

        Log.e("SURESH", "Full telPh---" + fullPhone_number);


        RegistrationRequest req = new RegistrationRequest();

        req.setEmail(ed_email.getText().toString());

        req.setPassword(password_ed.getText().toString());
        //  req.setTel(mEdtPhoneNumber.getText().toString());
        req.setTel(fullPhone_number);

        //Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();


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
                        Log.d("error message", "Utente già registrato.");

                        //   error_Alertbox("Utente già registrato.");

                        AppUtils.error_Alert("Utente già registrato.", context, alertDialog, Registration_One.this);


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
                dismissProgressbar();
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });

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
                       /* Toast.makeText(context, "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();*/
                    }
                });
        progressbar_hud.show();


    }

    private void getLoginApi() {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Registration_One.this);
            return;
        }

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

                        PreferenceUtil.clearSharedPreferences(context);


                        PreferenceUtil.getInstance().saveInt(context, Constants.USER_ID, res.getData().getToken().getId());
                        PreferenceUtil.getInstance().saveString(context, Constants.USER_CODE, res.getData().getToken().getCode());
                        PreferenceUtil.getInstance().saveString(context, Constants.REG_TOKEN, IDTOKEN);

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
        View dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.custom_error_alert, viewGroup, false);

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
            //   ed_email.setError("Enter valid email.");
            AppUtils.error_Alert("Inserisci una mail valida.", context, alertDialog, Registration_One.this);
            ed_email.setBackgroundResource(R.drawable.registration_input_red);
            // requestFocus(ed_email);
            return false;
        } else {


            ed_email.setBackgroundResource(R.drawable.registration_input);


            return true;

        }


    }

    private boolean validatePassword() {
        String pwd = password_ed.getText().toString().trim();
        //   String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        // String regexPassword = "^(?=.*[A-Z])(?=.*\d)[a-zA-Z\d!\"#$%&'()*+,\-./:;<=>?@[\]\^_{|}~]{8,}$";
        // final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\^_{|}~]{8,}$";

        if (TextUtils.isEmpty(pwd) || !pwd.matches(PASSWORD_PATTERN)) {
            Info.setVisibility(View.VISIBLE);
            //password_ed.setError("You must have 8 characters in your password");
            AppUtils.error_Alert("La password deve contenere almeno 8 caratteri, una lettera maiuscola e un numero", context, alertDialog, Registration_One.this);

            password_ed.setBackgroundResource(R.drawable.registration_input_red);
            return false;
        } else {
            password_ed.setBackgroundResource(R.drawable.registration_input);

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
            Tick.setVisibility(View.GONE);

            //mConfirmPassword.setError("Password Not matching.");
            /* AppUtils.error_Alert("Password Not matching.", context, alertDialog, Registration_One.this);*/

            mConfirmPassword.setBackgroundResource(R.drawable.registration_input_red);

            AppUtils.error_Alert("Attenzione! La password inserita non è corretta. Riprova!", context, alertDialog, Registration_One.this);

           /* ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.confirmpasswordalert, viewGroup, false);

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
            });*/
            return false;
        } else {
            mConfirmPassword.setBackgroundResource(R.drawable.registration_input);
            Info.setVisibility(View.GONE);
            Tick.setVisibility(View.VISIBLE);
            Tick.setImageResource(R.drawable.ic_right_small);
            mConfirmPassword.clearFocus();
            mEdtPhoneNumber.setFocusable(true);


            // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

            return true;

        }


    }


    private boolean validatePhoneNumber() {
        String phno = mEdtPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(phno) || phno.length() != 10) {
            //mEdtPhoneNumber.setError("Enter valid phone number");
            AppUtils.error_Alert("Enter valid phone number", context, alertDialog, Registration_One.this);

            mLinearLayout.setBackgroundResource(R.drawable.registration_phone_red);
            return false;
        } else {
            mLinearLayout.setBackgroundResource(R.drawable.registration_phone_green);
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
        dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.term, viewGroup, false);

        btn_done = (Button) dialogView.findViewById(R.id.confirm);

        TV_Leggi = (TextView) dialogView.findViewById(R.id.leggi);

        TV_Leggi2 = (TextView) dialogView.findViewById(R.id.leggi2);
        // addListenerOnButton();
        radioOneButton = (RadioButton) dialogView.findViewById(R.id.RadioButton_one);
        RadiobtntermTwo = (RadioButton) dialogView.findViewById(R.id.radio_btn_two_term);
        radioTwoButton = (RadioButton) dialogView.findViewById(R.id.RadioButton_two);

        radiofourTermButton = (RadioButton) dialogView.findViewById(R.id.radiobtnfour_term);


        RadioTerm = (RadioGroup) findViewById(R.id.radio_term);
        RadioTermTwo = (RadioGroup) findViewById(R.id.radiogrp_termtwo);

        if (Negative_First_term) {

            RadiobtntermTwo.setChecked(true);
        }

        if (NegativePositve_term) {

            radioOneButton.setChecked(true);
        }

        if (threeradiopositiveterm) {
            radioTwoButton.setChecked(true);

        }
        if (fourradionegativeterm) {
            radiofourTermButton.setChecked(true);

        }
       /* radioOneButton.setChecked(true);
        radioTwoButton.setChecked(true);
*/


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




              /*  radioSexGroup = (RadioGroup) dialogView.findViewById(R.id.radio_term);
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) dialogView.findViewById(selectedId);*/

                if (Negative_First_term || fourradionegativeterm) {
                    CheckedOrNot = false;

                    // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();


                    ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
                    //ToggleButton1.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Si prega di verificare tutti i termini e condizioni", Toast.LENGTH_SHORT).show();

                    ToggleButtononeColorChange();
                    checkFieldsForEmptyValues();

                }

                if (NegativePositve_term && threeradiopositiveterm) {
                    CheckedOrNot = true;

                    // ToggleButton1.setEnabled(false);
                    // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                    ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_green));
                    checkFieldsForEmptyValues();
                    ToggleButtononeColorChange();

                }


                if (!NegativePositve_term || !threeradiopositiveterm) {
                    // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                    CheckedOrNot = false;

                    ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));

                    Toast.makeText(getApplicationContext(), "Si prega di verificare tutti i termini e condizioni", Toast.LENGTH_SHORT).show();
                    //   ToggleButton1.setEnabled(false);


                    ToggleButtononeColorChange();

                }

                alertDialog.dismiss();


            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str = "";
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.RadioButton_one:
                if (checked)
                    str = "Android Selected";

                NegativePositve_term = true;

                System.out.println("gdgdvdcbbdbkkkk:::" + NegativePositve_term);


                Negative_First_term = false;


                break;
            case R.id.radio_btn_two_term:
                if (checked)
                    str = "AngularJS Selected";

                Negative_First_term = true;
                NegativePositve_term = false;
                break;

            case R.id.RadioButton_two:
                if (checked)
                    str = "Android Selected";

                threeradiopositiveterm = true;
                System.out.println("gdgdvdcbbdb:::" + threeradiopositiveterm);

                //Toast toast = Toast.makeText(getApplicationContext(), "Hello checked!!", Toast.LENGTH_SHORT);

                fourradionegativeterm = false;


                break;
            case R.id.radiobtnfour_term:
                if (checked)

                    str = "AngularJS Selected";

                fourradionegativeterm = true;
                threeradiopositiveterm = false;


                //   NegativePositiveFour = true;
                break;

        }


        //  Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked_Two(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str = "";
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.radiobtnfour_term:
                if (checked)
                    str = "AngularJS Selected";

                Negative_Second_Term = true;
                break;

        }


        //  Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked_Four(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str = "";
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.rb_one:
                if (checked)
                    str = "AngularJS Selected";

                Rb_one = true;

                Rb_two = false;


                Negative_Second_Manifesto = true;
                break;

            case R.id.rb_two:
                if (checked)
                    str = "AngularJS Selected";
                Rb_two = true;

                Rb_one = false;


                Negative_Second_Manifesto = true;
                break;
            case R.id.rb_three:
                if (checked)
                    str = "AngularJS Selected";

                Rb_three = true;
                Rb_four = false;

                Negative_Second_Manifesto = true;
                break;
            case R.id.rb_four:
                if (checked)
                    str = "AngularJS Selected";

                Rb_four = true;

                Rb_three = false;

                Negative_Four_Manifesto = true;
                break;
            case R.id.rb_five:
                if (checked)
                    str = "AngularJS Selected";

                Negative_Four_Manifesto = true;

                Rb_five = true;

                Rb_six = false;

                break;
            case R.id.rb_six:
                if (checked)
                    str = "AngularJS Selected";
                Rb_six = true;

                Rb_five = false;

                Negative_Six_Manifesto = true;
                break;
        }


        //  Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }


    public void onRadioButtonClicked_Three(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str = "";
        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.rb_two:
                if (checked)
                    str = "AngularJS Selected";

                Negative_Second_Manifesto = true;
                break;
            case R.id.rb_four:
                if (checked)
                    str = "AngularJS Selected";

                Negative_Four_Manifesto = true;
                break;
            case R.id.rb_six:
                if (checked)
                    str = "AngularJS Selected";

                Negative_Six_Manifesto = true;
                break;
        }


        //  Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    private void thirdPopUp() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.manifesto, viewGroup, false);

        btn_done = (Button) dialogView.findViewById(R.id.confirm);

        //Img_done=(ImageView) dialogView.findViewById(R.id.img_done);

        // pwd_alert_title = (TextView) dialogView.findViewById(R.id.forgot_pwd_title);

        //Now we need an AlertDialog.Builder object
        RadioManifest_One = (RadioGroup) dialogView.findViewById(R.id.radio_manifestone);
        RadioManifest_Two = (RadioGroup) dialogView.findViewById(R.id.radio_manifesttwo);
        RadioManifest_Three = (RadioGroup) dialogView.findViewById(R.id.radio_manifestthree);

        radiomOneButton = (RadioButton) dialogView.findViewById(R.id.rb_one);
        radiomTwoButton = (RadioButton) dialogView.findViewById(R.id.rb_two);
        radiomThreeButton = (RadioButton) dialogView.findViewById(R.id.rb_three);
        radiomFourButton = (RadioButton) dialogView.findViewById(R.id.rb_four);
        radiomfiveButton = (RadioButton) dialogView.findViewById(R.id.rb_five);
        radiomsixButton = (RadioButton) dialogView.findViewById(R.id.rb_six);

        if (Rb_one) {

            //RadiobtntermTwo.setChecked(true);

            radiomOneButton.setChecked(true);
        }

        if (Rb_two) {

            radiomTwoButton.setChecked(true);
        }

        if (Rb_three) {
            radiomThreeButton.setChecked(true);

        }
        if (Rb_four) {
            radiomFourButton.setChecked(true);
            //radiofourTermButton.setChecked(true);

        }
        if (Rb_five) {
            radiomfiveButton.setChecked(true);

        }
        if (Rb_six) {
            radiomsixButton.setChecked(true);
        }
/*
        radiomOneButton.setChecked(true);
        radiomThreeButton.setChecked(true);
        radiomfiveButton.setChecked(true);*/
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
                if (Rb_two || Rb_four || Rb_six) {
                    ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
                    // ToggleButton2.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Si prega di verificare tutti i termini e condizioni", Toast.LENGTH_SHORT).show();
                    ToggleButton2ColorChange();
                }
                if (Rb_one && Rb_three && Rb_five) {

                    CheckedOrNotToggle2 = true;

                    ToggleButton2.setEnabled(true);
                    //  ToggleButton2.setEnabled(false);

                    // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                    ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_switch_green));

                    ToggleButton2ColorChange();

                    checkFieldsForEmptyValues();

                }
                if (!Rb_one && !Rb_three && !Rb_five) {
                    CheckedOrNotToggle2 = false;
                    // CheckedOrNotToggle2 = true;
                    // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();
                    ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
                    // ToggleButton2.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Si prega di verificare tutti i termini e condizioni", Toast.LENGTH_SHORT).show();
                    ToggleButton2ColorChange();
                    checkFieldsForEmptyValues();
                }

                if (!Rb_one || !Rb_three | !Rb_five) {
                    CheckedOrNotToggle2 = false;
                    // Toast.makeText(getApplicationContext(), "sai is thop", Toast.LENGTH_SHORT).show();

                    ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
                    //ToggleButton2.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Si prega di verificare tutti i termini e condizioni", Toast.LENGTH_SHORT).show();
                    ToggleButton2ColorChange();

                    checkFieldsForEmptyValues();
                }


            }
        });
    }


    private void ToggleButtononeColorChange() {


        ToggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_selector));
                    CheckedOrNot = true;


                } else {
                    ToggleButton1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
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
                            CheckedOrNotToggle2 = true;
                        } else {
                            ToggleButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_redswitch));
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
                            dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.error_alert, viewGroup, false);

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
                dialogView = LayoutInflater.from(Registration_One.this).inflate(R.layout.info_alert, viewGroup, false);

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


        Intent I = new Intent(Registration_One.this, PersonalDetails.class);
        I.putExtra("tokenid", IDTOKEN);
        System.out.println("saikumar::::" + IDTOKEN);
        startActivity(I);


    }


    private void setUpView() {
        Info = (ImageButton) findViewById(R.id.info);
        Tick = (ImageButton) findViewById(R.id.tick);
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
