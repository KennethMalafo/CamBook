package com.cambook_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homepage extends AppCompatActivity {
    ImageView Settings, Ep_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Settings = findViewById(R.id.settings);
        Ep_logo = findViewById(R.id.ep_logo);

        Settings.setOnClickListener(view -> {
            Intent intent = new Intent(homepage.this, settings.class);
            startActivity(intent);
            finish();
        });

        Ep_logo.setOnClickListener(view -> {
            Intent intent = new Intent(homepage.this, services.class);
            startActivity(intent);
            finish();
        });
    }
    @Override
    public void onBackPressed() {

        //Exit Dialogue
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(homepage.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to Exit CamBook?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}