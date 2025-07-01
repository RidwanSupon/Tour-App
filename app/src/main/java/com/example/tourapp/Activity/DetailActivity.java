package com.example.tourapp.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapp.Domain.BookingModel;
import com.google.firebase.auth.FirebaseAuth;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tourapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ShapeableImageView tripImage, guideImage;
    private TextView titleText, descText, addressText, bedsText, durationText,
            priceText, guideNameText, ratingText, tourDateText;
    private Button startBookingBtn, submitBookingBtn, showPartnersBtn;
    private LinearLayout bookingForm;
    private EditText nameInput, phoneInput, peopleInput, childInput;

    private ImageView starIcon, locationIcon;
    private View dividerLine;

    private String tripTitle = "";
    private int tripPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // Bind views
        tripImage        = findViewById(R.id.pic);
        guideImage       = findViewById(R.id.tourGuidePic);
        titleText        = findViewById(R.id.titleTxt);
        descText         = findViewById(R.id.descriptionTxt);
        addressText      = findViewById(R.id.addressTxt);
        bedsText         = findViewById(R.id.bedCountTxt);
        durationText     = findViewById(R.id.durationTxt);
        priceText        = findViewById(R.id.priceTxt);
        guideNameText    = findViewById(R.id.tourGuideNameTxt);
        ratingText       = findViewById(R.id.scoreTxt);
        tourDateText     = findViewById(R.id.dateTimeTourTxt);

        startBookingBtn  = findViewById(R.id.bookNowBtn);
        submitBookingBtn = findViewById(R.id.submitBookingBtn);
        showPartnersBtn  = findViewById(R.id.viewTourPartnersBtn);

        bookingForm      = findViewById(R.id.bookingFormLayout);
        nameInput        = findViewById(R.id.contactNameInput);
        phoneInput       = findViewById(R.id.contactNumberInput);
        peopleInput      = findViewById(R.id.totalPeopleInput);
        childInput       = findViewById(R.id.childInput);

        starIcon         = findViewById(R.id.starIcon);
        locationIcon     = findViewById(R.id.locationIcon);
        dividerLine      = findViewById(R.id.dividerLine);

        Intent intent = getIntent();
        if (intent != null) {
            tripTitle  = intent.getStringExtra("title");
            tripPrice  = intent.getIntExtra("price", 0);

            titleText.setText(tripTitle);
            descText.setText(intent.getStringExtra("description"));
            addressText.setText(intent.getStringExtra("address"));
            bedsText.setText("Beds: " + intent.getIntExtra("bedCount", 0));
            durationText.setText(intent.getStringExtra("duration"));
            priceText.setText("৳" + tripPrice);
            guideNameText.setText("Guide: " + intent.getStringExtra("tourGuideName"));
            ratingText.setText(String.format(Locale.getDefault(), "%.1f", intent.getDoubleExtra("score", 0)));

            String dateTour = intent.getStringExtra("dateTour");
            String timeTour = intent.getStringExtra("timeTour");

            if ((dateTour != null && !dateTour.isEmpty()) || (timeTour != null && !timeTour.isEmpty())) {
                String showDateTime = "Tour Date & Time: ";
                if (dateTour != null && !dateTour.isEmpty()) showDateTime += dateTour;
                if (timeTour != null && !timeTour.isEmpty()) showDateTime += " at " + timeTour;
                tourDateText.setText(showDateTime);
            } else {
                tourDateText.setText("Tour Date & Time: N/A");
            }

            Glide.with(this).load(intent.getStringExtra("pic")).into(tripImage);
            Glide.with(this).load(intent.getStringExtra("tourGuidePic")).into(guideImage);
        }

        startBookingBtn.setOnClickListener(v -> {
            tripImage.setVisibility(View.GONE);
            guideImage.setVisibility(View.GONE);
            titleText.setVisibility(View.GONE);
            descText.setVisibility(View.GONE);
            addressText.setVisibility(View.GONE);
            bedsText.setVisibility(View.GONE);
            durationText.setVisibility(View.GONE);
            priceText.setVisibility(View.GONE);
            guideNameText.setVisibility(View.GONE);
            ratingText.setVisibility(View.GONE);
            tourDateText.setVisibility(View.GONE);
            startBookingBtn.setVisibility(View.GONE);
            showPartnersBtn.setVisibility(View.GONE);

            starIcon.setVisibility(View.GONE);
            locationIcon.setVisibility(View.GONE);
            dividerLine.setVisibility(View.GONE);

            bookingForm.setVisibility(View.VISIBLE);
        });

        submitBookingBtn.setOnClickListener(v -> {
            String name   = nameInput.getText().toString().trim();
            String phone  = phoneInput.getText().toString().trim();
            String people = peopleInput.getText().toString().trim();
            String child  = childInput.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || people.isEmpty()) {
                Toast.makeText(this,"Please fill required fields",Toast.LENGTH_SHORT).show();
                return;
            }

            int adults = Integer.parseInt(people);
            int kids   = child.isEmpty() ? 0 : Integer.parseInt(child);

            int adultTotal = tripPrice * adults;
            int childTotal = (int)(tripPrice * 0.5 * kids);
            int totalAmt   = adultTotal + childTotal;

            View payView     = getLayoutInflater().inflate(R.layout.dialog_payment,null);
            EditText etCard  = payView.findViewById(R.id.etCardNumber);
            EditText etPin   = payView.findViewById(R.id.etPin);
            TextView tvAmt   = payView.findViewById(R.id.tvAmount);
            tvAmt.setText("Amount: ৳" + totalAmt);

            AlertDialog payDlg = new AlertDialog.Builder(this)
                    .setView(payView)
                    .setCancelable(true)
                    .create();

            payView.findViewById(R.id.btnPayNow).setOnClickListener(pd -> {
                String card = etCard.getText().toString().trim();
                String pin  = etPin.getText().toString().trim();
                if (card.length() < 12 || pin.length() < 4) {
                    Toast.makeText(this,"Invalid card number or PIN",Toast.LENGTH_SHORT).show();
                    return;
                }
                payDlg.dismiss();
                saveBookingToFirebase(name, phone, adults, kids, totalAmt);
            });

            payDlg.show();
        });

        showPartnersBtn.setOnClickListener(v -> showTourPartnersDialog(tripTitle));
    }

    private void saveBookingToFirebase(String name, String phone, int adults, int children, int totalAmount) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookings");
        String bookingId = ref.push().getKey();
        String userId    = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "Unknown";
        long timestamp   = System.currentTimeMillis();

        BookingModel booking = new BookingModel(
                bookingId,
                tripTitle,
                name,
                phone,
                adults,
                children,
                userId,
                timestamp
        );

        ref.child(bookingId).setValue(booking)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this,
                            "Booking confirmed! Thank you, " + name,
                            Toast.LENGTH_LONG).show();

                    bookingForm.setVisibility(View.GONE);
                    startBookingBtn.setVisibility(View.VISIBLE);
                    nameInput.setText("");
                    phoneInput.setText("");
                    peopleInput.setText("");
                    childInput.setText("");

                    // Redirect to Home (MainActivity) after confirmation
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to submit booking.",
                                Toast.LENGTH_SHORT).show()
                );
    }

    private void showTourPartnersDialog(String title) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookings");
        ref.orderByChild("tripTitle").equalTo(title)
                .addListenerForSingleValueEvent(
                        new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot snap) {
                                ArrayList<String> names = new ArrayList<>();
                                for (com.google.firebase.database.DataSnapshot c : snap.getChildren()) {
                                    String n = c.child("contactName").getValue(String.class);
                                    if (n != null) names.add(n);
                                }
                                if (names.isEmpty()) {
                                    showAlert("Tour Partners",
                                            "No bookings found for \"" + title + "\"");
                                } else {
                                    StringBuilder sb = new StringBuilder();
                                    for (String n : names) sb.append("• ").append(n).append("\n");
                                    showAlert("Tour Partners for \"" + title + "\"", sb.toString());
                                }
                            }

                            @Override
                            public void onCancelled(com.google.firebase.database.DatabaseError err) {
                                Toast.makeText(DetailActivity.this,
                                        "Failed to load tour partners.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
