package com.hod.finalapp.model.database_objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item implements Parcelable
{
    private int mItemCategory; //category are sorted by the array in the strings_array_category xml.
    private int mItemRegion; // region are sorted by the array in the strings_array_region xml.
    private String mItemId;
    private String mOwnerId;
    private String mItemName;
    private String mItemDescription;
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

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String iLastUpdated) {
        mLastUpdated = iLastUpdated;
    }

    public int getmItemCategory() {
        return mItemCategory;
    }

    public void setmItemCategory(int mItemCategory) {
        this.mItemCategory = mItemCategory;
    }

    public int getmItemRegion() {
        return mItemRegion;
    }

    public void setmItemRegion(int mItemRegion) {
        this.mItemRegion = mItemRegion;
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
        mItemRegion = 0;
        mItemId = null;
        mOwnerId = null;
        mItemName = null;
        mItemDescription = null;
        mLastUpdated = null;
        mPicturesUrls = null;
    }

    public Item(String iOwnerId, String iItemName, String iItemDescription,
                int iRegion, int iCategory, String iLastUpdated, ArrayList<String> iPicturesUrls)
    {
        mItemId = null;
        mOwnerId = iOwnerId;
        mItemName = iItemName;
        mItemDescription = iItemDescription;
        mLastUpdated = iLastUpdated;
        mPicturesUrls = iPicturesUrls;
        mItemRegion = iRegion;
        mItemCategory = iCategory;
    }

    protected Item(Parcel in) {
        mItemCategory = in.readInt();
        mItemRegion = in.readInt();
        mItemId = in.readString();
        mOwnerId = in.readString();
        mItemName = in.readString();
        mItemDescription = in.readString();
        mLastUpdated = in.readString();
        mPicturesUrls = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mItemCategory);
        dest.writeInt(mItemRegion);
        dest.writeString(mItemId);
        dest.writeString(mOwnerId);
        dest.writeString(mItemName);
        dest.writeString(mItemDescription);
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
