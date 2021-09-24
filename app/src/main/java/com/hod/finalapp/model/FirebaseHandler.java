package com.hod.finalapp.model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseHandler
{
    private static FirebaseHandler mFirebaseHandler;
    private static final String SERVER_KEY = "1";
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseAuth.AuthStateListener m_FirebaseAuthListener;




    private FirebaseHandler()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseHandler getInstance()
    {
        if(mFirebaseHandler == null)
        {
            mFirebaseHandler = new FirebaseHandler();
        }

        return mFirebaseHandler;
    }

    public void signInUser(String iEmail, String iPassword, OnCompleteListener<AuthResult> iListener)
    {
        mFirebaseAuth.signInWithEmailAndPassword(iEmail, iPassword).addOnCompleteListener(iListener);
    }

    public void signUpUser(String iEmail, String iPassword, OnCompleteListener<AuthResult> iListener)
    {
        mFirebaseAuth.createUserWithEmailAndPassword(iEmail, iPassword).addOnCompleteListener(iListener);
    }

    public void updateUserData(UserProfileChangeRequest i_UserProfileChangeRequest,
                               OnCompleteListener<Void> iListener)
    {
        mFirebaseAuth.getCurrentUser().updateProfile(i_UserProfileChangeRequest).addOnCompleteListener(iListener);
    }

    public FirebaseUser getCurrentUser()
    {
        return mFirebaseAuth.getCurrentUser();
    }

    public void signUserOut()
    {
        mFirebaseAuth.signOut();
    }
}
