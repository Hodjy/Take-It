package com.hod.finalapp.view.fragments.item;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.hod.finalapp.R;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.view.fragments.dialog.ChangePictureDialogFragment;
import com.hod.finalapp.view.viewmodel.item.CreateNewItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateNewItemFragment extends Fragment
{
    private CreateNewItemViewModel mViewModel;
    private ArrayList<ImageView> mItemImages;
    private EditText mItemName;
    private EditText mItemDescription;
    private Button mCreateItemBtn;
    private Button mBackButton;

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
        mViewModel = new ViewModelProvider(this).get(CreateNewItemViewModel.class);

        initUI(rootView);
        initObservers();

        return rootView;
    }

    private void initUI(View iRootView)
    {
        mItemImages  = new ArrayList<>();
        mItemImages.add((iRootView.findViewById(R.id.fragment_create_new_item_photo_1_iv)));
        mItemImages.add((iRootView.findViewById(R.id.fragment_create_new_item_photo_2_iv)));
        mItemImages.add((iRootView.findViewById(R.id.fragment_create_new_item_photo_3_iv)));
        mItemImages.add((iRootView.findViewById(R.id.fragment_create_new_item_photo_4_iv)));

        mItemName = iRootView.findViewById(R.id.fragment_create_new_item_name_et);
        mItemDescription = iRootView.findViewById(R.id.fragment_create_new_item_description_et);
        mCreateItemBtn = iRootView.findViewById(R.id.fragment_create_new_item_create_btn);
        mBackButton = iRootView.findViewById(R.id.fragment_create_new_item_back_btn);

        setImagesClickListeners();

        mItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setItemName(s.toString());
            }
        });

        mItemDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setItemDescription(s.toString());
            }
        });

        mCreateItemBtn.setOnClickListener(v -> {
            mViewModel.createItem();
        });

        mBackButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void initObservers()
    {
        for(int i = 0; i < StorageManager.MAX_ITEM_PICTURES; i++)
        {
            int finalIndex = i;

            mViewModel.getPhotoUris(finalIndex).observe(getViewLifecycleOwner(), new Observer<Uri>() {
                @Override
                public void onChanged(Uri uri)
                {
                    if(uri != null || uri != Uri.EMPTY)
                    loadImageToIv(mItemImages.get(finalIndex), uri);
                }
            });
        }
    }

    private void setImagesClickListeners()
    {
        int i = 0;
        for(ImageView imageView : mItemImages)
        {
            int finalIndex = i;
            // set On click:
            imageView.setOnClickListener(v -> {
                new ChangePictureDialogFragment(new ChangePictureDialogFragment.IChangePictureDialogListener() {
                    @Override
                    public void onPictureUriReceived(Uri iImageUri)
                    {
                        mViewModel.setUri(iImageUri, finalIndex);
                    }
                })
                        .show(getParentFragmentManager(),ChangePictureDialogFragment.getDialogTag());
            });

            i++;
        }
    }

    private void loadImageToIv(ImageView iImageView, Uri iUri)
    {
        Glide.with(this)
                .load(iUri)
                .into(iImageView);
    }

}
