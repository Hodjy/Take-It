package com.hod.finalapp.view.viewmodel.user;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserProfileViewModel extends ViewModel
{
    private MutableLiveData<String> mFullName;
    private MutableLiveData<String> mUserName;
    private MutableLiveData<Uri> mProfilePictureUri;
    private MutableLiveData<ArrayList<Item>> mMyItemsList;
    private MutableLiveData<String> mError;

    public UserProfileViewModel()
    {
        User user = UserRepository.getInstance().getCurrentUser();
        mFullName = new MutableLiveData<>(user.getFirstName() + " " +  user.getLastName());
        mUserName = new MutableLiveData<>(user.getUsername());
        mError = new MutableLiveData<>();
        if(user.getPictureUrl() != null)
        {
            mProfilePictureUri = new MutableLiveData<>(Uri.parse(user.getPictureUrl()));
        }
        else
        {
            mProfilePictureUri = new MutableLiveData<>();
        }

        mMyItemsList = new MutableLiveData<>();
        ItemRepository.getInstance().subscribeToItemListChanged(getItemsListListener());
    }



    private ArrayList<Item> extractMyItems(ArrayList<Item> iMyItemsList)
    {
        ArrayList<Item> myItems = new ArrayList<>();
        for(Item item: iMyItemsList)
        {
            if(item.getOwnerId().equals(UserRepository.getInstance().getCurrentUser().getUserId())){
                myItems.add(item);
            }
        }

        return myItems;
    }

    public MutableLiveData<ArrayList<Item>> getMyItemsList()
    {
        if (mMyItemsList.getValue() == null)
        {
            mMyItemsList.postValue(
                    extractMyItems(
                            ItemRepository.getInstance().getAllItems()));
        }
        return mMyItemsList;
    }

    public MutableLiveData<String> getFullName() {
        return mFullName;
    }

    public MutableLiveData<String> getUserName() {
        return mUserName;
    }

    public MutableLiveData<Uri> getProfilePictureUri(){ return mProfilePictureUri; };

    public void changeUserProfilePicture(Uri iImageUri){
        UserRepository.getInstance().changeUserProfilePicture(iImageUri, getUrlListener());
    }

    private OnCompleteListener<Uri> getUrlListener ()
    {
        return new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    mProfilePictureUri.postValue(task.getResult());
                    UserRepository.getInstance().updateCurrentUserPhotoPath(task.getResult().toString());
                }
            }
        };
    }

    private OnCompleteListener<ArrayList<Item>> getItemsListListener(){
        return new OnCompleteListener<ArrayList<Item>>(){
            @Override
            public void onComplete(@NonNull @NotNull Task<ArrayList<Item>> task) {
                if(task.isSuccessful())
                {
                    mMyItemsList.postValue(extractMyItems(task.getResult()));
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
}
