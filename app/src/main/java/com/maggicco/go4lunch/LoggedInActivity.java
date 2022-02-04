package com.maggicco.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maggicco.go4lunch.databinding.ActivityLoggedInBinding;

public class LoggedInActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private ActivityLoggedInBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setBarComponents();


        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

//        binding.googleLogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.signOut();
//                checkUser();
//            }
//        });
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
            //binding.userNameTv.setText(userName);

            String userEmail = firebaseUser.getEmail();
            //binding.userEmailTv.setText(userEmail);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    //set drawer action bar
    public void setBarComponents(){

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_logged_in);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.yourLunchItem:
                        Toast.makeText(LoggedInActivity.this, "Your \n Lunch",Toast.LENGTH_SHORT).show();break;
                    case R.id.settingsItem:
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







}