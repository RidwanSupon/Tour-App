package com.example.tourapp.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    ShapeableImageView pic, tourGuidePic;
    TextView titleTxt, descriptionTxt, addressTxt, bedCountTxt, durationTxt, priceTxt, tourGuideNameTxt, scoreTxt;

    Button bookNowBtn, submitBookingBtn, viewTourPartnersBtn;
    LinearLayout bookingFormLayout;
    EditText contactNameInput, contactNumberInput, totalPeopleInput, childInput, bedRoomInput;

    String tripTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views
        pic = findViewById(R.id.pic);
        tourGuidePic = findViewById(R.id.tourGuidePic);
        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        addressTxt = findViewById(R.id.addressTxt);
        bedCountTxt = findViewById(R.id.bedCountTxt);
        durationTxt = findViewById(R.id.durationTxt);
        priceTxt = findViewById(R.id.priceTxt);
        tourGuideNameTxt = findViewById(R.id.tourGuideNameTxt);
        scoreTxt = findViewById(R.id.scoreTxt);

        bookNowBtn = findViewById(R.id.bookNowBtn);
        submitBookingBtn = findViewById(R.id.submitBookingBtn);
        viewTourPartnersBtn = findViewById(R.id.viewTourPartnersBtn);  // নতুন বাটন

        bookingFormLayout = findViewById(R.id.bookingFormLayout);
        contactNameInput = findViewById(R.id.contactNameInput);
        contactNumberInput = findViewById(R.id.contactNumberInput);
        totalPeopleInput = findViewById(R.id.totalPeopleInput);
        childInput = findViewById(R.id.childInput);
        bedRoomInput = findViewById(R.id.bedRoomInput);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            tripTitle = intent.getStringExtra("title");  // ট্রিপের নাম ধরে রাখলাম
            titleTxt.setText(tripTitle);
            descriptionTxt.setText(intent.getStringExtra("description"));
            addressTxt.setText(intent.getStringExtra("address"));
            bedCountTxt.setText("Beds: " + intent.getIntExtra("bedCount", 0));
            durationTxt.setText(intent.getStringExtra("duration"));
            priceTxt.setText("৳" + intent.getIntExtra("price", 0));
            tourGuideNameTxt.setText("Guide: " + intent.getStringExtra("tourGuideName"));
            scoreTxt.setText(String.format("%.1f", intent.getDoubleExtra("score", 0)));

            String picUrl = intent.getStringExtra("pic");
            String guidePicUrl = intent.getStringExtra("tourGuidePic");

            Glide.with(this).load(picUrl).into(pic);
            Glide.with(this).load(guidePicUrl).into(tourGuidePic);
        }

        bookNowBtn.setOnClickListener(v -> {
            bookingFormLayout.setVisibility(View.VISIBLE);
            bookNowBtn.setVisibility(View.GONE);
        });

        submitBookingBtn.setOnClickListener(v -> {
            String name = contactNameInput.getText().toString().trim();
            String number = contactNumberInput.getText().toString().trim();
            String people = totalPeopleInput.getText().toString().trim();
            String child = childInput.getText().toString().trim();
            String bed = bedRoomInput.getText().toString().trim();

            if (name.isEmpty() || number.isEmpty() || people.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save booking to Firebase
                DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

                // Create unique id
                String bookingId = bookingsRef.push().getKey();

                // Get current logged in userId
                String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                        FirebaseAuth.getInstance().getCurrentUser().getUid() : "Unknown";

                // Current timestamp
                long timestamp = System.currentTimeMillis();

                // Create booking model with userId and timestamp
                BookingModel booking = new BookingModel(
                        bookingId,
                        tripTitle,
                        name,
                        number,
                        Integer.parseInt(people),
                        child.isEmpty() ? 0 : Integer.parseInt(child),
                        bed,
                        userId,
                        timestamp
                );

                bookingsRef.child(bookingId).setValue(booking)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Booking Submitted! Thanks, " + name, Toast.LENGTH_LONG).show();
                            bookingFormLayout.setVisibility(View.GONE);
                            bookNowBtn.setVisibility(View.VISIBLE);

                            // Clear inputs
                            contactNameInput.setText("");
                            contactNumberInput.setText("");
                            totalPeopleInput.setText("");
                            childInput.setText("");
                            bedRoomInput.setText("");
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to submit booking.", Toast.LENGTH_SHORT).show());
            }
        });


        // নতুন বাটনের ক্লিক লিসেনার
        viewTourPartnersBtn.setOnClickListener(v -> {
            showTourPartnersDialog(tripTitle);
        });
    }

    private void showTourPartnersDialog(String tripTitle) {
        if (tripTitle == null || tripTitle.isEmpty()) {
            Toast.makeText(this, "Trip title not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        bookingsRef.orderByChild("tripTitle").equalTo(tripTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> names = new ArrayList<>();
                    for (DataSnapshot bookingSnap : snapshot.getChildren()) {
                        String name = bookingSnap.child("contactName").getValue(String.class);
                        if (name != null) {
                            names.add(name);
                        }
                    }
                    if (names.size() > 0) {
                        StringBuilder namesList = new StringBuilder();
                        for (String name : names) {
                            namesList.append(name).append("\n");
                        }
                        showAlert("Tour Partners for " + tripTitle, namesList.toString());
                    } else {
                        showAlert("Tour Partners", "No bookings found for this trip.");
                    }
                } else {
                    showAlert("Tour Partners", "No bookings found for this trip.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showAlert(String title, String message) {
        new AlertDialog.Builder(DetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
