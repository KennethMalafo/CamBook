package com.cambook_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class reviews_tabLayout extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private List<Review> reviewsList = new ArrayList<>();

    public reviews_tabLayout() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reviews_tab_layout, container, false);

        // Set up click listener for the "Add Review" button
        Button addReview = view.findViewById(R.id.add_review);
        addReview.setOnClickListener(v ->  {
            Intent intent = new Intent(getActivity(), AddReviews.class);
            startActivity(intent);
        });

        recyclerView = view.findViewById(R.id.reviews_recyclerView);
        adapter = new ReviewAdapter(reviewsList);
        recyclerView.setAdapter(adapter);

        // Load reviews from Firebase and add them to reviewsList
        loadReviewsFromFirebase();
        return view;
    }
    private void loadReviewsFromFirebase() {
        DatabaseReference reviewsReference = FirebaseDatabase.getInstance().getReference("Reviews");

        reviewsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewsList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot reviewSnapshot : userSnapshot.getChildren()) {
                        Review review = reviewSnapshot.getValue(Review.class);
                        if (review != null) {
                            reviewsList.add(0, review);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
