package com.cambook_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingSummaryAdapter extends RecyclerView.Adapter<BookingSummaryAdapter.ViewHolder> {

    private final List<BookingSummary> bookingList;
    private final OnBookingActionListener listener;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;

    public BookingSummaryAdapter(List<BookingSummary> bookingList, OnBookingActionListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("ProfilePics");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booked_events, parent, false);
        return new ViewHolder(view);
    }

    public void updateBookingList(List<BookingSummary> newList) {
        bookingList.clear();
        bookingList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingSummary booking = bookingList.get(position);

        // Check for null before setting text
        if (booking != null) {

            if (holder.userIDTextView != null) {
                holder.userIDTextView.setText("User ID: " + booking.getUserID());
            }

            if (holder.eventTypeTextView != null) {
                holder.eventTypeTextView.setText("Event Type: " + booking.getEventType());
            }
            if (holder.packageNameTextView != null) {
                holder.packageNameTextView.setText("Package Name: " + booking.getPackageName());
            }
            if (holder.provinceTextView != null) {
                holder.provinceTextView.setText("Province: " + booking.getProvince());
            }
            if (holder.cityTextView != null) {
                holder.cityTextView.setText("City: " + booking.getCity());
            }
            if (holder.barangayTextView != null) {
                holder.barangayTextView.setText("Barangay: " + booking.getBarangay());
            }
            if (holder.selectedDateTextView != null) {
                holder.selectedDateTextView.setText("Selected Date: " + booking.getSelectedDate());
            }
            if (holder.downpaymentTextView != null) {
                holder.downpaymentTextView.setText("Downpayment: " + booking.getDownpayment());
            }
            if (holder.totalPriceTextView != null) {
                holder.totalPriceTextView.setText("Total Price: " + booking.getTotalPrice());
            }

            if (holder.statusTextView != null) {
                holder.statusTextView.setText("Status: " + booking.getStatus());
            }

            if (holder.bookingIDTextView != null) {
                holder.bookingIDTextView.setText("Booking ID: " + booking.getBookingID());
            }

            // Load and set downpayment image
            if (holder.downpaymentImageView != null) {
                String downpaymentImageUrl = booking.getDownpaymentImageUrl();
                if (downpaymentImageUrl != null && !downpaymentImageUrl.isEmpty()) {
                    // Use Picasso to load and set the image
                    Picasso.get()
                            .load(downpaymentImageUrl)
                            .into(holder.downpaymentImageView);
                }

                String userId = booking.getUserID();
                Log.d("FirebaseStorage", "User ID: " + userId);
                String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/cambook-c6ad5.appspot.com/o/ProfilePics%2F" + userId + ".jpg?alt=media&token=329305b9-8339-4966-9c40-ff6f84cabbf8";
                loadProfilePicture(downloadUrl, holder.profilePictureImageView);

                // Set click listeners for accept and decline buttons
                if (holder.acceptButton != null) {
                    holder.acceptButton.setOnClickListener(view -> listener.onAcceptClicked(position));
                }

                if (holder.declineButton != null) {
                    holder.declineButton.setOnClickListener(view -> listener.onDeclineClicked(position));
                }
            } else {
                Log.e("BookingSummaryAdapter", "onBindViewHolder: booking is null at position " + position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePictureImageView;
        TextView userIDTextView;
        TextView eventTypeTextView;
        TextView packageNameTextView;
        TextView provinceTextView;
        TextView cityTextView;
        TextView barangayTextView;
        TextView selectedDateTextView;
        TextView downpaymentTextView;
        TextView totalPriceTextView;
        ImageView downpaymentImageView;
        TextView statusTextView;
        TextView bookingIDTextView;
        Button acceptButton;
        Button declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePictureImageView = itemView.findViewById(R.id.user_profile);
            userIDTextView = itemView.findViewById(R.id.userId);
            eventTypeTextView = itemView.findViewById(R.id.eventType);
            packageNameTextView = itemView.findViewById(R.id.packageName);
            provinceTextView = itemView.findViewById(R.id.province);
            cityTextView = itemView.findViewById(R.id.city);
            barangayTextView = itemView.findViewById(R.id.barangay);
            selectedDateTextView = itemView.findViewById(R.id.selectedDate);
            downpaymentTextView = itemView.findViewById(R.id.downpaymentPrice);
            totalPriceTextView = itemView.findViewById(R.id.totalPrice);
            downpaymentImageView = itemView.findViewById(R.id.downpayment);
            statusTextView = itemView.findViewById(R.id.status);
            bookingIDTextView = itemView.findViewById(R.id.bookingId);
            acceptButton = itemView.findViewById(R.id.acceptBooking);
            declineButton = itemView.findViewById(R.id.declineBooking);
        }
    }

    // Method to load and display the user profile picture
    private void loadProfilePicture(String downloadUrl, ImageView imageView) {
        Picasso.get()
                .load(downloadUrl)
                .placeholder(R.drawable.profile_pic) // Placeholder image while loading
                .into(imageView);
    }

    public interface OnBookingActionListener {
        void onAcceptClicked(int position);
        void onDeclineClicked(int position);
    }
}


