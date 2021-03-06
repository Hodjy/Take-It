package com.hod.finalapp.model.database_objects.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable
{
    private String mMessageText;
    private String mSenderUserId;
    private String mReceiverUserId;
    private String mMessagePictureUrl;
    private long mMessageSentTime;

    public ChatMessage()
    {
        mMessageText = null;
        mSenderUserId = null;
        mReceiverUserId = null;
        mMessagePictureUrl = null;
        mMessageSentTime = 0;
    }

    public ChatMessage(String iMessageText, String iSenderUserId, String iReceiverUserId, String iMessagePictureUrl, long iMessageSentTime)
    {
        mMessageText = iMessageText;
        mSenderUserId = iSenderUserId;
        mReceiverUserId = iReceiverUserId;
        mMessagePictureUrl = iMessagePictureUrl;
        mMessageSentTime = iMessageSentTime;
    }

    public long getMessageSentTime() {
        return mMessageSentTime;
    }

    public void setMessageSentTime(long mMessageSentTime) {
        this.mMessageSentTime = mMessageSentTime;
    }

    public String getMessageText() {
        return mMessageText;
    }

    public void setMessageText(String iMessageText) {
        mMessageText = iMessageText;
    }

    public String getSenderUserId() {
        return mSenderUserId;
    }

    public void setSenderUserId(String iSenderUserId) {
        mSenderUserId = iSenderUserId;
    }

    public String getReceiverUserId() {
        return mReceiverUserId;
    }

    public void setReceiverUserId(String iReceiverUserId) {
        mReceiverUserId = iReceiverUserId;
    }

    public String getMessagePictureUrl() {
        return mMessagePictureUrl;
    }

    public void setMessagePictureUrl(String iMessagePictureUrl) {
        mMessagePictureUrl = iMessagePictureUrl;
    }

    protected ChatMessage(Parcel in) {
        mMessageText= in.readString();
        mSenderUserId = in.readString();
        mReceiverUserId = in.readString();
        mMessagePictureUrl = in.readString();
        mMessageSentTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessageText);
        dest.writeString(mSenderUserId);
        dest.writeString(mReceiverUserId);
        dest.writeString(mMessagePictureUrl);
        dest.writeLong(mMessageSentTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };
}
