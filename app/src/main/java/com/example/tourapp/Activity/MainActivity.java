package com.example.tourapp.Activity;

import com.example.tourapp.Adapter.CategoryAdapter;
import com.example.tourapp.Adapter.PopularAdapter;
import com.example.tourapp.Adapter.RecommendedAdapter;
import com.example.tourapp.Adapter.SliderAdapter;
import com.example.tourapp.Domain.LocationModel;
import com.example.tourapp.Domain.PopularItem;
import com.example.tourapp.Domain.RecommendedItem;
import com.example.tourapp.Domain.SliderItems;
import com.example.tourapp.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.example.tourapp.R;
import android.widget.Toast;

import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseDatabase database;

    private final Handler sliderHandler = new Handler();

    // সার্চের জন্য পুরো Recommended লিস্ট রাখব এখানে
    private ArrayList<RecommendedItem> recommendedListFull = new ArrayList<>();
    private RecommendedAdapter recommendedAdapter;

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.viewPager2.getCurrentItem();
            int itemCount = binding.viewPager2.getAdapter() != null ? binding.viewPager2.getAdapter().getItemCount() : 0;
            if (itemCount == 0) return;

            int nextPosition = (currentPosition + 1) % itemCount;
            binding.viewPager2.setCurrentItem(nextPosition, true);

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

        // Bottom navigation listener যোগ করা হলো
        binding.chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.cart) {  // Profile
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.explorer) {  // Explorer এ ক্লিক করলে TripListActivity চালাও
                    Intent intent = new Intent(MainActivity.this, TripListActivity.class);
                    startActivity(intent);
                }
                // অন্য বাটন গুলোর জন্য কাজ লাগলে এখানে লিখবে
            }
        });

        // এখন TextWatcher দিয়ে auto search করবো
        binding.editTextText4.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                filterRecommendedList(query);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) { }
        });

        // যদি আগের button2.setOnClickListener থাকে, সেটা ডিলিট করো

        initLocations();
        initBanners();
        initCategory();
        initPopular();
        initRecommended();
    }

    private void initLocations() {
        DatabaseReference ref = database.getReference("Location");

        binding.locationSp.setEnabled(false); // ডেটা আসা পর্যন্ত disable রাখবে

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> cities = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        LocationModel location = data.getValue(LocationModel.class);
                        if (location != null && location.getLoc() != null && !location.getLoc().isEmpty()) {
                            cities.add(location.getLoc());
                        }
                    }
                }
                if (!cities.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item, cities);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                    binding.locationSp.setEnabled(true);
                } else {
                    binding.locationSp.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load locations.", Toast.LENGTH_SHORT).show();
                binding.locationSp.setEnabled(true);
            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float scale = 0.85f + (1 - Math.abs(position)) * 0.15f;
            page.setScaleY(scale);
        });

        binding.viewPager2.setPageTransformer(transformer);

        sliderHandler.postDelayed(sliderRunnable, 3000);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private void initBanners() {
        DatabaseReference ref = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        SliderItems item = data.getValue(SliderItems.class);
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

        DatabaseReference ref = database.getReference("Category");
        ArrayList<Category> list = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Category category = data.getValue(Category.class);
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

    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);

        DatabaseReference ref = database.getReference("Popular");
        ArrayList<PopularItem> list = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        PopularItem item = data.getValue(PopularItem.class);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        PopularAdapter adapter = new PopularAdapter(MainActivity.this, list);
                        binding.recyclerViewPopular.setAdapter(adapter);
                    }

                }
                binding.progressBarPopular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(View.GONE);
            }
        });
    }

    private void initRecommended() {
        binding.progressBarRecommended.setVisibility(View.VISIBLE);

        DatabaseReference ref = database.getReference("Item");
        recommendedListFull.clear();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recommendedListFull.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        RecommendedItem item = data.getValue(RecommendedItem.class);
                        if (item != null) {
                            recommendedListFull.add(item);
                        }
                    }
                    if (!recommendedListFull.isEmpty()) {
                        binding.recyclerViewRecommended.setLayoutManager(
                                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        recommendedAdapter = new RecommendedAdapter(MainActivity.this, recommendedListFull);
                        binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
                    }
                }
                binding.progressBarRecommended.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarRecommended.setVisibility(View.GONE);
            }
        });
    }

    // সার্চ ফিল্টার মেথড
    private void filterRecommendedList(String query) {
        boolean isSearching = !query.trim().isEmpty();

        // Banner এর parent ConstraintLayout (viewPager + progressBar)
        View bannerParent = findViewById(R.id.viewPager2).getParent() instanceof View ? (View) findViewById(R.id.viewPager2).getParent() : null;
        if (bannerParent != null) bannerParent.setVisibility(isSearching ? View.GONE : View.VISIBLE);

        // Category এর parent ConstraintLayout
        View categoryParent = findViewById(R.id.recyclerViewCategory).getParent() instanceof View ? (View) findViewById(R.id.recyclerViewCategory).getParent() : null;
        if (categoryParent != null) categoryParent.setVisibility(isSearching ? View.GONE : View.VISIBLE);

        // Popular এর parent ConstraintLayout + seeAllPopular (LinearLayout parent of seeAllPopular এর জন্য আলাদা handle)
        View popularParent = findViewById(R.id.recyclerViewPopular).getParent() instanceof View ? (View) findViewById(R.id.recyclerViewPopular).getParent() : null;
        if (popularParent != null) popularParent.setVisibility(isSearching ? View.GONE : View.VISIBLE);

        // Popular section এর seeAllPopular TextView এর parent LinearLayout কে hide/show করা দরকার
        View popularTitleLayout = findViewById(R.id.seeAllPopular).getParent() instanceof View ? (View) findViewById(R.id.seeAllPopular).getParent() : null;
        if (popularTitleLayout != null) popularTitleLayout.setVisibility(isSearching ? View.GONE : View.VISIBLE);

        // Recommended এর seeAllRecommendedBtn (TextView) + তার parent LinearLayout
        findViewById(R.id.seeAllRecommendedBtn).setVisibility(isSearching ? View.GONE : View.VISIBLE);

        View recommendedTitleLayout = findViewById(R.id.seeAllRecommendedBtn).getParent() instanceof View ? (View) findViewById(R.id.seeAllRecommendedBtn).getParent() : null;
        if (recommendedTitleLayout != null) recommendedTitleLayout.setVisibility(View.VISIBLE); // Recommended title/parent সবসময় show রাখবো

        // Recommended এর parent ConstraintLayout
        View recommendedParent = findViewById(R.id.recyclerViewRecommended).getParent() instanceof View ? (View) findViewById(R.id.recyclerViewRecommended).getParent() : null;
        if (recommendedParent != null) recommendedParent.setVisibility(View.VISIBLE);  // Recommended সবসময় show

        // এবার সার্চ টেক্সট অনুসারে ফিল্টারিং
        if (!isSearching) {
            // Search খালি হলে পুরো লিস্ট দেখাও
            recommendedAdapter.updateList(recommendedListFull);
        } else {
            ArrayList<RecommendedItem> filteredList = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            for (RecommendedItem item : recommendedListFull) {
                if (item.getTitle().toLowerCase().contains(lowerQuery) ||
                        item.getDescription().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(item);
                }
            }
            recommendedAdapter.updateList(filteredList);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
