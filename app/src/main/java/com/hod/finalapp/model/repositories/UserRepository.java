package com.hod.finalapp.model.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hod.finalapp.model.FirebaseHandler;
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

    private FirebaseAuth mFirebaseAuth;
    private User currentUser;

    private UserRepository()
    {
        //mFirebaseAuth = AuthenticationManager.getInstance().getAuth();
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

    public void registerNewUser(Context iContext, String iEmail, String iPassword,
                                  String iFirstName, String iLastName, OnCompleteListener listener)
    {
//        currentUser = new User(iEmail, iPassword,null,
//                iFirstName,iLastName,null);

        mAuthenticationManager.getAuth().createUserWithEmailAndPassword(iEmail, iPassword).addOnCompleteListener(listener);
    }





//    public void initUserData()
//    {
//        FirebaseHandler.getInstance().updateUserData(new UserProfileChangeRequest
//                        .Builder()
//                        .setDisplayName(currentUser.getFirstName()).build(),
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task)
//                    {
//                        Toast.makeText(iContext, "Registered!", Toast.LENGTH_SHORT).show();
//                        currentUser.setUserId(FirebaseHandler.getInstance().getCurrentUser().getUid());
//                        mUserTable.child(currentUser.getUserId()).setValue(currentUser);
//                    }
//                });
//    }
//    public void finilizeUserRegistration()
//    {
//
//    }

    /** Fetch new user instance from firebase **/
    public void updateCurrentUser() {
        mAuthenticationManager.updateCurrentUser();
    }

    /** push user to firebase **/
    public void pushUserToDatabase(User user){
        user.setUserId(mAuthenticationManager.getCurrentLoggedInUser().getUid());
        mUserTable.child(mAuthenticationManager.getCurrentLoggedInUser().getUid()).setValue(user);
    }
}
