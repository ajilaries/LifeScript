package com.example.lifescript.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifescript.R;
import com.example.lifescript.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (sessionManager.isLoggedIn()) {
                    intent = new Intent(SplashActivity.this, AddMemoryActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000); // 2 seconds delay
    }
}
