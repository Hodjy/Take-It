package com.hod.finalapp.view.viewmodel.chat;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatMessage;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.repositories.ChatRepository;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.ApplicationContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {

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
        mChatMessages = new MutableLiveData<>();
    }

    public boolean initChat(String iChatRoomID)
    {
        mCurrentChatRoom = ChatRepository.getInstance().tryGetChatRoom(iChatRoomID);
        isThisChatRoomNew = (mCurrentChatRoom == null);

        if(!isThisChatRoomNew)
        {
            unpackData();
        }

        return isThisChatRoomNew;
    }

    public void createChatRoom(Item iRelevantItem)
    {
        ChatMessage msg = new ChatMessage("hello world",iRelevantItem.getOwnerId()
                ,UserRepository.getInstance().getCurrentUser().getUserId(),iRelevantItem.getPicturesUrls().get(0));
        ArrayList<ChatMessage> test = new ArrayList();
        test.add(msg);
        mCurrentChatRoom = new ChatRoom(iRelevantItem.getOwnerId(),iRelevantItem.getItemId()
                ,UserRepository.getInstance().getCurrentUser().getUserId(),
                iRelevantItem.getPicturesUrls().get(0),iRelevantItem.getItemName(),test
                );

        unpackData();
    }

    private void unpackData()
    {
        mChatName.postValue(mCurrentChatRoom.getChatName());
        mChatImage.postValue(mCurrentChatRoom.getChatPictureUrl());
        mChatMessages.postValue(mCurrentChatRoom.getChatMessages());
    }



}
