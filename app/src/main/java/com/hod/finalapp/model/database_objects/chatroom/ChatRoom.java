package com.hod.finalapp.model.database_objects.chatroom;

import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ChatRoom implements Parcelable
{
    private String mOwnerId;
    private String mItemId;
    private String mReceiverId;
    private String mChatName;
    private String mChatPictureUrl;
    private ArrayList<ChatMessage> mChatMessages;
    private long mUpdatedTimeInMillis;

    public ChatRoom()
    {
        mOwnerId = null;
        mItemId = null;
        mReceiverId = null;
        mChatPictureUrl = null;
        mChatName = null;
        mChatMessages = null;
        mUpdatedTimeInMillis = 0;
    }

    public ChatRoom(String iOwnerId, String iItemId,
                    String iReceiverId, String iChatPictureUrl,
                    String iChatName, ArrayList<ChatMessage> iChatMessages)
    {
        mOwnerId = iOwnerId;
        mItemId = iItemId;
        mReceiverId = iReceiverId;
        mChatPictureUrl = iChatPictureUrl;
        mChatName = iChatName;
        mChatMessages = iChatMessages;
        mUpdatedTimeInMillis = GregorianCalendar.getInstance().getTimeInMillis();
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String iOwnerId) {
        mOwnerId = iOwnerId;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String iItemId) {
        mItemId = iItemId;
    }

    public String getReceiverId() {
        return mReceiverId;
    }

    public void setReceiverId(String iReceiverId) {
        mReceiverId = iReceiverId;
    }

    public String getChatName() {
        return mChatName;
    }

    public void setChatName(String iChatName) {
        mChatName = iChatName;
    }

    public String getChatPictureUrl() {
        return mChatPictureUrl;
    }

    public void setChatPictureUrl(String iChatPictureUrl) {
        mChatPictureUrl = iChatPictureUrl;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        ArrayList<ChatMessage> array = mChatMessages;
        if(array == null)
        {
            array = new ArrayList<>();
        }

        return array;
    }

    public void setChatMessages(ArrayList<ChatMessage> iChatMessages) {
        mChatMessages = iChatMessages;
    }

    public long getUpdatedTimeInMillis() {
        return mUpdatedTimeInMillis;
    }

    public void setUpdatedTimeInMillis(long mUpdatedTimeInMillis) {
        this.mUpdatedTimeInMillis = mUpdatedTimeInMillis;
    }

    protected ChatRoom(Parcel in) {
        mOwnerId = in.readString();
        mItemId = in.readString();
        mReceiverId = in.readString();
        mChatPictureUrl = in.readString();
        mChatName = in.readString();
        mChatMessages = in.createTypedArrayList(ChatMessage.CREATOR);
        mUpdatedTimeInMillis = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOwnerId);
        dest.writeString(mItemId);
        dest.writeString(mReceiverId);
        dest.writeString(mChatPictureUrl);
        dest.writeString(mChatName);
        dest.writeTypedList(mChatMessages);
        dest.writeLong(mUpdatedTimeInMillis);
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

    public String generateChatRoomId()
    {
        return mOwnerId + "_" + mItemId + "_" + mReceiverId;
    }
}


