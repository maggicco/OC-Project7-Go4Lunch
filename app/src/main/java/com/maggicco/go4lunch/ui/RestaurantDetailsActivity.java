package com.maggicco.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.ActivityMainBinding;

public class RestaurantDetailsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}