package com.project.agroworld.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.manufacture.ManufactureActivity;
import com.project.agroworld.ui.DashboardActivity;

public class Constants {

    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static String API_KEY = "cff3614749a8d630f28ea5c7f079d389";
    public static String NEWS_WEB_URL = "https://krishijagran.com/feeds/?utm_source=homepage&utm_medium=browse&utm_campaign=home_browse&utm_id=homepage_browse";
    public static int REQUEST_CODE = 99;
    public static int GPS_REQUEST_CODE = 999;

    public static void printLog(String message) {
        Log.d("AgroWorldUser", message);
    }

    public static void identifyUser(FirebaseUser user, Context context) {
        if (user != null) {
            if (user.getEmail().equals("devdeveloper66@gmail.com")) {
                //Manufacture user
                Intent manufacturerIntent = new Intent(context, ManufactureActivity.class);
                manufacturerIntent.putExtra("manufacturerUser", "manufacturer");
                context.startActivity(manufacturerIntent);
            } else if (user.getEmail().equals("nap.napster08@gmail.com")) {
                //transport user
               Intent transportIntent = new Intent(context, ManufactureActivity.class);
                transportIntent.putExtra("transportUser", "transport");
                context.startActivity(transportIntent);
            } else {
                //Farmer
                Intent intent = new Intent(context, DashboardActivity.class);
                context.startActivity(intent);
            }
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
