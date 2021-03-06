package com.hod.finalapp.view.fragments.user;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hod.finalapp.R;
import com.hod.finalapp.view.adapters.ItemAdapter;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.view.fragments.dialog.ChangePictureDialogFragment;
import com.hod.finalapp.view.viewmodel.user.UserProfileViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//TODO use material design shapes and this https://www.youtube.com/watch?v=QDp8X43oFy8 for all app design.
public class UserProfileFragment extends Fragment {
    private UserProfileViewModel mUserProfileViewModel;
    private TextView mFullname;
    private TextView mUsername;
    private ShapeableImageView mProfilePic;
    private Button mBackBtn;
    private RecyclerView mMyItemsListRecyclerView;
    private ItemAdapter mItemAdapter;
    private ArrayList<Item> mItemsList;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mUserProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        mItemsList = new ArrayList<>();
        initUI(rootView);
        initObservers();

        return rootView;
    }

    private void initUI(View iRootView) {
        mFullname = iRootView.findViewById(R.id.fragment_user_profile_fullname_tv);
        mUsername = iRootView.findViewById(R.id.fragment_user_profile_username_tv);
        mProfilePic = iRootView.findViewById(R.id.fragment_user_profile_user_profile_image);
        mBackBtn = iRootView.findViewById(R.id.fragment_user_profile_back_btn);
        mMyItemsListRecyclerView = iRootView.findViewById(R.id.fragment_user_profile_my_items_rv);

        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePictureDialogFragment mChangePictureDialogFragment = new ChangePictureDialogFragment(new ChangePictureDialogFragment.IChangePictureDialogListener() {
                    @Override
                    public void onPictureUriReceived(Uri iImageUri) {
                        mProfilePic.setImageResource(R.drawable.ic_baseline_account_circle_24);
                        mUserProfileViewModel.changeUserProfilePicture(iImageUri);
                    }
                });
                mChangePictureDialogFragment.show(getActivity().getSupportFragmentManager(), ChangePictureDialogFragment.getDialogTag());
            }
        });

        mItemAdapter = new ItemAdapter(mItemsList);
        mMyItemsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mMyItemsListRecyclerView.setHasFixedSize(true);
        mMyItemsListRecyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setListener(new ItemAdapter.ItemListener() {
            @Override
            public void onItemClicked(Item iItem) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", iItem);
                NavHostFragment.findNavController(UserProfileFragment.this).navigate(R.id.action_to_itemDescriptionFragment, bundle);
            }
        });

        mBackBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

    }

    private void initObservers() {
        mUserProfileViewModel.getFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mFullname.setText(s);
            }
        });

        mUserProfileViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mUsername.setText(s);
            }
        });

        mUserProfileViewModel.getProfilePictureUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
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

        mUserProfileViewModel.getMyItemsList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                mItemsList.clear();
                mItemsList = items;
                mItemAdapter.setmItems(mItemsList);
                mItemAdapter.notifyDataSetChanged();
            }
        });
    }
}
