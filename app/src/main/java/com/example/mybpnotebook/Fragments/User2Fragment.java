package com.example.mybpnotebook.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybpnotebook.ArrayAdapterClass;
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

    public User2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user2, container, false);
        rv_f1=view.findViewById(R.id.rv_f1);
        progressDialog=new ProgressDialog(getContext());

        bpRecord=new ArrayList<>();
        progressDialog.setMessage("Loading Your Data");
        progressDialog.show();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bpRecord.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    String uid=""+ds.getRef().getKey();

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("User2");
                    ref.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot dss:snapshot.getChildren()){
                                            modelRecord modelRecord=dss.getValue(modelRecord.class);
                                            bpRecord.add(modelRecord);
                                            progressDialog.dismiss();
                                        }
                                        arrayAdapter=new ArrayAdapterClass(getContext(), bpRecord);
                                        rv_f1.setAdapter(arrayAdapter);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}