package com.hod.finalapp.view.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.ApplicationContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CatalogMainScreenViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<ArrayList<Item>>> mItemsByCategory;
    private int mCategoriesAmount;

    public int getCategoriesAmount() {
        return mCategoriesAmount;
    }

    //TODO error msgs
    private MutableLiveData<String> mError;

    public CatalogMainScreenViewModel(@NonNull @NotNull Application application) {
        super(application);

        String[] categories = getApplication().getResources().getStringArray(R.array.categories);
        mCategoriesAmount = categories.length;
        ArrayList<ArrayList<Item>> newArraylist = new ArrayList<>();

        for (int i = 0; i < mCategoriesAmount; i++)
        {
            newArraylist.add(new ArrayList<>());
        }

        mItemsByCategory = new MutableLiveData<>(newArraylist);
    }


    public void initItemsListByCategory(){
        ItemRepository.getInstance().subscribeToItemListChanged(getItemsByCategoryListener());
        ItemRepository.getInstance().initItemsListInfo();
    }

    public MutableLiveData<ArrayList<ArrayList<Item>>> getItemsByCategory() {
        return mItemsByCategory;
    }

    private OnCompleteListener<ArrayList<Item>> getItemsByCategoryListener()
    {
        return new OnCompleteListener<ArrayList<Item>>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<ArrayList<Item>> task) {
                if(task.isSuccessful())
                {
                    mItemsByCategory.postValue(getItemsByCategory(task.getResult()));
                }
                else
                {
                    postError(task.getException().getMessage());
                }
            }
        };
    }

    private void postError(String iError)
    {
        mError.postValue(iError);
    }

    private ArrayList<ArrayList<Item>> getItemsByCategory(ArrayList<Item> iReceivedItems)
    {
        String[] itemCategories = ApplicationContext.getAppContext().getResources().getStringArray(R.array.categories);
        int categoriesAmount = itemCategories.length;
        ArrayList<ArrayList<Item>> itemsByCategory = new ArrayList<>();

        for (int i = 0; i < categoriesAmount; i++)
        {
            itemsByCategory.add(new ArrayList<>());
        }

        for(Item item : iReceivedItems) //enter the item into the corresponding list.
        {
            itemsByCategory
                    .get(item.getmItemCategory())
                    .add(item);
        }

        return itemsByCategory;
    }

}
