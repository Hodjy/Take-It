package com.hod.finalapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.chatroom.ChatMessage;
import com.hod.finalapp.model.repositories.UserRepository;

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
        TextView messageTimeTextTv;


        public MessageViewHolder(@NonNull @NotNull View messageView) {
            super(messageView);

            messagePictureIv = itemView.findViewById(R.id.message_layout_sender_picture);
            messageTextTv = itemView.findViewById(R.id.message_layout_message_text_sent);
            messageTimeTextTv = itemView.findViewById(R.id.message_layout_message_time_tv);
        }

        private void bindSentMessage() {
            ((RelativeLayout)itemView.findViewById(R.id.message_layout_received_relative_layout)).setVisibility(View.GONE);
            ((RelativeLayout)itemView.findViewById(R.id.message_layout_sent_relative_layout)).setVisibility(View.VISIBLE);
            messagePictureIv = itemView.findViewById(R.id.message_layout_sender_picture);
            messageTextTv = itemView.findViewById(R.id.message_layout_message_text_sent);
        }

        private void bindReceivedMessage() {
            ((RelativeLayout)itemView.findViewById(R.id.message_layout_sent_relative_layout)).setVisibility(View.GONE);
            ((RelativeLayout)itemView.findViewById(R.id.message_layout_received_relative_layout)).setVisibility(View.VISIBLE);
            messagePictureIv = itemView.findViewById(R.id.message_layout_receiver_picture);
            messageTextTv = itemView.findViewById(R.id.message_layout_message_text_received);
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

        if (message.getSenderUserId().equals(UserRepository.getInstance().getCurrentUser().getUserId())) {
            holder.bindSentMessage();
        } else {
            holder.bindReceivedMessage();
        }

        holder.messageTextTv.setText(message.getMessageText());
        holder.messageTimeTextTv.setText(message.getMessageSentTime());
        loadUriImage(holder, message);
    }

    private void loadUriImage(@NotNull MessageViewHolder holder, ChatMessage message) {
        Glide.with(holder.itemView.getContext())
                .load(message.getMessagePictureUrl())
                .error(R.drawable.ic_baseline_account_circle_24)
                .circleCrop()
                .into(holder.messagePictureIv);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
