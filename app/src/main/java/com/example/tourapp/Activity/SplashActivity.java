//package com.example.tourapp.Activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.tourapp.databinding.ActivitySplashBinding; // ✅ Make sure this matches your actual binding class name
//
//public class SplashActivity extends AppCompatActivity {
//
//    private ActivitySplashBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//
//        binding = ActivitySplashBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.startBtn.setOnClickListener(v ->
//                startActivity(new Intent(SplashActivity.this, MainActivity.class)));
//    }
//}
