package com.hod.finalapp.view.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hod.finalapp.R;
import com.hod.finalapp.view.viewmodel.user.UserItemsViewModel;

import org.jetbrains.annotations.NotNull;

public class UserItemsFragment extends Fragment
{
    UserItemsViewModel mViewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_items, container, false);
        mViewModel = new ViewModelProvider(this).get(UserItemsViewModel.class);

        return rootView;
    }
}
