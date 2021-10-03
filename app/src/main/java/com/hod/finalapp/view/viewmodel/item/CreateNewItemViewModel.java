package com.hod.finalapp.view.viewmodel.item;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CreateNewItemViewModel extends ViewModel
{
    private String mItemName = "";
    private String mItemDescription = "";
    private String mItemLocation = "";
    private ArrayList<MutableLiveData<Uri>> mPhotoUris;

    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public void setItemDescription(String mItemDescription) {
        this.mItemDescription = mItemDescription;
    }

    public void setItemLocation(String mItemLocation) {
        this.mItemLocation = mItemLocation;
    }

    public CreateNewItemViewModel()
    {
        mPhotoUris = new ArrayList<>();
        for(int i = 0; i < StorageManager.MAX_ITEM_PICTURES; i++)
        {
            mPhotoUris.add(new MutableLiveData<>());
        }
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

    public void setUri(Uri iUri, int iPressedImageIndex)
    {
        int amountOfUri = 0;
        MutableLiveData<Uri> relativeUri = mPhotoUris.get(iPressedImageIndex);

        if(relativeUri.getValue() == null) // if empty, check if empty before
        {
            setAtFirstEmptyUri(iUri, amountOfUri, relativeUri);
        }
        else // set normally.
        {
            relativeUri.postValue(iUri);
        }
    }

    private void setAtFirstEmptyUri(Uri iUri, int amountOfUri, MutableLiveData<Uri> relativeUri) {
        for(MutableLiveData<Uri> mutableUri : mPhotoUris)
        {
            if(mutableUri.getValue() == null)
            {
                mutableUri.postValue(iUri);
                break;
            }

            amountOfUri++;
        }

        if(amountOfUri == StorageManager.MAX_ITEM_PICTURES)
        {
            relativeUri.postValue(iUri);
        }
    }

    public MutableLiveData<Uri> getPhotoUris(int iIndex)
    {
        return mPhotoUris.get(iIndex);
    }
}
