package com.hod.finalapp.model.firebase.enums;

/**
 * Names for the main data holders.
 */
public enum eFirebaseDataTypes
{
    USERS("Users"),
    ITEMS("Items"),
    CHATROOMS("ChatRooms");

    public final String mTypeName;
    private eFirebaseDataTypes(String iName)
    {
        mTypeName = iName;
    }


}
