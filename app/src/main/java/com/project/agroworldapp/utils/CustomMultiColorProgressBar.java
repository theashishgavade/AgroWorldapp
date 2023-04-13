package com.project.agroworldapp.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.project.agroworldapp.R;

public class CustomMultiColorProgressBar extends Dialog {
    CircleProgressBar circle_multicolor_progressbar;
    public static CustomMultiColorProgressBar progressBarObj;

    public CustomMultiColorProgressBar(Context context, String dialog_msg) {
        super(context);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_custom_multicolor_progressbar);
        circle_multicolor_progressbar = findViewById(R.id.bar);
        circle_multicolor_progressbar.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_red_light);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ((TextView) findViewById(R.id.messg)).setText(dialog_msg);
    }

    public static CustomMultiColorProgressBar getInstance(Context c, String dialog_msg) {
        if (progressBarObj == null) {
            progressBarObj = new CustomMultiColorProgressBar(c, dialog_msg);
            progressBarObj.show();
        } else {
            progressBarObj.updateMessage(dialog_msg);
        }
        return progressBarObj;
    }

    public void hideProgressBar() {
        if (this != null) {
            dismiss();
            progressBarObj = null;
        }
    }

    public void showProgressBar() {
        show();
    }

    public void updateMessage(String dialog_msg) {
        ((TextView) findViewById(R.id.messg)).setText(dialog_msg);
    }
}
