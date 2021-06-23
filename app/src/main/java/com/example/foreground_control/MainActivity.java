package com.example.foreground_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foreground_control.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventStartService();
            }
        });

        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventStopService();
            }
        });

    }
    private void eventStopService() {
        Intent intent = new Intent(this , MyService.class);
        stopService(intent);

    }

    private void eventStartService() {
        Song song = new Song("Sài Gòn Đau Lòng Qúa" , "Hứa Kim Tuyền" , R.drawable.mp3 , R.raw.saigondaulongqua );
        // Khởi chạy unBound bằng start Service
        Intent intent = new Intent(this , MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_data_intent" , song);
        intent.putExtras(bundle);
        startService(intent);
    }
}