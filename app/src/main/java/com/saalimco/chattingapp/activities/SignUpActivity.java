package com.saalimco.chattingapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.saalimco.chattingapp.Models.Users;
import com.saalimco.chattingapp.databinding.ActivitySignUpBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignUpActivity extends AppCompatActivity {
 private ActivitySignUpBinding binding;
 private String encodedimage;
 private FirebaseDatabase database;
 private FirebaseAuth mAuth;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }
    private void setListeners(){
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.btnSignUp.setOnClickListener(view -> {
            if(isValidDetails()){
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private void showToast(String message){
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
        loading(true);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        mAuth.createUserWithEmailAndPassword(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Users users=new Users(binding.inputName.getText().toString(), binding.inputPassword.getText().toString(), binding.inputEmail.getText().toString(),encodedimage);
                        String id = task.getResult().getUser().getUid();
                        database.getReference().child("users").child(id).setValue(users);
                        loading(false);
                        showToast("Sign Up Complete");
                        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        showToast(task.getException().toString());
                        loading(false);
                    }
                });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap =Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
       if(result.getResultCode() == RESULT_OK){
           if(result.getData()!=null){
               Uri uri=result.getData().getData();
               try {
                   InputStream inputStream=getContentResolver().openInputStream(uri);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    binding.imageProfile.setImageBitmap(bitmap);
                    binding.textAddImg.setVisibility(View.GONE);
                    encodedimage=encodeImage(bitmap);
               }catch (FileNotFoundException e){
                   e.printStackTrace();
               }
           }
       }
    });

    private Boolean isValidDetails(){
        if(encodedimage == null){
            Toast.makeText(this, "PLease select profile pic", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "PLease enter Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter valid email ID");
        }else if(binding.inputName.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Password doesn't matches");
            return false;
        }else
            return true;
        return null;
    }

    public void loading(Boolean isloading){
        if(isloading){
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
        }
    }
}