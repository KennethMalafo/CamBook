package com.cambook_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class settings extends AppCompatActivity {

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.arrow1);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(settings.this, homepage.class);
            startActivity(intent);
            finish();
        });

    }
}