package com.project.agroworld.utils;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.project.agroworld.R;
import com.project.agroworld.ui.taskManager.AddTaskActivity;
import com.project.agroworld.ui.taskManager.SnoozeReceiver;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "AgroWorld";
    public static final String channelName = "Agro World";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName,
                NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification(Context context, String title, String desc, String date, String time, int maxIDCount) {
        Intent newIntent = new Intent(context, AddTaskActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, maxIDCount, newIntent, PendingIntent.FLAG_IMMUTABLE);
        Intent snoozeButton = new Intent(context, SnoozeReceiver.class);
        snoozeButton.setAction("com.agroworld.SnoozeReceiver");
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, maxIDCount, snoozeButton, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.app_icon4,
                        "Cancel", pendingSwitchIntent)
                        .build();

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(desc)
                .setSubText(date + ", " + time)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(desc)
                        .setBigContentTitle(title))
                .setContentIntent(pendingIntent1)
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[]{0, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .addAction(action)
                .setGroupSummary(true);
    }
}