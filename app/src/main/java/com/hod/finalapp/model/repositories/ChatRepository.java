package com.hod.finalapp.model.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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

    //Variable for UserChatViewModel
    private IChatRepositoryUserChatRoomListener mUserChatsListener;

    // Variables for ChatRoomViewModel
    private String mPreviousSubscribedRoomId = "";
    private ValueEventListener mPreviousSubscribedRoomListener;

    public interface IChatRepositoryUserChatRoomListener
    {
        public void onChildEventListener(boolean isNewChatroom, ChatRoom iChatRoom);
    }

    public void subscribeUserChatsListener(IChatRepositoryUserChatRoomListener iListener)
    {
        mUserChatsListener = iListener;
    }

    public ArrayList<ChatRoom> getUserChats()
    {
        return new ArrayList<>(mUserChats.values());
    }

    /***
     * Will subscribe to a individual chatroom for updates on changes.
     * Will unsubscribe to a previously subscribed one.
     * @param iChatRoomID
     * @param iListener
     */
    public void subscribeToAChatRoom(String iChatRoomID, ValueEventListener iListener)
    {
        if(!mPreviousSubscribedRoomId.isEmpty())
        {
            mChatTable.child(mPreviousSubscribedRoomId).removeEventListener(mPreviousSubscribedRoomListener);
        }

        mPreviousSubscribedRoomId = iChatRoomID;
        mPreviousSubscribedRoomListener = iListener;
        mChatTable.child(mPreviousSubscribedRoomId).addValueEventListener(iListener);
    }

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
                    ChatRoom chatRoom;
                    for(DataSnapshot chatRoomSnapShot : task.getResult().getChildren())
                    {
                        chatRoom = chatRoomSnapShot.getValue(ChatRoom.class);
                        if(isChatRoomForUser(chatRoom))
                        {
                            mUserChats.put(chatRoomSnapShot.getKey(),chatRoom);
                        }
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
        mChatTable.child(iChatRoom.getChatRoomId()).setValue(iChatRoom).addOnCompleteListener(listener);
    }

    /***
     *
     * @param iChatRoomID
     * @return null if not contains the room.
     */
    public ChatRoom tryGetChatRoom(String iChatRoomID)
    {
        return mUserChats.get(iChatRoomID);
    }

    //TODO Might need external listeners
    private ChildEventListener getChildEventListener()
    {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);

                if(isChatRoomForUser(chatRoom))
                {
                    mUserChats.put(snapshot.getKey(), chatRoom);
                    if(mUserChatsListener != null)
                    {
                        mUserChatsListener.onChildEventListener(true, chatRoom);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ChatRoom chatRoom =  snapshot.getValue(ChatRoom.class);

                if(isChatRoomForUser(chatRoom))
                {
                    mUserChats.replace(snapshot.getKey(), chatRoom);

                    if(mUserChatsListener != null)
                    {
                        mUserChatsListener.onChildEventListener(false, chatRoom);
                    }
                }
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

    private boolean isChatRoomForUser(ChatRoom iChatRoom)
    {
        String currentUserId = UserRepository.getInstance().getCurrentUser().getUserId();
        boolean isChatRoomForUser = false;

        if (iChatRoom.getOwnerId().equals(currentUserId) || iChatRoom.getReceiverId().equals(currentUserId)){
            isChatRoomForUser = true;
        }

        return isChatRoomForUser;
    }

}
