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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.BlurView;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;
import com.quadrant.govolt.Others.RetrofitClient;
import com.quadrant.govolt.R;
import com.quadrant.interfaces.PatentImageInterface;
import com.quadrant.interfaces.RegistrationAddingInterface;
import com.quadrant.interfaces.RegistrationDocumentInterface;
import com.quadrant.progressbar.KProgressHUD;
import com.quadrant.request.PatentImgeRequesOne;
import com.quadrant.request.RegistrationAddingRequest;
import com.quadrant.request.RegistrationDocumentRequest;
import com.quadrant.request.RegistrationImageRequest;
import com.quadrant.response.RegistrationAddingResponse;
import com.quadrant.response.RegistrationDocumentResponse;
import com.quadrant.response.patentImgResponseOne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class Patent_Registration extends AppCompatActivity implements View.OnClickListener {
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private static final int CAMERA_REQUEST = 1888;
    private Context con;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private EditText Data_rilascio, Valida_fino;
    private AlertDialog alertDialog;
    private View dialogView;
    private ImageView btn_done;
    private DatePicker dpDate;
    private Button ButtonOk;
    private Context context;
    private AwesomeValidation awesomeValidation;
    boolean checkortrue = false;
    private ImageView Image1, Image2;
    private EditText Titolare, Nazionalita, Tipo, Numero, CurrentDate, validDate, Autorita;
    boolean FirstImage = false;
    boolean SecondImage = false;
    Bitmap mphoto1, mphoto2;
    String TokenId;
    boolean isAllnotEmpty = false;
    boolean isCheckedAll;
    String firstbaseimage, secondbaseimage;
    private File finalFile;
    private String imagePath;
    String Valida_finomonthyr, Data_rilasciomonthyr;
    private boolean isTotolare = false;
    private boolean isNazo = false;
    private boolean isTipo = false;
    private boolean isNumero = false;
    private boolean isAutorita = false;
    String validaDate;

    private KProgressHUD progressbar_hud;

    private static final int pic_id = 123;

    boolean isEdtEmptyTito, isEdtEmptyNazi, isEdtEmptytipo, isEdtEmptyNumero, isEdtEmptyData, isEdtEmptyvalida, isEdtEmptyAutorita;

    private String secondDate = "";
    private String firstDate = "";
    private Uri imageUri1, imageUri2;

    boolean validdatevalue, expdatevalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petent_activity);
        context = this;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        Initializer();
        getData();
        Textchangers();

     /*   ButtonchangeMonitor();

        ButtoncolorCheck();*/
        awesomeValidation.addValidation(Patent_Registration.this, R.id.et_titolare, "", R.string.emptyerror);
        awesomeValidation.addValidation(Patent_Registration.this, R.id.et_nazionalita, "", R.string.emptyerror);
        awesomeValidation.addValidation(Patent_Registration.this, R.id.et_tipo, "", R.string.emptyerror);
        awesomeValidation.addValidation(Patent_Registration.this, R.id.et_numero, "", R.string.emptyerror);
        awesomeValidation.addValidation(Patent_Registration.this, R.id.et_autorita, "", R.string.emptyerror);

        ButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonOkClick();
            }
        });
        Valida_fino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                dialogView = LayoutInflater.from(Patent_Registration.this).inflate(R.layout.dp_alert_two, viewGroup, false);

                dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);
                // init
                dpDate.setMinDate(System.currentTimeMillis());


                btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);


                String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";
                // pwd_alert_title.setText(str);

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


                        secondDate = (dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear();
                        Valida_fino.setText(secondDate);
                        validaDate = (dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear();

                        if (firstDate != null) {
                            if (!firstDate.equalsIgnoreCase("")) {
                                if (secondDate.equalsIgnoreCase(firstDate)) {
                                    Valida_fino.setText("");
                                }

                            }
                        }


                        Valida_fino.setError(null);

                        Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                        DateFormat df = new SimpleDateFormat("MM/yy");
                        Valida_finomonthyr = df.format(cal.getTime());

                        System.out.println("hfhfhhfhf:::" + Valida_finomonthyr);

                        // Valida_finomonthyr = dpDate.getMonth() + 1 + "/" + dpDate.getYear();

                        Valida_fino.setTextColor(Color.WHITE);

                    }
                });


            }
        });


        Data_rilascio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                dialogView = LayoutInflater.from(Patent_Registration.this).inflate(R.layout.dp_alert_bd, viewGroup, false);

                dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);


                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -1);
                dpDate.setMaxDate(calendar.getTimeInMillis());
                //  dpDate.setMaxDate(System.currentTimeMillis());


                btn_done = (ImageView) dialogView.findViewById(R.id.done_btn);


                String str = "Digita nuovamente " + "\n" + "la tua password" + "\n" + " per conferma";
                // pwd_alert_title.setText(str);

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
                        //  alertDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


                        // validDateRisa = (dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear();

                        if (Data_rilascio.getText().toString().length() > 0) {

                            validdatevalue = true;
                        } else {

                            validdatevalue = false;

                        }
                        firstDate = (dpDate.getMonth() + 1) + "/" + dpDate.getDayOfMonth() + "/" + dpDate.getYear();
                        Data_rilascio.setText(firstDate);

                        if (secondDate != null) {
                            if (!secondDate.equalsIgnoreCase("")) {
                                if (firstDate.equalsIgnoreCase(secondDate)) {
                                    Data_rilascio.setText("");
                                }
                            }
                        }

                        if (Data_rilascio.getText().length() > 0) {
                            Data_rilascio.setBackgroundResource(R.drawable.ic_active_edittext);
                        } else {
                            Data_rilascio.setBackgroundResource(R.drawable.red_input_feild);
                        }

                        Data_rilascio.setError(null);
                        Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                        DateFormat df = new SimpleDateFormat("MM/yy");
                        Data_rilasciomonthyr = df.format(cal.getTime());

                        System.out.println("hfhfhhfhf:::" + Data_rilasciomonthyr);

                        Data_rilascio.setTextColor(Color.WHITE);
                        //int yr = dpDate.getYear();


                       /* Data_rilasciomonthyr = dpDate.getMonth() + 1 + "/" + dpDate.getYear();

                        Data_rilascio.setTextColor(Color.WHITE);
*/

                    }
                });


            }
        });


    }

    private void ButtoncolorCheck() {

        if (isEdtEmptyTito == true || isEdtEmptyAutorita == true || isEdtEmptyData == true || isEdtEmptyNazi == true || isEdtEmptytipo == true || isEdtEmptyNumero == true || isEdtEmptyvalida == true) {


        } else {


            ButtonOk.setTextColor(Color.parseColor("#FFFFFF"));
            ButtonOk.setBackgroundResource(R.drawable.border_confirm);
        }

    }

    private void ButtonchangeMonitor() {
        Titolare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Titolare.getText().toString().length() > 0) {
                    isEdtEmptyTito = false;
                } else {
                    isEdtEmptyTito = true;
                }
            }
        });
        Nazionalita.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Nazionalita.toString().trim().length() > 0) {
                    isEdtEmptyNazi = false;
                } else {
                    isEdtEmptyNazi = true;
                }
            }
        });
        Tipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Tipo.toString().trim().length() > 0) {
                    isEdtEmptytipo = false;
                } else {
                    isEdtEmptytipo = true;
                }
            }
        });

        Numero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Numero.toString().trim().length() > 0) {
                    isEdtEmptyNumero = false;
                } else {
                    isEdtEmptyNumero = true;
                }
            }
        });

        Data_rilascio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Data_rilascio.toString().trim().length() > 0) {
                    isEdtEmptyData = false;
                } else {
                    isEdtEmptyData = true;
                }
            }
        });

        Valida_fino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Valida_fino.toString().trim().length() > 0) {
                    isEdtEmptyvalida = false;
                } else {
                    isEdtEmptyvalida = true;
                }
            }
        });
        Autorita.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                if (Autorita.toString().trim().length() > 0) {
                    isEdtEmptyAutorita = false;
                } else {
                    isEdtEmptyAutorita = true;
                }
            }
        });

    }

    private boolean compareDates(String d1, String d2) {
        SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
        boolean b = false;
        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;

    }

    private void Textchangers() {
        Titolare.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Titolare.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Titolare.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Titolare.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Titolare.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Titolare.setBackgroundResource(R.drawable.ic_active_edittext);
                    isTotolare = true;

                } else {
                    //RED

                    Titolare.setBackgroundResource(R.drawable.red_input_feild);
                    isTotolare = false;
                }

                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Titolare.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Titolare.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Titolare.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        //Name
        Nazionalita.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Nazionalita.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Nazionalita.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Nazionalita.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Nazionalita.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Nazionalita.setBackgroundResource(R.drawable.ic_active_edittext);
                    isNazo = true;

                } else {
                    //RED

                    Nazionalita.setBackgroundResource(R.drawable.red_input_feild);
                    isNazo = false;
                }

                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Nazionalita.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Nazionalita.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Nazionalita.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
