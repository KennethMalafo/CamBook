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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class adminCreate extends AppCompatActivity {

    TextInputEditText Email, Password;
    Button createAccAsAdmin;
    FirebaseAuth adminAuth;
    ProgressBar progressBar;
    ImageButton arrow1;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    ".{6,}" +  // at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create);

        adminAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        createAccAsAdmin = findViewById(R.id.createAccAsAdmin);
        progressBar = findViewById(R.id.progressBar);

        arrow1 = findViewById(R.id.arrow1);

        arrow1.setOnClickListener(view -> {
            Intent intent = new Intent(adminCreate.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        createAccAsAdmin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(Email.getText());
            password = String.valueOf(Password.getText());

            if (TextUtils.isEmpty(email)) {
                showToast("Enter email!");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Enter a valid email!");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                showToast("Enter Password");
                return;
            }
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                showToast("Password too weak!");
                return;
            }
            if (password.contains(" ")) {
                showToast("Your password mustn't have a space!");
                return;
            }
            if (password.contains(".")) {
                showToast("Your password mustn't have a period!");
                return;
            }

            // Create admin account using FirebaseAuth
            adminAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Admin account created successfully
                            FirebaseUser user = adminAuth.getCurrentUser();

                            // Store additional information in the database
                            if (user != null) {
                                storeAdminInfo(user.getUid(), email, password);

                                // Check if an admin already exists
                                checkIfAdminExists(Objects.requireNonNull(user).getUid(), isAdminExists -> {
                                    if (isAdminExists) {
                                        // An admin already exists, consider handling this case
                                        showToast("Admin account already exists.");
                                    } else {
                                        // Set custom property indicating admin status
                                        setAdminStatus(user.getUid());
                                        showToast("Admin account created!");

                                        // Proceed to admin home
                                        Intent intent = new Intent(adminCreate.this, adminHome.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                // If account creation fails, display a message to the user
                                showToast("Admin account creation failed.");
                            }
                        }
                    });
        });
    }

    private void storeAdminInfo(String uid, String email, String password) {
        DatabaseReference adminInfoRef = FirebaseDatabase.getInstance().getReference("Admin");
        Map<String, Object> adminInfo = new HashMap<>();
        adminInfo.put("email", email);
        adminInfo.put("password", password);
        adminInfoRef.child(uid).setValue(adminInfo);
    }

    private void checkIfAdminExists(String uid, AdminCheckCallback callback) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");
        adminRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAdminExists = snapshot.exists();
                callback.onResult(isAdminExists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error, for example, by calling callback.onResult(false);
                callback.onResult(false);
            }
        });
    }


    private void setAdminStatus(String uid) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");
        adminRef.child(uid).setValue(true);
    }

    private void showToast(String message) {
        Toast.makeText(adminCreate.this, message, Toast.LENGTH_SHORT).show();
    }
}
