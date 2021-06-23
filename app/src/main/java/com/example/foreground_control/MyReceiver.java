package com.example.foreground_control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int actionMusic = intent.getIntExtra("action_music", 0);
        Intent intentService = new Intent(context , MyService.class);
        intentService.putExtra("data", actionMusic);
        Log.e("Log", "My RECEIVE : " + actionMusic);
        context.startService(intentService);
    }
}
