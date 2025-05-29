package com.example.tourapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tourapp.R;

public class OnboardingActivity extends AppCompatActivity {

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            // After onboarding, go to main activity
            startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
            finish();
        });
    }
}
