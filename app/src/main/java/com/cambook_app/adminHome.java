package com.cambook_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminHome extends AppCompatActivity implements BookingSummaryAdapter.OnBookingActionListener, SwipeRefreshLayout.OnRefreshListener {

    private DatabaseReference bookingsRef;
    private RecyclerView recyclerView;
    private BookingSummaryAdapter adapter;
    private List<BookingSummary> bookingList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Initialize Firebase Database reference
        bookingsRef = FirebaseDatabase.getInstance().getReference().child("Bookings");

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.bookedEvents_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Initialize BookingSummaryAdapter with an empty list
        adapter = new BookingSummaryAdapter(new ArrayList<>(), this);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Initialize bookingList
        bookingList = new ArrayList<>();

        // Load booking data from Firebase
        loadBookingData();

        Button logOut = findViewById(R.id.adminLogOut);
        logOut.setOnClickListener(view -> {
            // Show a confirmation dialog before logging out
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(adminHome.this);
            alertDialog.setTitle("Log Out");
            alertDialog.setMessage("Are you sure you want to Log Out your Account?");
            alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                // Perform the logout operation
                FirebaseAuth.getInstance().signOut();

                // Navigate back to the login screen (MainActivity)
                Intent intent = new Intent(adminHome.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
            alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });
    }

    private void loadBookingData() {
        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing bookingList before adding new bookings
                bookingList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot bookingSnapshot : userSnapshot.getChildren()) {
                        // Retrieve the bookingID from the child key
                        String bookingID = bookingSnapshot.getKey();

                        // Now get the BookingSummary data
                        BookingSummary booking = bookingSnapshot.getValue(BookingSummary.class);
                        if (booking != null) {
                            // Set the retrieved bookingID to the BookingSummary object
                            booking.setBookingID(bookingID);

                            Log.d("BookingData", "Booking ID: " + booking.getBookingID());
                            bookingList.add(booking);
                        }
                    }
                }

                // Update the adapter with the new booking list
                adapter.updateBookingList(bookingList);

                // Stop the swipe-to-refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                // Stop the swipe-to-refresh animation in case of an error
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAcceptClicked(int position) {
        // Get the booking at the specified position
        BookingSummary booking = bookingList.get(position);

        // Update the status to "accepted" in the Firebase Realtime Database
        updateBookingStatus(booking.getUserID(), booking.getBookingID(), "accepted");
    }

    @Override
    public void onDeclineClicked(int position) {
        Log.d("adminHome", "onDeclineClicked: bookingList size - " + (bookingList != null ? bookingList.size() : 0));

        if (bookingList == null || bookingList.isEmpty()) {
            Log.e("adminHome", "bookingList is null or empty");
            return;
        }

        if (position >= 0 && position < bookingList.size()) {
            // Get the booking at the specified position
            BookingSummary booking = bookingList.get(position);

            // Update the status to "declined" in the Firebase Realtime Database
            updateBookingStatus(booking.getUserID(), booking.getBookingID(), "declined");
        } else {
            // Log an error or show a message indicating an invalid position
            Log.e("adminHome", "Invalid position in onDeclineClicked: " + position);
            // Optionally, show a message to the user that the operation is invalid.
        }
    }

    private void updateBookingStatus(String userID, String bookingID, String newStatus) {
        if (bookingID != null) {
            DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference()
                    .child("Bookings").child(userID).child(bookingID).child("status");

            bookingRef.setValue(newStatus);
            // You may want to add additional logic or callbacks based on your use case
        } else {
            // Handle the case where bookingID is null, log an error, or take appropriate action
            Log.e("adminHome", "updateBookingStatus: bookingID is null");
        }
    }

    @Override
    public void onRefresh() {
        // Handle the refresh action here
        // Load the data again or perform any necessary actions
        loadBookingData();
    }
}


