package com.cambook_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterPage extends AppCompatActivity {
    TextInputEditText Fname;
    TextInputEditText Username;
    TextInputEditText Province;
    TextInputEditText City;
    TextInputEditText Street;
    TextInputEditText Email;
    TextInputEditText Password;
    Button DOB, Register;
    RadioGroup Gender;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference UsersInfo;
    ImageButton arrow1;

    String fname, username, province, city, street, dob, gender, email, password;
    private boolean dobButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        Fname = findViewById(R.id.fname);
        Username = findViewById(R.id.username);
        Province = findViewById(R.id.province);
        City = findViewById(R.id.city);
        Street = findViewById(R.id.street);
        DOB = findViewById(R.id.DoB);
        Gender = findViewById(R.id.gender);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Register = findViewById(R.id.Register);
        progressBar = findViewById(R.id.progressBar);

        arrow1 = findViewById(R.id.arrow1);

        UsersInfo = FirebaseDatabase.getInstance().getReference().child("User");

        arrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // on below line we are adding click listener for our pick date button
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to true when the button is clicked.
                dobButtonClicked = true;
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        RegisterPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                DOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = Objects.requireNonNull(Fname.getText()).toString();
                username = Objects.requireNonNull(Username.getText()).toString();
                province = Objects.requireNonNull(Province.getText().toString());
                city = Objects.requireNonNull(City.getText().toString());
                street = Objects.requireNonNull(Street.getText().toString());
                dob = String.valueOf(DOB.getText());
                int selectedId = Gender.getCheckedRadioButtonId();
                gender = "";
                email = Objects.requireNonNull(Email.getText()).toString();
                password = Objects.requireNonNull(Password.getText()).toString();

                if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(RegisterPage.this, "Enter your First name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterPage.this, "Enter your Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(province)) {
                    Toast.makeText(RegisterPage.this, "Enter your Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(RegisterPage.this, "Enter your Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(street)) {
                    Toast.makeText(RegisterPage.this, "Enter your Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!dobButtonClicked) {
                    // The user didn't click the "DOB" button.
                    // Show an error message or take appropriate action.
                    Toast.makeText(RegisterPage.this, "Choose your Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedId != -1) {
                    RadioButton selectedGender = findViewById(selectedId);
                    gender = selectedGender.getText().toString();
                } else {
                    // Gender is not selected, show an error message
                    Toast.makeText(RegisterPage.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterPage.this, "Enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterPage.this, "Enter a valid email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Get the UID of the user
                                    String uid = user.getUid();
                                    Toast.makeText(RegisterPage.this, "Successfully Created an Account",
                                            Toast.LENGTH_SHORT).show();
                                    Intent verificationIntent = new Intent(RegisterPage.this, policy_agreement.class);
                                    startActivity(verificationIntent);
                                    finish(); // Close the current activity
                                    Users users = new Users(fname, username, province, city, street, dob, gender, email, password);
                                    UsersInfo.child(uid).setValue(users);
                                } else {
                                    Toast.makeText(RegisterPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    /*@Override
    public void onBackPressed() {

        //Exit Dialogue
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterPage.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to Exit Cambook?");
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
