package com.cambook_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class userDetailsFragment extends Fragment {
    private ImageButton profileImage;

    //user profile details
    TextView Fname, Username, Province, City, Barangay, DOB, Gender, Email_Address;
    private String UserName, fName, eMail, pRovince, cIty, bArangay, dOb, gEnder;

    private View view;

    public userDetailsFragment() {
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
        view = inflater.inflate(R.layout.fragment_user_details, container, false);

        ImageButton back = view.findViewById(R.id.arrow1);
        back.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new userProfile())
                .addToBackStack(null)
                .commit());

        //user profile
        Fname = view.findViewById(R.id.fname_service);
        Username = view.findViewById(R.id.username_service);
        Province = view.findViewById(R.id.province_service);
        City = view.findViewById(R.id.city_service);
        Barangay = view.findViewById(R.id.barangay_service);
        DOB = view.findViewById(R.id.dob_service);
        Gender = view.findViewById(R.id.gender_service);
        Email_Address = view.findViewById(R.id.email_service);

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

        profileImage = view.findViewById(R.id.profile_pic_service);
        profileImage.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new editUserProfileFragment())
                .addToBackStack(null)
                .commit());

        //edit profile btn
        Button edit_profile = view.findViewById(R.id.edit_service);
        //edit profile OnClick
        edit_profile.setOnClickListener(buttonView -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new editUserProfileFragment())
                .addToBackStack(null)
                .commit());

        return view;
    }

    //show User Profile
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
                    UserName = users.username;
                    pRovince = users.province;
                    cIty = users.city;
                    bArangay = users.barangay;
                    dOb = users.dob;
                    gEnder = users.gender;
                    eMail = firebaseUser.getEmail();

                    //set text
                    Fname.setText("Welcome " + fName+ "!");
                    Username.setText(UserName);
                    Province.setText(pRovince);
                    City.setText(cIty);
                    Barangay.setText(bArangay);
                    DOB.setText(dOb);
                    Gender.setText(gEnder);
                    Email_Address.setText(eMail);

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
                }else{
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