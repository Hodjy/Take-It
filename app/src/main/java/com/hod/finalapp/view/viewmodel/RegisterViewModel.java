package com.hod.finalapp.view.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

public class RegisterViewModel extends AndroidViewModel
{
    private String mFirstname = "";
    private String mLastname = "";
    private String mUsername = "";
    private String mPassword = "";
    private String mConfirmPassword = "";

    private MutableLiveData<String> mErrorLiveData;
    private MutableLiveData<String> mCreateUserResult;

    private UserRepository mUserRepository;

    public RegisterViewModel(@NonNull @NotNull Application application) {
        super(application);
        mErrorLiveData = new MutableLiveData<String>();
        mCreateUserResult = new MutableLiveData<String>();
        mUserRepository = UserRepository.getInstance();
    }


    public void setFirstname(String iFirstname) {
        mFirstname = iFirstname;
    }

    public void setLastname(String iLastname) {
        mLastname = iLastname;
    }

    public void setUsername(String iUsername) {
        mUsername = iUsername;
    }

    public void setPassword(String iPassword) {
        mPassword = iPassword;
    }

    public void setConfirmPassword(String iConfirmPassword) {
        mConfirmPassword = iConfirmPassword;
    }

    public MutableLiveData<String> getCreateUserResult() {
        return mCreateUserResult;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return mErrorLiveData;
    }

    public void OnRegisterClicked(View v)
    {
        boolean detailsNotEmpty = !mFirstname.isEmpty() && !mLastname.isEmpty()
                && !mUsername.isEmpty() && !mPassword.isEmpty();

        if(detailsNotEmpty)
        {
            boolean passwordsMatch = mPassword.equals(mConfirmPassword);

            if(passwordsMatch)
            {
                mUserRepository.registerNewUser(mUsername, mPassword, setCreateUserListener());
            }
            else
            {
                String msg = getApplication().getString(R.string.passwords_do_not_match);
                mErrorLiveData.postValue(msg);
            }
        }
        else
        {
            String msg = getApplication().getString(R.string.please_fill_all_the_details);
            mErrorLiveData.postValue(msg);
        }
    }


    public OnCompleteListener setCreateUserListener() {
        return (OnCompleteListener<AuthResult>) task -> {
            if(task.isSuccessful()){
                finishSuccessfulRegister();
            }
            else
            {
                mCreateUserResult.postValue(task.getException().getMessage());
            }

        };
    }

    private void finishSuccessfulRegister() {
        User user = new User(mUsername, mPassword, null, mFirstname, mLastname, null);
        mUserRepository.pushUserToDatabase(user);
        mUserRepository.updateCurrentUser(user);

        mCreateUserResult.postValue("");

    }
}
