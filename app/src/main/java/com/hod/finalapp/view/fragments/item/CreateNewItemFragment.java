package com.hod.finalapp.view.fragments.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hod.finalapp.R;

import org.jetbrains.annotations.NotNull;

public class CreateNewItemFragment extends Fragment
{
    //location, type, *subtype*, 4 pictures, description, name and owner name(needs to be fetched realtime).
    //TODO grid https://youtu.be/k2R38Rv4Vmk maybe be good.(material design)
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_create_new_item, container, false);

        return rootView;
    }
}
