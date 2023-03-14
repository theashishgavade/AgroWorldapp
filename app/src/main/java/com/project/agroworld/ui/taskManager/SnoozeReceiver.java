package com.project.agroworld.ui.taskManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("com.agroworld.SnoozeReceiver")) {
            MusicControl.getInstance(context).stopMusic();
        }
    }
}
