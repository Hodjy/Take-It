package com.hod.finalapp.model.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager
{
    private static DatabaseManager mDatabaseManager;
    private static FirebaseDatabase mFirebaseDatabase;

    private DatabaseManager()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://socialapp-67e7d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public static DatabaseManager getInstance()
    {
        if(mDatabaseManager == null)
        {
            mDatabaseManager = new DatabaseManager();
        }

        return  mDatabaseManager;
    }

    public FirebaseDatabase getFirebaseDatabaseInstance()
    {
        return mFirebaseDatabase;
    }

}
