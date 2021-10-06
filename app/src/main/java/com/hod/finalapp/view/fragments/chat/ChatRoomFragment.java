package com.hod.finalapp.view.fragments.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.view.viewmodel.chat.ChatRoomViewModel;

import org.jetbrains.annotations.NotNull;

public class ChatRoomFragment extends Fragment
{
    private ChatRoomViewModel mViewModel;

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

        return rootView;
    }



    private void initUi(View rootView)
    {
        //ChatRoom chatRoom = getArguments().getParcelable("ChatRoom");


    }

    private void initObservers() {


    }
}
