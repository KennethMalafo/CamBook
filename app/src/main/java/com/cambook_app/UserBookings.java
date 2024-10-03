package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// UserBooking.java
public class UserBookings extends AppCompatActivity {
    ImageView cancel;
    private final List<BookingModel> bookingList = new ArrayList<>();
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookings);

        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(UserBookings.this, userProfile.class);
            startActivity(intent);
            finish();
        });

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display booked events from Firebase
        fetchBookedEventsFromFirebase();
    }

    // Fetch booked events from Firebase and update the RecyclerView
    private void fetchBookedEventsFromFirebase() {
        String userID = getCurrentUserID();
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference().child("Bookings").child(userID);

        // Attach a ValueEventListener to retrieve data
        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing data
                bookingList.clear();

                // Iterate through the dataSnapshot to retrieve booking details
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    String eventType = bookingSnapshot.child("eventType").getValue(String.class);
                    Long downpayment = bookingSnapshot.child("downpayment").getValue(Long.class);
                    Long totalPrice = bookingSnapshot.child("totalPrice").getValue(Long.class);
                    String status = bookingSnapshot.child("status").getValue(String.class);
                    String imageUrl = bookingSnapshot.child("eventImage").getValue(String.class);

                    Log.d("UserBookings", "Booking Snapshot: " + bookingSnapshot);
                    Log.d("UserBookings", "Event Type: " + eventType);
                    Log.d("UserBookings", "Downpayment: " + downpayment);
                    Log.d("UserBookings", "Total Price: " + totalPrice);

                    // Create a BookingModel object and add it to the list
                    BookingModel booking = new BookingModel(eventType, downpayment, totalPrice, status, imageUrl);
                    bookingList.add(booking);
                }

                // Reverse the order of the list to show the latest booking first
                Collections.reverse(bookingList);

                // Notify the adapter about the data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                Toast.makeText(UserBookings.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentUserID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }
}