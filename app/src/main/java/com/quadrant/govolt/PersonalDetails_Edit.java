package com.quadrant.govolt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PersonalDetails_Edit extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_personal_one);

        Button per_detail_avani  = (Button)findViewById(R.id.avani_pers_detail);
        per_detail_avani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPesonalDetails();
            }
        });

    }
    private void launchPesonalDetails() {
      /*  startActivity(new Intent(PersonalDetails_Edit.this, PersonalDetails.class));
        finish();*/
        startActivity(new Intent(PersonalDetails_Edit.this, Patent_Registration.class));
        finish();

    }

}
