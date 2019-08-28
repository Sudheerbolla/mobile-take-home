package com.guestlogix.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.guestlogix.R;
import com.guestlogix.utils.Constants;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initComponents();
    }

    @Override
    void initComponents() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finishAffinity();
            }
        }, Constants.ONE_THOUSAND);
    }

}
