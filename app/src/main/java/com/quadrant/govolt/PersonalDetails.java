package com.quadrant.govolt;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.BlurView;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.ErrorUtils;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.interfaces.RegistrationAddingInterface;
import com.quadrant.interfaces.RegistrationInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.RegistrationAddingRequest;
import com.quadrant.request.RegistrationRequest;
import com.quadrant.response.RegistrationAddingResponse;
import com.quadrant.response.RegistrationErrorResponse;
import com.quadrant.response.RegistrationResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonalDetails extends AppCompatActivity implements View.OnClickListener {


    private TextView Datepicker;
    private AlertDialog alertDialog;
    private View dialogView;
    private DatePicker dpDate;
    private EditText Tv_dp;
    private ImageView btn_done;
    private Context context;
    private RadioButton radioSexButton;
    String Gender;

    private EditText Cogname, Name, Codice, Location, Citta, Paese, Cap;

    private AwesomeValidation awesomeValidation;

    boolean checkortrue = false;

    private RadioGroup radioSexGroup;
    String TOKENID;

    boolean isAllnotEmpty = false;

    boolean isAllnotEmptyone = false;

    boolean isCheckedAll;
    Button per_detail_avani;
    private boolean isName = false;
    private boolean isCogName = false;
    private boolean isLocation = false;
    private boolean isCitta = false;
    private boolean isPaese = false;
    private boolean isCap = false;
    private boolean istv_dp = false;


    private boolean isAllValid = false;

    private KProgressHUD progressbar_hud;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details);

        context = this;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        Initialization();
        getData();

        addListenerOnButton();
        Textchangers();

        //   CallLoginApi();


        //  onFocusChange();

        awesomeValidation.addValidation(PersonalDetails.this, R.id.et_cogname, "", R.string.emptyerror);
        awesomeValidation.addValidation(PersonalDetails.this, R.id.et_name, "", R.string.emptyerror);
        //   awesomeValidation.addValidation(PersonalDetails.this, R.id.et_codice, "", R.string.emptyerror);
        awesomeValidation.addValidation(PersonalDetails.this, R.id.et_location, "", R.string.emptyerror);
        awesomeValidation.addValidation(PersonalDetails.this, R.id.et_citta, "", R.string.emptyerror);
        awesomeValidation.addValidation(PersonalDetails.this, R.id.et_cap, "", R.string.emptyerror);
        // Datepicker=findViewById(R.id.dp);
        if (Tv_dp.getText().toString().trim().isEmpty()) {
            //   Tv_dp.setError(getResources().getString(R.string.dateerror));
        }

        Tv_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                dialogView = LayoutInflater.from(PersonalDetails.this).inflate(R.layout.dp_alert, viewGroup, false);

                dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);
                // init

                btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);


                String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";

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

                              /*  Tv_dp.setText((dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear());
                                Tv_dp.setTextColor(Color.WHITE);
*/

                        Calendar userAge = new GregorianCalendar(dpDate.getYear(), dpDate.getMonth(), (dpDate.getDayOfMonth() + 1));
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.roll(Calendar.YEAR, -18);
                        if (minAdultAge.before(userAge)) {
                            Toast.makeText(getApplicationContext(), "L'età deve essere almeno 18 anni", Toast.LENGTH_SHORT).show();

                            Tv_dp.setText(""); //or you can use editText.setText("");

                            Tv_dp.setBackgroundResource(R.drawable.red_input_feild);

                        } else {
                            Tv_dp.setText(dpDate.getDayOfMonth() + "/" + (dpDate.getMonth() + 1) + "/" + dpDate.getYear());

                            Tv_dp.setBackgroundResource(R.drawable.ic_active_edittext);
                            Tv_dp.setTextColor(Color.WHITE);
                            //  Tv_dp.setError(null);

                        }

                        //checkFieldsForEmptyValues();
                         /*       Calendar userAge = new GregorianCalendar(dpDate.getYear(),(dpDate.getMonth() + 1),dpDate.getDayOfMonth());
                                Calendar minAdultAge = new GregorianCalendar();
                                minAdultAge.add(Calendar.YEAR, -18);
                                if (minAdultAge.before(userAge)) {
                                    Toast toast=Toast.makeText(getApplicationContext(),"18 Years Minimum",Toast.LENGTH_SHORT);;
                                } else{


                                    Tv_dp.setText((dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear());
                                    Tv_dp.setTextColor(Color.WHITE);
                                }*/

                    }
                });
            }
        });


        per_detail_avani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* startActivity(new Intent(PersonalDetails.this, Patent_Registration.class));*/
