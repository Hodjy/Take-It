package com.hod.finalapp.model.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationManager
{
    private static AuthenticationManager mAuthenticationManager;
    private static FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentLoggedInUser;

    private AuthenticationManager()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentLoggedInUser = mFirebaseAuth.getCurrentUser();
    }

    public static AuthenticationManager getInstance()
    {
        if(mAuthenticationManager == null)
        {
            mAuthenticationManager = new AuthenticationManager();
        }

        return mAuthenticationManager;
    }

    public FirebaseAuth getAuth()
    {
        return mFirebaseAuth;
    }

    public FirebaseUser getCurrentLoggedInUser()
    {
        return mCurrentLoggedInUser;
    }

    public void updateCurrentUser()
    {
        mCurrentLoggedInUser = mFirebaseAuth.getCurrentUser();
    }
}
