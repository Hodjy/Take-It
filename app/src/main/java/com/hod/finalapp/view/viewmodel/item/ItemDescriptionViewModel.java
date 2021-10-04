package com.hod.finalapp.view.viewmodel.item;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import java.util.ArrayList;

public class ItemDescriptionViewModel extends ViewModel {

    private Item mItem;

    public ItemDescriptionViewModel(Item iItem)
    {
        mItem = iItem;
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

    //TODO implement
    public String getItemLocation(){
        return mItem.getLocation();
    }

    public ArrayList<String> getItemPicturesList(){
        return mItem.getPicturesUrls();
    }

    public void deleteThisItem(){
        ItemRepository.getInstance().deleteItem(mItem);
    }

}
