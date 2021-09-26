package com.hod.finalapp.model.repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import org.jetbrains.annotations.NotNull;

public class UserRepository
{
    private static UserRepository mUserRepository;
    private final DatabaseReference mUserTable;

    private AuthenticationManager mAuthenticationManager;

    private User mCurrentUser;

    private UserRepository()
    {
        mAuthenticationManager = AuthenticationManager.getInstance();

        mUserTable = DatabaseManager.getInstance().getFirebaseDatabaseInstance()
                .getReference(eFirebaseDataTypes.USERS.mTypeName);
    }

    public static UserRepository getInstance()
    {
        if(mUserRepository == null)
        {
            mUserRepository = new UserRepository();
        }

        return  mUserRepository;
    }

    public void registerNewUser(String iUsername, String iPassword, OnCompleteListener iListener)
    {
        mAuthenticationManager.getAuth().createUserWithEmailAndPassword(iUsername, iPassword).addOnCompleteListener(iListener);
        //TODO if we do need the user firstname in the authentication account, check the ver 0.1 "initUserData" method call on UserRepositoryClass.
    }

    /** Fetch new user instance from firebase **/
    public void updateCurrentUser(User user) {
        mAuthenticationManager.updateCurrentUser();
        mCurrentUser = user;
    }

    /** push user to firebase **/
    public void pushUserToDatabase(User user){
        user.setUserId(mAuthenticationManager.getCurrentLoggedInUser().getUid());
        mUserTable.child(mAuthenticationManager.getCurrentLoggedInUser().getUid()).setValue(user);
    }

    public User getCurrentUser()
    {
        return mCurrentUser;
    }

    public void fetchLoggedInUser()
    {
        mUserTable.child(mAuthenticationManager.getAuth().getCurrentUser().getUid());
    }

    public void initUserInfo()
    {
        mUserTable.child(mAuthenticationManager.getAuth().getCurrentUser().getUid()).addValueEventListener(getUserListener());
    }


    private ValueEventListener getUserListener()
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    mCurrentUser = snapshot.getValue(User.class);
                    Log.d("UserTest", mCurrentUser.getFirstName());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
    }
}
