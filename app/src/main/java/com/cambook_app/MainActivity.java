package com.cambook_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already authenticated on app start
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserTypeAndRedirect(currentUser.getUid());
        }

        Button button = findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcc();
            }

            private void createAcc() {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.logIN);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAcc();
            }

            private void loginAcc() {
                Intent intent = new Intent(MainActivity.this, LogInPage.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.logInAsAdmin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin();
            }

            private void loginAdmin() {
                Intent intent = new Intent(MainActivity.this, adminLogIn.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.createAccAsAdmin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAdmin();
            }

            private void createAdmin() {
                Intent intent = new Intent(MainActivity.this, adminCreate.class);
                startActivity(intent);
            }
        });
    }

    // ...

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            checkUserTypeAndRedirect(currentUser.getUid());
        } else {
            Log.d("MainActivity", "onStart: Current user is null");
        }
    }

    private void checkUserTypeAndRedirect(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    // User is a regular user, redirect to starting page
                    Intent userIntent = new Intent(MainActivity.this, startingPage.class);
                    startActivity(userIntent);
                    finish();
                } else {
                    // Check if the user is an admin
                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot adminSnapshot) {
                            if (adminSnapshot.exists()) {
                                // User is an admin, redirect to admin section
                                Intent adminIntent = new Intent(MainActivity.this, adminHome.class);
                                startActivity(adminIntent);
                                finish();
                            } else {
                                Log.d("MainActivity", "checkUserTypeAndRedirect: Admin snapshot does not exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("MainActivity", "checkUserTypeAndRedirect: Admin database error", databaseError.toException());
                            // Handle the error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "checkUserTypeAndRedirect: User database error", databaseError.toException());
                // Handle the error
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to Exit CamBook?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}