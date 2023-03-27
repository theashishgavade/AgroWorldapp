package com.project.agroworld.ui.activity;

import static com.project.agroworld.utils.Constants.setAppLocale;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.db.PreferenceHelper;
import com.project.agroworld.utils.Constants;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = PreferenceHelper.getInstance(this);
        boolean isHindi = preferenceHelper.getData(Constants.HINDI_KEY);
        if (isHindi) {
            setAppLocale(SplashScreen.this, "hi");
        } else {
            setAppLocale(SplashScreen.this, "en");
        }
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    Constants.identifyUser(user, SplashScreen.this);
                } else {
                    Intent splashIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(splashIntent);
                }
                finish();
            }
        }, 2000);
    }

}