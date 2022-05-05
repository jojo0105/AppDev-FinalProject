package com.example.discovery;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.discovery.Models.Visit;

public class Notification {

    public Notification() {
    }

    public void getScheculeConfimaionNotif(Context context, Visit visit){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        String message = "Visit " + visit.getPark().getFullName() + " on the " + visit.getDateString();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification");
        builder.setContentTitle("Schecule Confirmation");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.banner_intro);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }

}
