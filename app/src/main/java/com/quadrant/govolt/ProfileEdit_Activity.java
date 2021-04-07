package com.quadrant.govolt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
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
import com.quadrant.interfaces.RequestInterface;
import com.quadrant.interfaces.UserInformation;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.LoginRequest;
import com.quadrant.request.ProfileRequest;
import com.quadrant.response.LoginErrorResponse;
import com.quadrant.response.LoginResonse;
import com.quadrant.response.ProfileRequestResponse;
import com.quadrant.response.UserInfoResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileEdit_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back_btn, arrow_img, buttonOk_Profile;
    private LinearLayout layout_one, layout_two;
    private EditText edittext_one, edittext_two, ed_country_code, ed_phone_no, ed_p_name, ed_p_surname, ed_p_country,
            ed_p_city, ed_taxid_cf;
    private TextView date_of_birth;
    private DatePicker dpDate;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView title;
    private Context context;
    private String fullname, address, telephone, email, userfullname;

    private KProgressHUD progressbar_hud;

    private String TAG = "ProfileEdit_Activity";
    private CircleImageView _profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_personal);
        context = this;

        SetUpView();


        ImageView back = (ImageView) findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Dati personali");

        ImageView _homeIcon = (ImageView)findViewById(R.id.home_icon);
        _homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileEdit_Activity.this, HomeActivity.class);
                startActivity(i);
            }
        });



        ContryCodePhoneNumber();

        try {
            SetValues();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void SetValues() throws ParseException {
         String name = PreferenceUtil.getInstance().getString(context, Constants.NAME, "");
        String surname = PreferenceUtil.getInstance().getString(context, Constants.SUR_NAME, "");
        fullname = name + " " + surname;

        address = PreferenceUtil.getInstance().getString(context, Constants.RESIDENCY, "");
        telephone = PreferenceUtil.getInstance().getString(context, Constants.TELEPHONE, "");
        email = PreferenceUtil.getInstance().getString(context, Constants.USER_EMAIL, "");
        userfullname = PreferenceUtil.getInstance().getString(context, Constants.NAME, "");
        String dateofBirth = PreferenceUtil.getInstance().getString(context, Constants.BIRTH, "");
        String recidency = PreferenceUtil.getInstance().getString(context, Constants.RESIDENCY, "");
        String zip_cp = PreferenceUtil.getInstance().getString(context, Constants.ZIPCODE_CAP, "");
        String country = PreferenceUtil.getInstance().getString(context, Constants.COUNTRY, "");
        String city = PreferenceUtil.getInstance().getString(context, Constants.CITY, "");
        System.out.println("yyyyyy::::"+city);
        String tax_cf = PreferenceUtil.getInstance().getString(context, Constants.TAXID_CF, "");

        System.out.println("zzzzzz::::"+tax_cf);


        if (name != null) {
            ed_p_name.setText("" +surname );
        }
        if (surname != null) {
            ed_p_surname.setText("" + name);
        }
        if (dateofBirth != null && dateofBirth !="") {

            System.out.println("dateofbirth:::"+dateofBirth);
            String dateStr;
            if (dateofBirth.contains("GMT")){
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.format(new Date(dateofBirth));

                System.out.println("dateformatcheck::::"+sdf);
                //String ndob = new SimpleDateFormat("EEE, d MMM HH:mm:ss Z yyyy").format(dateofBirth);
                dateStr = dateofBirth;
            }else{
                SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                // Create a calendar object that will convert the date and time value in milliseconds to date.
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(dateofBirth));
                dateStr =  formatter.format(calendar.getTime());
            }

            System.out.println("ghhhhghg:::"+dateStr);
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            Date date = (Date)formatter.parse(dateStr);
            System.out.println(date);

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+01:00"));
            cal.setTime(date);
            String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
            System.out.println("formatedDate : " + formatedDate);


//:Tue Sep 04 00:00:00 GMT+05:30 1984
            date_of_birth.setText("" + formatedDate);




            System.out.println("convertdate::::"+dateofBirth);
        }
        if (recidency != null) {
            edittext_one.setText("" + recidency);
        }
        if (zip_cp != null) {
            edittext_two.setText("" + zip_cp);
        }
        if (country != null) {
            ed_p_country.setText("" + country);
        }



       String prefix = telephone.substring(0, 3);
        String phone = telephone.substring(3, telephone.length());

       // Log.e(TAG, "Prefix---"+prefix+"==="+"no--"+phone);


        if(prefix!=null){
            ed_country_code.setText(""+prefix);
        }

        if (phone != null) {
            ed_phone_no.setText("" + phone);
        }



        if (city != null) {
            ed_p_city.setText("" + city);
        }

        if (tax_cf != null) {
            ed_taxid_cf.setText("" + tax_cf);
        } else {
            ed_taxid_cf.setText("");
        }


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

    private void ContryCodePhoneNumber() {
        ed_country_code.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Integer textlength1 = ed_country_code.getText().length();

                /*if (textlength1 >= 3) {
                    ed_phone_no.requestFocus();
                }*/
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



    private void SetUpView() {
        layout_one = (LinearLayout) findViewById(R.id.loc_ll);
        layout_two = (LinearLayout) findViewById(R.id.layout_ll);
        edittext_one = (EditText) findViewById(R.id.edit_one);
        edittext_two = (EditText) findViewById(R.id.edit_two);
        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        ed_country_code = (EditText) findViewById(R.id.country_code);
        ed_phone_no = (EditText) findViewById(R.id.phone_no);
        ed_p_name = (EditText) findViewById(R.id.p_name);
        ed_p_surname = (EditText) findViewById(R.id.p_surname);
        ed_p_country = (EditText) findViewById(R.id.p_country);
        ed_p_city = (EditText) findViewById(R.id.p_city);
        ed_taxid_cf = (EditText) findViewById(R.id.taxid_cf);

        buttonOk_Profile = (ImageView)findViewById(R.id.buttonOk_pro);
        buttonOk_Profile.setOnClickListener(this);

        arrow_img = (ImageView) findViewById(R.id.date_img);
        arrow_img.setOnClickListener(this);

        back_btn = (ImageView) findViewById(R.id.back_img);
        back_btn.setOnClickListener(this);
        date_of_birth.setOnClickListener(this);


        _profileImg = (CircleImageView)findViewById(R.id.profile_image);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.date_img:
                DatePickerOne();
                break;
            case R.id.buttonOk_pro:



                if(ed_p_name.getText().toString().length()>0) {
                    if (ed_p_surname.getText().toString().length() > 0) {
                        if (date_of_birth.getText().toString().length() > 0) {
                            if (edittext_one.getText().toString().length() > 0) {
                                if (edittext_two.getText().toString().length() > 0) {
                                    if (ed_p_city.getText().toString().length() > 0) {
                                        if (ed_p_country.getText().toString().length() > 0) {
                                            if (ed_phone_no.getText().toString().length() > 0) {
                                                if (ed_country_code.getText().toString().length() > 0) {
                                                    try {
                                                        Buttonclick();
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);
                                                }
                                            } else {
                                                AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                                            }
                                        } else {
                                            AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                                        }

                                    } else {
                                        AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                                    }

                                } else {
                                    AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                                }

                            } else {
                                AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                            }

                        } else {
                            AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                        }

                    } else {
                        AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                    }
                }else{
                        AppUtils.Alert_information("Per favore compila tutti i campi", context, alertDialog, ProfileEdit_Activity.this);

                    }





                break;
        }
    }

    private void Buttonclick() throws ParseException {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, ProfileEdit_Activity.this);
            return;
        }


     //   Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();


        showProgressbar();

        ProfileRequest req = new ProfileRequest();

        req.setName(ed_p_name.getText().toString());
        req.setSurname(ed_p_surname.getText().toString());

        String dob = date_of_birth.getText().toString();

        System.out.println("dob::::::"+dob);



       /* DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = (Date)formatter.parse(dob);
        System.out.println("Todayis...." +date.toString());
*/
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        Date date = (Date) formatter.parse(dob);
        System.out.println("Todayis...." + date.getTime());


