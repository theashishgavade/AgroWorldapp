package com.project.agroworld.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.ui.DashboardActivity;
import com.project.agroworld.ui.UserProfileActivity;

import java.util.Locale;

public class Constants {

    public static final String BASE_URL_WEATHER = "https://api.openweathermap.org/data/2.5/";
    public static final String BASE_URL_SHEET_DB = "https://sheetdb.io/api/v1/";
    public static final String API_KEY = "92f4e9a9c233be99f0b33d1c58c72386";
    public static final String NEWS_WEB_URL = "https://krishijagran.com/feeds/?utm_source=homepage&utm_medium=browse&utm_campaign=home_browse&utm_id=homepage_browse";
    public static final int REQUEST_CODE = 99;
    public static final int GPS_REQUEST_CODE = 999;
    public static final String ENGLISH_KEY = "EnglishLang";
    public static final String HINDI_KEY = "HindiLang";
    public static final String RAZORPAY_KEY_ID = "rzp_test_RRBbG8wGxAa23c";
    public static final String APP_ICON_LINK = "https://firebasestorage.googleapis.com/v0/b/agro-world-55872.appspot.com/o/ic_launcher-playstore.png?alt=media&token=6f72edc5-6dc3-40b7-ac65-cdf1bada9532";


    public static void printLog(String message) {
        Log.d("AgroWorldUser", message);
    }

    public static void identifyUser(FirebaseUser user, Context context) {
        if (user != null) {
            if (user.getEmail().equals("devdeveloper66@gmail.com")) {
                //Manufacture user
                Intent manufacturerIntent = new Intent(context, UserProfileActivity.class);
                manufacturerIntent.putExtra("manufacturerUser", "manufacturer");
                context.startActivity(manufacturerIntent);
            } else if (user.getEmail().equals("nap.napster08@gmail.com")) {
                //transport user
                Intent transportIntent = new Intent(context, UserProfileActivity.class);
                transportIntent.putExtra("transportUser", "transport");
                context.startActivity(transportIntent);
            } else {
                //Farmer
                Intent intent = new Intent(context, DashboardActivity.class);
                context.startActivity(intent);
            }
        }
    }

    public static void setAppLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.createConfigurationContext(config);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void bindImage(View view, String url, ImageView imageView) {
        Glide.with(view).load(url).into(imageView);
    }

    public static boolean contactValidation(String contact) {
        return contact.length() == 10;
    }

}
