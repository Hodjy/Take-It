package com.hod.finalapp.view.viewmodel.chat;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.repositories.ChatRepository;
import com.hod.finalapp.view.ApplicationContext;

import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class UserChatsViewModel extends ViewModel
{

    private MutableLiveData<ArrayList<ChatRoom>> mMyChatsList;

    public UserChatsViewModel()
    {
        mMyChatsList = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<ChatRoom>> getMyChatsListLiveData() {
        return mMyChatsList;
    }

    public void initMyChatsListLiveData(){
        mMyChatsList.postValue(getSortedUserChatsList());
        initMyChatsListListener();
    }

    private void initMyChatsListListener(){
        ChatRepository.getInstance().subscribeUserChatsListener(new ChatRepository.IChatRepositoryUserChatRoomListener() {
            @Override
            public void onChildEventListener(boolean isNewChatroom, ChatRoom iChatRoom) {
                if(!isNewChatroom)
                {
                    int listSize = mMyChatsList.getValue().size();
                    for(int i = 0 ;listSize > i; i++)
                    {
                        if(mMyChatsList.getValue().get(i).getChatRoomId().equals(iChatRoom.getChatRoomId()))
                        {
                            mMyChatsList.getValue().remove(i);
                            break;
                        }
                    }
                }
                mMyChatsList.getValue().add(iChatRoom);
                mMyChatsList.postValue(mMyChatsList.getValue());
            }
        });
    }

    private ArrayList<ChatRoom> getSortedUserChatsList(){
        ArrayList<ChatRoom> tempList = ChatRepository.getInstance().getUserChats();
        Collections.sort(tempList, ChatRoom.Comparators.UpdatedTime);

        return tempList;
    }

        /*
            ChatRepository.getInstance().createNewChat(new ChatRoom("Hod", "10", "Ofir", "", "Name", null),
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task task) {
                        Toast.makeText(ApplicationContext.getAppContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });
     */
}
