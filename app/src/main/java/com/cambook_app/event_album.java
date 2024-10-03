package com.cambook_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class event_album extends Fragment {

    public event_album() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event_album, container, false);

        Button bookingTransitionButton = view.findViewById(R.id.events_BookingTransaction);
        bookingTransitionButton.setOnClickListener(v -> {
            // Intent to start the bookingTransaction
            Intent intent = new Intent(getActivity(), bookingTransaction.class);
            startActivity(intent);
        });

        return view;
    }
}