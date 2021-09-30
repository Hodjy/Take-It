package com.hod.finalapp.model.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private static ItemRepository mItemRepository;
    private final DatabaseReference mItemTable;

    private ArrayList<Item> mItemsList;

    private ItemRepository()
    {
        mItemTable = DatabaseManager.getInstance().getFirebaseDatabaseInstance()
                .getReference(eFirebaseDataTypes.ITEMS.mTypeName);

        mItemTable.addValueEventListener(getItemsListListener());
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

}
