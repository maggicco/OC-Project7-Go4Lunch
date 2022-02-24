package com.maggicco.go4lunch.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.ActivityMainBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailsActivity extends AppCompatActivity {

    @BindView(R.id.resto_name)
    TextView restoName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        String titleDet = intent.getStringExtra("RESTAURANT_NAME");
        restoName.setText(titleDet);
    }
}