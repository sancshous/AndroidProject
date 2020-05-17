package com.example.androidproject;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BirthdayAlarmReceiver extends BroadcastReceiver {
    WeakReference<Context> contextRef;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "Birthday";
    private GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        contextRef = new WeakReference<>(context);
        Context weakContext = contextRef.get();
        int id = intent.getIntExtra("PERSON_ID", 0);
        String message = intent.getStringExtra("MESSAGE");
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "weather", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Description");
        channel.enableVibration(true);
        channel.setShowBadge(true);

        Intent notificationIntent = new Intent(weakContext.getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("notification", id);
        notificationIntent.putExtra("callback", "done");

        PendingIntent pendingIntent = PendingIntent.getActivity(weakContext.getApplicationContext(),
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(weakContext.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Напоминание")
                .setContentText(message)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setShowWhen(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(weakContext.getApplicationContext());
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
        Intent prealarm = new Intent(weakContext, BirthdayAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(weakContext, 0, prealarm, 0);
        AlarmManager alarmManager = (AlarmManager) weakContext.getSystemService(Context.ALARM_SERVICE);
        calendar.add(Calendar.YEAR, 1);
        if(calendar.isLeapYear(calendar.get(Calendar.YEAR))){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), alarmIntent);
        }else alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), alarmIntent);
    }
}
