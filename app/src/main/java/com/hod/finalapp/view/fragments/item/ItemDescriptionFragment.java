package com.hod.finalapp.view.fragments.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hod.finalapp.R;

import org.jetbrains.annotations.NotNull;

public class ItemDescriptionFragment extends Fragment
{
    private RecyclerView mItemPicturesRecyclerView;

    private TextView mItemNameTv;
    private TextView mItemLocationTv;
    private TextView mItemDescriptionTv;
    private Button mBackBtn;
    private FloatingActionButton mFloatingActionButton;

    ////location, type, *subtype*, 4 pictures, description, name and owner name(needs to be fetched realtime).
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_item_description, container, false);

        initUI(rootView);

        return rootView;
    }

    private void initUI(View iRootView) {

        mItemNameTv = iRootView.findViewById(R.id.fragment_item_description_item_name_tv);
        mItemLocationTv = iRootView.findViewById(R.id.fragment_item_description_item_location_tv);
        mItemDescriptionTv = iRootView.findViewById(R.id.fragment_item_description_item_description_tv);
        mBackBtn = iRootView.findViewById(R.id.fragment_item_description_back_btn);
        mFloatingActionButton = iRootView.findViewById(R.id.fragment_item_description_fab);

        //TODO VIEWMODEL METOD RETURN IF THIS IS THE USER OR NOT
        if(true){
            mFloatingActionButton.setImageDrawable(iRootView.getResources().getDrawable(R.drawable.ic_baseline_account_circle_24, this.getActivity().getTheme()));
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO OPEN 
                }
            });
        }
        else{
            mFloatingActionButton.setImageDrawable(iRootView.getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_24, this.getActivity().getTheme()));
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO OPEN EDIT FRAGMENT
                }
            });
        }

    }
}
