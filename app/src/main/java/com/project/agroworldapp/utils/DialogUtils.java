package com.project.agroworldapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.project.agroworldapp.R;

import java.util.Objects;

public class DialogUtils {

    public static void showAlertDialog(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        dialog.setContentView(R.layout.dialog_warning);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.setCancelable(false);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        TextView txt_msg = dialog.findViewById(R.id.txt_msg);
        ImageView tvDismiss = dialog.findViewById(R.id.tvDismiss);
        ImageView ivDialogStatus = dialog.findViewById(R.id.ivDialogIcon);
        ivDialogStatus.setImageResource(R.drawable.ic_baseline_warning_24);

        txt_msg.setText(message);
        tvDismiss.setOnClickListener(view -> dialog.dismiss());

        btn_ok.setOnClickListener(v -> {
            dialog.dismiss();
            if (message.equalsIgnoreCase(context.getString(R.string.enable_gps_service))) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((Activity) context).startActivityForResult(intent, Constants.GPS_REQUEST_CODE);
            }
            if (message.equalsIgnoreCase(context.getString(R.string.check_internet_connection))) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        dialog.show();
    }
}
