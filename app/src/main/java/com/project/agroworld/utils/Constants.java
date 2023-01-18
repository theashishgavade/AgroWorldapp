package com.project.agroworld.utils;

import android.util.Log;

public class Constants {

    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static String API_KEY = "cff3614749a8d630f28ea5c7f079d389";
    public static String NEWS_WEB_URL = "https://krishijagran.com/feeds/?utm_source=homepage&utm_medium=browse&utm_campaign=home_browse&utm_id=homepage_browse";
    public static int REQUEST_CODE = 99;
    public static int GPS_REQUEST_CODE = 999;

    public static void printLog(String message) {
        Log.d("AgroWorldUser", message);
    }
}
