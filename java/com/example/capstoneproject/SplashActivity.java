package com.example.capstoneproject;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private static boolean splashLoaded = false;
    ImageView imageView;
    Animation frombus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!splashLoaded) {
            setContentView(R.layout.activity_splash);
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, GetStartedActivity.class));
                    finish();
                }
            }, secondsDelayed * 3000);

            splashLoaded = true;
        }
        else {
            Intent goToGetActivityStarted = new Intent(SplashActivity.this, GetStartedActivity.class);
            goToGetActivityStarted.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToGetActivityStarted);
            finish();
        }
        imageView = (ImageView)findViewById(R.id.imbus);
        frombus = AnimationUtils.loadAnimation(this,R.anim.frombus);
        imageView.setAnimation(frombus);
    }
}
