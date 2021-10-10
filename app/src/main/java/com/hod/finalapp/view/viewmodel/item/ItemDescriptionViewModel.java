package com.hod.finalapp.view.viewmodel.item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.firebase.enums.eUserDataTypes;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemDescriptionViewModel extends AndroidViewModel {

    private Item mItem;
    private MutableLiveData<String> mOwnerName;

    public ItemDescriptionViewModel(@NonNull @NotNull Application application)
    {
        super(application);
        mOwnerName = new MutableLiveData<>();
    }

    public void setItem(Item iItem)
    {
        mItem = iItem;
    }

    public Item getItem()
    {
        return mItem;
    }

    public boolean isMyItem(){
        return UserRepository.getInstance().getCurrentUser().getUserId().equals(mItem.getOwnerId());
    }

    public String getItemName(){
        return mItem.getItemName();
    }

    public String getItemDescription(){
        return mItem.getItemDescription();
    }

    public ArrayList<String> getItemPicturesList(){
        return mItem.getPicturesUrls();
    }

    public void deleteThisItem(){
        ItemRepository.getInstance().deleteItem(mItem);
    }

    public String getItemRegion()
    {
        return mItem.getItemRegion();
    }

    public String generateChatRoomId()
    {
        String chatRoomID = ChatRoom.generateChatRoomId(mItem.getOwnerId(),mItem.getItemId(),UserRepository.getInstance().getCurrentUser().getUserId());

        return chatRoomID;
    }

    public void loadOwnerName()
    {
        UserRepository.getInstance().getUserById(mItem.getOwnerId(), new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                   String ownerName = task.getResult().child(eUserDataTypes.FIRST_NAME.name).getValue(String.class);
                   mOwnerName.postValue(ownerName);
                }
                else
                {
                    mOwnerName.postValue("");
                }
            }
        });
    }

    public MutableLiveData<String> getOwnerName()
    {
        return mOwnerName;
    }

}