//sai


                if (isCogName) {
                    Cogname.setBackgroundResource(R.drawable.ic_active_edittext);
                } else {
                    Cogname.setBackgroundResource(R.drawable.red_input_feild);
                }
                if (isName) {
                    Name.setBackgroundResource(R.drawable.ic_active_edittext);
                } else {
                    Name.setBackgroundResource(R.drawable.red_input_feild);
                }
                if (istv_dp) {
                    Tv_dp.setBackgroundResource(R.drawable.ic_active_edittext);
                } else {
                    Tv_dp.setBackgroundResource(R.drawable.red_input_feild);
                }
                if (isLocation) {
                    Location.setBackgroundResource(R.drawable.ic_active_edittext);
                } else {
                    Location.setBackgroundResource(R.drawable.red_input_feild);
                }
                if (isCitta) {
                    Citta.setBackgroundResource(R.drawable.ic_active_edittext);
                } else {
                    Citta.setBackgroundResource(R.drawable.red_input_feild);
                }
                if (isPaese) {
                    Paese.setBackgroundResource(R.drawable.city_input);
                } else {
                    Paese.setBackgroundResource(R.drawable.small_input_button);
                }
                if (isCap) {
                    Cap.setBackgroundResource(R.drawable.city_input);
                } else {
                    Cap.setBackgroundResource(R.drawable.small_input_button);
                }


                if (!isCogName || !isName || !istv_dp || !isLocation || !isCitta || !isPaese || !isCap) {


                    //PopUP

                    AppUtils.error_Alert("Si prega di compilare tutti i campi obbligatori", context, alertDialog, PersonalDetails.this);
                }


                /*int selectedId = radioSexGroup.getCheckedRadioButtonId();

                System.out.println("uuuuu::" + selectedId);

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
                System.out.println("vvvvv::" + radioSexButton);


                if (selectedId == 2131296708) {

                    Gender = "female";

                } else if (selectedId == 2131296709) {

                    Gender = "male";
                }
*/


                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);

                if (radioSexButton.getText().toString().equals("F")) {

                    Gender = "female";
                } else if (radioSexButton.getText().toString().equals("M")) {

                    Gender = "male";
                }
                if (awesomeValidation.validate()) {
                    //  Toast.makeText(PersonalDetails.this, "Validation Successfull", Toast.LENGTH_LONG).show();
                    // Cogname.setBackgroundResource(R.drawable.red_input_feild);
                    //Cogname.setError("field is empty");
                    Name.setBackgroundResource(R.drawable.red_input_feild);
                    // Name.setError("field is empty");
                   /* Codice.setBackgroundResource(R.drawable.red_input_feild);
                    Codice.setError("field is empty");*/
                    Location.setBackgroundResource(R.drawable.red_input_feild);
                    // Location.setError("field is empty");
                    Citta.setBackgroundResource(R.drawable.red_input_feild);

                    Tv_dp.setBackgroundResource(R.drawable.red_input_feild);
                    //  Citta.setError("field is empty");
                    Cap.setBackgroundResource(R.drawable.small_input_button);
                    //  Cap.setError("field is empty");
                    Paese.setBackgroundResource(R.drawable.small_input_button);
                    //  Paese.setError("field is empty");

                    if (Tv_dp.getText().toString().trim().isEmpty())
                        //  Tv_dp.setError("field is empty");

                        checkortrue = true;
                    //  m.setBackgroundResource(R.drawable.red_input_feild);

                    //process the data further
                } else {
                    awesomeValidation.clear();
                    checkortrue = true;
                    if (checkortrue == true) {
                        // Toast.makeText(PersonalDetails.this, "Validation completed", Toast.LENGTH_LONG).show();
/*
                        Cogname.setBackgroundResource(R.drawable.edittext_image_change);
                        Name.setBackgroundResource(R.drawable.edittext_image_change);
                        Codice.setBackgroundResource(R.drawable.edittext_image_change);
                        Location.setBackgroundResource(R.drawable.edittext_image_change);
                        Citta.setBackgroundResource(R.drawable.edittext_image_change);
                        Cap.setBackgroundResource(R.drawable.edittext_image_change);
                        Paese.setBackgroundResource(R.drawable.edittext_image_change);*/

                        isCheckedAll = CheckifiledsEmpty();

                        if (!isCheckedAll) {


                          /* per_detail_avani.setTextColor(Color.parseColor("#FFFFFF"));
                            per_detail_avani.setBackgroundResource(R.drawable.border_confirm);*/
                        }
                        System.out.println("gdhsghsg:::" + isCheckedAll);

                        if (isCheckedAll == false) {

                            if (Tv_dp.getText().toString().trim().equals("Data di nascita") || Tv_dp.getText().toString().trim().equals("")) {
                                //   Tv_dp.setError(getResources().getString(R.string.dateerror));
                            } else {

                                Tv_dp.setBackgroundResource(R.drawable.ic_active_edittext);


                                //Tv_dp.setError(null);
                                try {
                                    PostData();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        //startActivity(new Intent(PersonalDetails.this, Patent_Registration.class));
                    }


                }


            }
        });


        if (isCogName || isName || isLocation || isCitta || isPaese || isCap) {


            per_detail_avani.setTextColor(Color.parseColor("#FFFFFF"));
            per_detail_avani.setBackgroundResource(R.drawable.border_confirm);


            //PopUP

            //AppUtils.error_Alert("Si prega di compilare tutti i campi obbligatori", context, alertDialog, PersonalDetails.this);
        }


    }

    private void submitButtonColor() {
        per_detail_avani.setTextColor(Color.parseColor("#FFFFFF"));
        per_detail_avani.setBackgroundResource(R.drawable.border_confirm);
    }

    private void submitButtonBlack() {
        per_detail_avani.setTextColor(Color.parseColor("#FFFFFF"));
        per_detail_avani.setBackgroundResource(R.drawable.round_corner_black);
    }

    private void Textchangers() {


        Cogname.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Cogname.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Cogname.setBackgroundResource(R.drawable.ic_active_edittext);


                } else {
                    //RED

                    Cogname.setBackgroundResource(R.drawable.red_input_feild);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Cogname.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Cogname.setBackgroundResource(R.drawable.ic_active_edittext);
                    isCogName = true;


                } else {
                    //RED

                    Cogname.setBackgroundResource(R.drawable.red_input_feild);
                    isCogName = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Cogname.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Cogname.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Cogname.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });

        //Name

        Name.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Name.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Name.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Name.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Name.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Name.setBackgroundResource(R.drawable.ic_active_edittext);
                    isName = true;

                } else {
                    //RED

                    Name.setBackgroundResource(R.drawable.red_input_feild);
                    isName = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Name.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Name.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Name.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });

        //datepicker

        Tv_dp.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Tv_dp.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Tv_dp.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Tv_dp.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Tv_dp.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Tv_dp.setBackgroundResource(R.drawable.ic_active_edittext);
                    istv_dp = true;

                } else {
                    //RED

                    Tv_dp.setBackgroundResource(R.drawable.red_input_feild);
                    istv_dp = false;
                }

                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Tv_dp.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Tv_dp.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Tv_dp.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });


        // Codice

        Codice.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Codice.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Codice.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    // Codice.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Codice.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Codice.setBackgroundResource(R.drawable.ic_active_edittext);
                    // isName = true;

                } else {
                    //RED

                    //   Codice.setBackgroundResource(R.drawable.red_input_feild);
                    // isName = false;
                }

                // checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Name.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Codice.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Codice.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
