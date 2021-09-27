package com.hod.finalapp.view.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.UserRepository;

public class SplashScreenViewModel extends ViewModel
{
    MutableLiveData<Boolean> mIsUserLoggedIn;

    public SplashScreenViewModel()
    {
        mIsUserLoggedIn = new MutableLiveData<>();
    }

    public void initStartup()
    {
        boolean isLoggedin = UserRepository.getInstance().isUserLoggedIn();

        if(isLoggedin)
        {
            UserRepository.getInstance().initUserInfo(getUserListener());
        }
        else
        {
            mIsUserLoggedIn.postValue(false);
        }
    }

    private OnCompleteListener getUserListener()
    {
        return (OnCompleteListener<User>) task ->
        {
            if(task.isSuccessful())
            {
                mIsUserLoggedIn.postValue(true); //user info fetched.
            }
        };
    }

    public MutableLiveData<Boolean> getIsUserLoggedIn() {
        return mIsUserLoggedIn;
    }
}
