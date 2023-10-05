package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_profile extends AppCompatActivity{

    TextView Fname, Username, Province, City, Barangay, DOB, Gender, Email_Address;
    private String UserName, fName, eMail, pRovince, cIty, bArangay, dOb, gEnder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

        if (firebaseUser == null) {
            Intent intent = new Intent(user_profile.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            showUserProfile(firebaseUser);
        }

        logOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(user_profile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

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
                Toast.makeText(user_profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
