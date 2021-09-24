package com.hod.finalapp.model.database_objects;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{


    private String mUsername;
    private String mUserId;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private String mToken;
    private String mPictureUrl;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String iUsername) {
        mUsername = iUsername;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String iUserId) {
        mUserId = iUserId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String iFirstName) {
        mFirstName = iFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String iLastName) {
        mLastName = iLastName;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String iToken) {
        mToken = iToken;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String iPictureUrl) {
        mPictureUrl = iPictureUrl;
    }
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String iPassword) {
        mPassword = iPassword;
    }

    public User(){}

    public User(String iUsername,String iPassword, String iUserId,
                String iFirstName, String iLastName,String iPictureUrl)
    {
        mUsername = iUsername;
        mPassword = iPassword;
        mUserId = iUserId;
        mFirstName = iFirstName;
        mLastName = iLastName;
        mPictureUrl = iPictureUrl;
    }

    protected User(Parcel in) {
        mUsername = in.readString();
        mPassword = in.readString();
        mUserId = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mToken = in.readString();
        mPictureUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUsername);
        dest.writeString(mPassword);
        dest.writeString(mUserId);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mToken);
        dest.writeString(mPictureUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
