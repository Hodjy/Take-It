package com.hod.finalapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hod.finalapp.R;
import com.hod.finalapp.model.repositories.UserRepository;

import org.jetbrains.annotations.NotNull;

public class CatalogMainScreenFragment extends Fragment
{
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalog_main_screen, container, false);

        //TextView firstnameTv = rootView.findViewById(R.id.fragment_user_main_screen_firstname_tv);
        //firstnameTv.setText(UserRepository.getInstance().getCurrentUser().getFirstName());

        return rootView;
    }
}