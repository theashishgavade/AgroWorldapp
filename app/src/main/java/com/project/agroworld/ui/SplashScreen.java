package com.project.agroworld.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.DashboardActivity;
import com.project.agroworld.R;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent;
                if (user != null) {
                    splashIntent = new Intent(SplashScreen.this, DashboardActivity.class);
                }else {
                    splashIntent = new Intent(SplashScreen.this, LoginActivity.class);
                }
                startActivity(splashIntent);
                finish();
            }
        }, 2000);
    }
}