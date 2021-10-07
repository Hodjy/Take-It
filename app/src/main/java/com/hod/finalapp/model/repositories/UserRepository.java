package com.hod.finalapp.model.repositories;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.firebase.AuthenticationManager;
import com.hod.finalapp.model.firebase.DatabaseManager;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.firebase.enums.eFirebaseDataTypes;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class UserRepository
{
    private static UserRepository mUserRepository;
    private final DatabaseReference mUserTable;

    private AuthenticationManager mAuthenticationManager;
    private OnCompleteListener mUserChangedListener;

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

    public void signInUser(String iUsername, String iPassword, OnCompleteListener iListener)
    {
        mAuthenticationManager.getAuth().signInWithEmailAndPassword(iUsername, iPassword).addOnCompleteListener(iListener);
    }

    public void sendRegistrationToServer(String iToken)
    {
        mUserTable.child(mAuthenticationManager.getCurrentLoggedInUser().getUid()).child("token").setValue(iToken);
    }

    public void updateCurrentUserToken()
    {
        FirebaseMessaging.getInstance().getToken()
        .addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if(task.isSuccessful())
                {
                    sendRegistrationToServer(task.getResult());
                }
            }
        });
    }

    /** Fetch new user instance from firebase **/
    public void updateCurrentUser(User user) {
        mAuthenticationManager.updateCurrentUser();
        mCurrentUser = user;
    }

    /** push user to firebase **/
    public void pushUserToDatabase(User user){
        mAuthenticationManager.updateCurrentUser();
        user.setUserId(mAuthenticationManager.getCurrentLoggedInUser().getUid());
        mUserTable.child(mAuthenticationManager.getCurrentLoggedInUser().getUid()).setValue(user);
    }

    public User getCurrentUser()
    {
        return mCurrentUser;
    }

    public boolean isUserLoggedIn()
    {
        boolean isLoggedIn = false;

        if(mAuthenticationManager.getAuth().getCurrentUser() != null)
        {
            isLoggedIn = true;
        }

        return  isLoggedIn;
    }

    public void initUserInfo(OnCompleteListener iOnCompleteListener)
    {
        mUserChangedListener = iOnCompleteListener;
        mAuthenticationManager.updateCurrentUser();
        mUserTable.child(mAuthenticationManager.getCurrentLoggedInUser().getUid()).addValueEventListener(getUserListener());
    }


    private ValueEventListener getUserListener()
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    mCurrentUser = snapshot.getValue(User.class);
                    if(mUserChangedListener != null)
                    {
                        mUserChangedListener.onComplete(new Task() {
                            @Override
                            public boolean isComplete() {
                                return true;
                            }

                            @Override
                            public boolean isSuccessful() {
                                return true;
                            }

                            @Override
                            public boolean isCanceled() {
                                return false;
                            }

                            @Nullable
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object getResult() {
                                return mCurrentUser;
                            }

                            @Nullable
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Object getResult(@NonNull @NotNull Class aClass) throws Throwable {
                                return mCurrentUser;
                            }

                            @Nullable
                            @org.jetbrains.annotations.Nullable
                            @Override
                            public Exception getException() {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnSuccessListener(@NonNull @NotNull OnSuccessListener onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener onSuccessListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                                return null;
                            }

                            @NonNull
                            @NotNull
                            @Override
                            public Task addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                                return null;
                            }
                        });
                        mUserChangedListener = null;
                    }
                    Log.d("UserTest", mCurrentUser.getFirstName());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
    }

    public void signUserOut()
    {
        sendRegistrationToServer("");
        mAuthenticationManager.getAuth().signOut();
        setCurrentUserDataNull();
    }

    private void setCurrentUserDataNull()
    {
        mCurrentUser = null;
        mAuthenticationManager.updateCurrentUser();
    }

    public void updateCurrentUserPhotoPath(String path) {
        mCurrentUser.setPictureUrl(path);
        mUserTable.child(mAuthenticationManager.getCurrentLoggedInUser().getUid()).child("pictureUrl").setValue(path);
        mAuthenticationManager.updateCurrentUser();
    }

    public void changeUserProfilePicture(Uri iImageUri, OnCompleteListener<Uri> urlListener){
        StorageManager.getInstance().uploadUserProfilePicture(iImageUri, mAuthenticationManager.getCurrentLoggedInUser().getUid(), urlListener);
    }
}
