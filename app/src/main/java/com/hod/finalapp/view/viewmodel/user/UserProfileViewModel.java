package com.hod.finalapp.view.viewmodel.user;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.UserRepository;

public class UserProfileViewModel extends ViewModel
{
    private MutableLiveData<String> mFullName;
    private MutableLiveData<Uri> mProfilePictureUri;

    public UserProfileViewModel()
    {
        User user = UserRepository.getInstance().getCurrentUser();
        mFullName = new MutableLiveData<>(user.getFirstName() + " " +  user.getLastName());
        if(user.getPictureUrl() != null)
        {
            mProfilePictureUri = new MutableLiveData<>(Uri.parse(user.getPictureUrl()));
        }
        else
        {
            mProfilePictureUri = new MutableLiveData<>();
        }
    }

    public MutableLiveData<String> getFullName() {
        return mFullName;
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


    //TODO subscribe to user changed in order to extract details.


}
