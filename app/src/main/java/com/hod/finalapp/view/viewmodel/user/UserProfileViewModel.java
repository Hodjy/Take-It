package com.hod.finalapp.view.viewmodel.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.UserRepository;

public class UserProfileViewModel extends ViewModel
{
    private MutableLiveData<String> mFullName;

    public UserProfileViewModel()
    {
        User user = UserRepository.getInstance().getCurrentUser();
        mFullName = new MutableLiveData<>(user.getFirstName() + " " +  user.getLastName());
    }

    public MutableLiveData<String> getFullName() {
        return mFullName;
    }

    //TODO subscribe to user changed in order to extract details.


}
