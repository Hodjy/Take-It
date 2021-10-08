package com.hod.finalapp.view.viewmodel.chat;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.hod.finalapp.MainActivity;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatMessage;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.repositories.ChatRepository;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.ApplicationContext;
import com.hod.finalapp.view.adapters.MessageAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRoomViewModel extends ViewModel
{
    private final String API_TOKEN_KEY = "AAAA2jIyTSA:APA91bGa57OfRR-a95imv4hvrMj_uwOMJpL-QP8Dnk7-5QUELDSRrSS6pVm8Vc7iloAcoBCkDVSWTHLRLwqr0WsVD108LtuX0hbnC01sTCv1fhei-YDIGMRTK-v_QtwOpzglOyhOlPTQ";

    private String mOtherUserId = "";
    private String mCurrentUserId = "";
    private String mOtherUserToken= "";
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
        UserRepository.getInstance().getUserTokenByUseId(mOtherUserId, onCompleteGetToken());
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
                ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                temp.add(chatMessage);
                mChatMessages.postValue(temp);
                sendNotification(chatMessage);
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

    private void testNotif(ChatMessage iChatMessage)
    {

    }

    private void sendNotification(ChatMessage iChatMessage){
        String textToSend = UserRepository.getInstance().getCurrentUser().getFirstName() +
                " " +
                UserRepository.getInstance().getCurrentUser().getLastName() + ": " +
                iChatMessage.getMessageText();

        String titleToSend = mCurrentChatRoom.getChatName();
        final JSONObject rootObject  = new JSONObject();
        try {
            //TODO CHECK IF BREAKS ON LOGGED OFF NO TOKEN USERS
            String tokenToSend = "/token/" + mOtherUserToken;


            JSONObject notificationJObject = new JSONObject();
            notificationJObject.put("title",titleToSend);
            notificationJObject.put("body",textToSend);

            rootObject.put("to", tokenToSend);
            rootObject.put("notification", notificationJObject);



            String url = "https://fcm.googleapis.com/fcm/send";

            RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(
                        VolleyError error) {

                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization","key="+API_TOKEN_KEY);
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return rootObject.toString().getBytes();
                }
            };
            queue.add(request);
            queue.start();


        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private OnCompleteListener onCompleteGetToken() {
        return new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = (DataSnapshot) task.getResult();
                    String token = dataSnapshot.getValue(String.class);

                    mOtherUserToken = token;

                }
            }
        };
    }

}
