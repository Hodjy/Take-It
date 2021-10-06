package com.hod.finalapp.view.viewmodel.item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemDescriptionViewModel extends AndroidViewModel {

    private Item mItem;

    public ItemDescriptionViewModel(@NonNull @NotNull Application application) {
        super(application);
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
        String[] regions = getApplication().getResources().getStringArray(R.array.regions);
        String itemRegion = regions[mItem.getmItemRegion()];

        return itemRegion;
    }

    public String generateChatRoomId()
    {
        String chatRoomID = ChatRoom.generateChatRoomId(mItem.getOwnerId(),mItem.getItemId(),UserRepository.getInstance().getCurrentUser().getUserId());

        return chatRoomID;
    }

}
