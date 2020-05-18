package com.example.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class BirthdayAlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "Birthday";
    @Override
    public void onReceive(Context context, Intent intent) {
        GregorianCalendar calendar = (GregorianCalendar) Calendar.getInstance();
        int id = intent.getIntExtra("PERSON_ID", 0);
        String message = intent.getStringExtra("MESSAGE");
        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("notification", id);
        notificationIntent.putExtra("callback", "done");
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        String title = context.getResources().getString(R.string.Remind);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setShowWhen(true)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context.getApplicationContext());
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
        Intent prealarm = new Intent(context, BirthdayAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, prealarm, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        calendar.add(Calendar.YEAR, 1);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }
}
