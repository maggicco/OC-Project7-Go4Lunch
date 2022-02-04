package com.maggicco.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.maggicco.go4lunch.databinding.ActivityMainBinding;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}