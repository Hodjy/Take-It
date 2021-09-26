package com.hod.finalapp.model.firebase;

import com.google.firebase.storage.FirebaseStorage;

public class StorageManager
{
    private static StorageManager mStorageManager;
    private FirebaseStorage mFirebaseStorage;

    private StorageManager()
    {
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    public static StorageManager getInstance()
    {
        if(mStorageManager == null)
        {
            mStorageManager = new StorageManager();
        }

        return mStorageManager;
    }

    public FirebaseStorage getStorage()
    {
        return mFirebaseStorage;
    }
}
