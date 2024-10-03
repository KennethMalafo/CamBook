package com.cambook_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class chooseEvent extends Fragment {

    private ImageView selectedImageView; // Keep track of the currently selected image

    private RelativeLayout BookPre_debut;
    private RelativeLayout BookPre_wedding;
    private RelativeLayout BookEvent;
    private RelativeLayout BookVideos;

    private Button proceed;

    public chooseEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_event, container, false);

        ImageView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            // Intent to start the bookingTransaction
            Intent intent = new Intent(getActivity(), services.class);
            startActivity(intent);
        });

        BookPre_debut = view.findViewById(R.id.bookPre_debut);
        BookPre_wedding = view.findViewById(R.id.bookPre_wedding);
        BookEvent = view.findViewById(R.id.bookEvent);
        BookVideos = view.findViewById(R.id.bookVideos);

        ImageView pre_debut = BookPre_debut.findViewById(R.id.pre_debut_img);
        ImageView pre_wedding = BookPre_wedding.findViewById(R.id.pre_wedding_img);
        ImageView event = BookEvent.findViewById(R.id.event_img);
        ImageView videos = BookVideos.findViewById(R.id.videos_img);

        // Load the drawables
        Drawable selectedPre_debut = getResources().getDrawable(R.drawable.selected_image);
        Drawable selectedPre_wedding = getResources().getDrawable(R.drawable.selected_image1);
        Drawable selectedEvent = getResources().getDrawable(R.drawable.selected_image2);
        Drawable selectedVideos = getResources().getDrawable(R.drawable.selected_image3);

        Drawable unselectedPre_debut = getResources().getDrawable(R.drawable.unselected_image);
        Drawable unselectedPre_wedding = getResources().getDrawable(R.drawable.unselected_image1);
        Drawable unselectedEvent = getResources().getDrawable(R.drawable.unselected_image2);
        Drawable unselectedVideos = getResources().getDrawable(R.drawable.unselected_image3);

        // Set initial drawables
        pre_debut.setImageDrawable(unselectedPre_debut);
        pre_wedding.setImageDrawable(unselectedPre_wedding);
        event.setImageDrawable(unselectedEvent);
        videos.setImageDrawable(unselectedVideos);

        pre_debut.setOnClickListener(v -> handleImageSelection(pre_debut, selectedPre_debut, unselectedPre_debut));
        pre_wedding.setOnClickListener(v -> handleImageSelection(pre_wedding, selectedPre_wedding, unselectedPre_wedding));
        event.setOnClickListener(v -> handleImageSelection(event, selectedEvent, unselectedEvent));
        videos.setOnClickListener(v -> handleImageSelection(videos, selectedVideos, unselectedVideos));

        proceed = view.findViewById(R.id.packagesNxtButton);
        proceed.setEnabled(false);

        proceed.setOnClickListener(view1 -> {
            // Check if an image is selected
            if (selectedImageView != null) {
                Fragment fragmentToNavigate = null;

                // Determine which image is selected and set the corresponding fragment
                if (selectedImageView == BookPre_debut.findViewById(R.id.pre_debut_img)) {
                    fragmentToNavigate = new pre_debut_confirmEventFragment();
                } else if (selectedImageView == BookPre_wedding.findViewById(R.id.pre_wedding_img)) {
                    fragmentToNavigate = new pre_wedding_confirmEventFragment();
                } else if (selectedImageView == BookEvent.findViewById(R.id.event_img)) {
                    Toast.makeText(requireContext(), "Under Maintenance", Toast.LENGTH_SHORT).show();
                } else if (selectedImageView == BookVideos.findViewById(R.id.videos_img)) {
                    Toast.makeText(requireContext(), "Under Maintenance", Toast.LENGTH_SHORT).show();
                }

                // Navigate to the selected fragment
                if (fragmentToNavigate != null) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.bookingTransactionContainer, fragmentToNavigate)
                            .addToBackStack(null)
                            .commit();
                }
            } else {
                Toast.makeText(requireContext(), "Please select an event before proceeding", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void handleImageSelection(ImageView imageView, Drawable selectedDrawable, Drawable unselectedDrawable) {
        // Check if the clicked image is already selected
        if (selectedImageView == imageView) {
            // Unselect the image
            selectedImageView = null;
            imageView.setImageDrawable(unselectedDrawable);

            // Disable the Proceed button when no image is selected
            proceed.setEnabled(false);
        } else {
            // Unselect the currently selected image (if any)
            if (selectedImageView != null) {
                selectedImageView.setImageDrawable(getUnselectedDrawable(selectedImageView));
            }

            // Select the clicked image
            selectedImageView = imageView;
            selectedDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            imageView.setImageDrawable(selectedDrawable);

            // Enable the Proceed button
            proceed.setEnabled(true);
        }
    }


    private Drawable getUnselectedDrawable(ImageView imageView) {
        // Return the appropriate unselected drawable based on the image view
        if (imageView == BookPre_debut.findViewById(R.id.pre_debut_img)) {
            return getResources().getDrawable(R.drawable.unselected_image);
        } else if (imageView == BookPre_wedding.findViewById(R.id.pre_wedding_img)) {
            return getResources().getDrawable(R.drawable.unselected_image1);
        } else if (imageView == BookEvent.findViewById(R.id.event_img)) {
            return getResources().getDrawable(R.drawable.unselected_image2);
        } else if (imageView == BookVideos.findViewById(R.id.videos_img)) {
            return getResources().getDrawable(R.drawable.unselected_image3);
        }

        return null;
    }
}
