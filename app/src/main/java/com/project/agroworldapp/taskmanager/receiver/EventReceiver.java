package com.project.agroworldapp.taskmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.agroworldapp.taskmanager.MusicControl;
import com.project.agroworldapp.utils.NotificationHelper;

public class EventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("com.project.agroworld")) {
            Bundle bundle = intent.getExtras();
            String task = bundle.getString("task");
            String desc = bundle.getString("desc");
            String date = bundle.getString("date");
            String time = bundle.getString("time");
            int maxIDCount = bundle.getInt("maxIDCount", 0);
            System.out.println("EventReceiverMaxID " + maxIDCount);

            if (bundle.getString("setNotify").equalsIgnoreCase("SetNotification")) {
                MusicControl.getInstance(context).playMusic();
                NotificationHelper notificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(context, task, desc, date, time, maxIDCount);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(maxIDCount, nb.build());
            } else {
                System.out.println("Notification also canceled for " + maxIDCount);
                NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(context);
                notificationManager1.cancel(maxIDCount);
            }
        }
    }
}
