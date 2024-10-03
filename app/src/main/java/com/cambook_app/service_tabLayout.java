package com.cambook_app;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class service_tabLayout extends Fragment {
    CardView Pre_debut, Pre_Wedding, Events, Videos;

    public service_tabLayout() {
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
        View view = inflater.inflate(R.layout.fragment_service_tab_layout, container, false);

        Pre_debut = view.findViewById(R.id.pre_debut_container);
        Pre_debut.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new pre_debut_album())
                .addToBackStack(null)
                .commit());

        Pre_Wedding = view.findViewById(R.id.pre_wedding_container);
        Pre_Wedding.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new pre_wedding_album())
                .addToBackStack(null)
                .commit());

        Events = view.findViewById(R.id.events_container);
        Events.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new event_album())
                .addToBackStack(null)
                .commit());

        Videos = view.findViewById(R.id.videos_container);
        Videos.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new videos_album())
                .addToBackStack(null)
                .commit());

        return view;
    }
}
