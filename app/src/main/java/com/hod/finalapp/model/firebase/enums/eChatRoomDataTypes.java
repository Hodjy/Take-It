package com.hod.finalapp.model.firebase.enums;

/**
 * Names for the chatroom holders.
 */
public enum eChatRoomDataTypes
{
    CHAT_MESSAGES("chatMessages");

    public final String mTypeName;
    private eChatRoomDataTypes(String iName)
    {
        mTypeName = iName;
    }
}
