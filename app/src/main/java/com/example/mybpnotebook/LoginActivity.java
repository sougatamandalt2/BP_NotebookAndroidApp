package com.example.mybpnotebook;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private SignInButton btnSignInWithGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    public static final int RC_SIGN_IN = 100;
    private String name,email,image;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignInWithGoogle = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        requestGoogleSignIn();


        btnSignInWithGoogle.setOnClickListener(view -> {
            signIn();
        });

    }

    private void requestGoogleSignIn(){
        // Configure sign-in to request the user’s basic profile like name and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {

        //getting user credentials with the help of AuthCredential method and also passing user Token Id.
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //trying to sign in user using signInWithCredential and passing above credentials of user.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew==false){
                                Intent i = new Intent(LoginActivity.this, HomepageActivity.class);
                                startActivity(i);
                            }
                            else{
                                saveFirebaseData();
                            }

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "User authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating user with firebase using received token id
                firebaseAuthWithGoogle(account.getIdToken());

                //assigning user information to variables
                name = account.getDisplayName();
                email = account.getEmail();
                image = account.getPhotoUrl().toString();

                //create sharedPreference to store user data when user signs in successfully
//                SharedPreferences.Editor editor = getApplicationContext()
//                        .getSharedPreferences("MyPrefs",MODE_PRIVATE)
//                        .edit();
//////                editor.putString("username", name);
//////                editor.putString("useremail", email);
//////                editor.putString("userPhoto", userPhoto);
//////                editor.apply();

                Log.i(TAG, "onActivityResult: Success");

            } catch (ApiException e) {
                Log.e(TAG, "onActivityResult: " + e.getMessage());
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent i= new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(i);
        }
    }

    private void saveFirebaseData() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Creating Your Account....");
        progressDialog.show();
        String timeStamp=""+System.currentTimeMillis();

        HashMap<String,Object> hashmap=new HashMap<>();

        hashmap.put("uid",mAuth.getUid());
        hashmap.put("name",""+name);
        hashmap.put("email",""+email);
        hashmap.put("photo",""+image);
        hashmap.put("timeStamp",""+timeStamp);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(mAuth.getUid()).setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Intent i=new Intent(LoginActivity.this,HomepageActivity.class);
                        i.putExtra("name",name);
                        Toast.makeText(LoginActivity.this,"Registration Successful",Toast.LENGTH_SHORT);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onBackPressed() {
        finishAffinity();
    }

}