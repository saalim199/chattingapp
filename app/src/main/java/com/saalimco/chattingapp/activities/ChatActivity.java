package com.saalimco.chattingapp.activities;

import androidx.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saalimco.chattingapp.Models.ChatMessage;
import com.saalimco.chattingapp.Models.Constants;
import com.saalimco.chattingapp.Models.Conversation;
import com.saalimco.chattingapp.Models.Users;
import com.saalimco.chattingapp.adapters.ChatAdapter;
import com.saalimco.chattingapp.databinding.ActivityChatBinding;
import com.saalimco.chattingapp.network.ApiClient;
import com.saalimco.chattingapp.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private Users recieverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String conversionId=null;
    private Users senderUser;
    private Boolean isRecieverAvailable=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSender();
        setListeners();
        loadRecieverDetails();
        init();
        setChatMessages();
    }

    private void getSender(){
        database=FirebaseDatabase.getInstance();
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    if((snapshot1.getKey().equals(mAuth.getUid()))){
                        senderUser=(Users) snapshot1.getValue(Users.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getRemoteMsgHeaders(), messageBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful()){
                    try {
                        if (response.body()!=null){
                            JSONObject responseJson= new JSONObject(response.body());
                            JSONArray results=responseJson.getJSONArray("results");
                            if(responseJson.getInt("failure")==1){
                                JSONObject error= (JSONObject) results.get(0);
                                Toast.makeText(ChatActivity.this, error.getString("error"), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(ChatActivity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChatActivity.this, "Error: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Toast.makeText(ChatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenAvailabilityOfReciever(){
        database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(recieverUser.getUserid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
                if(user.getAvailability() !=null){
                    int availability= Objects.requireNonNull(user.getAvailability()).intValue();
                    isRecieverAvailable= availability == 1;
                    recieverUser.setFcmtoken(user.getFcmtoken());
                    if(recieverUser.getProfilepic()==null){
                        recieverUser.setProfilepic(user.getProfilepic());
                        chatAdapter.setRecieverProfileImage(getBitmapFromEncodedString(recieverUser.getProfilepic()));
                        chatAdapter.notifyItemRangeChanged(0, chatMessages.size());
                    }
                }
                if(isRecieverAvailable){
                    binding.txtAvailability.setVisibility(View.VISIBLE);
                }else{
                    binding.txtAvailability.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

   private void setChatMessages(){
        database.getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    ChatMessage chatMessage=snapshot1.getValue(ChatMessage.class);
                    if((mAuth.getUid()+recieverUser.getUserid()).equals(chatMessage.getSenderId()+chatMessage.getReceiverId())){
                        chatMessages.add(chatMessage);
                    }
                    if((mAuth.getUid()+recieverUser.getUserid()).equals(chatMessage.getReceiverId()+chatMessage.getSenderId())){
                        chatMessages.add(chatMessage);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                binding.chatRecyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                if(conversionId==null){
                    checkforConversation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   }

    private void sendMessage(){
        final ChatMessage chatMessage=new ChatMessage(mAuth.getUid(),binding.inputMessage.getText().toString());
        chatMessage.setDateTime(new Date().getTime());
        chatMessage.setReceiverId(recieverUser.getUserid());
        database.getReference().child("chats").push().setValue(chatMessage).addOnSuccessListener(unused -> {

        });
        if(conversionId!=null){
            updateConversation(binding.inputMessage.getText().toString());
        }else{
            Conversation conversation=new Conversation();
            conversation.setSenderId(mAuth.getUid());
            conversation.setSenderName(senderUser.getUsername());
            conversation.setSenderImage(senderUser.getProfilepic());
            conversation.setRecieverId(recieverUser.getUserid());
            conversation.setRecieverName(recieverUser.getUsername());
            conversation.setRecieverImage(recieverUser.getProfilepic());
            conversation.setLastmessage(binding.inputMessage.getText().toString());
            conversation.setTimestamp(new Date().getTime());
            addConversion(conversation);
        }
        if(!isRecieverAvailable){
            try {
                JSONArray tokens=new JSONArray();
                tokens.put(recieverUser.getFcmtoken());
                JSONObject data=new JSONObject();
                data.put("user", mAuth.getUid());
                data.put("username", senderUser.getUsername());
                data.put("fcmtoken", senderUser.getFcmtoken());
                data.put("message", binding.inputMessage.getText().toString());

                JSONObject body=new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA, data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);
                sendNotification(body.toString());
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        binding.inputMessage.setText(null);
    }

    private void init() {
        chatMessages=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();
        chatAdapter=new ChatAdapter(chatMessages, getBitmapFromEncodedString(recieverUser.getProfilepic()),mAuth.getUid());
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database=FirebaseDatabase.getInstance();
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage){
        if(encodedImage!=null){
            byte[] bytes= Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }else{
            return null;
        }
    }

    private void loadRecieverDetails(){
        recieverUser=(Users) getIntent().getSerializableExtra("user");
        binding.txtName.setText(recieverUser.getUsername());
    }
    private void setListeners() {
        binding.imageBack.setOnClickListener(v-> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private void addConversion(Conversation conversation){
        conversionId= database.getReference().child("conversation").push().getKey();
        database.getReference().child("conversation").child(conversionId).setValue(conversation);
    }
    private void updateConversation(String message){
        database.getReference().child("conversation").child(conversionId).child("lastmessage").setValue(message);
        database.getReference().child("conversation").child(conversionId).child("timestamp").setValue(new Date().getTime());
    }
    private void checkforConversation(){
        if(chatMessages.size() != 0){
            database.getReference().child("conversation").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        Conversation conversation=snapshot1.getValue(Conversation.class);
                        if(mAuth.getUid().equals(conversation.getSenderId()) && recieverUser.getUserid().equals(conversation.getRecieverId())){
                            conversionId=snapshot1.getKey();
                            break;
                        }
                        if(recieverUser.getUserid().equals(conversation.getSenderId()) && mAuth.getUid().equals(conversation.getRecieverId())){
                            conversionId=snapshot1.getKey();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReciever();
    }
}