package com.saalimco.chattingapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onPause() {
        super.onPause();
        database.getReference().child("users").child(mAuth.getUid()).child("availability").setValue(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("users").child(mAuth.getUid()).child("availability").setValue(1);
    }
}
