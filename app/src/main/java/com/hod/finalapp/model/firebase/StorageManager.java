package com.hod.finalapp.model.firebase;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.enums.eStorageFolders;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class StorageManager
{
    public final static int MAX_ITEM_PICTURES = 4;
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

    public void uploadItemPicture(ArrayList<Uri> iImageUris, String iItemID,ArrayList<Uri> iStoredImagesUri, OnCompleteListener<ArrayList<Uri>> urlListener)
    {
        if(iStoredImagesUri.size() < iImageUris.size())
        {
            int index = iStoredImagesUri.size();
            Uri uri = iImageUris.get(index);

            uploadItemPicture(uri, iItemID, new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                iStoredImagesUri.add(task.getResult());
                                uploadItemPicture(iImageUris, iItemID, iStoredImagesUri, urlListener);
                            }
                            else
                            {
                                urlListener.onComplete(new Task<ArrayList<Uri>>() {
                                    @Override
                                    public boolean isComplete() {
                                        return false;
                                    }

                                    @Override
                                    public boolean isSuccessful() {
                                        return false;
                                    }

                                    @Override
                                    public boolean isCanceled() {
                                        return false;
                                    }

                                    @Nullable
                                    @org.jetbrains.annotations.Nullable
                                    @Override
                                    public ArrayList<Uri> getResult() {
                                        return null;
                                    }

                                    @Nullable
                                    @org.jetbrains.annotations.Nullable
                                    @Override
                                    public <X extends Throwable> ArrayList<Uri> getResult(@NonNull @NotNull Class<X> aClass) throws X {
                                        return null;
                                    }

                                    @Nullable
                                    @org.jetbrains.annotations.Nullable
                                    @Override
                                    public Exception getException() {
                                        return task.getException();
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public Task<ArrayList<Uri>> addOnSuccessListener(@NonNull @NotNull OnSuccessListener<? super ArrayList<Uri>> onSuccessListener) {
                                        return null;
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public Task<ArrayList<Uri>> addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener<? super ArrayList<Uri>> onSuccessListener) {
                                        return null;
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public Task<ArrayList<Uri>> addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener<? super ArrayList<Uri>> onSuccessListener) {
                                        return null;
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public Task<ArrayList<Uri>> addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                                        return null;
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public Task<ArrayList<Uri>> addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                                        return null;
                                    }

                                    @NonNull
                                    @NotNull
                                    @Override
                                    public Task<ArrayList<Uri>> addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                                        return null;
                                    }
                                });
                            }
                        }
                    },
                    index);
        }
        else{
            urlListener.onComplete(new Task<ArrayList<Uri>>() {
                @Override
                public boolean isComplete() {
                    return true;
                }

                @Override
                public boolean isSuccessful() {
                    return true;
                }

                @Override
                public boolean isCanceled() {
                    return false;
                }

                @Nullable
                @org.jetbrains.annotations.Nullable
                @Override
                public ArrayList<Uri> getResult() {
                    return iStoredImagesUri;
                }

                @Nullable
                @org.jetbrains.annotations.Nullable
                @Override
                public <X extends Throwable> ArrayList<Uri> getResult(@NonNull @NotNull Class<X> aClass) throws X {
                    return iStoredImagesUri;
                }

                @Nullable
                @org.jetbrains.annotations.Nullable
                @Override
                public Exception getException() {
                    return null;
                }

                @NonNull
                @NotNull
                @Override
                public Task<ArrayList<Uri>> addOnSuccessListener(@NonNull @NotNull OnSuccessListener<? super ArrayList<Uri>> onSuccessListener) {
                    return null;
                }

                @NonNull
                @NotNull
                @Override
                public Task<ArrayList<Uri>> addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener<? super ArrayList<Uri>> onSuccessListener) {
                    return null;
                }

                @NonNull
                @NotNull
                @Override
                public Task<ArrayList<Uri>> addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener<? super ArrayList<Uri>> onSuccessListener) {
                    return null;
                }

                @NonNull
                @NotNull
                @Override
                public Task<ArrayList<Uri>> addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                    return null;
                }

                @NonNull
                @NotNull
                @Override
                public Task<ArrayList<Uri>> addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                    return null;
                }

                @NonNull
                @NotNull
                @Override
                public Task<ArrayList<Uri>> addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                    return null;
                }
            });
        }
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

    public void deleteItemPictures(Item iItem){
        StorageReference tempRef;
        ArrayList<String> itemsPictureUrls = iItem.getPicturesUrls();
        for(String url : itemsPictureUrls)
        {
            if(!url.equals(""))
            {
            mFirebaseStorage.getReferenceFromUrl(url).delete();
            }
        }
    }
}
