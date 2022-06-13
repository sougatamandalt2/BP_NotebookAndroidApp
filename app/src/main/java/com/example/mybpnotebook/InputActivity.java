package com.example.mybpnotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class InputActivity extends AppCompatActivity {

    ImageView iv_back;
    AutoCompleteTextView dropdown_menu;
    TextInputEditText tv_sys,tv_dia;
    MaterialButton btn_submit;
    Integer user;
    ProgressDialog progressDialog;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        iv_back=findViewById(R.id.iv_back);
        dropdown_menu=findViewById(R.id.dropdown_menu);
        tv_dia=findViewById(R.id.tv_dia);
        tv_sys=findViewById(R.id.tv_sys);
        btn_submit=findViewById(R.id.btn_submit);

        ProgressDialog progressDialog=new ProgressDialog(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String[] items={"User1","User2"};
        ArrayAdapter<String> itemAdapter=new ArrayAdapter<>(InputActivity.this,R.layout.list_item,items);
        dropdown_menu.setAdapter(itemAdapter);
        dropdown_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    user=1;
                }
                else{
                    user=2;
                }
            }
        });
        
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(dropdown_menu.getText().toString())){
                    Toast.makeText(InputActivity.this, "Select a User", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(tv_sys.getText().toString())){
                    Toast.makeText(InputActivity.this, "Please Enter Systalic Value", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(tv_dia.getText().toString())){
                    Toast.makeText(InputActivity.this, "Please Enter Diastolic Value", Toast.LENGTH_SHORT).show();
                    return;
                }

                int a=Integer.parseInt(tv_sys.getText().toString());
                int b=Integer.parseInt(tv_dia.getText().toString());

                if (a<b){
                    Toast.makeText(InputActivity.this, "Please Enter right Value", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(a<80 || a>250){
                    Toast.makeText(InputActivity.this, "Enter a Valid Systalic Value", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(b<40 || b>150){
                    Toast.makeText(InputActivity.this, "Enter a Valid Diastalic Value", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(a+b>190 && a+b<=210){
                    status="Normal";
                }
                else if(a+b>= 240){
                    status="Very High";
                }
                else if (a+b>210 && a+b<240){
                    status="High";
                }
                else if(a+b<= 190 && a+b>=175){
                    status="Low";
                }
                else if (a+b<175){
                    status="Very Low";
                }
                if(user==1){
                    progressDialog.setMessage("Saving Order Details");
                    progressDialog.show();
                    String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String timestrap=""+System.currentTimeMillis();
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("User1");
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("uid",FirebaseAuth.getInstance().getUid());
                    hashMap.put("user","User-1");
                    hashMap.put("timestrap",timestrap);
                    hashMap.put("sys",tv_sys.getText().toString());
                    hashMap.put("dia",tv_dia.getText().toString());
                    hashMap.put("status",status);
                    hashMap.put("date",date);
                    hashMap.put("time",currentTime);

                    reference.child(timestrap).setValue(hashMap);
                    progressDialog.dismiss();
                    Toast.makeText(InputActivity.this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InputActivity.this,HomepageActivity.class));
                }
                else{
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Saving Order Details");
                    progressDialog.show();
                    String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String timestrap=""+System.currentTimeMillis();
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("User2");
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("uid",FirebaseAuth.getInstance().getUid());
                    hashMap.put("timestrap",timestrap);
                    hashMap.put("user","User-2");
                    hashMap.put("sys",tv_sys.getText().toString());
                    hashMap.put("dia",tv_dia.getText().toString());
                    hashMap.put("status",status);
                    hashMap.put("date",date);
                    hashMap.put("time",currentTime);

                    reference.child(timestrap).setValue(hashMap);
                    progressDialog.dismiss();
                    Toast.makeText(InputActivity.this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InputActivity.this,HomepageActivity.class));
                }
            }
        });
    }

    private void user2() {
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Saving Order Details");
        progressDialog.show();
        String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String timestrap=""+System.currentTimeMillis();
        Date currentTime = Calendar.getInstance().getTime();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("User2");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",FirebaseAuth.getInstance().getUid());
        hashMap.put("timestrap",timestrap);
        hashMap.put("sys",tv_sys.getText().toString());
        hashMap.put("dia",tv_dia.getText().toString());
        hashMap.put("status",status);
        hashMap.put("date",date);
        hashMap.put("time",currentTime);

        reference.child(timestrap).setValue(timestrap);
        progressDialog.dismiss();
        Toast.makeText(this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(InputActivity.this,HomepageActivity.class));
    }

    private void user1() {
        progressDialog.setMessage("Saving Order Details");
        progressDialog.show();
        String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String timestrap=""+System.currentTimeMillis();
        Date currentTime = Calendar.getInstance().getTime();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("User1");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",FirebaseAuth.getInstance().getUid());
        hashMap.put("timestrap",timestrap);
        hashMap.put("sys",tv_sys.getText().toString());
        hashMap.put("dia",tv_dia.getText().toString());
        hashMap.put("status",status);
        hashMap.put("date",date);
        hashMap.put("time",currentTime);

        reference.child(timestrap).setValue(timestrap);
        progressDialog.dismiss();
        Toast.makeText(this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(InputActivity.this,HomepageActivity.class));
    }

    private void inputdata() {
        if(TextUtils.isEmpty(dropdown_menu.getText().toString())){
            Toast.makeText(this, "Select a User", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(tv_sys.getText().toString())){
            Toast.makeText(this, "Please Enter Systalic Value", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(tv_dia.getText().toString())){
            Toast.makeText(this, "Please Enter Diastolic Value", Toast.LENGTH_SHORT).show();
            return;
        }

        int a=Integer.parseInt(tv_sys.getText().toString());
        int b=Integer.parseInt(tv_dia.getText().toString());
        if(a<80 || a>250){
            Toast.makeText(this, "Enter a Valid Systalic Value", Toast.LENGTH_SHORT).show();
            return;
        }
        if(b<40 || b>150){
            Toast.makeText(this, "Enter a Valid Diastalic Value", Toast.LENGTH_SHORT).show();
            return;
        }

        if(a+b>=185 && a+b<215){
            status="Normal";
        }
        else if(a+b>=250){
            status="Very High";
        }
        else if (a+b>=215 && a+b<250){
            status="High";
        }
        else if(a+b< 185 && a+b>170){
            status="Low";
        }
        else if (a+b<=170){
            status="Very Low";
        }
    }
}