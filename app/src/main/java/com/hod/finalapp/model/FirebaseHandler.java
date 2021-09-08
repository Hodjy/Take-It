package com.hod.finalapp.model;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

public class FirebaseHandler
{
    private static FirebaseHandler m_FirebaseHandler;
    private static final String SERVER_KEY = "1";
    private static FirebaseAuth m_FirebaseAuth;
    private static FirebaseAuth.AuthStateListener m_FirebaseAuthListener;




    private FirebaseHandler()
    {
        m_FirebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseHandler getInstance()
    {
        if(m_FirebaseHandler == null)
        {
            m_FirebaseHandler = new FirebaseHandler();
        }

        return m_FirebaseHandler;
    }

    public void signInUser(String i_Email, String i_Password, OnCompleteListener<AuthResult> i_Listener)
    {
        m_FirebaseAuth.signInWithEmailAndPassword(i_Email, i_Password).addOnCompleteListener(i_Listener);
    }

    public void signUpUser(String i_Email, String i_Password, OnCompleteListener<AuthResult> i_Listener)
    {
        m_FirebaseAuth.createUserWithEmailAndPassword(i_Email, i_Password).addOnCompleteListener(i_Listener);
    }

    public void updateUserData(UserProfileChangeRequest i_UserProfileChangeRequest,
                               OnCompleteListener<Void> i_Listener)
    {
        m_FirebaseAuth.getCurrentUser().updateProfile(i_UserProfileChangeRequest).addOnCompleteListener(i_Listener);
    }

    public FirebaseUser getCurrentUser()
    {
        return m_FirebaseAuth.getCurrentUser();
    }

    public void signUserOut()
    {
        m_FirebaseAuth.signOut();
    }
}
