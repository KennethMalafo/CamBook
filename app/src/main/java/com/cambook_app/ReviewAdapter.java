package com.cambook_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviewList;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("ProfilePics");
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reviews, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);

        String userId = review.getUserId();
        Log.d("FirebaseStorage", "User ID: " + userId);
        String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/cambook-c6ad5.appspot.com/o/ProfilePics%2F" + userId + ".jpg?alt=media&token=329305b9-8339-4966-9c40-ff6f84cabbf8";
        loadProfilePicture(downloadUrl, holder.userProfileImageView);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView fullNameTextView;
        private final TextView reviewTextView;
        private final ImageView userProfileImageView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your ViewHolder views here
            fullNameTextView = itemView.findViewById(R.id.full_name);
            reviewTextView = itemView.findViewById(R.id.review);
            userProfileImageView = itemView.findViewById(R.id.user_profile);
        }

        public void bind(Review review) {
            // Bind data to your ViewHolder views here
            fullNameTextView.setText(review.getFullName());
            reviewTextView.setText(review.getReviewText());
        }
    }

    // Method to load and display the user profile picture
    private void loadProfilePicture(String downloadUrl, ImageView imageView) {
        Picasso.get()
                .load(downloadUrl)
                .placeholder(R.drawable.profile_pic) // Placeholder image while loading
                .into(imageView);
    }
}