//Location
        Location.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Location.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Location.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Location.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Location.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Location.setBackgroundResource(R.drawable.ic_active_edittext);
                    isLocation = true;

                } else {
                    //RED

                    Location.setBackgroundResource(R.drawable.red_input_feild);
                    isLocation = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Location.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Location.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Location.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });

        //Citta

        Citta.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Citta.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Citta.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Citta.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Citta.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Citta.setBackgroundResource(R.drawable.ic_active_edittext);
                    isCitta = true;

                } else {
                    //RED

                    Citta.setBackgroundResource(R.drawable.red_input_feild);
                    isCitta = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Citta.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Citta.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Citta.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });

        // Paese
        Paese.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Paese.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Paese.setBackgroundResource(R.drawable.city_input);

                } else {
                    //RED

                    Paese.setBackgroundResource(R.drawable.small_input_button);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Paese.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Paese.setBackgroundResource(R.drawable.city_input);
                    isPaese = true;

                } else {
                    //RED

                    Paese.setBackgroundResource(R.drawable.small_input_button);
                    isPaese = false;
                }


                checkFieldsForEmptyValues();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Paese.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Paese.setBackgroundResource(R.drawable.city_input);

                } else {
                    //RED

                    Paese.setBackgroundResource(R.drawable.small_input_button);

                }
            }
        });


        // Cap

        Cap.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Cap.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Cap.setBackgroundResource(R.drawable.city_input);

                    per_detail_avani.setTextColor(Color.parseColor("#FFFFFF"));
                    per_detail_avani.setBackgroundResource(R.drawable.border_confirm);

                } else {
                    //RED

                    Cap.setBackgroundResource(R.drawable.small_input_button);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Cap.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Cap.setBackgroundResource(R.drawable.city_input);
                    isCap = true;

                } else {
                    //RED

                    Cap.setBackgroundResource(R.drawable.small_input_button);
                    isCap = false;
                }

                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Cap.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Cap.setBackgroundResource(R.drawable.city_input);

                } else {
                    //RED

                    Cap.setBackgroundResource(R.drawable.small_input_button);

                }
            }
        });

    }

    private void checkFieldsForEmptyValues() {
        //  private EditText Cogname, Name, Codice, Location, Citta, Paese, Cap;

        String s1 = Cogname.getText().toString();
        String s2 = Name.getText().toString();
        //  String s3 = Codice.getText().toString();
        String s4 = Location.getText().toString();
        String s5 = Citta.getText().toString();
        String s6 = Paese.getText().toString();
        String s7 = Cap.getText().toString();
        String s8 = Tv_dp.getText().toString();


        if (s1.equals("") || s2.equals("") || s4.equals("") || s5.equals("") || s6.equals("") || s7.equals("") || s8.equals("")) {
            per_detail_avani.setBackgroundResource(R.drawable.round_corner_black);
        } else {
            per_detail_avani.setTextColor(Color.parseColor("#FFFFFF"));
            per_detail_avani.setBackgroundResource(R.drawable.border_confirm);
        }
    }

    private boolean CheckifiledsEmpty() {

        isAllnotEmpty = false;

        if (TextUtils.isEmpty(Cogname.getText().toString())) {

            isAllnotEmpty = true;
            // Cogname.setError("field is empty");
            Cogname.setBackgroundResource(R.drawable.red_input_feild);
            return isAllnotEmpty;

        }
        if (TextUtils.isEmpty(Name.getText().toString())) {

            Name.requestFocus();

            //   Name.setError("field is empty");
            Name.setBackgroundResource(R.drawable.red_input_feild);

            isAllnotEmpty = true;

            //    return isAllnotEmpty;
        }

        if (TextUtils.isEmpty(Location.getText().toString())) {

            Location.requestFocus();

            isAllnotEmpty = true;
            //  Location.setError("field is empty");
            return isAllnotEmpty;

        }
        if (TextUtils.isEmpty(Citta.getText().toString())) {
            Citta.requestFocus();

            //   Citta.setError("field is empty");

            isAllnotEmpty = true;
            return isAllnotEmpty;
        }

        if (TextUtils.isEmpty(Paese.getText().toString())) {
            Paese.requestFocus();

            isAllnotEmpty = true;
            //   Paese.setError("field is empty");

            return isAllnotEmpty;

        }
        if (TextUtils.isEmpty(Cap.getText().toString())) {
            Cap.requestFocus();

            isAllnotEmpty = true;
            // Cap.setError("field is empty");
            return isAllnotEmpty;

        }


        return isAllnotEmpty;


        //    isAllnotEmptyone=false;

    }

    private void getData() {

        TOKENID = getIntent().getStringExtra("tokenid");

        System.out.println("ttttttt::::" + TOKENID);
    }

    private void CallLoginApi() {


    }

    private void addListenerOnButton() {


    }

    private void PostData() throws ParseException {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, PersonalDetails.this);
            return;
        }


        showProgressbar();

        RegistrationAddingRequest req = new RegistrationAddingRequest();

        req.setSurname(Cogname.getText().toString());

        req.setName(Name.getText().toString());
        //radio
        req.setSex(Gender);
        //Dob
        String dob = Tv_dp.getText().toString();
        //  String dateofbirth = dob+"12:00";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        Date date = (Date) formatter.parse(dob);
        System.out.println("Todayis...." + date.getTime());


