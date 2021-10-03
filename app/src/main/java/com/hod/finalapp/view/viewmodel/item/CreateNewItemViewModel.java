package com.hod.finalapp.view.viewmodel.item;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CreateNewItemViewModel extends AndroidViewModel
{
    private String mItemName = "";
    private String mItemDescription = "";
    private String mItemLocation = "";
    private MutableLiveData<String> mFinishedUploadError;
    private ArrayList<MutableLiveData<Uri>> mPhotoUris;

    public CreateNewItemViewModel(@NonNull @NotNull Application application) {
        super(application);

        mFinishedUploadError = new MutableLiveData<>();
        mPhotoUris = new ArrayList<>();
        for(int i = 0; i < StorageManager.MAX_ITEM_PICTURES; i++)
        {
            mPhotoUris.add(new MutableLiveData<>());
        }
    }

    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public void setItemDescription(String mItemDescription) {
        this.mItemDescription = mItemDescription;
    }

    public void setItemLocation(String mItemLocation) {
        this.mItemLocation = mItemLocation;
    }

    public MutableLiveData<String> getFinishedUpLoadError()
    {
        return mFinishedUploadError;
    }



    public void createItem()
    {
        boolean isAnyFieldEmpty = mItemName.isEmpty() && mItemDescription.isEmpty();
        if(!isAnyFieldEmpty)
        {
            ArrayList<Uri> uris = new ArrayList<>();
            for (MutableLiveData<Uri> uri : mPhotoUris) {
                uris.add(uri.getValue());
            }

            Item newItem = new Item(
                    UserRepository.getInstance().getCurrentUser().getUserId(),
                    mItemName, mItemDescription, mItemLocation, GregorianCalendar.getInstance().getTime().toString(), null );

            ItemRepository.getInstance().uploadNewItem(newItem, uris, getFinishUploadListener());
        }
        else
        {
            String error = getApplication().getString(R.string.please_fill_all_the_details);
            mFinishedUploadError.postValue(error);
        }
    }
    /*
    get current time, help with this documentation:
    https://developer.android.com/reference/java/util/GregorianCalendar
     */

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

    private OnCompleteListener getFinishUploadListener()
    {
        return new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if(task.isSuccessful())
                {
                    mFinishedUploadError.postValue("");
                }
                else
                {
                    mFinishedUploadError.postValue(task.getException().getMessage());
                }
            }
        };
    }

}
