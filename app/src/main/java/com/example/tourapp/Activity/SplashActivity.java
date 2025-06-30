package com.example.tourapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // Already logged in → Go to MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Not logged in → Go to LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish(); // Close SplashActivity
    }
}
