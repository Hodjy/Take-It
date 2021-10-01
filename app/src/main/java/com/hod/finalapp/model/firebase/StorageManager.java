package com.hod.finalapp.model.firebase;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hod.finalapp.model.firebase.enums.eStorageFolders;

public class StorageManager
{
    private final int MAX_ITEM_PICTURES = 4;
    private static StorageManager mStorageManager;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageRef;

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

    public void uploadUserProfilePicture(Uri imageUri, String CurrentUserID,
                                         OnCompleteListener<Uri> urlListener){
        mStorageRef = mFirebaseStorage.getReference(eStorageFolders.USERS_PROFILE_PICTURES.name);
        String path = CurrentUserID + ".png";

        uploadPicture(imageUri, path, urlListener);
    }

    public void uploadItemPicture(Uri imageUri, String itemID, OnCompleteListener<Uri> urlListener, int iPhotoIndex){
        mStorageRef = mFirebaseStorage.getReference(eStorageFolders.ITEMS_PICTURES.name).child(itemID);
        //String path = itemID + "_" + UUID.randomUUID() + ".png"; OLD
        if(iPhotoIndex >= MAX_ITEM_PICTURES || iPhotoIndex < 0)
        {
            throw new IndexOutOfBoundsException("index out of bound, max pictures per items are:" + MAX_ITEM_PICTURES);
        }

        String path = iPhotoIndex + ".png";
        uploadPicture(imageUri, path, urlListener);

    }

    private void uploadPicture(Uri imageUri, String path, OnCompleteListener<Uri> urlListener){
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/png")
                .build();

        UploadTask uploadTask = mStorageRef.child(path).putFile(imageUri, metadata);

        setUriListener(uploadTask, path, urlListener);
    }


    private void setUriListener(UploadTask uploadTask, String path, OnCompleteListener<Uri> urlListener) {
        Task<Uri> uri = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful())// can't get the link
                    return null;
                return mStorageRef.child(path).getDownloadUrl();
            }
        });

        uri.addOnCompleteListener(urlListener);
    }

    //TODO PROGRESSBAR LISTENER
}
