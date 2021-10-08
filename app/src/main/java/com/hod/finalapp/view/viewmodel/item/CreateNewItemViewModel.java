package com.hod.finalapp.view.viewmodel.item;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.services.ServerUploadService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CreateNewItemViewModel extends AndroidViewModel
{
    private boolean mIsCreating = false;
    private int mItemRegion = 0;
    private int mItemCategory = 0;
    private String mItemName = "";
    private String mItemDescription = "";
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

    public MutableLiveData<String> getFinishedUpLoadError()
    {
        return mFinishedUploadError;
    }



    public void createItem()
    {
        if(!mIsCreating)
        {
            boolean isAnyFieldEmpty = mItemName.isEmpty() && mItemDescription.isEmpty();
            boolean isUserPickedPicture = (mPhotoUris.get(0).getValue() == null);
            if(!isAnyFieldEmpty)
            {
                if(!isUserPickedPicture)
                {
                    mIsCreating = true;
                    createAndSendItemToUpload();
                }
                else
                {
                    String error = getApplication().getString(R.string.a_picture_is_required);
                    postError(error);
                }
            }
            else
            {
                String error = getApplication().getString(R.string.please_fill_all_the_details);
                postError(error);
            }
        }
        else
        {
            String error = getApplication().getString(R.string.please_wait_for_item_creation);
            postError(error);
        }

    }

    private void postError(String error) {
        mFinishedUploadError.postValue(error);
    }

    private void createAndSendItemToUpload() {
        ArrayList<Uri> uris = new ArrayList<>();
        for (MutableLiveData<Uri> uri : mPhotoUris) {
            if(uri.getValue() == null)
            {
                break;
            }
            uris.add(uri.getValue());
        }

        Item newItem = new Item(
                UserRepository.getInstance().getCurrentUser().getUserId(),
                mItemName, mItemDescription, mItemRegion, mItemCategory, GregorianCalendar.getInstance().getTime().toString(), null );

        Intent intent = new Intent(getApplication().getBaseContext(),ServerUploadService.class);
        intent.putParcelableArrayListExtra("uris", uris);
        intent.putExtra("item",newItem);

        IntentFilter intentFilter = new IntentFilter(ServerUploadService.SERVICE_NAME);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                postError(intent.getExtras().getString("error"));
            }
        };

        LocalBroadcastManager.getInstance(getApplication().getApplicationContext())
        .registerReceiver(broadcastReceiver, intentFilter);

        getApplication().startService(intent);

        //TODO return if not working
        //ItemRepository.getInstance().uploadNewItem(newItem, uris, getFinishUploadListener());
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
                    postError("");
                }
                else
                {
                    postError(task.getException().getMessage());
                }
                mIsCreating = false;
            }
        };
    }

    public void setItemCategory(int iCategoryNumber)
    {
        mItemCategory = iCategoryNumber;
    }

    public void setItemRegion(int iRegionNumber)
    {
        mItemRegion = iRegionNumber;
    }
}
