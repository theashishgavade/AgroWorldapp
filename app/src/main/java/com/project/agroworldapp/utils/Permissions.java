package com.project.agroworldapp.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.project.agroworldapp.R;

public class Permissions {

    public static boolean isGpsEnable(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean enableLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (enableLocation) {
            return true;
        } else {
            if (context instanceof Activity) {
                DialogUtils.showAlertDialog(context, context.getString(R.string.enable_gps_service));
            }
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean checkConnection(Context context) {
        if (isOnline(context)) {
            return true;
        } else {
            DialogUtils.showAlertDialog(context, context.getString(R.string.check_internet_connection));
        }
        return false;
    }
}
