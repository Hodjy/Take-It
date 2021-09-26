package com.hod.finalapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.hod.finalapp.R;
import com.hod.finalapp.model.FirebaseHandler;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment
{
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        EditText emailET = rootView.findViewById(R.id.fragment_sign_in_email_et);
        EditText passwordET = rootView.findViewById(R.id.fragment_sign_in_password_et);
        Button signInBtn = rootView.findViewById(R.id.fragment_sign_in_sign_in_btn);
        Button backBtn = rootView.findViewById(R.id.fragment_sign_in_back_btn);

        signInBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(!email.isEmpty() && !password.isEmpty())
                {
                    FirebaseHandler.getInstance().signInUser(email, password,
                            new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        assert getParentFragment() != null;
                                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_to_userMainScreenFragment);
                                    }
                                    else
                                    {
                                        Snackbar.make(getView(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        backBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());



        return rootView;
    }
}
