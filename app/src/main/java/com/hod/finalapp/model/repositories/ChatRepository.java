package com.hod.finalapp.model.repositories;

import com.google.firebase.database.DatabaseReference;
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

public class ChatRepository {

    private static ChatRepository mChatRepository;
    private final DatabaseReference mChatTable;

    private ChatRepository()
    {
        mChatTable = DatabaseManager.getInstance().getFirebaseDatabaseInstance()
                .getReference(eFirebaseDataTypes.CHATROOMS.mTypeName);
    }

    public static ChatRepository getInstance()
    {
        if(mChatRepository == null)
        {
            mChatRepository = new ChatRepository();
        }

        return  mChatRepository;
    }

    public void createNewChat(){

    }

}
