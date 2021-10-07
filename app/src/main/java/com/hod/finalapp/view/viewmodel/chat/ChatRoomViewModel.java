package com.hod.finalapp.view.viewmodel.chat;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatMessage;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.repositories.ChatRepository;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.ApplicationContext;
import com.hod.finalapp.view.adapters.MessageAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel
{
    private String mOtherUserId = "";
    private String mCurrentUserId = "";
    private MutableLiveData<String> mChatImage;
    private MutableLiveData<String> mChatName;
    private MutableLiveData<ArrayList<ChatMessage>> mChatMessages;
    private ChatRoom mCurrentChatRoom;
    private boolean isThisChatRoomNew;

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() {
        return mChatMessages;
    }

    public MutableLiveData<String> getChatImage() {
        return mChatImage;
    }

    public MutableLiveData<String> getChatName() {
        return mChatName;
    }

    public ChatRoomViewModel()
    {
        mChatImage = new MutableLiveData<>();
        mChatName = new MutableLiveData<>();
        mChatMessages = new MutableLiveData<>(new ArrayList<>());
    }

    public boolean initChat(String iChatRoomID)
    {
        mCurrentChatRoom = ChatRepository.getInstance().tryGetChatRoom(iChatRoomID);
        isThisChatRoomNew = (mCurrentChatRoom == null);
        mCurrentUserId = UserRepository.getInstance().getCurrentUser().getUserId();

        if(!isThisChatRoomNew)
        {
            boolean isCurrentUserChatReceiver = mCurrentChatRoom.getReceiverId().equals(mCurrentUserId);
            if(isCurrentUserChatReceiver)
            {
                mOtherUserId = mCurrentChatRoom.getOwnerId();
            }
            else
            {
                mOtherUserId = mCurrentChatRoom.getReceiverId();
            }

            unpackData();
            ChatRepository.getInstance().subscribeToAChatRoom(mCurrentChatRoom.getChatRoomId(),getNewMessageListener());
        }

        return isThisChatRoomNew;
    }

    public void createChatRoom(Item iRelevantItem)
    {
        ArrayList<ChatMessage> emptyList = new ArrayList();
        mOtherUserId = iRelevantItem.getOwnerId();
        mCurrentUserId = UserRepository.getInstance().getCurrentUser().getUserId();
        mCurrentChatRoom = new ChatRoom(iRelevantItem.getOwnerId(),iRelevantItem.getItemId()
                ,mCurrentUserId, iRelevantItem.getPicturesUrls().get(0),
                iRelevantItem.getItemName(),emptyList
                );

        unpackData();
    }

    private void unpackData()
    {
        mChatName.postValue(mCurrentChatRoom.getChatName());
        mChatImage.postValue(mCurrentChatRoom.getChatPictureUrl());
        //mChatMessages.postValue(mCurrentChatRoom.getChatMessages());
    }

    public void sendMessage(String iMessageText)
    {
        ChatMessage newChatMessage = new ChatMessage(iMessageText, mCurrentUserId, mOtherUserId, UserRepository.getInstance().getCurrentUser().getPictureUrl());

        if(isThisChatRoomNew)
        {
            mCurrentChatRoom.addMessage(newChatMessage);
            ChatRepository.getInstance().createNewChat(mCurrentChatRoom, getNewMessageListener());
            isThisChatRoomNew = false;
        }
        else
        {
            ChatRepository.getInstance().sendMessage(mCurrentChatRoom.getChatRoomId(), newChatMessage);
        }
    }

    private ChildEventListener getNewMessageListener()
    {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName)
            {
                ArrayList<ChatMessage> temp = mChatMessages.getValue();
                temp.add(snapshot.getValue(ChatMessage.class));
                mChatMessages.postValue(temp);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
    }

}
