package com.maggicco.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.ActivityLoggedInBinding;

public class LoggedInActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private ActivityLoggedInBinding binding;

    //View view  = binding.topNavigation;
    private TextView textView;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        setTopNavigation();
        setBottomNavigation();
        checkUser();

    }

    private void checkUser(){
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            //user not logged in
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            String userName = firebaseUser.getDisplayName();
            navigationView = binding.topNavigation;
            View header = navigationView.getHeaderView(0);
            TextView userNameTv = header.findViewById(R.id.userNameTv);
            userNameTv.setText(userName);

            String userEmail = firebaseUser.getEmail();
            TextView userEmailTv = header.findViewById(R.id.userEmailTv);
            userEmailTv.setText(userEmail);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    //Set top navigation
    public void setTopNavigation(){

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_logged_in);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.top_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.yourLunchItem:
                        //Intent intent = new Intent( view.getContext(), RestaurantDetailsActivity.class);
                        //startActivity(intent);
                        Toast.makeText(LoggedInActivity.this, "Your \n Lunch",Toast.LENGTH_SHORT).show();break;
                    case R.id.settingsItem:
                        showCustomDialog();
                        Toast.makeText(LoggedInActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    case R.id.logoutItem:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    default:
                        return true;
                }

                return true;

            }
        });

    }

    //Set bottom navigation
    public void setBottomNavigation(){

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem itemBottom) {
                switch (itemBottom.getItemId()) {
                    case R.id.action_mapView:
                        Toast.makeText(LoggedInActivity.this, "Map View", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_listView:
                        Toast.makeText(LoggedInActivity.this, "List View", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_workmates:
                        Toast.makeText(LoggedInActivity.this, "Workmates", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

    }


    //Function to display the custom dialog.
    void showCustomDialog() {
        final Dialog dialog = new Dialog(LoggedInActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.settings_dialog);

        //Initializing the views of the dialog.


        dialog.show();
    }



}