package com.cambook_app;

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

import java.util.regex.Pattern;

public class LogInPage extends AppCompatActivity {

    TextInputEditText Email, Password;
    Button LogIN;
    FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_log_in_page);

        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        LogIN = findViewById(R.id.LogIN);
        progressBar = findViewById(R.id.progressBar);

        arrow1 = findViewById(R.id.arrow1);

        arrow1.setOnClickListener(view -> {
            Intent intent = new Intent(LogInPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        LogIN.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = String.valueOf(Email.getText());
            password = String.valueOf(Password.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LogInPage.this, "Enter email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LogInPage.this, "Enter a valid email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LogInPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                Toast.makeText(LogInPage.this, "Password too weak!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LogInPage.this, "You're logged in!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInPage.this, homepage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogInPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }
    /*@Override
    public void onBackPressed() {

        //Exit Dialogue
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogInPage.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to Exit CamBook?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }**/
}