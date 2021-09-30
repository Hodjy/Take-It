package com.hod.finalapp.model.database_objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item implements Parcelable
{
    private String mItemId;
    private String mOwnerId;
    private String mItemName;
    private String mItemDescription;
    private String mLocation; //might be enum, if not then maybe coords?
    private String mLastUpdated;
    private ArrayList<String> mPicturesUrls;

    // private enum(spesific type) mCategory
    // private enum(spesific type) mSubCategory


    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String iItemId) {
        mItemId = iItemId;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String iOwnerId) {
        mOwnerId = iOwnerId;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String iItemName) {
        mItemName = iItemName;
    }

    public String getItemDescription() {
        return mItemDescription;
    }

    public void setItemDescription(String iItemDescription) {
        mItemDescription = iItemDescription;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String iLocation) {
        mLocation = iLocation;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String iLastUpdated) {
        mLastUpdated = iLastUpdated;
    }

    public ArrayList<String> getPicturesUrls() {
        return mPicturesUrls;
    }

    public void setPicturesUrls(ArrayList<String> iPicturesUrls) {
        mPicturesUrls = iPicturesUrls;
    }

    public Item(String iItemId, String iOwnerId, String iItemName, String iItemDescription,
                String iLocation, String iLastUpdated, ArrayList<String> iPicturesUrls)
    {
        mItemId = iItemId;
        mOwnerId = iOwnerId;
        mItemName = iItemName;
        mItemDescription = iItemDescription;
        mLocation = iLocation;
        mLastUpdated = iLastUpdated;
        mPicturesUrls = iPicturesUrls;
    }

    protected Item(Parcel in) {
        mItemId = in.readString();
        mOwnerId = in.readString();
        mItemName = in.readString();
        mItemDescription = in.readString();
        mLocation = in.readString();
        mLastUpdated = in.readString();
        mPicturesUrls = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItemId);
        dest.writeString(mOwnerId);
        dest.writeString(mItemName);
        dest.writeString(mItemDescription);
        dest.writeString(mLocation);
        dest.writeString(mLastUpdated);
        dest.writeStringList(mPicturesUrls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
