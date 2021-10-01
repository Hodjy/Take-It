package com.hod.finalapp.view.viewmodel.item;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CreateNewItemViewModel extends ViewModel
{

    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public void setItemDescription(String mItemDescription) {
        this.mItemDescription = mItemDescription;
    }

    public void setItemLocation(String mItemLocation) {
        this.mItemLocation = mItemLocation;
    }

    public void setPhotoUrls(ArrayList<String> mPhotoUrls) {
        this.mPhotoUrls = mPhotoUrls;
    }

    private String mItemName = "";
    private String mItemDescription = "";
    private String mItemLocation = "";
    private ArrayList<String> mPhotoUrls;

    public CreateNewItemViewModel()
    {

    }

    public void createItem()
    {
        Item newItem = new Item(
                UserRepository.getInstance().getCurrentUser().getUserId(),
                mItemName, mItemDescription, mItemLocation, GregorianCalendar.getInstance().getTime().toString(), new ArrayList<>()
                );

        ItemRepository.getInstance().uploadNewItem(newItem);
    }
    /*
    get current time, help with this documentation:
    https://developer.android.com/reference/java/util/GregorianCalendar
     */

    private OnCompleteListener getOnItemCreateListener()
    {
        return new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if(task.isSuccessful())
                {
                    //success
                }
                else
                {
                    //failed
                }
            }
        };
    }
}
