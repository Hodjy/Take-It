package com.hod.finalapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
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
        Button signInAsGuestBtn = rootView.findViewById(R.id.fragment_welcome_screen_sign_in_as_guest_btn);
        Button registerBtn = rootView.findViewById(R.id.fragment_welcome_screen_register_btn);
        ImageView welcomeImage = rootView.findViewById(R.id.fragment_welcome_screen_app_image);
        loadImage(welcomeImage, rootView);

        signInBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_to_signInFragment));
        signInAsGuestBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_to_userMainScreenFragment));
        registerBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_to_registerFragment));

        return rootView;
    }

    private void loadImage(ImageView imageView,View iRootView)
    {
        Glide.with(iRootView.getContext())
                .load(R.drawable.app_logo)
                .into(imageView);
    }
}
