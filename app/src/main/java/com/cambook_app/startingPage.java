package com.cambook_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class startingPage extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3500;
    ImageView overlayImageView ,s_logo, txt_logo,  Ep_logo, textBG;
    TextView text;
    Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        overlayImageView = findViewById(R.id.overlayImageView);
        // Initialize AnimationDrawable and start it
        AnimationDrawable animationDrawable = (AnimationDrawable) overlayImageView.getBackground();
        animationDrawable.start();

        s_logo = findViewById(R.id.small_logo);
        txt_logo = findViewById(R.id.logo_txt_small);
        Ep_logo = findViewById(R.id.ep_logo);
        textBG = findViewById(R.id.text_bg);
        text = findViewById(R.id.app_txt);

        //animations
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom);

        // Set animations
        s_logo.startAnimation(top);
        txt_logo.startAnimation(top);
        Ep_logo.startAnimation(bottom);
        textBG.startAnimation(bottom);
        text.startAnimation(bottom);

        // Delayed task using Handler
        new Handler().postDelayed(() -> {
            // Start a new activity after the specified delay
            Intent intent = new Intent(startingPage.this, services.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
}