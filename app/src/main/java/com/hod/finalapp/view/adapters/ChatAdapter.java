package com.hod.finalapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatRoom> mChats;

    public ChatAdapter(ArrayList<ChatRoom> iChats) {
        this.mChats = iChats;
    }

    public void setChatRoom(ArrayList<ChatRoom> mChatsList) {
        this.mChats = mChatsList;
    }

    public interface ChatListener{
        void onItemClicked(ChatRoom chatRoom);
    }

    private ChatListener listener;

    public void setListener(ChatListener listener)
    {
        this.listener = listener;
    }

    public void setItems(ArrayList<ChatRoom> iChats) {
        this.mChats = iChats;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView chatPictureIv;
        TextView chatNameTv;


        public ChatViewHolder(@NonNull @NotNull View chatView) {
            super(chatView);

            chatNameTv = itemView.findViewById(R.id.fragment_chat_room_chat_name);
            chatPictureIv = itemView.findViewById(R.id.fragment_chat_room_chat_picture);

            chatView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    listener.onItemClicked(mChats.get(adapterPosition));
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatRoom chat = mChats.get(position);
        holder.chatNameTv.setText(chat.getChatName());
        loadUriImage(holder, chat);
    }

    private void loadUriImage(@NotNull ChatViewHolder holder, ChatRoom chat) {
        Glide.with(holder.itemView.getContext())
                .load(chat.getChatPictureUrl())
                .error(R.drawable.ic_baseline_account_circle_24)
                .circleCrop()
                .into(holder.chatPictureIv);
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }
}
