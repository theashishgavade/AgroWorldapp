package com.project.agroworldapp.taskmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.agroworldapp.taskmanager.MusicControl;

public class SnoozeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("com.agroworld.SnoozeReceiver")) {
            MusicControl.getInstance(context).stopMusic();
        }
    }
}
