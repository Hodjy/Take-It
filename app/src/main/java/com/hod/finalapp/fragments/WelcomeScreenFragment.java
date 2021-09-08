package com.hod.finalapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseUser;
import com.hod.finalapp.R;
import com.hod.finalapp.model.FirebaseHandler;

public class WelcomeScreenFragment extends Fragment
{
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        Button signInBtn = rootView.findViewById(R.id.fragment_welcome_screen_sign_in_btn);
        Button registerBtn = rootView.findViewById(R.id.fragment_welcome_screen_register_btn);

        signInBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_to_signInFragment));
        registerBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_to_registerFragment));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseHandler.getInstance().getCurrentUser();

        if(user != null)
        {
            NavHostFragment.findNavController(this).navigate(R.id.action_to_userMainScreenFragment);
        }
    }
}
