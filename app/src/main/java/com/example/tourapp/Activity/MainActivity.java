package com.example.tourapp.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tourapp.Adapter.CategoryAdapter;
import com.example.tourapp.Adapter.SliderAdapter;
import com.example.tourapp.Domain.Location;
import com.example.tourapp.Domain.SliderItems;
import com.example.tourapp.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseDatabase database;

    // Handler and Runnable for automatic banner sliding
    private final Handler sliderHandler = new Handler();

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.viewPager2.getCurrentItem();
            int itemCount = binding.viewPager2.getAdapter() != null ? binding.viewPager2.getAdapter().getItemCount() : 0;
            if (itemCount == 0) return;

            // Move to next position, loop to first if at end
            int nextPosition = (currentPosition + 1) % itemCount;
            binding.viewPager2.setCurrentItem(nextPosition, true);

            // Schedule next slide after 3 seconds
            sliderHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        initLocations();
        initBanners();
        initCategory();
    }

    private void initLocations() {
        // Static city list for spinner
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Dhaka");
        cities.add("CoxsBazar");
        cities.add("Barishal");
        cities.add("Cumilla");
        cities.add("Chattogram");
        cities.add("Khulna");
        cities.add("Rajshahi");
        cities.add("Sylhet");
        cities.add("Rangpur");
        cities.add("Mymensingh");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.locationSp.setAdapter(adapter);

        // Firebase location data fetching code is commented out
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));

        // Setup ViewPager2 properties for better UX
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        // CompositePageTransformer to combine multiple effects
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();

        // Add margin between pages
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        // Add scaling effect for smooth transition
        compositePageTransformer.addTransformer((page, position) -> {
            float scale = 0.85f + (1 - Math.abs(position)) * 0.15f;
            page.setScaleY(scale);
        });

        binding.viewPager2.setPageTransformer(compositePageTransformer);

        // Start automatic banner sliding every 3 seconds
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // Reset the timer when user manually swipes to prevent conflicts
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private void initBanners() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        SliderItems item = issue.getValue(SliderItems.class);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                    banners(items);
                }
                binding.progressBarBanner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarBanner.setVisibility(View.GONE);
            }
        });
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        DatabaseReference myref = database.getReference("Category");
        ArrayList<Category> list = new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Category category = dataSnapshot.getValue(Category.class);
                        if (category != null) {
                            list.add(category);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewCategory.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        CategoryAdapter adapter = new CategoryAdapter(list);
                        binding.recyclerViewCategory.setAdapter(adapter);
                    }
                }
                binding.progressBarCategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarCategory.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to avoid memory leaks
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
