package com.project.agroworld.ui.activity;

import static com.project.agroworld.utils.Constants.setAppLocale;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityManufactureBinding;
import com.project.agroworld.db.PreferenceHelper;
import com.project.agroworld.manufacture.activity.ManufactureActivity;
import com.project.agroworld.manufacture.activity.ManufactureDataActivity;
import com.project.agroworld.transport.activity.TransportActivity;
import com.project.agroworld.transport.activity.TransportDataActivity;
import com.project.agroworld.utils.Constants;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityManufactureBinding binding;
    PreferenceHelper preferenceHelper;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userType;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manufacture);
        preferenceHelper = PreferenceHelper.getInstance(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();

        if (intent != null) {
            userType = intent.getStringExtra("manufacturerUser");
            if (Objects.equals(userType, "manufacturer")) {
                binding.tvUserType.setText(getString(R.string.show_product));
                binding.tvUserType.setText(getString(R.string.manufacture_panel));
            } else {
                binding.tvUserType.setText(getString(R.string.show_vehicles));
                binding.tvUserType.setText(getString(R.string.transport_panel));
            }
        }

        if (user != null) {
            Constants.bindImage(binding.ivMfrProfile, String.valueOf(user.getPhotoUrl()), binding.ivMfrProfile);
            binding.tvMfrName.setText(user.getDisplayName());
            binding.tvMfrEmail.setText(user.getEmail());
            binding.tvMfrWelcomeMsg.setText("Welcome '" + user.getDisplayName() + "' to the AgroWorld");
        }

        binding.btnMfrProceed.setOnClickListener(v -> {
            showProceedLanguageSelection("Proceed");
        });

        binding.btnShowHistory.setOnClickListener(v -> {
            showProceedLanguageSelection("History");
        });
    }

    private void showProceedLanguageSelection(String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this);
        alertDialog.setTitle("Select language to upload data");
        alertDialog.setIcon(R.drawable.app_icon4);
        String[] items = {"Hindi", "English"};
        alertDialog.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (key.equals("Proceed")) {
                            navigateToUploadPageWithLang("Hindi");
                        } else {
                            navigateToHistoryPage("Hindi", true);
                        }
                        dialog.dismiss();
                        break;
                    case 1:
                        if (key.equals("Proceed")) {
                            navigateToUploadPageWithLang("English");
                        } else {
                            navigateToHistoryPage("English", false);
                        }
                        dialog.dismiss();
                        break;
                    default:

                }
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void navigateToUploadPageWithLang(String language) {
        Intent intent1;
        if (Objects.equals(userType, "manufacturer")) {
            intent1 = new Intent(UserProfileActivity.this, ManufactureActivity.class);
        } else {
            intent1 = new Intent(UserProfileActivity.this, TransportActivity.class);
        }
        intent1.putExtra("selectedDataLanguage", language);
        intent1.putExtra("isActionWithData", false);
        startActivity(intent1);
    }

    private void navigateToHistoryPage(String key, boolean isLocalized) {
        Intent intent1;
        if (Objects.equals(userType, "manufacturer")) {
            intent1 = new Intent(UserProfileActivity.this, ManufactureDataActivity.class);
        } else {
            intent1 = new Intent(UserProfileActivity.this, TransportDataActivity.class);
        }
        intent1.putExtra("localizedData", isLocalized);
        startActivity(intent1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logoutUser();
                return true;
            case R.id.adminHindiLang:
                setAppLocale(this, "hi");
                preferenceHelper.saveData(Constants.ENGLISH_KEY, false);
                preferenceHelper.saveData(Constants.HINDI_KEY, true);
                Constants.showToast(this, getString(R.string.launguage_updated_to_hindi));
                startActivity(new Intent(this, UserProfileActivity.class));
                finish();
                return true;
            case R.id.adminEnglishLang:
                setAppLocale(this, "en");
                preferenceHelper.saveData(Constants.ENGLISH_KEY, true);
                preferenceHelper.saveData(Constants.HINDI_KEY, false);
                Constants.showToast(this, getString(R.string.launguage_updated));
                startActivity(new Intent(this, UserProfileActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        Constants.logoutAlertMessage(UserProfileActivity.this, auth);
    }
}