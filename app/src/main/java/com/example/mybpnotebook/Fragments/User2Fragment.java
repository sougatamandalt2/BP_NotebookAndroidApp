package com.example.mybpnotebook.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybpnotebook.ArrayAdapterClass;
import com.example.mybpnotebook.HomepageActivity;
import com.example.mybpnotebook.R;
import com.example.mybpnotebook.modelRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User2Fragment extends Fragment {

    private ProgressDialog progressDialog;
    ArrayList<modelRecord> bpRecord;
    ArrayAdapterClass arrayAdapter;
    RecyclerView rv_f1;
    TextView empty_view;

    public User2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user2, container, false);
        rv_f1=view.findViewById(R.id.rv_f1);
        empty_view=view.findViewById(R.id.empty_view);
        progressDialog=new ProgressDialog(getContext());

        progressDialog.setMessage("Loading User2 Data ______");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        bpRecord=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("User2");
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
                            arrayAdapter=new ArrayAdapterClass(getContext(), bpRecord);
                            rv_f1.setAdapter(arrayAdapter);
                        }
                        else{
                            empty_view.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        progressDialog.dismiss();
        return view;
    }
}