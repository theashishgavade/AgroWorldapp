package com.project.agroworld.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.manufacture.ManufactureDataPost;
import com.project.agroworld.transport.TransportActivity;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView ivMfrProfile;
    private Button btnMfrLogout, btnMfrProceed;
    private String userType;
    private TextView tvMfrEmail, tvMfrName, tvMfrWelcomeMsg, tvUserType;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacture);
        initView();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();

        if (intent != null) {
            userType = intent.getStringExtra("manufacturerUser");
            if (Objects.equals(userType, "manufacturer")) {
                tvUserType.setText("Manufacturer Panel");
            } else {
                tvUserType.setText("Transport Panel");
            }
        }

        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(ivMfrProfile);
            tvMfrName.setText(user.getDisplayName());
            tvMfrEmail.setText(user.getEmail());
            tvMfrWelcomeMsg.setText("Welcome '" + user.getDisplayName() + "' to the Agro World team ");
        }

        btnMfrLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setIcon(R.drawable.app_icon4)
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            auth.signOut();
                            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                            finish();
                        }
                    }).create().show();
        });

        btnMfrProceed.setOnClickListener(v -> {
            Intent intent1;
            if (Objects.equals(userType, "manufacturer")) {
                intent1 = new Intent(UserProfileActivity.this, ManufactureDataPost.class);
            } else {
                intent1 = new Intent(UserProfileActivity.this, TransportActivity.class);
            }
            startActivity(intent1);
        });

    }

    private void initView() {
        ivMfrProfile = findViewById(R.id.ivMfrProfile);
        tvMfrEmail = findViewById(R.id.tvMfrEmail);
        tvMfrName = findViewById(R.id.tvMfrName);
        tvMfrWelcomeMsg = findViewById(R.id.tvMfrWelcomeMsg);
        tvUserType = findViewById(R.id.tvUserType);
        btnMfrLogout = findViewById(R.id.btnMfrLogout);
        btnMfrProceed = findViewById(R.id.btnMfrProceed);


    }
}