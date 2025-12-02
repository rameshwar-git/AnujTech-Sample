package com.anujtech.app.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.anujtech.app.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingReceiver extends FirebaseMessagingService {
    private static final int NOTIFICATION_REQUEST_CODE = 1234;
    String TAG = "NotificationReceive";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FCM", "Subscribed to 'all' topic");
                    } else {
                        Log.e("FCM", "Topic subscription failed");
                    }
                });

    }
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if (message.getNotification() != null)
            ShowNotification(
                    message.getNotification().getTitle(),
                    message.getNotification().getBody()
            );
    }
    private void ShowNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(getApplicationContext(), MyFirebaseMessagingReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), String.valueOf(NOTIFICATION_REQUEST_CODE))
                        .setSmallIcon(R.drawable.notification_icon)
                        .setAutoCancel(true)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setContent(getCustomDesign(messageTitle, messageBody));
        }
        else {
            notificationBuilder.setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000,1000})
                    .setSmallIcon(R.drawable.notification_icon);
        }
        NotificationChannel channel = new NotificationChannel(String.valueOf(NOTIFICATION_REQUEST_CODE),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1234, notificationBuilder.build());
    }

    private RemoteViews getCustomDesign(String messageTitle, String messageBody) {
        @SuppressLint("RemoteViewLayout")
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification);
        remoteViews.setTextViewText(R.id.notify_title, messageTitle);
        remoteViews.setTextViewText(R.id.notify_message, messageBody);
        remoteViews.setImageViewResource(R.id.icon, R.drawable.notification_icon);
        return remoteViews;
    }
}