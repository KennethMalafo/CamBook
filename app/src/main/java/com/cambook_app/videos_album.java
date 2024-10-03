package com.cambook_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

public class videos_album extends Fragment {

    private boolean[] isFullScreenArray;

    public videos_album() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos_album, container, false);

        VideoView videoView1 = view.findViewById(R.id.videoView1);
        VideoView videoView2 = view.findViewById(R.id.videoView2);
        VideoView videoView3 = view.findViewById(R.id.videoView3);
        VideoView videoView4 = view.findViewById(R.id.videoView4);

        isFullScreenArray = new boolean[]{false, false, false, false}; // Initialize with the number of VideoViews

        initializeVideoView(videoView1, R.raw.videosalbum1);
        initializeVideoView(videoView2, R.raw.videosalbum3);
        initializeVideoView(videoView3, R.raw.videosalbum2);
        initializeVideoView(videoView4, R.raw.videosalbum4);

        setClickListeners(videoView1, 0);
        setClickListeners(videoView2, 1);
        setClickListeners(videoView3, 2);
        setClickListeners(videoView4, 3);

        Button bookingTransitionButton = view.findViewById(R.id.videos_BookingTransaction);
        bookingTransitionButton.setOnClickListener(v -> {
            // Intent to start the bookingTransaction
            Intent intent = new Intent(getActivity(), bookingTransaction.class);
            startActivity(intent);
        });

        return view;
    }

    private void initializeVideoView(VideoView videoView, int rawResourceId) {
        videoView.setVideoPath("android.resource://" + requireActivity().getPackageName() + "/" + rawResourceId);

        MediaController mediaController = new MediaController(requireContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    private void setClickListeners(VideoView videoView, int index) {
        videoView.setOnClickListener(v -> toggleFullScreen(videoView, index));
    }

    private void toggleFullScreen(VideoView videoView, int index) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        androidx.appcompat.app.ActionBar actionBar = activity.getSupportActionBar();

        if (!isFullScreenArray[index]) {
            // Go full screen
            if (actionBar != null) {
                actionBar.hide();
            }
            setFullscreen(videoView, true);
        } else {
            // Exit full screen
            if (actionBar != null) {
                actionBar.show();
            }
            setFullscreen(videoView, false);
        }
        isFullScreenArray[index] = !isFullScreenArray[index];
    }

    private void setFullscreen(VideoView videoView, boolean fullscreen) {
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        if (fullscreen) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        videoView.setLayoutParams(layoutParams);
    }
}