//        req.setBirth(date.toString());
        req.setBirth(String.valueOf(date.getTime()));
        req.setResidence(Location.getText().toString());
        req.setResidence_city(Citta.getText().toString());
        req.setResidence_country(Paese.getText().toString());
        req.setCap(Cap.getText().toString());
        req.setCf(Codice.getText().toString());
        // req.setSex(Gender);
        //req.set(Citta.getText().toString());

        //Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();


        //

        String token = PreferenceUtil.getInstance().getString(context, Constants.REG_TOKEN, "");

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        String bearer_authorization = "Bearer " + token;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RegistrationAddingInterface geo_details = retrofit.create(RegistrationAddingInterface.class);
        Call<RegistrationAddingResponse> resultRes = geo_details.GetResponseAddingReg(Constants.TOKEN, bearer_authorization, req);
        resultRes.enqueue(new Callback<RegistrationAddingResponse>() {
            @Override
            public void onResponse(Call<RegistrationAddingResponse> call, Response<RegistrationAddingResponse> response) {


                if (!response.isSuccessful()) {
                   /* Log.e(TAG, "--Response code---"+response.code());
                    Log.e(TAG, "--Response ---"+response.body());
                    dismissProgressbar();
*/
                    if (response.code() != 200) {

                        //Toast.makeText(context, ""+error.getError().getDescription(), Toast.LENGTH_SHORT).show();
                        //   RegistrationErrorResponse error = ErrorUtils.parseErrorReg(response);
                        // â€¦ and use it to show error information

                        // â€¦ or just log the issue like weâ€™re doing :)
                        //   Log.d("error message", error.getError().getDescription());

                        // error_Alertbox(error.getError().getDescription());


                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {

                        Intent I = new Intent(PersonalDetails.this, Patent_Registration.class);

                        I.putExtra("Tokenid", TOKENID);
                        startActivity(I);
/*
                        Toast.makeText(getApplicationContext(), "Hello Javatpoint", Toast.LENGTH_SHORT).show();*/

                        //  launchPesonalDetails();

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

                      */
                    }


                }
                dismissProgressbar();

            }

            @Override
            public void onFailure(Call<RegistrationAddingResponse> call, Throwable t) {
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

    private void onFocusChange() {
        Cogname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {


                    Cogname.setBackgroundResource(R.drawable.edittext_border);


                } else {


                }

            }
        });

        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {


                    Name.setBackgroundResource(R.drawable.edittext_border);


                } else {


                }


            }
        });

        Codice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Codice.setBackgroundResource(R.drawable.edittext_border);


                } else {


                }


            }
        });


        Location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Location.setBackgroundResource(R.drawable.edittext_border);

                } else {


                }


            }
        });

        Citta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {


                    Citta.setBackgroundResource(R.drawable.edittext_border);


                } else {


                }


            }
        });

        Cap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Cap.setBackgroundResource(R.drawable.edittext_border);


                } else {


                }


            }
        });

    }

    private void Initialization() {
        Tv_dp = findViewById(R.id.dp_et);
        Cogname = findViewById(R.id.et_cogname);
        Name = findViewById(R.id.et_name);
        Codice = findViewById(R.id.et_codice);
        Location = findViewById(R.id.et_location);
        Citta = findViewById(R.id.et_citta);
        Cap = findViewById(R.id.et_cap);

        radioSexGroup = (RadioGroup) findViewById(R.id.radio_sex);

        Paese = (EditText) findViewById(R.id.et_paese);
        per_detail_avani = (Button) findViewById(R.id.done_avani);
    }

    @Override
    public void onClick(View v) {

    }


/*
    class BlurAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        private final String TAG = BlurAsyncTask.class.getName();

        protected Bitmap doInBackground(Void... arg0) {

            Bitmap map = AppUtils.takeScreenShot(PersonalDetails.this);
            Bitmap fast = new BlurView().fastBlur(map, 30);

            return fast;
        }


        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                final Drawable draw = new BitmapDrawable(getResources(), result);
                Window window = alertDialog.getWindow();
                window.setBackgroundDrawable(draw);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);
                alertDialog.show();

            }

        }
    }
*/

}