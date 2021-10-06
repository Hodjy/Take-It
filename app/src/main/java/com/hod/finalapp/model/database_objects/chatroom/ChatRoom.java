package com.hod.finalapp.model.database_objects.chatroom;

import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class ChatRoom implements Parcelable, Comparable<ChatRoom>
{
    private String mChatRoomId;
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
        mChatRoomId = null;
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
        generateChatRoomId();
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

    public String getChatRoomId() {
        return mChatRoomId;
    }

    public void setChatRoomId(String mChatRoomId) {
        this.mChatRoomId = mChatRoomId;
    }

    protected ChatRoom(Parcel in) {
        mOwnerId = in.readString();
        mItemId = in.readString();
        mReceiverId = in.readString();
        mChatPictureUrl = in.readString();
        mChatName = in.readString();
        mChatMessages = in.createTypedArrayList(ChatMessage.CREATOR);
        mUpdatedTimeInMillis = in.readLong();
        mChatRoomId = in.readString();
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
        dest.writeString(mChatRoomId);
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

    private void generateChatRoomId()
    {
        mChatRoomId = mOwnerId + "_" + mItemId + "_" + mReceiverId;
    }

    /***
     * Use to generate the key if needed to find a spesific room.
     * @param iOwnerId
     * @param iItemId
     * @param iReceiver
     * @return
     */
    public static String generateChatRoomId(String iOwnerId, String iItemId,String iReceiver)
    {
        return iOwnerId + "_" + iItemId + "_" + iReceiver;
    }

    /***
     * add a message to the list and update the "updated time"
     * @param iChatMessage
     */
    public void addMessage(ChatMessage iChatMessage)
    {
        mChatMessages.add(iChatMessage);
        mUpdatedTimeInMillis = GregorianCalendar.getInstance().getTimeInMillis();
    }

    @Override
    public int compareTo(ChatRoom o) {
        return Comparators.UpdatedTime.compare(this, o);
    }

    public static class Comparators {

        public static Comparator<ChatRoom> UpdatedTime = new Comparator<ChatRoom>() {
            @Override
            public int compare(ChatRoom o1, ChatRoom o2) {
                int i;
                if((o1.getUpdatedTimeInMillis() - o2.getUpdatedTimeInMillis() ) > 0)
                    i = 1;
                else
                    i = -1;
                return i;
            }
        };
    }

}


