package com.hod.finalapp.model.database_objects.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ChatRoom implements Parcelable
{
    private String mOwnerId;
    private String mItemId;
    private String mReceiverId;
    private ArrayList<ChatMessage> mChatMessages;

    public ChatRoom(String iOwnerId, String iItemId,
                    String iReceiverId, ArrayList<ChatMessage> iChatMessages)
    {
        mOwnerId = iOwnerId;
        mItemId = iItemId;
        mReceiverId = iReceiverId;
        mChatMessages = iChatMessages;
    }

    protected ChatRoom(Parcel in) {
        mOwnerId = in.readString();
        mItemId = in.readString();
        mReceiverId = in.readString();
        mChatMessages = in.createTypedArrayList(ChatMessage.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOwnerId);
        dest.writeString(mItemId);
        dest.writeString(mReceiverId);
        dest.writeTypedList(mChatMessages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatRoom> CREATOR = new Creator<ChatRoom>() {
        @Override
        public ChatRoom createFromParcel(Parcel in) {
            return new ChatRoom(in);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            return new ChatRoom[size];
        }
    };
}


