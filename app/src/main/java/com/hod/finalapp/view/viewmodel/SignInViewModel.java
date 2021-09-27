package com.hod.finalapp.view.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.hod.finalapp.R;
import com.hod.finalapp.model.FirebaseHandler;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

public class SignInViewModel extends AndroidViewModel
{
    private String mUsername = "";
    private String mPassword = "";
    private MutableLiveData<String> mSignInError;

    public SignInViewModel(@NonNull @NotNull Application application) {
        super(application);

        mSignInError = new MutableLiveData<>();
    }


    public void setUsername(String iUserName)
    {
        mUsername = iUserName;
    }

    public void setPassword(String iPassword)
    {
        mPassword = iPassword;
    }

    public MutableLiveData<String> getSignInError()
    {
        return mSignInError;
    }

    public void signIn()
    {
        if(!mUsername.isEmpty() && !mPassword.isEmpty())
        {
            //TODO now
            UserRepository.getInstance().signInUser(mUsername,mPassword,getSignInListener());
        }
        else
        {
            String error = getApplication().getString(R.string.please_fill_all_the_details);
            postError(error);
        }
    }

    private OnCompleteListener getSignInListener()
    {
        return new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if(task.isSuccessful())
                {
                    UserRepository.getInstance().initUserInfo(getUserInitListener());
                }
                else
                {
                    postError(task.getException().getMessage());
                }
            }
        };
    }

    private OnCompleteListener getUserInitListener()
    {
        return (OnCompleteListener<User>) task ->
        {
            if(task.isSuccessful())
            {
                postError(""); // will post blank thus success.
            }
        };
    }

    private void postError(String error)
    {
        mSignInError.postValue(error);
    }
}
