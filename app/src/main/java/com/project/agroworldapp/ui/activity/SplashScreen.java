package com.project.agroworldapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.db.PreferenceHelper;
import com.project.agroworldapp.utils.Constants;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = PreferenceHelper.getInstance(this);
        boolean isHindi = preferenceHelper.getData(Constants.HINDI_KEY);
        if (isHindi) {
            Constants.setAppLocale(SplashScreen.this, "hi");
        } else {
            Constants.setAppLocale(SplashScreen.this, "en");
        }
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        /**
        identifyUser()- It will identify user type & It will navigate user to the appropriate screen.
                        -> Manufacturer user
                        -> Transport user
                        -> Farmer
        */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Constants.identifyUser(user, SplashScreen.this);
                } else {
                    Intent splashIntent = new Intent(SplashScreen.this, SignInActivity.class);
                    startActivity(splashIntent);
                }
                finish();
            }
        }, 2000);
    }

}