package com.hod.finalapp.model.database_objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item implements Parcelable
{
    private int mItemCategory; //category are sorted by the array in the strings_array_category xml.
    private String mItemId;
    private String mOwnerId;
    private String mItemName;
    private String mItemDescription;
    private long mUploadedTime;
    private ArrayList<String> mPicturesUrls;

    private double mItemLatitude;
    private double mItemLongitude;

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

    public long getUploadedTime() {
        return mUploadedTime;
    }

    public void setUploadedTime(long iUploadedTime) {
        mUploadedTime = iUploadedTime;
    }

    public int getmItemCategory() {
        return mItemCategory;
    }

    public void setItemCategory(int iItemCategory) {
        mItemCategory = iItemCategory;
    }

    public double getItemLatitude() {
        return mItemLatitude;
    }

    public void setItemLatitude(double iItemLatitude) {
        mItemLatitude = iItemLatitude;
    }

    public double getItemLongitude() {
        return mItemLongitude;
    }

    public void setItemLongitude(double iItemLongitude) {
        mItemLongitude = iItemLongitude;
    }

    public ArrayList<String> getPicturesUrls() {
        ArrayList<String> array = mPicturesUrls;
        if(array == null)
        {
            array = new ArrayList<>();
            array.add("");
        }

        return array;
    }

    public void setPicturesUrls(ArrayList<String> iPicturesUrls) {
        mPicturesUrls = iPicturesUrls;
    }

    public Item()
    {
        mItemCategory = 0;
        mItemLatitude = 0.0;
        mItemLongitude = 0.0;
        mItemId = null;
        mOwnerId = null;
        mItemName = null;
        mItemDescription = null;
        mUploadedTime = 0;
        mPicturesUrls = null;
    }

    public Item(String iOwnerId, String iItemName, String iItemDescription,
                int iCategory, long iUploadedTime, ArrayList<String> iPicturesUrls,
                double iItemLatitude, double iItemLongitude)
    {
        mItemId = null;
        mOwnerId = iOwnerId;
        mItemName = iItemName;
        mItemDescription = iItemDescription;
        mUploadedTime = iUploadedTime;
        mPicturesUrls = iPicturesUrls;
        mItemLatitude = iItemLatitude;
        mItemLongitude = iItemLongitude;
        mItemCategory = iCategory;
    }

    protected Item(Parcel in) {
        mItemCategory = in.readInt();
        mItemLatitude = in.readDouble();
        mItemLongitude = in.readDouble();
        mItemId = in.readString();
        mOwnerId = in.readString();
        mItemName = in.readString();
        mItemDescription = in.readString();
        mUploadedTime = in.readLong();
        mPicturesUrls = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mItemCategory);
        dest.writeDouble(mItemLatitude);
        dest.writeDouble(mItemLongitude);
        dest.writeString(mItemId);
        dest.writeString(mOwnerId);
        dest.writeString(mItemName);
        dest.writeString(mItemDescription);
        dest.writeLong(mUploadedTime);
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
