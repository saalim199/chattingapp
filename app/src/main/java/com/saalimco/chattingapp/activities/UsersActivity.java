package com.saalimco.chattingapp.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saalimco.chattingapp.Models.Users;
import com.saalimco.chattingapp.adapters.UsersAdapter;
import com.saalimco.chattingapp.databinding.ActivityUsersBinding;
import com.saalimco.chattingapp.listeners.UserListener;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        getUser();
    }
    private void setListeners(){
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void getUser(){
        loading(true);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Users> users=new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users user=dataSnapshot.getValue(Users.class);
                    user.setUserid(dataSnapshot.getKey());
                    if(!user.getUserid().equals(mAuth.getUid())){
                            users.add(user);
                    }
                }
                if(users.size()>0){
                    loading(false);
                    UsersAdapter usersAdapter=new UsersAdapter(users,UsersActivity.this);
                    binding.usersRecyclerView.setAdapter(usersAdapter);
                    binding.usersRecyclerView.setVisibility(View.VISIBLE);

                }else{
                    loading(false);
                    showError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showError(){
        binding.txtErrorMessage.setText("No user available");
        binding.txtErrorMessage.setVisibility(View.VISIBLE);
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(Users user) {
        Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}