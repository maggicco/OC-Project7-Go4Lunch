package com.maggicco.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "GoogleActivity";
    private static final String TAG_FB = "FaceBookActivity";
    private static final int RC_SIGN_IN = 9001;
    private Button emailSignInBtn;
    private Button registerBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CallbackManager callbackManager;
    private String userId;
    private String userImageUrl;
    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        checkUser();

//        SignInButton signInButton = findViewById(R.id.googleLoginBtn);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
        binding.googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });


        //FBOOK
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_buttonFB);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this,
                        Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG_FB, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG_FB, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG_FB, "facebook:onError", error);
                    }
                });
            }
        });

        emailSignInBtn = findViewById(R.id.emailLoginBtn);
        emailSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginEmailActivity.class);
                startActivity(intent);
            }
        });

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //check if user is already signed in then go to loggedInActivity
    private void checkUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            startActivity(new Intent(this, LoggedInActivity.class));
            finish();
        }

    }

    // START signin with google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed" + e.getMessage());
            }
        }
    }

    // START auth_with_google
    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(TAG, "firebaseAuthWithGoogle : begin firebase auth with google");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "onSuccess: Logged in");
                        //get logged in user
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        //get user info
                        userId = firebaseUser.getUid();
                        userName = firebaseUser.getDisplayName();
                        userEmail = firebaseUser.getEmail();
                        userImageUrl = firebaseUser.getPhotoUrl().toString();
                        Log.d(TAG, "Username: "+userName+" Userid: "+userEmail+" profileUrl: "+userImageUrl);

                        Log.d(TAG, "onSuccess: userId: " + userId + " - " + userName);
                        Log.d(TAG, "onSuccess: userEmail: " + userEmail);

                        //check if new or existing user
                        if (authResult.getAdditionalUserInfo().isNewUser()){
                            //new user Account created
                            Log.d(TAG, "onSuccess Account created ...\n" + userEmail);
                            Toast.makeText(LogInActivity.this, "Account Created...\n" + userEmail, Toast.LENGTH_SHORT).show();

                            DocumentReference documentReference = firebaseFirestore.collection("users").document();
                            Map<String,Object> user = new HashMap<>();
                            user.put("userId", userId);
                            user.put("userName", userName);
                            user.put("userEmail", userEmail);
                            user.put("userImageUrl", userImageUrl);
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
                            Toast.makeText(LogInActivity.this, "Existing User...\n" + userEmail, Toast.LENGTH_SHORT).show();
                        }

                        startActivity(new Intent(LogInActivity.this, LoggedInActivity.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Login Failed " + e.getMessage());
                    }
                });
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG_FB, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_FB, "signInWithCredential:success");
                            //get logged in user
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            //get user info
                            userId = firebaseUser.getUid();
                            userName = firebaseUser.getDisplayName();
                            userEmail = firebaseUser.getEmail();
                            userImageUrl = firebaseUser.getPhotoUrl().toString();
                            Log.d(TAG, "Username: "+userName+" Userid: "+userEmail+" profileUrl: "+userImageUrl);

                            Log.d(TAG_FB, "onSuccess: userId: " + userId + " - " + userName);
                            Log.d(TAG_FB, "onSuccess: userEmail: " + userEmail);

                            //check if new or existing user
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                //new user Account created
                                Log.d(TAG, "onSuccess Account created ...\n" + userEmail);
                                Toast.makeText(LogInActivity.this, "Account Created...\n" + userEmail, Toast.LENGTH_SHORT).show();

                                DocumentReference documentReference = firebaseFirestore.collection("users").document();
                                Map<String,Object> user = new HashMap<>();
                                user.put("userName", userName);
                                user.put("userEmail", userEmail);
                                user.put("userImageUrl", userImageUrl);
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
                                Toast.makeText(LogInActivity.this, "Existing User...\n" + userEmail, Toast.LENGTH_SHORT).show();
                            }

                            startActivity(new Intent(LogInActivity.this, LoggedInActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_FB, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }

}