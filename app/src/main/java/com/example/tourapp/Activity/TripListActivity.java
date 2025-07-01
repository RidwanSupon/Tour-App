package com.example.tourapp.Activity;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapp.Adapter.TripAdapter;
import com.example.tourapp.Domain.TripItem;
import com.example.tourapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<TripItem> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list); // ✅ ঠিক layout ফাইল

        recyclerView = findViewById(R.id.recyclerViewTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripList = new ArrayList<>();
        adapter = new TripAdapter(this, tripList);
        recyclerView.setAdapter(adapter);

        loadTripsFromFirebase();
    }

    private void loadTripsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Item");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tripList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    TripItem item = snap.getValue(TripItem.class);
                    if (item != null) {
                        tripList.add(item);
                    }
                }
                adapter.notifyDataSetChanged(); // UI refresh
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }
}
