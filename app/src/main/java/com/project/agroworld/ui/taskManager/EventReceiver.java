package com.project.agroworld.ui.taskManager;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.agroworld.utils.NotificationHelper;

public class EventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("com.agroworld.co")) {
            Bundle bundle = intent.getExtras();
            String task = bundle.getString("task");
            String desc = bundle.getString("desc");
            String date = bundle.getString("date");
            String time = bundle.getString("time");
            int maxIDCount = bundle.getInt("maxIDCount", 0);
            MusicControl.getInstance(context).playMusic();
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification(context, task, desc, date, time, maxIDCount);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(maxIDCount, nb.build());
//            Notification notification = nb.build();
//            notification.defaults |= Notification.DEFAULT_SOUND;
        }
    }
}
