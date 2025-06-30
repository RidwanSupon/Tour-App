package com.example.tourapp.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tourapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ListView bookingsListView;
    Button logoutBtn;

    ArrayList<String> bookedTripsList;
    ArrayAdapter<String> adapter;

    FirebaseAuth mAuth;
    DatabaseReference bookingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bookingsListView = findViewById(R.id.bookingsListView);
        logoutBtn = findViewById(R.id.logoutBtn);

        bookedTripsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookedTripsList);
        bookingsListView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        bookingsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                bookedTripsList.clear();
                for (DataSnapshot bookingSnap : snapshot.getChildren()) {
                    String tripTitle = bookingSnap.child("tripTitle").getValue(String.class);
                    if (tripTitle != null && !bookedTripsList.contains(tripTitle)) {
                        bookedTripsList.add(tripTitle);
                    }
                }
                adapter.notifyDataSetChanged();

                if (bookedTripsList.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
            }
        });

        bookingsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTripTitle = bookedTripsList.get(position);
            showTripDetailsDialog(selectedTripTitle);
        });

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showTripDetailsDialog(String tripTitle) {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");
        bookingsRef.orderByChild("tripTitle").equalTo(tripTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    showAlert("Info", "No bookings found for this trip.");
                    return;
                }

                ArrayList<String> partners = new ArrayList<>();

                for (DataSnapshot bookingSnap : snapshot.getChildren()) {
                    String name = bookingSnap.child("contactName").getValue(String.class);
                    if (name != null && !partners.contains(name)) {
                        partners.add(name);
                    }
                }

                DatabaseReference tripsRef = FirebaseDatabase.getInstance().getReference("Item");
                tripsRef.orderByChild("title").equalTo(tripTitle).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot tripSnapshot) {
                        String guidePhone = "No guide phone found";
                        if (tripSnapshot.exists()) {
                            for (DataSnapshot tripSnap : tripSnapshot.getChildren()) {
                                guidePhone = tripSnap.child("tourGuidePhone").getValue(String.class);
                                if (guidePhone == null || guidePhone.isEmpty()) {
                                    guidePhone = "No guide phone found";
                                }
                                break;
                            }
                        }

                        StringBuilder message = new StringBuilder();
                        message.append("Tour Guide Phone: ").append(guidePhone).append("\n\n");
                        message.append("Tour Partners:\n");
                        for (String partner : partners) {
                            message.append(partner).append("\n");
                        }
                        showAlert("Trip Details for " + tripTitle, message.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ProfileActivity.this, "Failed to load trip details.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlert(String title, String message) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_trip_details, null);
        TextView messageTextView = dialogView.findViewById(R.id.dialogMessage);
        messageTextView.setText(message);

        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("OK", null)
                .show();
    }
}
