package com.plutos_seup.tweetags;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;

    long delay_time;
    long time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable,delay_time);
        time = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }
}
