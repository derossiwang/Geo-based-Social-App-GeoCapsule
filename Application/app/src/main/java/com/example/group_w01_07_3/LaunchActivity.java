package com.example.group_w01_07_3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group_w01_07_3.features.onboarding.OnBoardingActivity;

/**
 * Displaying app launch animation for 2 seconds, and direct firs time user to onboarding page
 */
public class LaunchActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000; //animation duration
    ImageView logo;
    TextView title, subtitle;
    Animation logoAnim, textAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launch);

        //load animations
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_down);
        textAnim = AnimationUtils.loadAnimation(this, R.anim.down_to_top);

        //hooks
        logo = findViewById(R.id.launch_logo);
        title = findViewById(R.id.launch_title);
        subtitle = findViewById(R.id.launch_subtitle);

        logo.setAnimation(logoAnim);
        title.setAnimation(textAnim);
        subtitle.setAnimation(textAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, SPLASH_TIME_OUT);
    }
}