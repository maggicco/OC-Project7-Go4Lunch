package com.maggicco.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maggicco.go4lunch.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmailRegisterActivity extends AppCompatActivity {

    private String TAG = EmailRegisterActivity.class.getSimpleName();
    private TextInputEditText userEmail, userPassword, confirmUserPassword, userName;
    private String userImageUrl;
    private ProgressBar loadingProgress;
    private Button registerBtn, signInBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName = findViewById(R.id.registerName);
        userEmail = findViewById(R.id.registerMail);
        userPassword = findViewById(R.id.registerPassword);
        confirmUserPassword = findViewById(R.id.registerPasswordConfirm);

        loadingProgress = findViewById(R.id.registerProgressBar);
        registerBtn = findViewById(R.id.registerBtn);

        loadingProgress.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = confirmUserPassword.getText().toString();
                final String name = userName.getText().toString();


                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(password2)) {

                    showMessage("Please Verify all fields");
                    registerBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                } else {

                    CreateUserAccount(email, name, password);
                }
            }
        });

    }
    private void CreateUserAccount(String email, String name, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "onSuccess: Logged in");
                            //get logged in user
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            //get user info
                            userId = firebaseUser.getUid();
                            String username = firebaseUser.getDisplayName();
                            String useremail = firebaseUser.getEmail();
                            Log.d(TAG, "Username: "+userName+" Userid: "+userEmail);

                            Log.d(TAG, "onSuccess: userId: " + userId + " - " + userName);
                            Log.d(TAG, "onSuccess: userEmail: " + userEmail);

                            updateUserInfo(name, firebaseAuth.getCurrentUser());

                            //check if new or existing user
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                //new user Account created
                                Log.d(TAG, "onSuccess Account created ...\n" + userEmail);
                                Toast.makeText(EmailRegisterActivity.this, "Account Created...\n" + userEmail, Toast.LENGTH_SHORT).show();

                                DocumentReference documentReference = firebaseFirestore.collection("users").document();
                                Map<String,Object> user = new HashMap<>();
                                user.put("userName", username);
                                user.put("userEmail", useremail);
                                user.put("userImageUrl", "");
                                user.put("userLikeId", Arrays.asList());
                                user.put("userChosenRestaurantId", "");
                                user.put("userChosenRestaurantName", "");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: User profile crated " + userId + "-" + userName);
                                    }
                                });

                            }else {
                                Log.d(TAG, "onSuccess: Existing user ...\n" + userEmail);
                                Toast.makeText(EmailRegisterActivity.this, "Existing User...\n" + userEmail, Toast.LENGTH_SHORT).show();
                            }

                            startActivity(new Intent(EmailRegisterActivity.this, LoggedInActivity.class));
                            finish();

                        } else {

                            // account creation failed
                            showMessage("account creation failed" + task.getException().getMessage());
                            registerBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });


    }


    private void updateUserInfo(String name, FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url
        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentUser.updateProfile(profleUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            // user info updated successfully
                            showMessage("Register Complete");
                            updateUI();
                        }

                    }
                });
    }

    private void updateUI() {
        Intent loggedInActivity = new Intent(getApplicationContext(), LoggedInActivity.class);
        startActivity(loggedInActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


}