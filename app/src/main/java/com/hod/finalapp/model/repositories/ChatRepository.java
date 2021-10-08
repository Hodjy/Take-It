package com.hod.finalapp.model.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.hod.finalapp.model.database_objects.chatroom.ChatMessage;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.enums.eChatRoomDataTypes;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;

public class ChatRepository {

    private static ChatRepository mChatRepository;
    private final DatabaseReference mChatTable;
    private Hashtable<String,ChatRoom> mUserChats;

    //Variable for UserChatViewModel
    private IChatRepositoryUserChatRoomListener mUserChatsListener;

    // Variables for ChatRoomViewModel
    private String mPreviousSubscribedRoomId = "";
    private ChildEventListener mPreviousSubscribedRoomListener;

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
    public void subscribeToAChatRoom(String iChatRoomID, ChildEventListener iListener)
    {
        removeChatRoomListener();
        mPreviousSubscribedRoomId = iChatRoomID;
        mPreviousSubscribedRoomListener = iListener;
        mChatTable.child(mPreviousSubscribedRoomId)
                .child(eChatRoomDataTypes.CHAT_MESSAGES.mTypeName)
                .addChildEventListener(iListener);
    }

    private void removeChatRoomListener() {
        if(!mPreviousSubscribedRoomId.isEmpty())
        {
            mChatTable.child(mPreviousSubscribedRoomId)
                    .child(eChatRoomDataTypes.CHAT_MESSAGES.mTypeName)
                    .removeEventListener(mPreviousSubscribedRoomListener);
        }
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
                        chatRoom = convertSnapshotToChatRoom(chatRoomSnapShot);
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

        return mChatRepository;
    }

    public void createNewChat(ChatRoom iChatRoom, ChildEventListener iListener)
    {
        mChatTable.child(iChatRoom.getChatRoomId())
                .setValue(iChatRoom);
        subscribeToAChatRoom(iChatRoom.getChatRoomId(),iListener);
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

    public void sendMessage(String iChatRoomId, ChatMessage iNewChatMessage)
    {
        String msgKey = mChatTable.child(iChatRoomId).child(eChatRoomDataTypes.CHAT_MESSAGES.mTypeName).push().getKey();
        mChatTable.child(iChatRoomId).child(eChatRoomDataTypes.CHAT_MESSAGES.mTypeName).child(msgKey).setValue(iNewChatMessage);
        mChatTable.child(iChatRoomId).child(eChatRoomDataTypes.UPDATED_TIME_IN_MILLIS.mTypeName).setValue(GregorianCalendar.getInstance().getTimeInMillis());
    }

    //TODO Might need external listeners
    private ChildEventListener getChildEventListener()
    {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                mChatTable.child(snapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        ChatRoom chatRoom;
                        chatRoom = convertSnapshotToChatRoom(task.getResult());

                        if(isChatRoomForUser(chatRoom))
                        {
                            mUserChats.put(snapshot.getKey(), chatRoom);
                            if(mUserChatsListener != null)
                            {
                                mUserChatsListener.onChildEventListener(true, chatRoom);
                            }
                        }
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                mChatTable.child(snapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        ChatRoom chatRoom;
                        chatRoom = convertSnapshotToChatRoom(task.getResult());
                        if(isChatRoomForUser(chatRoom))
                        {
                            mUserChats.replace(snapshot.getKey(), chatRoom);

                            if(mUserChatsListener != null)
                            {
                                mUserChatsListener.onChildEventListener(false, chatRoom);
                            }
                        }
                    }
                });


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

    private ChatRoom convertSnapshotToChatRoom(DataSnapshot iDataSnapShot)
    {
        ChatRoom convertedChatRoom;
        String chatName = iDataSnapShot.child(eChatRoomDataTypes.CHAT_NAME.mTypeName).getValue(String.class);
        String chatPictureUrl = iDataSnapShot.child(eChatRoomDataTypes.CHAT_PICTURE_URL.mTypeName).getValue(String.class);
        String chatRoomId = iDataSnapShot.child(eChatRoomDataTypes.CHAT_ROOM_ID.mTypeName).getValue(String.class);
        String itemId = iDataSnapShot.child(eChatRoomDataTypes.ITEM_ID.mTypeName).getValue(String.class);
        String ownerId = iDataSnapShot.child(eChatRoomDataTypes.OWNER_ID.mTypeName).getValue(String.class);
        String receiverId = iDataSnapShot.child(eChatRoomDataTypes.RECEIVER_ID.mTypeName).getValue(String.class);
        Long updateTimeInMillis = iDataSnapShot.child(eChatRoomDataTypes.UPDATED_TIME_IN_MILLIS.mTypeName).getValue(Long.class);
        ArrayList<ChatMessage> messages = new ArrayList<>();

        for (DataSnapshot ds : iDataSnapShot.child(eChatRoomDataTypes.CHAT_MESSAGES.mTypeName).getChildren())
        {
            messages.add(ds.getValue(ChatMessage.class));
        }

        convertedChatRoom = new ChatRoom(ownerId,itemId,receiverId,chatPictureUrl,chatName,messages);
        convertedChatRoom.setUpdatedTimeInMillis(updateTimeInMillis);
        convertedChatRoom.setChatRoomId(chatRoomId);

        return convertedChatRoom;
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

    public static void closeRepository()
    {
        if(mChatRepository != null)
        {
            mChatRepository.removeChatRoomListener();
        }

        mChatRepository = null;
    }
}
