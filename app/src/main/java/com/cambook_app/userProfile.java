package com.cambook_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userProfile extends Fragment {

    private ImageButton profileImage;

    TextView Fname;

    private String fName;
    private View view;

    public userProfile() {
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
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        Fname = view.findViewById(R.id.user_fname);

        //getting user from firebase service
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        //checking if the user is null service
        if (firebaseUser == null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else {
            showUserProfile(firebaseUser);
        }

        profileImage = view.findViewById(R.id.user_profile_pic);
        profileImage.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new editUserProfileFragment())
                .addToBackStack(null)
                .commit());

        CardView user_info = view.findViewById(R.id.user_information);
        user_info.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new userDetailsFragment())
                .addToBackStack(null)
                .commit());

        CardView booking = view.findViewById(R.id.booking);
        booking.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), UserBookings.class);
            startActivity(intent);
        });

        //logout btn service
        CardView logOut = view.findViewById(R.id.LogOUT);
        //logout button service
        logOut.setOnClickListener(buttonView -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Log Out");
            alertDialog.setMessage("Are you sure you want to Log Out your Account?");
            alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            });
            alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String firebaseUserUid = firebaseUser.getUid();

        //Extracting User Reference from Database for "User"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User/UserID");
        referenceProfile.child(firebaseUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null) {

                    fName = users.fname;

                    //set text
                    Fname.setText("Welcome " + fName + "!");

                    //Set User Profile Pic
                    Uri uri = firebaseUser.getPhotoUrl();
                    if (uri != null) {
                        // User has a profile image, hide the "Click to change profile" text
                        TextView clickToChangeProfileText = view.findViewById(R.id.click_to_change_profile_text);
                        if (clickToChangeProfileText != null) {
                            clickToChangeProfileText.setVisibility(View.GONE);
                        }
                    }

                    Picasso.get()
                            .load(uri)
                            .into(profileImage);
                } else {
                    Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}