package com.saalimco.chattingapp.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saalimco.chattingapp.Models.ChatMessage;
import com.saalimco.chattingapp.databinding.ItemContainerRecievedMessageBinding;
import com.saalimco.chattingapp.databinding.ItemContainerSentMessageBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> chatMessages;
    private Bitmap recieverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT =1;
    public static final int VIEW_TYPE_RECIEVED =2;

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap recieverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.recieverProfileImage = recieverProfileImage;
        this.senderId = senderId;
    }

    public void setRecieverProfileImage(Bitmap bitmap){
        recieverProfileImage=bitmap;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),parent,false
                    )
            );
        }else{
            return new RecievedMessageViewHolder(
                    ItemContainerRecievedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),parent,false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chat=chatMessages.get(position);

            if(getItemViewType(position)==VIEW_TYPE_SENT){
                ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
            }else{
                ((RecievedMessageViewHolder) holder).setData(chatMessages.get(position), recieverProfileImage);
            }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECIEVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding=itemContainerSentMessageBinding;
        }
        void setData(ChatMessage chatMessage){
            binding.txtMessage.setText(chatMessage.message);
            Date date=new Date(chatMessage.getDateTime());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM dd, yyyy - hh:mm a");
            String strdate=simpleDateFormat.format(date);
            binding.txtDateTime.setText(strdate);
        }
    }

    static class RecievedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerRecievedMessageBinding binding;

        RecievedMessageViewHolder(ItemContainerRecievedMessageBinding itemContainerRecievedMessageBinding){
            super(itemContainerRecievedMessageBinding.getRoot());
            binding=itemContainerRecievedMessageBinding;
        }

        void setData(ChatMessage chatMessage,Bitmap recieverProfileImage){
            binding.txtMessage.setText(chatMessage.message);
            Date date=new Date(chatMessage.getDateTime());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM dd, yyyy - hh:mm a");
            String strdate=simpleDateFormat.format(date);
            binding.txtDateTime.setText(strdate);
            if(recieverProfileImage!=null){
                binding.imageProfile.setImageBitmap(recieverProfileImage);
            }
        }
    }
}
