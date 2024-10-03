package com.cambook_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class bookingTransaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_transaction);

        chooseEvent transaction = new chooseEvent();
        loadFragment(transaction);
    }
    // Function to load a fragment into the container
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bookingTransactionContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}