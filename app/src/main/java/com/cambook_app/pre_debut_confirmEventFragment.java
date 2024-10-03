package com.cambook_app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class pre_debut_confirmEventFragment extends Fragment {

    private static final String BOOKINGS_REF = "Bookings";
    private static final String PACKAGE_A = "PackageA";
    private static final String PACKAGE_B = "PackageB";

    private final DatabaseReference offersRef = FirebaseDatabase.getInstance().getReference().child("Offers");
    private static final int REQUEST_IMAGE_SELECTION = 2;
    private String downpaymentImageUrl;

    private View view;

    private String selectedPackage;
    private boolean addOn1Selected = false;
    private boolean addOn2Selected = false;
    private boolean isDateSelected = false;
    private TextView selectedDateTextView;

    // Method to get the current user's ID
    private String getCurrentUserID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }

    public pre_debut_confirmEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pre_debut_confirm_event, container, false);

        ImageView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.bookingTransactionContainer, new chooseEvent())
                .addToBackStack(null)
                .commit());

        // Call method to retrieve and display details for PackageA
        retrieveAndDisplayPackageDetails(PACKAGE_A);

        // Call method to retrieve and display details for PackageB
        retrieveAndDisplayPackageDetails(PACKAGE_B);

        RadioGroup packageARadioGroup = view.findViewById(R.id.packageARadioGroup);
        RadioGroup packageBRadioGroup = view.findViewById(R.id.packageBRadioGroup);

        RadioButton radioButtonPackageA = view.findViewById(R.id.radioButtonPackageA);
        RadioButton radioButtonPackageB = view.findViewById(R.id.radioButtonPackageB);

        // Set OnClickListener for RadioButtonPackageA
        radioButtonPackageA.setOnClickListener(v -> {
            // When RadioButtonPackageA is clicked, set the selected package
            selectedPackage = PACKAGE_A;
            // Uncheck RadioButtonPackageB
            radioButtonPackageB.setChecked(false);
        });

        // Set OnClickListener for RadioButtonPackageB
        radioButtonPackageB.setOnClickListener(v -> {
            // When RadioButtonPackageB is clicked, set the selected package
            selectedPackage = PACKAGE_B;
            // Uncheck RadioButtonPackageA
            radioButtonPackageA.setChecked(false);
        });

        CheckBox checkBoxAddOns1 = view.findViewById(R.id.checkBoxAddOns1);
        CheckBox checkBoxAddOns2 = view.findViewById(R.id.checkBoxAddOns2);

        checkBoxAddOns1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the addon selection status
            addOn1Selected = isChecked;
        });

        checkBoxAddOns2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the addon selection status
            addOn2Selected = isChecked;
        });

        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        RelativeLayout selectBookingDate = view.findViewById(R.id.selectBookingDate);

        selectBookingDate.setOnClickListener(view -> showDatePickerDialog());

        Button uploadDp = view.findViewById(R.id.uploadDpButton);
        uploadDp.setOnClickListener(v -> captureImage());

        Button next = view.findViewById(R.id.packagesNxtButton);
        next.setOnClickListener(v -> {

            if (!isDateSelected) {
                Toast.makeText(getContext(), "Please select a booking date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected package
            if (selectedPackage != null && !selectedPackage.isEmpty()) {
                int packagePrice = 0;

                String selectedDate = selectedDateTextView.getText().toString();

                // Fetch the price based on the selected package
                if (PACKAGE_A.equals(selectedPackage)) {
                    TextView packageAPriceTextView = view.findViewById(R.id.packageAPrice);
                    packagePrice = Integer.parseInt(packageAPriceTextView.getText().toString());
                } else if (PACKAGE_B.equals(selectedPackage)) {
                    TextView packageBPriceTextView = view.findViewById(R.id.packageBPrice);
                    packagePrice = Integer.parseInt(packageBPriceTextView.getText().toString());
                }

                int total = packagePrice;

                // Add price of selected add-ons
                if (addOn1Selected) {
                    TextView addOn1PriceTextView = view.findViewById(R.id.addOnsDetail1Price);
                    int addOn1Price = Integer.parseInt(addOn1PriceTextView.getText().toString());
                    total += addOn1Price;
                }

                if (addOn2Selected) {
                    TextView addOn2PriceTextView = view.findViewById(R.id.addOnsDetail2Price);
                    int addOn2Price = Integer.parseInt(addOn2PriceTextView.getText().toString());
                    total += addOn2Price;
                }

                // Retrieve values for province, city, and barangay from EditText fields
                String province = ((EditText) view.findViewById(R.id.provincePay)).getText().toString();
                String city = ((EditText) view.findViewById(R.id.cityPay)).getText().toString();
                String barangay = ((EditText) view.findViewById(R.id.barangayPay)).getText().toString();

                if (province.isEmpty() || city.isEmpty() || barangay.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields (Province, City, Barangay)", Toast.LENGTH_SHORT).show();
                    return;
                }

                savePackageDetails(selectedPackage, total, selectedDate, province, city, barangay);

                Intent intent = new Intent(getActivity(), services.class);
                startActivity(intent);
            } else {
                // Handle the case where no package is selected
                Toast.makeText(getContext(), "Please select a package", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getContext(), "Successfully Booked", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
    private void retrieveAndDisplayPackageDetails(String packageName) {
        offersRef.child(packageName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve details from the dataSnapshot for the specified package
                    int price = dataSnapshot.child("price").getValue(Integer.class);
                    String hours = dataSnapshot.child("hours").getValue(String.class);
                    String quality = dataSnapshot.child("quality").getValue(String.class);
                    String venue = dataSnapshot.child("venue").getValue(String.class);
                    String storage = dataSnapshot.child("storage").getValue(String.class);
                    String props = dataSnapshot.child("props").getValue(String.class);
                    String output = dataSnapshot.child("output").getValue(String.class);

                    // Update TextViews in the XML layout with the retrieved data for the specified package
                    if (PACKAGE_A.equals(packageName)) {
                        TextView priceTextView = view.findViewById(R.id.packageAPrice);
                        priceTextView.setText(String.valueOf(price));
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView priceTextView = view.findViewById(R.id.packageBPrice);
                        priceTextView.setText(String.valueOf(price));
                    }
                    if (PACKAGE_A.equals(packageName)) {
                        TextView hoursTextView = view.findViewById(R.id.packageADetail1);
                        hoursTextView.setText(hours);
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView hoursTextView = view.findViewById(R.id.packageBDetail1);
                        hoursTextView.setText(hours);
                    }
                    if (PACKAGE_A.equals(packageName)) {
                        TextView qualityTextView = view.findViewById(R.id.packageADetail2);
                        qualityTextView.setText(quality);
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView qualityTextView = view.findViewById(R.id.packageBDetail2);
                        qualityTextView.setText(quality);
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView venueTextView = view.findViewById(R.id.packageBDetail3);
                        venueTextView.setText(venue);
                    }
                    if (PACKAGE_A.equals(packageName)) {
                        TextView storageTextView = view.findViewById(R.id.packageADetail3);
                        storageTextView.setText(storage);
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView storageTextView = view.findViewById(R.id.packageBDetail4);
                        storageTextView.setText(storage);
                    }
                    if (PACKAGE_A.equals(packageName)) {
                        TextView propsTextView = view.findViewById(R.id.packageADetail4);
                        propsTextView.setText(props);
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView propsTextView = view.findViewById(R.id.packageBDetail5);
                        propsTextView.setText(props);
                    }
                    if (PACKAGE_A.equals(packageName)) {
                        TextView outputTextView = view.findViewById(R.id.packageADetail5);
                        outputTextView.setText(output);
                    }
                    if (PACKAGE_B.equals(packageName)) {
                        TextView outputTextView = view.findViewById(R.id.packageBDetail6);
                        outputTextView.setText(output);
                    }

                    // Retrieve and display add-ons
                    retrieveAndDisplayAddOns(packageName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void retrieveAndDisplayAddOns(String packageName) {
        DatabaseReference addOnsRef = FirebaseDatabase.getInstance().getReference().child("AddOns");

        addOnsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot addOnSnapshot : dataSnapshot.getChildren()) {
                    String addOnDetail1 = addOnSnapshot.child("detail1").getValue(String.class);
                    String addOnDetail2 = addOnSnapshot.child("detail2").getValue(String.class);
                    Integer addOnPrice1 = addOnSnapshot.child("AddOnPrice1").getValue(Integer.class);
                    Integer addOnPrice2 = addOnSnapshot.child("AddOnPrice2").getValue(Integer.class);

                    if (addOnDetail1 != null) {
                        TextView addOnsDetail1 = view.findViewById(R.id.addOnsDetail1);
                        addOnsDetail1.setText((addOnDetail1));
                    }

                    if (addOnDetail2 != null) {
                        TextView addOnsDetail1 = view.findViewById(R.id.addOnsDetail2);
                        addOnsDetail1.setText((addOnDetail2));
                    }

                    if (addOnPrice1 != null) {
                        TextView addOnsDetail1Price = view.findViewById(R.id.addOnsDetail1Price);
                        addOnsDetail1Price.setText(String.valueOf(addOnPrice1));
                    }

                    if (addOnPrice2 != null) {
                        TextView addOnsDetail2Price = view.findViewById(R.id.addOnsDetail2Price);
                        addOnsDetail2Price.setText(String.valueOf(addOnPrice2));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (datePicker, year1, month1, day1) -> {
                    // Update the selectedDateTextView with the selected date
                    String selectedDate = day1 + "/" + (month1 + 1) + "/" + year1;
                    selectedDateTextView.setText(selectedDate);

                    // Set the flag to indicate that a date is selected
                    isDateSelected = true;
                }, year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }
    private void captureImage() {
        selectImageFromGallery();
    }
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECTION);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECTION && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Get the selected image
                Uri selectedImageUri = data.getData();
                Bitmap imageBitmap = getBitmapFromUri(selectedImageUri);

                // Upload the image to Firebase Storage
                if (imageBitmap != null) {
                    uploadImageToFirebaseStorage(imageBitmap);
                } else {
                    // Handle the case where imageBitmap is null
                    Toast.makeText(getContext(), "Failed to load the selected image", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where data is null
                Toast.makeText(getContext(), "Intent data is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper method to convert Uri to Bitmap
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void uploadImageToFirebaseStorage(Bitmap imageBitmap) {
        // Get a reference to the Firebase Storage root
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create a unique filename for the image (using current timestamp)
        String filename = "downpayment_image_" + System.currentTimeMillis() + ".jpg";

        // Create a reference to the image file
        StorageReference imageRef = storageRef.child("downpayment_images_pre-debut").child(filename);

        // Convert the Bitmap to a byte array (you might want to compress it)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the byte array to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);

        // Register observers to listen for when the upload is successful or fails
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully, get the download URL
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Update downpaymentImageUrl with the obtained URL
                downpaymentImageUrl = uri.toString();

                // Display a message or update the UI as needed
                Toast.makeText(getContext(), "Image uploaded and saved", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
        });
    }
    // Additional method for storing package details
    private void savePackageDetails(String packageName, int totalPrice, String selectedDate, String province, String city, String barangay) {
        // Get the current user's ID
        String userID = getCurrentUserID();

        if (userID != null) {
            offersRef.child(packageName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve details from the dataSnapshot for the specified package
                        int price = dataSnapshot.child("price").getValue(Integer.class);
                        String hours = dataSnapshot.child("hours").getValue(String.class);
                        String quality = dataSnapshot.child("quality").getValue(String.class);
                        String venue = dataSnapshot.child("venue").getValue(String.class);
                        String storage = dataSnapshot.child("storage").getValue(String.class);
                        String props = dataSnapshot.child("props").getValue(String.class);
                        String output = dataSnapshot.child("output").getValue(String.class);

                        // Create a unique booking ID
                        String bookingID = FirebaseDatabase.getInstance().getReference().child(BOOKINGS_REF).child(userID).push().getKey();

                        Log.d("BookingFragment", "Selected Package: " + selectedPackage);
                        Log.d("BookingFragment", "Booking ID: " + bookingID);

                            DatabaseReference userBookingsRef = FirebaseDatabase.getInstance()
                                    .getReference().child(BOOKINGS_REF).child(userID).child(Objects.requireNonNull(bookingID));

                            Map<String, Object> bookingDetails = new HashMap<>();
                            bookingDetails.put("packageName", packageName);
                            bookingDetails.put("price", price);
                            bookingDetails.put("hours", hours);
                            bookingDetails.put("quality", quality);
                            bookingDetails.put("venue", venue);
                            bookingDetails.put("storage", storage);
                            bookingDetails.put("props", props);
                            bookingDetails.put("output", output);
                            bookingDetails.put("selectedDate", selectedDate);
                            bookingDetails.put("province", province);
                            bookingDetails.put("city", city);
                            bookingDetails.put("barangay", barangay);
                            bookingDetails.put("eventType", "pre_debut");
                            bookingDetails.put("downpayment", 2000);
                            bookingDetails.put("status", "pending");
                            bookingDetails.put("totalPrice", totalPrice);
                            bookingDetails.put("downpaymentImageUrl", downpaymentImageUrl);
                            // Include addon details in the booking
                            fetchAndStoreAddOnDetails(bookingDetails, userBookingsRef);

                            // Include the user ID in the booking details
                            bookingDetails.put("userID", userID);

                            userBookingsRef.setValue(bookingDetails);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }
    }
    private void fetchAndStoreAddOnDetails(Map<String, Object> bookingDetails, DatabaseReference userBookingsRef) {
        DatabaseReference addOnsRef = FirebaseDatabase.getInstance().getReference().child("AddOns");

        addOnsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot addOnSnapshot : dataSnapshot.getChildren()) {
                    String addOnName = addOnSnapshot.getKey(); // Get the add-on name (e.g., "AddOn1", "AddOn2")

                    // Check if the add-on is selected by the user
                    boolean isAddOnSelected = false;
                    if (addOnName.equals("AddOn1")) {
                        isAddOnSelected = addOn1Selected;
                    } else if (addOnName.equals("AddOn2")) {
                        isAddOnSelected = addOn2Selected;
                    }

                    if (isAddOnSelected) {
                        String addOnDetail1 = addOnSnapshot.child("detail1").getValue(String.class);
                        String addOnDetail2 = addOnSnapshot.child("detail2").getValue(String.class);
                        Integer addOnPrice1 = addOnSnapshot.child("AddOnPrice1").getValue(Integer.class);
                        Integer addOnPrice2 = addOnSnapshot.child("AddOnPrice2").getValue(Integer.class);

                        // Create a unique key for each add-on
                        String uniqueKey = "addOn_" + addOnName;

                        // Add addon details to the bookingDetails map with unique keys
                        bookingDetails.put(uniqueKey + "_Name", addOnName);
                        bookingDetails.put(uniqueKey + "_Detail1", addOnDetail1);
                        bookingDetails.put(uniqueKey + "_Detail2", addOnDetail2);
                        bookingDetails.put(uniqueKey + "_Price1", addOnPrice1);
                        bookingDetails.put(uniqueKey + "_Price2", addOnPrice2);
                    }
                }

                // Update the userBookingsRef with the complete booking details
                userBookingsRef.updateChildren(bookingDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}