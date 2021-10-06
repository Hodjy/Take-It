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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.view.adapters.PictureAdapter;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.view.fragments.dialog.YesNoDialogFragment;
import com.hod.finalapp.view.viewmodel.item.ItemDescriptionViewModel;

import org.jetbrains.annotations.NotNull;

public class ItemDescriptionFragment extends Fragment
{
    private ItemDescriptionViewModel mViewModel;

    private RecyclerView mItemPicturesRecyclerView;
    private TextView mItemNameTv;
    private TextView mItemRegionTv;
    private TextView mItemDescriptionTv;
    private Button mBackBtn;
    private FloatingActionButton mFloatingActionButton;

    private PictureAdapter mPictureAdapter;



    ////location, type, *subtype*, 4 pictures, description, name and owner name(needs to be fetched realtime).
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_item_description, container, false);

        initViewModel();
        initUI(rootView,this);

        return rootView;
    }

    private void initViewModel() {
        assert getArguments() != null;
        Item item = getArguments().getParcelable("item");
        mViewModel = new ViewModelProvider(this).get(ItemDescriptionViewModel.class);
        mViewModel.setItem(item);
    }

    private void initUI(View iRootView, Fragment iThisFragment) {

        mItemNameTv = iRootView.findViewById(R.id.fragment_item_description_item_name_tv);
        mItemNameTv.setText(mViewModel.getItemName());

        mItemRegionTv = iRootView.findViewById(R.id.fragment_item_description_item_region_tv);
        mItemRegionTv.setText(mViewModel.getItemRegion());

        mItemDescriptionTv = iRootView.findViewById(R.id.fragment_item_description_item_description_tv);
        mItemDescriptionTv.setText(mViewModel.getItemDescription());

        mBackBtn = iRootView.findViewById(R.id.fragment_item_description_back_btn);
        mBackBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        mFloatingActionButton = iRootView.findViewById(R.id.fragment_item_description_fab);

        if(mViewModel.isMyItem()){
            mFloatingActionButton.setImageDrawable(iRootView.getResources().getDrawable(R.drawable.ic_baseline_delete_24, this.getActivity().getTheme()));
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YesNoDialogFragment yesNoDialogFragment = new YesNoDialogFragment(getResources().getString(R.string.are_you_sure_you_want_to_delete), new YesNoDialogFragment.IYesNoDialogFragmentListener() {
                        @Override
                        public void userResponse(boolean iIsUserAccepted) {
                            if(iIsUserAccepted){
                                mViewModel.deleteThisItem();
                                NavHostFragment.findNavController(ItemDescriptionFragment.this).popBackStack();
                            }
                        }
                    });
                    yesNoDialogFragment.show(getActivity().getSupportFragmentManager(), YesNoDialogFragment.getDialogTag());
                }
            });
        }
        else{
            mFloatingActionButton.setImageDrawable(iRootView.getResources().getDrawable(R.drawable.ic_baseline_chat_bubble_24, this.getActivity().getTheme()));
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO OPEN MESSAGE FRAGMENT
                    Bundle bundle = new Bundle();
                    bundle.putString("chat_room_id", mViewModel.generateChatRoomId());
                    bundle.putParcelable("item", mViewModel.getItem());
                    NavHostFragment.findNavController(iThisFragment).navigate(R.id.action_to_chatRoomFragment, bundle);
                }
            });
        }

        mItemPicturesRecyclerView = iRootView.findViewById(R.id.fragment_item_description_picture_rv);
        mPictureAdapter = new PictureAdapter(mViewModel.getItemPicturesList());
        mItemPicturesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mItemPicturesRecyclerView.setHasFixedSize(true);
        mItemPicturesRecyclerView.setAdapter(mPictureAdapter);
        mPictureAdapter.setListener(new PictureAdapter.PictureListener() {
            @Override
            public void onItemClicked(int position, View view) {

            }
        });
    }
}