//Location
        Tipo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Tipo.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Tipo.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Tipo.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Tipo.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Tipo.setBackgroundResource(R.drawable.ic_active_edittext);
                    isTipo = true;

                } else {
                    //RED

                    Tipo.setBackgroundResource(R.drawable.red_input_feild);
                    isTipo = false;
                }

                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Tipo.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Tipo.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Tipo.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });

        //Numero
        Numero.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Numero.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Numero.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Numero.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Numero.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Numero.setBackgroundResource(R.drawable.ic_active_edittext);
                    isNumero = true;

                } else {
                    //RED

                    Numero.setBackgroundResource(R.drawable.red_input_feild);
                    isNumero = false;
                }

                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Numero.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Numero.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Numero.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        //Citta
        Autorita.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Autorita.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Autorita.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Autorita.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Autorita.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Autorita.setBackgroundResource(R.drawable.ic_active_edittext);
                    isAutorita = true;


                } else {
                    //RED

                    Autorita.setBackgroundResource(R.drawable.red_input_feild);
                    isAutorita = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Autorita.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Autorita.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Autorita.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        Valida_fino.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Valida_fino.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Valida_fino.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Valida_fino.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Valida_fino.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Valida_fino.setBackgroundResource(R.drawable.ic_active_edittext);
                    //  isAutorita = true;


                } else {
                    //RED

                    Autorita.setBackgroundResource(R.drawable.red_input_feild);
                    //isAutorita = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Valida_fino.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Valida_fino.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Valida_fino.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
        Data_rilascio.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = Data_rilascio.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Data_rilascio.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Data_rilascio.setBackgroundResource(R.drawable.red_input_feild);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Integer textlength1 = Data_rilascio.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Data_rilascio.setBackgroundResource(R.drawable.ic_active_edittext);
                    //  isAutorita = true;


                } else {
                    //RED

                    Data_rilascio.setBackgroundResource(R.drawable.red_input_feild);
                    //isAutorita = false;
                }


                checkFieldsForEmptyValues();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Integer textlength1 = Data_rilascio.getText().length();

                // mEdtcountryCode.setText("+39");

                if (textlength1 > 0) {
                    //green

                    Data_rilascio.setBackgroundResource(R.drawable.ic_active_edittext);

                } else {
                    //RED

                    Data_rilascio.setBackgroundResource(R.drawable.red_input_feild);

                }
            }
        });
    }

    private void checkFieldsForEmptyValues() {

        // private EditText Titolare, Nazionalita, Tipo, Numero, CurrentDate, validDate, Autorita;

        String s1 = Titolare.getText().toString();
        String s2 = Nazionalita.getText().toString();
        //  String s3 = Codice.getText().toString();
        String s3 = Tipo.getText().toString();
        String s4 = Numero.getText().toString();
        String s5 = Autorita.getText().toString();
        String s6 = Valida_fino.getText().toString();
        String s7 = Data_rilascio.getText().toString();


        if (s1.length() > 0 && s2.length() > 0 && s3.length() > 0 && s4.length() > 0 && s5.length() > 0 && s6.length() > 0 && s7.length() > 0 && FirstImage == true && SecondImage == true) {
            ButtonOk.setTextColor(Color.parseColor("#FFFFFF"));
            ButtonOk.setBackgroundResource(R.drawable.border_confirm);
        } else {


            ButtonOk.setBackgroundResource(R.drawable.round_corner_black);
        }
    }

    private void getData() {


        TokenId = getIntent().getStringExtra("Tokenid");
    }

    private void Initializer() {

        Data_rilascio = findViewById(R.id.rilascio);
        Valida_fino = findViewById(R.id.fino);
        ButtonOk = findViewById(R.id.buttonOk);
        Titolare = (EditText) findViewById(R.id.et_titolare);
        Nazionalita = (EditText) findViewById(R.id.et_nazionalita);
        Tipo = (EditText) findViewById(R.id.et_tipo);
        Numero = (EditText) findViewById(R.id.et_numero);
        Autorita = (EditText) findViewById(R.id.et_autorita);
        Image1 = (ImageView) findViewById(R.id.image1);
        Image2 = (ImageView) findViewById(R.id.image2);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);

    }

    private void ButtonOkClick() {


        if (isTotolare) {
            Titolare.setBackgroundResource(R.drawable.ic_active_edittext);
        } else {
            Titolare.setBackgroundResource(R.drawable.red_input_feild);
        }
        if (isNazo) {
            Nazionalita.setBackgroundResource(R.drawable.ic_active_edittext);
        } else {
            Nazionalita.setBackgroundResource(R.drawable.red_input_feild);
        }
        if (isTipo) {
            Tipo.setBackgroundResource(R.drawable.ic_active_edittext);
        } else {
            Tipo.setBackgroundResource(R.drawable.red_input_feild);
        }
        if (isNumero) {
            Numero.setBackgroundResource(R.drawable.ic_active_edittext);
        } else {
            Numero.setBackgroundResource(R.drawable.red_input_feild);
        }
        if (isAutorita) {
            Autorita.setBackgroundResource(R.drawable.ic_active_edittext);
        } else {
            Autorita.setBackgroundResource(R.drawable.red_input_feild);
        }


        if (!isTotolare || !isNazo || !isTipo || !isNumero || !isAutorita) {


            //PopUP

            AppUtils.error_Alert("Si prega di compilare tutti i campi obbligatori", context, alertDialog, Patent_Registration.this);
        }


        if (awesomeValidation.validate()) {
            // Toast.makeText(Patent_Registration.this, "Validation Successfully", Toast.LENGTH_LONG).show();
            Titolare.setBackgroundResource(R.drawable.red_input_feild);
            //  Titolare.setError("field is empty");
            Nazionalita.setBackgroundResource(R.drawable.red_input_feild);
            // Nazionalita.setError("field is empty");
            Tipo.setBackgroundResource(R.drawable.red_input_feild);
            // Tipo.setError("field is empty");
            Numero.setBackgroundResource(R.drawable.red_input_feild);

            Data_rilascio.setBackgroundResource(R.drawable.red_input_feild);

            Valida_fino.setBackgroundResource(R.drawable.red_input_feild);
            //  Numero.setError("field is empty");
            Autorita.setBackgroundResource(R.drawable.red_input_feild);

            checkortrue = true;
            //  m.setBackgroundResource(R.drawable.red_input_feild);

            //process the data further
        } else {
            awesomeValidation.clear();
            checkortrue = true;

            /*if (Image1.getDrawable().getConstantState().equals
                    (getResources().getDrawable(R.drawable.your_drawable).getConstantState()){//set here your drawable name(your_drawable)
                image.setVisibility(View.VISIBLE);
            }else{
                image.setVisibility(View.GONE);
            }*/


            if (checkortrue == true) {

                // Toast.makeText(Patent_Registration.this, "Validation completed", Toast.LENGTH_LONG).show();

                /*Titolare.setBackgroundResource(R.drawable.edittext_image_change);
                Nazionalita.setBackgroundResource(R.drawable.edittext_image_change);
                Tipo.setBackgroundResource(R.drawable.edittext_image_change);
                Numero.setBackgroundResource(R.drawable.edittext_image_change);
                Autorita.setBackgroundResource(R.drawable.edittext_image_change);*/


                isCheckedAll = CheckifiledsEmpty();


                System.out.println("dggdggdgdggdg:::" + isCheckedAll);

                if (isCheckedAll == false) {

                    if (FirstImage == false && SecondImage == false && FirstImage == false || SecondImage == false) {


                        // Toast.makeText(Patent_Registration.this, "Si prega di caricare l'immagine", Toast.LENGTH_LONG).show();
                        AppUtils.error_Alert("Compila tutti i campi richiesti per poter continuare. Ricorda di inserire le immagini fronte e retro della tua patente", context, alertDialog, Patent_Registration.this);

                    } else if ((Data_rilascio.getText().toString().trim().equals("Data rilascio") || (Valida_fino.getText().toString().trim().equals("Valida fino")) || (Valida_fino.getText().toString().trim().equals("")))) {

                        //   Data_rilascio.setError(getResources().getString(R.string.dateerror));
                        //  Valida_fino.setError(getResources().getString(R.string.dateerror));

                    } else {

                        ButtonOk.setTextColor(Color.parseColor("#FFFFFF"));
                        ButtonOk.setBackgroundResource(R.drawable.border_confirm);
                      /*  Data_rilascio.setError(null);
                        Valida_fino.setError(null);
*/


                        try {
                            PostData();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }


    }

    private boolean CheckifiledsEmpty() {

        isAllnotEmpty = false;
        if (TextUtils.isEmpty(Titolare.getText().toString())) {

            // Titolare.setError("Field is Empty");

            isAllnotEmpty = true;
            return isAllnotEmpty;
            //  Toast.makeText(PersonalDetails.this, "hhhhhh", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(Nazionalita.getText().toString())) {

            Nazionalita.requestFocus();
            // Nazionalita.setError("Field is Empty");

            isAllnotEmpty = true;
            return isAllnotEmpty;

        }
        if (TextUtils.isEmpty(Tipo.getText().toString())) {
            Tipo.requestFocus();

            //  Tipo.setError("Field is Empty");

            isAllnotEmpty = true;
            return isAllnotEmpty;
        }
        if (TextUtils.isEmpty(Numero.getText().toString())) {
            Numero.requestFocus();

            //   Numero.setError("Field is Empty");

            // Citta.setError("field is empty");

            isAllnotEmpty = true;
            return isAllnotEmpty;
        }
        if (TextUtils.isEmpty(Autorita.getText().toString())) {

            Autorita.requestFocus();

            //  Autorita.setError("Field is Empty");


            isAllnotEmpty = true;
            return isAllnotEmpty;
        }

        return isAllnotEmpty;

    }

    private void PostData() throws ParseException {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Patent_Registration.this);
            return;
        }


        showProgressbar();

        RegistrationDocumentRequest req = new RegistrationDocumentRequest();


        req.setDriving_lincense_holder_name(Titolare.getText().toString());
        req.setDriving_license_nationality(Nazionalita.getText().toString());
        req.setGuy(Tipo.getText().toString());
        req.setDriving_license_number(Numero.getText().toString());


        String dob = Data_rilascio.getText().toString();
       /* DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = (Date)formatter.parse(dob);
        System.out.println("Todayis...." +date.getTime());
*/

        req.setDriving_release_date(dob);

        String dob1 = Valida_fino.getText().toString();
       /* DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = (Date)formatter.parse(dob1);
        System.out.println("Todayis...." +date1.getTime());
*/

        req.setDriving_expiry_date(dob1);

      /*  String dob = Data_rilascio.getText().toString();
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        Date date = (Date)formatter.parse(dob);
        System.out.println("Todayis...." +date.getTime());

       // Long timeandDate = date.getTime();
        //System.out.println("nfdjnjfnnf::::"+timeandDate);
        req.setDriving_release_date(date.getTime());

        String dob1 = Valida_fino.getText().toString();
        DateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
       // formatter1.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
        Date date1 = (Date)formatter.parse(dob1);
        System.out.println("Todayis...." +date1.getTime());
       // Long timeandDate1 = date1.getTime();
        req.setDriving_expiry_date(date1.getTime());*/
        req.setDriving_authorities(Autorita.getText().toString());

        String token = PreferenceUtil.getInstance().getString(context, Constants.REG_TOKEN, "");

        Log.e("TOKEN", "token---" + token);
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        // String bearer_authorization = "Bearer " + TokenId;
        String bearer_authorization = "Bearer " + token;

        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        RegistrationDocumentInterface geo_details = retrofit.create(RegistrationDocumentInterface.class);
        Call<RegistrationDocumentResponse> resultRes = geo_details.GetResponseAddingReg(Constants.TOKEN, bearer_authorization, req);
        resultRes.enqueue(new Callback<RegistrationDocumentResponse>() {
            @Override
            public void onResponse(Call<RegistrationDocumentResponse> call, Response<RegistrationDocumentResponse> response) {


                if (!response.isSuccessful()) {

                    if (response.code() != 200) {


                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {
                        //RegistrationDocumentResponse res = response.body();
                        Intent i = new Intent(Patent_Registration.this, UploadImageActivity.class);
                        startActivity(i);

                    }


                }

                dismissProgressbar();

                //PostFirstImagedata(firstbaseimage,  0);
            }

            @Override
            public void onFailure(Call<RegistrationDocumentResponse> call, Throwable t) {
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

    private void PostFirstImagedata(String baseimg, int number) {

        boolean _isInternetAvailable = Constants.isInternetAvailable(context);

        if (!_isInternetAvailable) {


            AppUtils.error_Alert("Per favore connettiti a intenet", context, alertDialog, Patent_Registration.this);
            return;
        }


        PatentImgeRequesOne req = new PatentImgeRequesOne();
        req.setType(number);
        req.setFile(baseimg);


        //   showProgressbar();
        String token = PreferenceUtil.getInstance().getString(context, Constants.REG_TOKEN, "");
        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        // String bearer_authorization = "Bearer " + TokenId;
        String bearer_authorization = "Bearer " + token;


        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        PatentImageInterface geo_details = retrofit.create(PatentImageInterface.class);
        Call<patentImgResponseOne> resultRes = geo_details.GetResponseAddingReg(Constants.TOKEN, bearer_authorization, number, baseimg);
        resultRes.enqueue(new Callback<patentImgResponseOne>() {
            @Override
            public void onResponse(Call<patentImgResponseOne> call, Response<patentImgResponseOne> response) {


                if (!response.isSuccessful()) {

                    if (response.code() != 200) {


                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {


                        // Toast.makeText(getApplicationContext(), "Hello Javatpoint1", Toast.LENGTH_SHORT).show();

                    }


                }
                //  PostSecondImagedata(secondbaseimage,  1);

            }

            @Override
            public void onFailure(Call<patentImgResponseOne> call, Throwable t) {
                // Log.e(TAG, "--Fail---" + t.getMessage());
                //  dismissProgressbar();

            }


        });


    }

    private void PostSecondImagedata(String baseimg, int number) {


        PatentImgeRequesOne req = new PatentImgeRequesOne();
        req.setType(number);
        req.setFile(baseimg);

        //   showProgressbar();
        String token = PreferenceUtil.getInstance().getString(context, Constants.REG_TOKEN, "");

        String authorization = PreferenceUtil.getInstance().getString(context, Constants.USER_CODE, "");
        // String bearer_authorization = "Bearer " + TokenId;
        String bearer_authorization = "Bearer " + token;


        Retrofit retrofit = RetrofitClient.getClient(Constants.BASE_URL);

        PatentImageInterface geo_details = retrofit.create(PatentImageInterface.class);
        Call<patentImgResponseOne> resultRes = geo_details.GetResponseAddingReg(Constants.TOKEN, bearer_authorization, number, baseimg);
        resultRes.enqueue(new Callback<patentImgResponseOne>() {
            @Override
            public void onResponse(Call<patentImgResponseOne> call, Response<patentImgResponseOne> response) {


                if (!response.isSuccessful()) {

                    if (response.code() != 200) {


                    }


                } else {
                    // Log.e(TAG, "--Success---");


                    if (response.code() == 200) {


                        //  Toast.makeText(getApplicationContext(), "Hello Javatpoint1", Toast.LENGTH_SHORT).show();

                    }


                }

            }

            @Override
            public void onFailure(Call<patentImgResponseOne> call, Throwable t) {


            }


        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.image1:

                if (FirstImage == false) {

                    UploadImage_One();

                    checkFieldsForEmptyValues();
                } else {

                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(Patent_Registration.this).inflate(R.layout.imagepopup, viewGroup, false);

                    ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

                    ImageView remove = (ImageView) dialogView.findViewById(R.id.remove);

                    ImageView right = (ImageView) dialogView.findViewById(R.id.right);

                    img.setImageBitmap(mphoto1);
                    checkFieldsForEmptyValues();
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
                            checkFieldsForEmptyValues();
                            Image1.setImageResource(R.drawable.ic_camera_icon_one);

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
                break;

            case R.id.image2:
                if (SecondImage == false) {
                    UploadImage_Two();
                    checkFieldsForEmptyValues();
                } else {

                   /* ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(Patent_Registration.this).inflate(R.layout.imagepopup_one, viewGroup, false);

                    ImageView img = (ImageView) dialogView.findViewById(R.id.imageview1);

                    img.setImageBitmap(mphoto2);

                    ImageView remove =(ImageView)dialogView.findViewById(R.id.remove);

                    ImageView right =(ImageView)dialogView.findViewById(R.id.right);


                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SecondImage=false;
                            alertDialog.dismiss();

                            Image2.setImageResource(R.drawable.ic_camera_icon_one);

                        }
                    });


                    //TextView tv_error = (TextView) dialogView.findViewById(R.id.title);

                 //   tv_error.setText("" + error_message);

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

                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(Patent_Registration.this).inflate(R.layout.imagepopup, viewGroup, false);

                    ImageView img = (ImageView) dialogView.findViewById(R.id.imageView);

                    ImageView remove = (ImageView) dialogView.findViewById(R.id.remove);

                    ImageView right = (ImageView) dialogView.findViewById(R.id.right);

                    img.setImageBitmap(mphoto2);
                    checkFieldsForEmptyValues();
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SecondImage = false;
                            alertDialog.dismiss();
                            checkFieldsForEmptyValues();
                            Image2.setImageResource(R.drawable.ic_camera_icon_one);

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
                break;

        }

    }

    private void UploadImage_Two() {
        if (ContextCompat.checkSelfPermission(Patent_Registration.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(Patent_Registration.this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(Patent_Registration.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Patent_Registration.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Patent_Registration.this,
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

//
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
//            startActivityForResult(takePictureIntent, 2);

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            mphoto1 = (Bitmap) data.getExtras().get("data");
//            Image1.setImageBitmap(mphoto1);
            mphoto1 = null;
            Bitmap compressedBitmap = null;
            try {
                mphoto1 = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri1);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mphoto1.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                compressedBitmap = AppUtils.rotateImageIfRequired(this, compressedBitmap, imageUri1);
                mphoto1 = compressedBitmap;
                Image1.setImageBitmap(compressedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ButtonOk.setVisibility(View.VISIBLE);

            FirstImage = true;
            checkFieldsForEmptyValues();
            Uri tempUri = getImageUri(getApplicationContext(), compressedBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

            imagePath = finalFile.getPath();

            //AppUtils.UploaddocumentFile_Api(context, imagePath, 0);


            AppUtils.deleteImage(imageUri1, this);

            AppUtils.UploadLicenceFile_Api(context, imagePath, 0);

           /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

             firstbaseimage = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
//            mphoto2 = (Bitmap) data.getExtras().get("data");
//            Image2.setImageBitmap(mphoto2);
            mphoto2 = null;
            Bitmap compressedBitmap = null;
            try {
                mphoto2 = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri2);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mphoto2.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                compressedBitmap = AppUtils.rotateImageIfRequired(this, compressedBitmap, imageUri2);
                Image2.setImageBitmap(compressedBitmap);
                mphoto2 = compressedBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri tempUri = getImageUri(getApplicationContext(), compressedBitmap);

            ButtonOk.setVisibility(View.VISIBLE);

            SecondImage = true;
            checkFieldsForEmptyValues();
//            Uri tempUri = getImageUri(getApplicationContext(), mphoto2);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(getRealPathFromURI(tempUri));

            imagePath = finalFile.getPath();

            AppUtils.deleteImage(imageUri2, this);

            AppUtils.UploadLicenceFile_Api(context, imagePath, 1);

           /* ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            mphoto2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            secondbaseimage = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
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

    private void UploadImage_One() {


        if (ContextCompat.checkSelfPermission(Patent_Registration.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(Patent_Registration.this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(Patent_Registration.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Patent_Registration.this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(Patent_Registration.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {

            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                checkIfAlreadyhavePermission();
            }

         /*   Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
*/


            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Front Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri1 = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
            startActivityForResult(intent, 1);

//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 2);
//            startActivityForResult(takePictureIntent, 1);


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
                        ActivityCompat.requestPermissions(Patent_Registration.this,
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
                        startInstalledAppDetailsActivity(Patent_Registration.this);

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
                            saveToPreferences(Patent_Registration
                                    .this, ALLOW_KEY, true);
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

    /*class BlurAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        private final String TAG = BlurAsyncTask.class.getName();

        protected Bitmap doInBackground(Void... arg0) {

            Bitmap map = AppUtils.takeScreenShot(Patent_Registration.this);
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
    }*/

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
