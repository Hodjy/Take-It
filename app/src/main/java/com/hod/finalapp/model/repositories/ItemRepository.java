package com.hod.finalapp.model.repositories;

import android.app.Activity;

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
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class ItemRepository {

    private static ItemRepository mItemRepository;
    private final DatabaseReference mItemTable;

    private OnCompleteListener mItemsListChangedListener;

    private ArrayList<Item> mItemsList;

    private ItemRepository()
    {
        mItemTable = DatabaseManager.getInstance().getFirebaseDatabaseInstance()
                .getReference(eFirebaseDataTypes.ITEMS.mTypeName);
        mItemsList = new ArrayList<>();
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


    public void initItemsListInfo(OnCompleteListener iOnCompleteListener)
    {
        mItemsListChangedListener = iOnCompleteListener;
        mItemTable.addValueEventListener(getItemsListListener());
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

                    if(mItemsListChangedListener != null)
                    {
                        mItemsListChangedListener.onComplete(new Task() {
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
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void uploadNewItem(Item iItem)
    {
        String id = mItemTable.push().getKey();
        iItem.setItemId(id);
        mItemTable.child(id).setValue(iItem);
    }

}
