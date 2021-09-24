package com.hod.finalapp.model.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hod.finalapp.model.FirebaseHandler;
import com.hod.finalapp.model.database_objects.User;

import org.jetbrains.annotations.NotNull;

public class UserRepository
{
    private final DatabaseReference mUserTable;

    public UserRepository()
    {
        mUserTable = FirebaseDatabase.getInstance("https://socialapp-67e7d-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users");
    }

    public void registerNewUser(Context iContext, String iEmail, String iPassword,
                                  String iFirstName, String iLastName)
    {
        User newUser = new User(iEmail, iPassword,null,
                iFirstName,iLastName,null);

        FirebaseHandler.getInstance().signUpUser(newUser.getUsername(), newUser.getPassword(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {

                            FirebaseHandler.getInstance().updateUserData(new UserProfileChangeRequest
                                            .Builder()
                                            .setDisplayName(newUser.getFirstName()).build(),
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task)
                                        {
                                            Toast.makeText(iContext, "Registered!", Toast.LENGTH_SHORT).show();
                                            newUser.setUserId(FirebaseHandler.getInstance().getCurrentUser().getUid());
                                            mUserTable.child(newUser.getUserId()).setValue(newUser);
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(iContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
