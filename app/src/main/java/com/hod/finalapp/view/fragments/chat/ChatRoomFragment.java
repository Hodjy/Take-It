package com.hod.finalapp.view.fragments.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.view.fragments.user.UserProfileFragment;
import com.hod.finalapp.view.viewmodel.chat.ChatRoomViewModel;

import org.jetbrains.annotations.NotNull;

public class ChatRoomFragment extends Fragment
{
    private RecyclerView
    private ChatRoomViewModel mViewModel;
    private Button mBackButton;
    private ImageView mChatRoomPicture;
    private TextView mChatRoomName;
    private EditText mChatRoomTextInput;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        mViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        initUi(rootView);
        initObservers();
        initVm();

        return rootView;
    }

    private void initVm()
    {
        String chatroomID = getArguments().getString("chat_room_id");
        boolean isChatRoomNew = mViewModel.initChat(chatroomID);

        if(isChatRoomNew)
        {
            Item relevantItem = getArguments().getParcelable("item");
            mViewModel.createChatRoom(relevantItem);
        }
    }



    private void initUi(View iRootView)
    {
        mBackButton = iRootView.findViewById(R.id.fragment_chat_room_back_btn);
        mChatRoomPicture = iRootView.findViewById(R.id.fragment_chat_room_chat_picture);
        mChatRoomName = iRootView.findViewById(R.id.fragment_chat_room_chat_name);
        // its not really an edit text
        mChatRoomTextInput = iRootView.findViewById(R.id.fragment_chat_room_message_et);

    }

    private void initObservers()
    {
        mViewModel.getChatName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mChatRoomName.setText(s);
            }
        });

        mViewModel.getChatImage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                loadImageToChatImage(s);
            }
        });

    }

    private void loadImageToChatImage(String iImageLink)
    {
        Glide.with(this)
                .load(iImageLink)
                .circleCrop()
                .into(mChatRoomPicture);
    }

    private void initRecyclerView()
    {

    }
}
