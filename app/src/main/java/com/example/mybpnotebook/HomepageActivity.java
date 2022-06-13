package com.example.mybpnotebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybpnotebook.Fragments.User1Fragment;
import com.example.mybpnotebook.Fragments.User2Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomepageActivity extends AppCompatActivity {

    FloatingActionButton floating_action_button;
    ImageView iv_profile;
    RecyclerView rv;
    TextView button1, button2,bp_records;
    FrameLayout linear;
    ProgressDialog progressDialog;
    ArrayList<modelRecord> bpRecord;
    ArrayAdapterClass arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        isOnline();

        progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("Please Wait...");

        floating_action_button=findViewById(R.id.floating_action_button);
        iv_profile=findViewById(R.id.iv_profile);
        bp_records=findViewById(R.id.bp_records);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        rv=findViewById(R.id.rv);
        linear=findViewById(R.id.linear);

        loadRecord();

//        toggleButton=findViewById(R.id.toggleButton);
        FirebaseUser account=FirebaseAuth.getInstance().getCurrentUser();
        String personImage = Objects.requireNonNull(account.getPhotoUrl()).toString();
        ImageView userImage = findViewById(R.id.iv_profile);
        Glide.with(this).load(personImage).into(userImage);

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,ProfileActivity.class));
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User1Fragment user1Fragment=new User1Fragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linear,user1Fragment);
                transaction.commit();
                bp_records.setText("Blood Pressure Records (User-1)");
                Toast.makeText(HomepageActivity.this, "User1 is selected.", Toast.LENGTH_SHORT).show();
                button1.setBackground(ContextCompat.getDrawable(HomepageActivity.this, R.drawable.shape05));
                button2.setBackground(ContextCompat.getDrawable(HomepageActivity.this, R.drawable.shape03));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User2Fragment user2Fragment=new User2Fragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.linear,user2Fragment);
                transaction.commit();
                bp_records.setText("Blood Pressure Records (User-2)");
                Toast.makeText(HomepageActivity.this, "User2 is selected.", Toast.LENGTH_SHORT).show();
                button1.setBackground(ContextCompat.getDrawable(HomepageActivity.this, R.drawable.shape03));
                button2.setBackground(ContextCompat.getDrawable(HomepageActivity.this, R.drawable.shape05));
            }
        });

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,InputActivity.class));
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,ProfileActivity.class));
            }
        });
    }

    private void loadRecord() {
        bpRecord=new ArrayList<>();
        progressDialog.setMessage("Loading User1 Data ______");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("User1");
        ref.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            bpRecord.clear();
                            for(DataSnapshot dss:snapshot.getChildren()){
                                modelRecord modelRecord=dss.getValue(modelRecord.class);
                                bpRecord.add(modelRecord);
                            }
                            arrayAdapter=new ArrayAdapterClass(HomepageActivity.this, bpRecord);
                            rv.setAdapter(arrayAdapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        progressDialog.dismiss();
    }


    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(HomepageActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void onBackPressed() {
        finishAffinity();
    }
}