//        req.setBirth(date.toString());

        req.setBirth(String.valueOf(date.getTime()));

        req.setCf(ed_taxid_cf.getText().toString());
        String  res= edittext_one.getText().toString();
        req.setResidence(res);
        req.setCap(edittext_two.getText().toString());
        req.setResidence_city(ed_p_city.getText().toString());
        req.setResidence_country(ed_p_country.getText().toString());


        String phno = ed_phone_no.getText().toString();

        String _prefixPhone = "000";
        if(ed_country_code.getText().toString().length()>0){
            _prefixPhone = ed_country_code.getText().toString();
        }

        String fullPhone_number = _prefixPhone;
        fullPhone_number = fullPhone_number.concat(phno);

        Log.e("SURESH", "Full telPh---"+fullPhone_number);


        req.setTelephone(fullPhone_number);
        //req.setTelephone(ed_phone_no.getText().toString());



        final Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RequestInterface register_details = retrofit.create(RequestInterface.class);

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        // String bearer_authorization = "Bearer " + TokenId;
        String bearer_authorization = "Bearer " + authorization;

        Call<ProfileRequestResponse> resultRes = register_details.GetResponsereq(Constants.TOKEN,bearer_authorization, req);
        resultRes.enqueue(new Callback<ProfileRequestResponse>() {
            @Override
            public void onResponse(Call<ProfileRequestResponse> call, Response<ProfileRequestResponse> response) {



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
                        //Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

                        ProfileRequestResponse res = response.body();

                        AppUtils.UserStatusApiCall(context);

                        Log.e(TAG, "---DONE--");

                       // RefreashScreen();


                    }

                    dismissProgressbar();
                }
            }

            @Override
            public void onFailure(Call<ProfileRequestResponse> call, Throwable t) {
                Log.e(TAG, "--Fail---" + t.getMessage());
                dismissProgressbar();

            }


        });

    }

    private void RefreashScreen() {
        //showProgressbar();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {

                if (android.os.Build.VERSION.SDK_INT >= 11){
                    //Code for recreate
                    recreate();

                }else{
                    //Code for Intent
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }

               // dismissProgressbar();

            }

        }, 2000);
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

    private void DatePickerOne() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(ProfileEdit_Activity.this).inflate(R.layout.dp_alert_one, viewGroup, false);

        dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);
        // init

        ImageView btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);


        String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";

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
                Calendar userAge = new GregorianCalendar(dpDate.getYear(), dpDate.getDayOfMonth(), (dpDate.getMonth() + 1));
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    Toast.makeText(getApplicationContext(), "L'età deve essere almeno 18 anni", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    date_of_birth.setText(""); //or you can use editText.setText("");

                //    date_of_birth.setBackgroundResource(R.drawable.red_input_feild);

                } else {
                    date_of_birth.setText(dpDate.getDayOfMonth() + "/" + (dpDate.getMonth() + 1) + "/" + dpDate.getYear());
                    date_of_birth.setTextColor(Color.BLACK);

                }


               /* try {
                    Buttonclick();
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/


            }
        });
    }


}
