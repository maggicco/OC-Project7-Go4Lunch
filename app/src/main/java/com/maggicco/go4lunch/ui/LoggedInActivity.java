package com.maggicco.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.ActivityLoggedInBinding;
import com.maggicco.go4lunch.util.NotificationReceiver;

import java.util.Calendar;

public class LoggedInActivity extends AppCompatActivity {

    private static final String TAG = "Delete Account";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    private ActivityLoggedInBinding binding;

    //View view  = binding.topNavigation;
    private TextView textView;
    private Button deleteButton;
    private ToggleButton toggleButton;

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
//                        Intent intent = new Intent( view.getContext(), RestaurantDetailsActivity.class);
//                        startActivity(intent);
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
                    case R.id.action_mapView_fragment:
                        Toast.makeText(LoggedInActivity.this, "Map View", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_listView_fragment:
                        Toast.makeText(LoggedInActivity.this, "List View", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_workmates_fragment:
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Initializing the views of the dialog.
        toggleButton = dialog.findViewById(R.id.dialog_alarm_btn);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = ((ToggleButton) v).isChecked();
                if(checked){

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 21);
                    calendar.set(Calendar.MINUTE, 28);
                    calendar.set(Calendar.SECOND, 00);

                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,
                            intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);



                    Toast.makeText(getApplicationContext(), "Les notification activées", Toast.LENGTH_SHORT).show();
                }else {

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

    //For notification Calender and Alarm menager
//    public void setCalendar(){
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 20);
//        calendar.set(Calendar.MINUTE, 15);
//        calendar.set(Calendar.SECOND, 0);
//        if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.HOUR_OF_DAY, 0);
//        Intent intent = new Intent(this, LoggedInActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        }
//    }

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