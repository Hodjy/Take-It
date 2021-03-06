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
import com.hod.finalapp.model.ApplicationContext;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatRoom> mChats;
    private View mRootView;

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
        TextView chatLastSentMessageTime;


        public ChatViewHolder(@NonNull @NotNull View chatView) {
            super(chatView);

            chatNameTv = itemView.findViewById(R.id.fragment_chat_room_chat_name);
            chatPictureIv = itemView.findViewById(R.id.fragment_chat_room_chat_picture);
            chatLastSentMessageTime = itemView.findViewById(R.id.chat_layout_message_time_tv);

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
        mRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(mRootView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatRoom chat = mChats.get(position);
        String lastMessageSender = chat.getChatMessages().get(chat.getChatMessages().size() - 1).getSenderUserId();
        boolean isChatroomHasUnreadMessageForUser = (
                !lastMessageSender.equals(UserRepository.getInstance().getCurrentUser().getUserId()))
                && (chat.getIsPendingMessage() == 1);

        if(isChatroomHasUnreadMessageForUser)
        {
            holder.itemView.setBackgroundColor(mRootView.getResources().
                    getColor(R.color.blue_dark, mRootView.getContext().getTheme()));
        }
        else
        {
            holder.itemView.setBackgroundColor(mRootView.getResources().
                    getColor(R.color.blue_light, mRootView.getContext().getTheme()));
        }

        holder.chatNameTv.setText(chat.getChatName());
        int lastIndex = chat.getChatMessages().size();
        String time = getLocalTimeFromMillis(chat.getChatMessages().get(lastIndex - 1).getMessageSentTime());
        holder.chatLastSentMessageTime.setText(time);
        loadUriImage(holder, chat);
    }

    private void loadUriImage(@NotNull ChatViewHolder holder, ChatRoom chat) {
        Glide.with(holder.itemView.getContext())
                .load(chat.getChatPictureUrl())
                .error(R.drawable.ic_baseline_account_circle_24)
                //.circleCrop()
                .into(holder.chatPictureIv);
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private String getLocalTimeFromMillis(long iMessageTime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(iMessageTime);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        String time = hour + ":";
        if(minute < 10)
        {
            time += "0";
        }
        time += minute;

        return time;
    }
}
