package com.maggicco.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.ActivityLoggedInBinding;
import com.maggicco.go4lunch.util.NotificationReceiver;
import com.maggicco.go4lunch.ui.RestaurantDetailsActivity;
import java.util.Calendar;

public class LoggedInActivity extends AppCompatActivity {

    private static final String TAG = "Delete Account";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;


    private ActivityLoggedInBinding binding;

    //View view  = binding.topNavigation;
    private TextView textView;
    private Button deleteButton;
    private ToggleButton toggleButton;

    private static String TOGGLE_PREFS = "toogle_prefs";
    private static String SWITCH_STATUS = "switch_status";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean switch_status;

    private View view;
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
            // get photo in Firebase
//            ImageView profileImage = findViewById(R.id.profileImage);
//            if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
//                Glide.with(this)
//                        .load(firebaseUser.getPhotoUrl())
//                        .apply(RequestOptions.circleCropTransform())
//                        .into(profileImage);
//            } else {
//
//                profileImage.setImageResource(R.mipmap.ic_launcher_round);
//            }

            // get email and name
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
                        Intent intent = new Intent( LoggedInActivity.this, RestaurantDetailsActivity.class);
                        startActivity(intent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    //Set bottom navigation
    public void setBottomNavigation(){

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        MapsViewFragment mapsViewFragment = new MapsViewFragment();
        ListViewFragment listViewFragment = new ListViewFragment();
        WorkMatesFragment workMatesFragment = new WorkMatesFragment();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem itemBottom) {
                switch (itemBottom.getItemId()) {
                    case R.id.action_mapView_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mapsViewFragment).commit();
                        return true;
                    case R.id.action_listView_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, listViewFragment).commit();
                        return true;
                    case R.id.action_workmates_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, workMatesFragment).commit();
                        return true;
                }
                return false;
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        toggleButton = dialog.findViewById(R.id.dialog_alarm_btn);
        //Shared Preferences
        sharedPreferences = getSharedPreferences(TOGGLE_PREFS, MODE_PRIVATE);
        editor = getSharedPreferences(TOGGLE_PREFS, MODE_PRIVATE).edit();

        switch_status = sharedPreferences.getBoolean(SWITCH_STATUS, false);
        toggleButton.setChecked(switch_status);

        //Initializing the views of the dialog.
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){

                    editor.putBoolean(SWITCH_STATUS, true);
                    editor.apply();
                    toggleButton.setChecked(true);


                    Toast.makeText(getApplicationContext(), "Les notification activées", Toast.LENGTH_SHORT).show();
                }else{

                    editor.putBoolean(SWITCH_STATUS, false);
                    editor.apply();
                    toggleButton.setChecked(false);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 11);
                    calendar.set(Calendar.MINUTE, 23);
                    calendar.set(Calendar.SECOND, 00);

                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,
                            intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

                    Toast.makeText(getApplicationContext(), "Les notification désactivées", Toast.LENGTH_SHORT).show();
                }

            }
        });

        deleteButton = dialog.findViewById(R.id.dialog_delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteUser();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(LoggedInActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        dialog.show();

    }



    // Delete user from FireBase
    public void deleteUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }

}