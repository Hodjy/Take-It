package com.hod.finalapp.model.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;

public class ChatRepository {

    private static ChatRepository mChatRepository;
    private final DatabaseReference mChatTable;
    private Hashtable<String,ChatRoom> mUserChats;

    private ChatRepository()
    {
        mChatTable = DatabaseManager.getInstance().getFirebaseDatabaseInstance()
                .getReference(eFirebaseDataTypes.CHATROOMS.mTypeName);
        mUserChats = new Hashtable<>();

        //Initial DB load.
        mChatTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DataSnapshot chatRoom : task.getResult().getChildren())
                    {
                        mUserChats.put(chatRoom.getKey(),chatRoom.getValue(ChatRoom.class));
                    }
                }
            }
        });

        mChatTable.addChildEventListener(getChildEventListener());
    }

    public static ChatRepository getInstance()
    {
        if(mChatRepository == null)
        {
            mChatRepository = new ChatRepository();
        }

        return  mChatRepository;
    }

    public void createNewChat(ChatRoom iChatRoom, OnCompleteListener listener)
    {
        mChatTable.child(iChatRoom.generateChatRoomId()).setValue(iChatRoom).addOnCompleteListener(listener);
    }

    //TODO Might need external listeners
    private ChildEventListener getChildEventListener()
    {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                mUserChats.put(snapshot.getKey(), snapshot.getValue(ChatRoom.class));
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                mUserChats.replace(snapshot.getKey(), snapshot.getValue(ChatRoom.class));
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                mUserChats.remove(snapshot.getKey());
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
