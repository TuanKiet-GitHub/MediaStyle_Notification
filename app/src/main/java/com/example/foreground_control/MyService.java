package com.example.foreground_control;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.foreground_control.Channel_Id.CHANNEL_ID;


public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private static final int ACTION_PAUSE = 1 ;
    private static final int ACTION_RESUME = 2 ;
    private static final int ACTION_CLEAR = 3 ;
    private boolean isPlaying = false ;
    private Song msong ;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Nhận dữ liệu trong OnStartCommand
        Bundle dataIntent = intent.getExtras();
        if (dataIntent !=null)
        {
            Song song = (Song) dataIntent.get("key_data_intent");
            if(song!=null)
            {
                msong = song ;
                startMusic(song);
                sendNotification(song);
            }
        }
        int actionMusic = intent.getIntExtra("data" , 10 );
        Log.e("Log", "My SERVICE : " + actionMusic);
        handleActionMucsic(actionMusic);
        // Update view trên notification thì phải gửi lại notification nên trong handleActionMusic chúng ta phải gửi lại notification


        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if (mediaPlayer ==  null)
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext() , song.getResource());
        }
        mediaPlayer.start();
        isPlaying = true ;
        // Chạy nhạc phải tắt tiếng chuông của notification
    }

    private void sendNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImage());
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.tv_title_song , song.getTitle() );
        remoteViews.setTextViewText(R.id.tv_single_song , song.getSingle());
        remoteViews.setImageViewBitmap(R.id.imgView, bitmap);
        remoteViews.setImageViewResource(R.id.imagePause , R.drawable.pause);
        // Bắt sự kiện click
        if(isPlaying)
        {
            remoteViews.setOnClickPendingIntent(R.id.imagePause , getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.imagePause, R.drawable.pause);
        }
        else
        {
            remoteViews.setOnClickPendingIntent(R.id.imagePause , getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.imagePause, R.drawable.play);
        }
        remoteViews.setOnClickPendingIntent(R.id.imgClear, getPendingIntent(this , ACTION_CLEAR));


        Notification notification = new NotificationCompat.Builder(this , CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null) // SET Sound null chỉ có thể set trong android 8 trở xuống nếu xét từ android 8 trở lên phải xét trong channel _ id
                // Nếu còn có tiếng rùi gỡ ứng dụng cày lại hoặc là clear Storage
                .build();
        startForeground(1, notification);

    }

    private PendingIntent getPendingIntent(Context context , int action) {
        Intent intent = new Intent(this , MyReceiver.class);
        intent.putExtra("action_music" , action);
        Log.e("Log", "Get Pending Intent : " + action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action , intent , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void handleActionMucsic(int action)
    {
         switch (action)
         {
             case ACTION_PAUSE:
                 pauseMusic();
                 break;
             case ACTION_RESUME:
                 resumeMusic();
                 break;
             case ACTION_CLEAR:
                 stopSelf();
                 Log.e("Log", "CLEAR");
                 break;
         }
    }


    private void resumeMusic() {
        Log.e("Log" , "BEFORE RESUME MUSIC : " + isPlaying);
        if (mediaPlayer != null && !isPlaying)
        {
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(msong);

        }
        Log.e("Log" , "RESUME MUSIC : " + isPlaying);
    }

    private void pauseMusic()
    {
        Log.e("Log" , "BEFORE PAUSE MUSIC : " + isPlaying);
         if (mediaPlayer != null && isPlaying)
         {
             mediaPlayer.pause();
             isPlaying = false;// Chú ý thay đổi trạng thái trước khi gửi nếu không không thể resume dc
             sendNotification(msong);

         }
        Log.e("Log" , "PAUSE MUSIC : " + isPlaying);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false ;
        }
    }
}
