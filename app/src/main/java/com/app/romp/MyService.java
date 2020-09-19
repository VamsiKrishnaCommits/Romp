package com.app.romp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MyService extends FirebaseMessagingService {
    NotificationChannel notificationChannel;
    Activity activity;
    public MyService(){
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String CHANNEL_ID="order status";
        String CHANNEL_ORDER=remoteMessage.getData().get("title");
        String CHANNEL_ITEM=remoteMessage.getData().get("message");
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //create notification channel if the api level is greater than oreo
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ITEM, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(CHANNEL_ORDER);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher_foreground).setContentTitle(CHANNEL_ORDER).setContentText(CHANNEL_ITEM).setPriority(NotificationCompat.PRIORITY_MAX).setDefaults(NotificationCompat.DEFAULT_ALL);
        NotificationManagerCompat manager = NotificationManagerCompat.from(getBaseContext());
        manager.notify(0, builder.build());
    }
}
