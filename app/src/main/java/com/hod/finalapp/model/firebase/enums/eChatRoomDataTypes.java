package com.hod.finalapp.model.firebase.enums;

/**
 * Names for the chatroom holders.
 */
public enum eChatRoomDataTypes
{
    CHAT_MESSAGES("chatMessages"),
    CHAT_NAME("chatName"),
    CHAT_PICTURE_URL("chatPictureUrl"),
    CHAT_ROOM_ID("chatRoomId"),
    ITEM_ID("itemId"),
    OWNER_ID("ownerId"),
    RECEIVER_ID("receiverId"),
    UPDATED_TIME_IN_MILLIS("updatedTimeInMillis");


    public final String mTypeName;
    private eChatRoomDataTypes(String iName)
    {
        mTypeName = iName;
    }
}
