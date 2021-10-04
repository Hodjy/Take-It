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
    private ArrayList<Item> mMyItemsList;

    public UserProfileViewModel()
    {
        User user = UserRepository.getInstance().getCurrentUser();
        mFullName = new MutableLiveData<>(user.getFirstName() + " " +  user.getLastName());
        mUserName = new MutableLiveData<>(user.getUsername());
        if(user.getPictureUrl() != null)
        {
            mProfilePictureUri = new MutableLiveData<>(Uri.parse(user.getPictureUrl()));
        }
        else
        {
            mProfilePictureUri = new MutableLiveData<>();
        }
        mMyItemsList = new ArrayList<>();
    }

    public ArrayList<Item> getMyItemsList(ArrayList<Item> iMyItemsList){
        mMyItemsList.clear();
        for(Item item: iMyItemsList)
        {
            if(item.getOwnerId().equals(UserRepository.getInstance().getCurrentUser().getUserId())){
                mMyItemsList.add(item);
            }
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


}
