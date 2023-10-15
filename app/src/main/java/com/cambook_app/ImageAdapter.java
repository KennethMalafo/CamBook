package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
public class ImageAdapter extends FragmentStateAdapter {

    public ImageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a Fragment for the given position
        return ImageFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        // Return the number of pages (images)
        return 4; // Change this to the actual number of images
    }
}
