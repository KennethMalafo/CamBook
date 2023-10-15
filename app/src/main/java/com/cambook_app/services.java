package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class services extends AppCompatActivity {

    //sections for bottom navigation view
    private View section1, section2, section3;

    //user profile details
    TextView Fname, Username, Province, City, Barangay, DOB, Gender, Email_Address;
    private String UserName, fName, eMail, pRovince, cIty, bArangay, dOb, gEnder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        // Inflate the layouts for each section
        section1 = findViewById(R.id.layout_section1);
        section2 = findViewById(R.id.layout_section2);
        section3 = findViewById(R.id.layout_section3);
        // Set initial alpha for the sections
        section1.setAlpha(1.0f);
        section2.setAlpha(0.0f);
        section3.setAlpha(0.0f);

        //services adjust in imageFragment
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);

        //bottom navigation view onclick function
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    showSection(section1);
                    break;
                case R.id.nav_chat:
                    showSection(section2);
                    break;
                case R.id.nav_profile:
                    showSection(section3);
                    break;
            }
            return true;
        });

        //user profile
        Fname = findViewById(R.id.fname);
        Username = findViewById(R.id.username);
        Province = findViewById(R.id.province);
        City = findViewById(R.id.city);
        Barangay = findViewById(R.id.barangay);
        DOB = findViewById(R.id.dob);
        Gender = findViewById(R.id.gender);
        Email_Address = findViewById(R.id.email);

        Button logOUT = findViewById(R.id.LogOUT);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        //checking if the user is null
        if (firebaseUser == null) {
            Intent intent = new Intent(services.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            showUserProfile(firebaseUser);
        }

        //logout button
        logOUT.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(services.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to show a specific section with a fade-in/fade-out animation
    private void showSection(View section) {
        if (section.equals(section1)) {
            fadeIn(section1);
            fadeOut(section2);
            fadeOut(section3);
        } else if (section.equals(section2)) {
            fadeOut(section1);
            fadeIn(section2);
            fadeOut(section3);
        } else if (section.equals(section3)) {
            fadeOut(section1);
            fadeOut(section2);
            fadeIn(section3);
        }
    }
    // Helper method for fade-in animation
    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(300);
        fadeIn.start();
    }

    // Helper method for fade-out animation
    private void fadeOut(View view) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        fadeOut.start();
    }

    //show User Profile
    private void showUserProfile(FirebaseUser firebaseUser) {
        String firebaseUserUid = firebaseUser.getUid();

        //Extracting User Reference from Database for "UserInfo"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User");
        referenceProfile.child(firebaseUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if (users != null){
                    fName = users.fname;
                    UserName = users.username;
                    pRovince = users.province;
                    cIty = users.city;
                    bArangay = users.barangay;
                    dOb = users.dob;
                    gEnder = users.gender;
                    eMail = firebaseUser.getEmail();

                    //set text
                    Fname.setText(fName);
                    Username.setText(UserName);
                    Province.setText(pRovince);
                    City.setText(cIty);
                    Barangay.setText(bArangay);
                    DOB.setText(dOb);
                    Gender.setText(gEnder);
                    Email_Address.setText(eMail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(services.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onBackPressed() {
        //Exit Dialogue
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(services.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to Exit CamBook?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}