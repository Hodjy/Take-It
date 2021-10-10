package com.hod.finalapp.view.fragments.item;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.hod.finalapp.R;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.view.fragments.dialog.ChangePictureDialogFragment;
import com.hod.finalapp.view.viewmodel.item.CreateNewItemViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateNewItemFragment extends Fragment
{
    private CreateNewItemViewModel mViewModel;
    private ArrayList<ImageView> mItemImages;
    private EditText mItemName;
    private EditText mItemDescription;
    private Button mCreateItemBtn;
    private Button mSetLocationBtn;
    private Button mBackButton;
    private TextView mLocationTv;

    private ActivityResultLauncher<String> requestPermissionLauncher;

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
        initObservers(rootView, this);
        initLaunchers(rootView);

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
        mSetLocationBtn = iRootView.findViewById(R.id.fragment_create_new_item_set_location_btn);
        mBackButton = iRootView.findViewById(R.id.fragment_create_new_item_back_btn);
        mLocationTv = iRootView.findViewById(R.id.fragment_create_new_item_location_tv);

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

        mSetLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if(ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    else
                    {
                        setLocation();
                    }
                }
                else
                {
                    setLocation();
                }
            }
        });

        mBackButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        initSpinner(iRootView,
                R.array.categories,
                R.id.fragment_create_new_item_category_spinner,
                "category");
    }

    private void initObservers(View iRootView, Fragment iThisFragment)
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

        mViewModel.getFinishedUpLoadError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(""))
                {
                    String success = getActivity().getString(R.string.item_created);
                    Snackbar.make(iRootView, success, Snackbar.LENGTH_LONG).show();
                    NavHostFragment.findNavController(iThisFragment).popBackStack();
                }
                else
                {
                    Snackbar.make(iRootView, s, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mViewModel.getLocationResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mLocationTv.setText(s);
            }
        });
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
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(iImageView);
    }


    private void initSpinner(View iRootView, int iSpinnerItemsArrayResource, int iSpinnerViewId, String iSpinnerType) {
        ArrayList<String> items = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(iSpinnerItemsArrayResource)));
        Spinner categorySpinner = (Spinner) iRootView.findViewById(iSpinnerViewId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               userPickedFromSpinner(iSpinnerType, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void userPickedFromSpinner(String iSpinnerType, int iPosition)
    {
        switch(iSpinnerType)
        {
            case "category":
                mViewModel.setItemCategory(iPosition);
                break;
        }
    }


    private void setLocation(){
        mViewModel.getLocation(getContext());
    }


    private void initLaunchers(View iRootView) {

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (!result)
                {
                    Snackbar.make(iRootView, getActivity().getString(R.string.no_permissions), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
