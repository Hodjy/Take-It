package com.hod.finalapp.view.fragments.item;

import android.media.Image;
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
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hod.finalapp.R;
import com.hod.finalapp.view.fragments.dialog.ChangePictureDialogFragment;
import com.hod.finalapp.view.viewmodel.item.CreateNewItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;

public class CreateNewItemFragment extends Fragment
{
    private CreateNewItemViewModel mViewModel;
    private ArrayList<ImageViewUriPair> mItemImages;
    private EditText mItemName;
    private EditText mItemDescription;
    private Button mCreateItemBtn;
    private Button mBackButton;

    private class ImageViewUriPair
    {
        private ImageView mImageView;
        private Uri mUri;

        ImageViewUriPair(ImageView iImageView, Uri iUri)
        {
            mImageView = iImageView;
            mUri = iUri;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public void setImageView(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        public Uri getUri() {
            return mUri;
        }

        public void setUri(Uri mUri) {
            this.mUri = mUri;
        }
    }

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

        return rootView;
    }

    private void initUI(View iRootView)
    {
        mItemImages  = new ArrayList<>();
        mItemImages.add(new ImageViewUriPair((iRootView.findViewById(R.id.fragment_create_new_item_photo_1_iv)), null));
        mItemImages.add(new ImageViewUriPair((iRootView.findViewById(R.id.fragment_create_new_item_photo_2_iv)), null));
        mItemImages.add(new ImageViewUriPair((iRootView.findViewById(R.id.fragment_create_new_item_photo_3_iv)), null));
        mItemImages.add(new ImageViewUriPair((iRootView.findViewById(R.id.fragment_create_new_item_photo_4_iv)), null));

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

    private void setImagesClickListeners()
    {
        for(ImageViewUriPair pair : mItemImages)
        {
            // set On click:
            pair.getImageView().setOnClickListener(v -> {
                new ChangePictureDialogFragment(new ChangePictureDialogFragment.IChangePictureDialogListener() {
                    @Override
                    public void onPictureUriReceived(Uri iImageUri)
                    {
                        if(pair.getUri() == null)
                        {
                            if(!loadImageOnFirstEmptyIV(iImageUri))// if all images already got an image, load on the currently pressed one.
                            {
                                pair.setUri(iImageUri);
                                loadSelectedPicture(pair);
                            }
                        }
                        else
                        {
                            pair.setUri(iImageUri);
                            loadSelectedPicture(pair);
                        }
                    }
                })
                        .show(getParentFragmentManager(),ChangePictureDialogFragment.getDialogTag());
            });
        }
    }
    //TODO the photo system is a bit messed up, will change this tomorrow.

    /***
     * Checks if a pair has a null for uri, then displays the picture on the first that has one.
     * @param iImageUri
     * @return
     */
    private boolean loadImageOnFirstEmptyIV(Uri iImageUri)
    {
        boolean iIsLoadedOnFirstEmpty = false;

        for(ImageViewUriPair pair : mItemImages)
        {
            if(pair.getUri() == null)//if imageview does not have a picture
            {
                pair.setUri(iImageUri);
                loadSelectedPicture(pair);
                iIsLoadedOnFirstEmpty = true;

                break;
            }
        }

        return iIsLoadedOnFirstEmpty;
    }

    private void loadSelectedPicture(ImageViewUriPair iPair) {
        loadImageToIv(iPair);

    }

    private void loadImageToIv(ImageViewUriPair iPair)
    {
        Glide.with(this)
                .load(iPair.getUri())
                .into(iPair.getImageView());

        Snackbar.make(getView(),mItemImages.get(0).mUri.toString() + "=" + iPair.mUri, Snackbar.LENGTH_LONG).show();
    }

}
