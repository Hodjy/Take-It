package com.hod.finalapp.view.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.repositories.ItemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CatalogMainScreenViewModel extends ViewModel {

    //TODO ASK HOD AND IMPLEMENT ITEM

    private MutableLiveData<ArrayList<Item>> mItemsListLiveData;

    public CatalogMainScreenViewModel() {
        mItemsListLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Item>> getItemsListLiveData() {
        return mItemsListLiveData;
    }

    public void getItemsList(){
//        mItemsListLiveData.postValue(ItemRepository.getInstance().getItemsList());
//        ItemRepository.getInstance().getItemsList();
    }



//    private OnCompleteListener<ArrayList<Item>> getItemsListListener(){
//        return new OnCompleteListener<ArrayList<Item>>(){
//            @Override
//            public void onComplete(@NonNull @NotNull Task<ArrayList<Item>> task) {
//                if(task.isSuccessful())
//                {
//                    mItemsListLiveData.postValue(task.getResult());
//                }
//            }
//        };
//    }
}
