package com.cambook_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// BookingAdapter.java
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private final List<BookingModel> bookingList;

    public BookingAdapter(List<BookingModel> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        // Bind booking data to ViewHolder
        holder.eventTypeTextView.setText("Event Type: " + booking.getEventType());
        holder.downpaymentTextView.setText(""+ booking.getDownpayment());
        holder.totalPriceTextView.setText("" + booking.getTotalPrice());
        holder.statusTextView.setText(booking.getStatus());

        // Load image based on event type
        if ("pre_debut".equals(booking.getEventType()) && holder.eventImageview != null) {
            holder.eventImageview.setImageResource(R.drawable.pre_debutalbum7);
        } else if ("pre_wedding".equals(booking.getEventType()) && holder.eventImageview != null) {
            holder.eventImageview.setImageResource(R.drawable.pre_weddingalbum7);
        }

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTypeTextView;
        TextView downpaymentTextView;
        TextView totalPriceTextView;
        TextView statusTextView;
        ImageView eventImageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTypeTextView = itemView.findViewById(R.id.bookingEventType);
            downpaymentTextView = itemView.findViewById(R.id.bookingDownpaymentPrice);
            totalPriceTextView = itemView.findViewById(R.id.bookingTotalPrice);
            statusTextView = itemView.findViewById(R.id.bookingStatus);
            eventImageview = itemView.findViewById(R.id.bookingImage);
        }
    }
}