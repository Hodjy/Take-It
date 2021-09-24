package com.hod.finalapp.model.database_objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable
{
    private String mItemId;
    private String mOwnerId;
    private String mItemName;
    private String mItemDescription;
    private String mLocation; //might be enum, if not then maybe coords?
    private String mLastUpdated;
    // private enum(spesific type) mCategory
    // private enum(spesific type) mSubCategory

    public Item(String iItemId, String iOwnerId, String iItemName, String iItemDescription,
                String iLocation, String iLastUpdated)
    {
        mItemId = iItemId;
        mOwnerId = iOwnerId;
        mItemName = iItemName;
        mItemDescription = iItemDescription;
        mLocation = iLocation;
        mLastUpdated = iLastUpdated;
    }

    protected Item(Parcel in) {
        mItemId = in.readString();
        mOwnerId = in.readString();
        mItemName = in.readString();
        mItemDescription = in.readString();
        mLocation = in.readString();
        mLastUpdated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItemId);
        dest.writeString(mOwnerId);
        dest.writeString(mItemName);
        dest.writeString(mItemDescription);
        dest.writeString(mLocation);
        dest.writeString(mLastUpdated);
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
