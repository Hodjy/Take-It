package com.hod.finalapp.view.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CatalogMainScreenViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Item>> mItemsListLiveData;

    public CatalogMainScreenViewModel() {
        mItemsListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Item>> getItemsListLiveData() {
        return mItemsListLiveData;
    }

    public void getItemsList(){
        ItemRepository.getInstance().initItemsListInfo(getItemsListListener());
    }



    private OnCompleteListener<ArrayList<Item>> getItemsListListener(){
        return new OnCompleteListener<ArrayList<Item>>(){
            @Override
            public void onComplete(@NonNull @NotNull Task<ArrayList<Item>> task) {
                if(task.isSuccessful())
                {
                    mItemsListLiveData.postValue(task.getResult());
                }
            }
        };
    }

    public boolean isMyItem(Item iItem){
        return iItem.getOwnerId().equals(UserRepository.getInstance().getCurrentUser().getUserId());
    }
}
