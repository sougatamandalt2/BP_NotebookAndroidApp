package com.example.mybpnotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    CircularImageView dp;
    ImageView iv_back,logout;
    TextView tv_email,tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dp=findViewById(R.id.dp);
        iv_back=findViewById(R.id.iv_back);
        tv_email=findViewById(R.id.tv_email);
        tv_name=findViewById(R.id.tv_name);
        logout=findViewById(R.id.logout);

        FirebaseUser account= FirebaseAuth.getInstance().getCurrentUser();
        String personImage = Objects.requireNonNull(account.getPhotoUrl()).toString();
        ImageView userImage = findViewById(R.id.dp);
        Glide.with(this).load(personImage).into(userImage);


        String name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        tv_name.setText(name);

        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        tv_email.setText(email);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Sign Out Successfull.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}