package com.hod.finalapp.view.fragments.user;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;
import com.hod.finalapp.view.fragments.dialog.ChangePictureDialogFragment;
import com.hod.finalapp.view.viewmodel.user.UserProfileViewModel;

import org.jetbrains.annotations.NotNull;
//TODO use material design shapes and this https://www.youtube.com/watch?v=QDp8X43oFy8 for all app design.
public class UserProfileFragment extends Fragment
{
    private UserProfileViewModel mViewModel;
    private TextView mFullname;
    private ShapeableImageView mProfilePic;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile,container,false);
        mViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        initUI(rootView);
        initObservers();

        return rootView;
    }

    private void initUI(View iRootView)
    {
        mFullname = iRootView.findViewById(R.id.fragment_user_profile_fullname_tv);
        mProfilePic = iRootView.findViewById(R.id.fragment_user_profile_user_profile_image);

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePictureDialogFragment mChangePictureDialogFragment = new ChangePictureDialogFragment(new ChangePictureDialogFragment.IChangePictureDialogListener() {
                    @Override
                    public void onPictureUriReceived(Uri iImageUri) {
                        mProfilePic.setImageResource(R.drawable.ic_baseline_account_circle_24);
                        mViewModel.changeUserProfilePicture(iImageUri);
                    }
                });
                mChangePictureDialogFragment.show(getActivity().getSupportFragmentManager(), ChangePictureDialogFragment.getDialogTag());
            }
        });
    }

    private void initObservers()
    {
        mViewModel.getFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mFullname.setText(s);
            }
        });

        mViewModel.getProfilePictureUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                Glide.with(UserProfileFragment.this)
                        .load(uri)
                        .circleCrop()
                        .into(mProfilePic);

//                                        .apply(RequestOptions.skipMemoryCacheOf(true))
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                        .circleCrop()
            }
        });
    }
}
