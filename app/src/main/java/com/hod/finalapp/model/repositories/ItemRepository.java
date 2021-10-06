package com.hod.finalapp.model.repositories;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;

public class ItemRepository {

    private static ItemRepository mItemRepository;
    private final DatabaseReference mItemTable;

    private ArrayList<OnCompleteListener> mItemsListChangedListeners;

    private ArrayList<Item> mItemsList;

    private ItemRepository()
    {
        mItemTable = DatabaseManager.getInstance().getFirebaseDatabaseInstance()
                .getReference(eFirebaseDataTypes.ITEMS.mTypeName);
        mItemsList = new ArrayList<>();
        mItemsListChangedListeners = new ArrayList<>();
    }

    public static ItemRepository getInstance()
    {
        if(mItemRepository == null)
        {
            mItemRepository = new ItemRepository();
        }

        return  mItemRepository;
    }

    public ArrayList<Item> getItemsList() {
        return mItemsList;
    }


    public void initItemsListInfo()
    {
        mItemTable.addValueEventListener(getItemsListListener());
    }

    public void subscribeToItemListChanged(OnCompleteListener iOnCompleteListener)
    {
        mItemsListChangedListeners.add(iOnCompleteListener);
    }

    private ValueEventListener getItemsListListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Item item;
                    mItemsList.clear();
                    for(DataSnapshot itemData: snapshot.getChildren()){
                        item = itemData.getValue(Item.class);
                        mItemsList.add(item);
                    }

                    Collections.reverse(mItemsList);

                    if(mItemsListChangedListeners.size() > 0)
                    {
                        Task task = new Task() {
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
                            public Object getResult() {
                                return mItemsList;
                            }

                            @Nullable
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object getResult(@NonNull @NotNull Class aClass) throws Throwable {
                                return mItemsList;
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
                            public Task addOnSuccessListener(@NonNull @NotNull OnSuccessListener onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                                return null;
                            }
                        };
                        for(OnCompleteListener listener : mItemsListChangedListeners)
                        {
                            listener.onComplete(task);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void uploadNewItem(Item iItem, ArrayList<Uri> iItemPhotoUris, OnCompleteListener iFinishedUploadListener)
    {
        String id = mItemTable.push().getKey();
        iItem.setItemId(id);

        StorageManager.getInstance().uploadItemPicture(iItemPhotoUris,
                iItem.getItemId(), new ArrayList<>(),
                getMultiphotoUploadListener(iItem, iFinishedUploadListener));
    }

    private OnCompleteListener<ArrayList<Uri>> getMultiphotoUploadListener(Item iItem, OnCompleteListener iFinishedUploadListener)
    {
        return new OnCompleteListener<ArrayList<Uri>>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<ArrayList<Uri>> task)
            {
                if(task.isSuccessful())
                {
                    ArrayList<String> uriStrings = new ArrayList<>();
                    ArrayList<Uri> receivedUris = task.getResult();

                    for (Uri uri : receivedUris)
                    {
                        uriStrings.add(uri.toString());
                    }

                    iItem.setPicturesUrls(uriStrings);
                    mItemTable.child(iItem.getItemId()).setValue(iItem);

                    iFinishedUploadListener.onComplete(new Task() {
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
                        public Object getResult() {
                            return null;
                        }

                        @Nullable
                        @org.jetbrains.annotations.Nullable
                        @Override
                        public Object getResult(@NonNull @NotNull Class aClass) throws Throwable {
                            return null;
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
                        public Task addOnSuccessListener(@NonNull @NotNull OnSuccessListener onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                            return null;
                        }
                    });
                }
                else
                {
                    mItemTable.child(iItem.getItemId()).setValue(null);
                    iFinishedUploadListener.onComplete(new Task() {
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
                        public Object getResult() {
                            return null;
                        }

                        @Nullable
                        @org.jetbrains.annotations.Nullable
                        @Override
                        public Object getResult(@NonNull @NotNull Class aClass) throws Throwable {
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
                        public Task addOnSuccessListener(@NonNull @NotNull OnSuccessListener onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                            return null;
                        }

                        @NonNull
                        @NotNull
                        @Override
                        public Task addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                            return null;
                        }
                    });
                }
            }
        };
    }

    public void deleteItem(@NonNull Item iItem)
    {
        StorageManager.getInstance().deleteItemPictures(iItem);
        mItemTable.child(iItem.getItemId()).removeValue();
    }

    public ArrayList<Item> getAllItems()
    {
        return mItemsList;
    }

}
