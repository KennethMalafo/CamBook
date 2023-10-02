package com.cambook_app;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity{

    TextView Acc, Username;
    private String Email, UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button logOUT = findViewById(R.id.LogOUT);
        Acc = findViewById(R.id.Acc);
        Username = findViewById(R.id.username);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            Intent intent = new Intent(home.this, MainActivity.class);
            startActivity(intent);
        } else {
            showUserProfile(firebaseUser);
        }

        logOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
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
                    Email = firebaseUser.getEmail();
                    UserName = users.username;

                    //set text
                    Acc.setText(Email);
                    Username.setText(UserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
