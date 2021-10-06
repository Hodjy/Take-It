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

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {

    private ChatRoom mCurrentChatRoom;

    public ChatRoomViewModel()
    {

    }

    public void initChat(String iChatRoomID, Item iRelevantItem)
    {

    }

}
