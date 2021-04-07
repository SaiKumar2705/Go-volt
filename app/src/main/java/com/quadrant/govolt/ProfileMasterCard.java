package com.quadrant.govolt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.quadrant.govolt.Others.AppUtils;
import com.quadrant.govolt.Others.BlurView;
import com.quadrant.govolt.Others.Constants;
import com.quadrant.govolt.Others.PreferenceUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMasterCard extends AppCompatActivity implements View.OnClickListener{
    private Button ok_btn;
    private AlertDialog alertDialog;
    private Context context;
    private View dialogView;
    private ImageView btn_done, down_img;
    private DatePicker dpDate;
    private TextView date_pick;
    private EditText cvv_edit, name_card;
    private LinearLayout ll_one,ll_two, ll_three;
    private CircleImageView _profileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_mastercard);
        context = this;

        setUpView();

        firstEditboxClicked();
        thirdEditboxClicked();

        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Metodo di pagamento");

        ImageView _homeIcon = (ImageView)findViewById(R.id.home_icon);
        _homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileMasterCard.this, HomeActivity.class);
                startActivity(i);
            }
        });

        ImageView back  = (ImageView)findViewById(R.id.back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



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

    private void setUpView() {

        _profileImg = (CircleImageView)findViewById(R.id.profile_image);


        EditText cardForm =(EditText) findViewById(R.id.card_form);

         name_card =(EditText) findViewById(R.id.name_ed);
         ll_one = (LinearLayout)findViewById(R.id.master_ll_name);

        date_pick = (TextView)findViewById(R.id.expire_date);
         ll_two = (LinearLayout)findViewById(R.id.layout_two);

        ll_three = (LinearLayout)findViewById(R.id.third_ll);
        cvv_edit =(EditText) findViewById(R.id.cvv_ed);

        down_img = (ImageView)findViewById(R.id.img_arrow);
        down_img.setOnClickListener(this);

        cardForm.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());

        date_pick.setOnClickListener(this);

        ll_one.setBackgroundResource(R.drawable.ic_text_feild_singleline);
        ll_three.setBackgroundResource(R.drawable.ic_text_feild_singleline);

    }
    private void firstEditboxClicked() {
        name_card.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ll_one.setBackgroundResource(R.drawable.ic_text_feild_blur);
                } else {
                    ll_one.setBackgroundResource(R.drawable.ic_text_feild_singleline);
                }
            }
        });
    }

    private void thirdEditboxClicked() {
        cvv_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ll_three.setBackgroundResource(R.drawable.ic_text_feild_blur);
                } else {
                    ll_three.setBackgroundResource(R.drawable.ic_text_feild_singleline);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_arrow:
                    DatePickerOne();
                    break;
            }
    }
    private void DatePickerOne() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(ProfileMasterCard.this).inflate(R.layout.dp_alert_one, viewGroup, false);

        dpDate = (DatePicker) dialogView.findViewById(R.id.datePicker1);
        // init

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
                Calendar cal = new GregorianCalendar(dpDate.getYear(), (dpDate.getMonth()), dpDate.getDayOfMonth());
                DateFormat df = new SimpleDateFormat("MM/yy");
                String date = df.format(cal.getTime());
                date_pick.setText(date);

                date_pick.setTextColor(Color.BLACK);

            }
        });

    }

}
