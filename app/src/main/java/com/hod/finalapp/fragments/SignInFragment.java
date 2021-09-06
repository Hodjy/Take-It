package com.hod.finalapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.hod.finalapp.R;

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

        EditText userNameET = rootView.findViewById(R.id.fragment_sign_in_user_name_et);
        EditText passwordET = rootView.findViewById(R.id.fragment_sign_in_password_et);
        Button signInBtn = rootView.findViewById(R.id.fragment_sign_in_sign_in_btn);
        Button backBtn = rootView.findViewById(R.id.fragment_sign_in_back_btn);

        signInBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = userNameET.getText().toString();
                String password = passwordET.getText().toString();

                if(!username.isEmpty() && !password.isEmpty())
                {
                    //TODO log in user
                    Toast.makeText(getContext(), "Name:" + username + " Pass: " + password, Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());



        return rootView;
    }
}
