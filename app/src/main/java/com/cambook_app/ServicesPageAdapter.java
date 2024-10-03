package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ServicesPageAdapter extends FragmentStateAdapter {
    public ServicesPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
            default:
                return new service_tabLayout();
            case 1:
                return new reviews_tabLayout();
            case 2:
                return new about_tabLayout();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}