package com.example.foreground_control;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Channel_Id extends Application {
    public static final String CHANNEL_ID ="channel_service_example";
    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    private void createChannelNotification() {
        // Test xem version co tren 26 khong
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel Service Example" ,
                    NotificationManager.IMPORTANCE_DEFAULT
                    );
            channel.setSound(null, null ); // từ android 8 trở lên phải vào đây set
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null)
            {
                manager.createNotificationChannel(channel);
            }

        }
    }
}
