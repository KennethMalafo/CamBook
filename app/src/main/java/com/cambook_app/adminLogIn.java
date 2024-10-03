package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class adminLogIn extends AppCompatActivity {
    TextInputEditText Email, Password;
    Button LogINasAdmin;
    FirebaseAuth adminAuth;
    ProgressBar progressBar;
    ImageButton arrow1;
    private static final Pattern PASSWORD_PATTERN =

            Pattern.compile("^" +

                    //"(?=S+$)" +                     // no white spaces
                    ".{6,}" +                              // at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        adminAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        LogINasAdmin = findViewById(R.id.LogInAsAdmin);
        progressBar = findViewById(R.id.progressBar);

        arrow1 = findViewById(R.id.arrow1);

        arrow1.setOnClickListener(view -> {
            Intent intent = new Intent(adminLogIn.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        LogINasAdmin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(Email.getText());
            password = String.valueOf(Password.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(adminLogIn.this, "Enter email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(adminLogIn.this, "Enter a valid email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(adminLogIn.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                Toast.makeText(adminLogIn.this, "Password too weak!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.contains(" ")) {
                Toast.makeText(adminLogIn.this, "Your password mustn't have a space!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.contains(".")) {
                Toast.makeText(adminLogIn.this, "Your password mustn't have a period!", Toast.LENGTH_SHORT).show();
                return;
            }


            adminAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = adminAuth.getCurrentUser();

                            // Check if the logged-in user is an admin
                            checkAdminStatus(Objects.requireNonNull(user).getUid(), isAdmin -> {
                                if (isAdmin) {
                                    Toast.makeText(adminLogIn.this, "You're logged in as admin!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(adminLogIn.this, adminHome.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If the logged-in user is not an admin, show an error message
                                    Toast.makeText(adminLogIn.this, "You do not have admin privileges.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(adminLogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


        });
    }
    private void checkAdminStatus(String uid, AdminCheckCallback callback) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");
        adminRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAdmin = snapshot.exists();
                callback.onResult(isAdmin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(false);
            }
        });
    }
}
