package com.saalimco.chattingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.saalimco.chattingapp.R;
import com.saalimco.chattingapp.databinding.ActivitySignInBinding;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    private void setListeners(){
        binding.textCreatenewacc.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.btnSignIn.setOnClickListener(view -> {
            SignIn();
        });

    }
    private void SignIn(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(!binding.inputEmail.getText().toString().isEmpty() && !binding.inputPassword.getText().toString().isEmpty()){
            loading(true);
            mAuth.signInWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString())
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                            loading(false);
                           Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
                           Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                           startActivity(intent);
                       }
                       else{
                           loading(false);
                           Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    });
        }
        else {
            loading(false);
            Toast.makeText(this, "Enter all credentials", Toast.LENGTH_SHORT).show();
        }
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}