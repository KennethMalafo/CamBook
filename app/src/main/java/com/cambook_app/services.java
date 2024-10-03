package com.cambook_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class services extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        // Initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation_service);

        // Set a listener for the bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    loadFragment(new homeFragment());
                    return true;
                case R.id.nav_chat:
                    loadFragment(new chatFragment());
                    return true;
                case R.id.nav_profile:
                    loadFragment(new userProfile());
                    return true;
            }
            return false;
        });

        // Set the default selected tab.
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        // Exit Dialogue
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // Update the selected item in the bottom navigation view based on the current fragment
        if (currentFragment instanceof homeFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if (currentFragment instanceof chatFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_chat);
        } else if (currentFragment instanceof userDetailsFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }


        if (currentFragment instanceof homeFragment) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Exit App");
            alertDialog.setMessage("Do you want to Exit CamBook?");
            alertDialog.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
            alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
