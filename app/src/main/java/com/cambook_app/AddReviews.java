package com.cambook_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddReviews extends AppCompatActivity {

    private EditText reviewEditText;
    private EditText editFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reviews);

        reviewEditText = findViewById(R.id.review);
        editFullName = findViewById(R.id.editFullName);

        Button saveReviewButton = findViewById(R.id.save_review);

        saveReviewButton.setOnClickListener(view -> saveReview());
    }

    private void saveReview() {
        String userReview = reviewEditText.getText().toString();
        String fullname = editFullName.getText().toString();

        if (!TextUtils.isEmpty(userReview) && !TextUtils.isEmpty(fullname)) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                String userId = user.getUid(); // This is the user ID

                // Create a Review object
                Review review = new Review(userId, fullname, userReview);

                // Get the reference to the "Reviews" node
                DatabaseReference reviewsReference = FirebaseDatabase.getInstance().getReference("Reviews").child(userId);

                // Push the review to the user ID node in Firebase
                String reviewId = reviewsReference.push().getKey();
                reviewsReference.child(reviewId).setValue(review);

                // Clear the EditText after saving
                reviewEditText.setText("");
                editFullName.setText("");

                Toast.makeText(AddReviews.this, "Review saved successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddReviews.this, services.class);
                startActivity(intent);
                finish();
            } else {
                // Handle the case where the user is not authenticated
                Toast.makeText(AddReviews.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show an error message if the review or fullname is empty
            Toast.makeText(AddReviews.this, "Please enter a review and fullname", Toast.LENGTH_SHORT).show();
        }
    }
}
