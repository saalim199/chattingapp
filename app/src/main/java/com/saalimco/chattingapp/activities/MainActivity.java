package com.saalimco.chattingapp.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.saalimco.chattingapp.Models.ChatMessage;
import com.saalimco.chattingapp.Models.Conversation;
import com.saalimco.chattingapp.Models.Users;
import com.saalimco.chattingapp.adapters.RecentConversationsAdapter;
import com.saalimco.chattingapp.databinding.ActivityMainBinding;
import com.saalimco.chattingapp.listeners.ConversationListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ConversationListener {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listeners();
        init();
        setDetails();
        getToken();
        listenConversations();
    }

    private void init(){
        conversations=new ArrayList<>();
        conversationsAdapter=new RecentConversationsAdapter(conversations,MainActivity.this);
        binding.conversationRecyclerView.setAdapter(conversationsAdapter);
        database=FirebaseDatabase.getInstance();
    }

    private void listenConversations(){
        database=FirebaseDatabase.getInstance();
        database.getReference().child("conversation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conversations.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Conversation conversation=snapshot1.getValue(Conversation.class);
                    if(mAuth.getUid().equals(conversation.getSenderId()) || mAuth.getUid().equals(conversation.getRecieverId())){
                        String senderId=conversation.getSenderId();
                        String recieverId=conversation.getRecieverId();
                        ChatMessage chatMessage=new ChatMessage();
                        chatMessage.senderId=senderId;
                        chatMessage.receiverId=recieverId;
                        if(mAuth.getUid().equals(senderId)){
                            chatMessage.conversionImage=conversation.getRecieverImage();
                            chatMessage.conversionName=conversation.getRecieverName();
                            chatMessage.conversionId=conversation.getRecieverId();
                        }else{
                            chatMessage.conversionName=conversation.getSenderName();
                            chatMessage.conversionImage=conversation.getSenderImage();
                            chatMessage.conversionId=conversation.getSenderId();
                        }
                        chatMessage.message=conversation.getLastmessage();
                        conversations.add(chatMessage);
                    }
                }
                conversationsAdapter.notifyDataSetChanged();
                binding.conversationRecyclerView.setAdapter(conversationsAdapter);
                binding.conversationRecyclerView.smoothScrollToPosition(0);
                binding.conversationRecyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void listeners(){
        binding.imglogout.setOnClickListener(view -> {
            Logout();
        });
        binding.fabNewChat.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), UsersActivity.class));
        });
    }
    public void setDetails(){
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                binding.txtName.setText(users.getUsername());
                byte[] bytes= Base64.decode(users.getProfilepic(), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                binding.imageProfile.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    public void updateToken(String token){
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        database.getReference().child("users").child(mAuth.getUid()).child("fcmtoken").setValue(token)
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                   }else{
                       Toast.makeText(this, "Token update failed", Toast.LENGTH_SHORT).show();
                   }
                });
    }

    public void Logout(){
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(mAuth.getUid()).child("availability").setValue(0);
        database.getReference().child("users").child(mAuth.getUid()).child("fcmtoken").setValue(null)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                mAuth.signOut();
                                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MainActivity.this, SignInActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(this, "Unable to sign out..", Toast.LENGTH_SHORT).show();
                            }
                        });

        mAuth.signOut();

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

    @Override
    public void onConversionClicked(Users user) {
        Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}