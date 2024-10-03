package com.cambook_app;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class editUserProfileFragment extends Fragment {

    private ImageButton uploadProfilePic;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    public editUserProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        ImageButton back = view.findViewById(R.id.arrow1);
        back.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new userDetailsFragment())
                .addToBackStack(null)
                .commit());

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        uploadProfilePic = view.findViewById(R.id.edit_profile_pic);
        Button saveProf = view.findViewById(R.id.save_profile);

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePics");

        Uri uri = firebaseUser.getPhotoUrl();
        Picasso.get().load(uri).into(uploadProfilePic);

        // Choosing Picture to Upload
        uploadProfilePic.setOnClickListener(v -> openFileChooser());

        // Uploading Picture and updating user information
        saveProf.setOnClickListener(v -> uploadProfileAndUserInfo());

        return view;
    }

    // Function to open the image chooser
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            uploadProfilePic.setImageURI(uriImage);
        }
    }

    // Function to upload the image to Firebase Storage and update user information
    private void uploadProfileAndUserInfo() {
        if (uriImage != null) {
            // Save Image with Uid of the current user
            StorageReference fileReference = storageReference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid() + "." + getFileExtension(uriImage));

            // Upload Picture to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                firebaseUser = auth.getCurrentUser();

                // Update user profile image
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                firebaseUser.updateProfile(profileUpdates);

                // Update user information in the database
                updateUserInfoInDatabase(uri.toString());
            })).addOnFailureListener(e -> {
                // Handle failure
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
            });
        } else {
            // If no image is selected, just update user information
            updateUserInfoInDatabase(null);
        }
    }

    private void updateUserInfoInDatabase(String profileImageUrl) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User/UserID").child(firebaseUser.getUid());

        // Get other information to be updated from your UI elements
        String editedName = Objects.requireNonNull(((TextInputEditText) requireView().findViewById(R.id.editFname)).getText()).toString().trim();
        String editedUsername = Objects.requireNonNull(((TextInputEditText) requireView().findViewById(R.id.editUsername)).getText()).toString().trim();
        String editedProvince = Objects.requireNonNull(((TextInputEditText) requireView().findViewById(R.id.editProvince)).getText()).toString().trim();
        String editedCity = Objects.requireNonNull(((TextInputEditText) requireView().findViewById(R.id.editCity)).getText()).toString().trim();
        String editedBarangay = Objects.requireNonNull(((TextInputEditText) requireView().findViewById(R.id.editBarangay)).getText()).toString().trim();

        // Get selected gender
        RadioGroup editGenderGroup = requireView().findViewById(R.id.edit_gender);
        int selectedGenderId = editGenderGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = requireView().findViewById(selectedGenderId);
        String editedGender = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";
        // Add more fields as needed

        // Update user information in the database
        if (!TextUtils.isEmpty(editedName)) {
            userRef.child("fname").setValue(editedName); // Save Full Name
        }
        if (!TextUtils.isEmpty(editedUsername)) {
            userRef.child("username").setValue(editedUsername); // Save Username
        }
        if (!TextUtils.isEmpty(editedProvince)) {
            userRef.child("province").setValue(editedProvince); // Save Province
        }
        if (!TextUtils.isEmpty(editedCity)) {
            userRef.child("city").setValue(editedCity); // Save City
        }
        if (!TextUtils.isEmpty(editedBarangay)) {
            userRef.child("barangay").setValue(editedBarangay); // Save Barangay
        }
        if (!TextUtils.isEmpty(editedGender)) {
            userRef.child("gender").setValue(editedGender); // Save gender
        }

        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Navigate back to the userProfile fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new userProfile())
                .addToBackStack(null)
                .commit();
    }

    // Obtain File Extension
    private String getFileExtension(Uri uri) {
        ContentResolver cR = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
