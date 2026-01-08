package com.example.wiseroute12;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge and set layout
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configure RangeSlider for age (two thumbs)
        RangeSlider sliderAge = findViewById(R.id.sliderAge);
        if (sliderAge != null) {
            sliderAge.setValueFrom(0f);
            sliderAge.setValueTo(100f);
            sliderAge.setStepSize(1f);
            // default: full range selected, adjust as needed
            sliderAge.setValues(Arrays.asList(18f, 65f));
        }

        // Configure single Slider for time (hours)
        Slider sliderTime = findViewById(R.id.sliderTime);
        if (sliderTime != null) {
            sliderTime.setValueFrom(0f);
            sliderTime.setValueTo(24f);
            sliderTime.setStepSize(1f);
            sliderTime.setValue(4f); // default 4 hours
        }

        // Configure single Slider for distance (km)
        Slider sliderDistance = findViewById(R.id.sliderDistance);
        if (sliderDistance != null) {
            sliderDistance.setValueFrom(0f);
            sliderDistance.setValueTo(50f);
            sliderDistance.setStepSize(0.5f);
            sliderDistance.setValue(5f); // default 5 km
        }

        // Apply system bars padding using WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}