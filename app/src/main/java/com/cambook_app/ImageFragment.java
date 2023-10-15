package com.cambook_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    public static ImageFragment newInstance(int position) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        // Get the ImageView and TextView references
        ImageView imageView = rootView.findViewById(R.id.imageView);
        TextView textView = rootView.findViewById(R.id.txt);

        // Get the position from arguments
        int position = getArguments().getInt(ARG_POSITION);

        // Load the appropriate image based on position
        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.pre_debut);
                textView.setText("Pre-Debut");
                break;
            case 1:
                imageView.setImageResource(R.drawable.pre_wedding);
                textView.setText("Pre-Wedding");
                break;
            case 2:
                imageView.setImageResource(R.drawable.birthday);
                textView.setText("Events");
                break;
            case 3:
                imageView.setImageResource(R.drawable.videos);
                textView.setText("Videos");
                break;
            default:
                // Handle the case for additional positions if needed
                break;
        }
        // Set a click listener for the ImageView
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event based on the image position
                handleImageClick(position);
            }
        });

        return rootView;
    }
    // Method to handle the click event based on image position
    private void handleImageClick(int position) {
        switch (position) {
            case 0:
                // Handle the click for Image 1, for example, open a new activity
                break;
            case 1:
                // Handle the click for Image 2
                break;
            case 2:
                // Handle the click for Image 3
                break;
            case 3:
                // Handle the click for Image 4
                break;
            default:
                // Handle other cases if needed
                break;
        }
    }
}