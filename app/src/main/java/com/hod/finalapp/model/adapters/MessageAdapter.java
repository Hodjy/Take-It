package com.hod.finalapp.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatMessage;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private ArrayList<ChatMessage> messages;

    public MessageAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView messagePictureIv;
        TextView messageTextTv;


        public MessageViewHolder(@NonNull @NotNull View messageView) {
            super(messageView);

            messageTextTv = itemView.findViewById(R.id.message_layout_sender_picture);
            messagePictureIv = itemView.findViewById(R.id.message_layout_message_text);

        }
    }

    @NonNull
    @NotNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        MessageViewHolder messageViewHolder = new MessageViewHolder(view);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextTv.setText(message.getMessageText());
        loadUriImage(holder, message);
    }

    private void loadUriImage(@NotNull MessageViewHolder holder, ChatMessage message) {
        Glide.with(holder.itemView.getContext())
                .load(message.getMessagePictureUrl())
                .error(R.drawable.ic_baseline_account_circle_24)
                .into(holder.messagePictureIv);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
