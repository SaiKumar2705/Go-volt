package com.quadrant.govolt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity_Refersh extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_map_refresh);

        Intent i = new Intent(HomeActivity_Refersh.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
