package com.hod.finalapp.model.database_objects.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable
{
    private String mText;
    private String mSenderUserId;
    private String mReceiverUserId;

    public ChatMessage(String iText, String iSenderUserId, String iReceiverUserId)
    {
        mText = iText;
        mSenderUserId = iSenderUserId;
        mReceiverUserId = iReceiverUserId;
    }

    protected ChatMessage(Parcel in) {
        mText = in.readString();
        mSenderUserId = in.readString();
        mReceiverUserId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
        dest.writeString(mSenderUserId);
        dest.writeString(mReceiverUserId);
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
