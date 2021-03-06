package com.hod.finalapp.view.fragments.chat;

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

import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.view.adapters.ChatAdapter;
import com.hod.finalapp.view.adapters.ItemAdapter;
import com.hod.finalapp.view.fragments.user.UserProfileFragment;
import com.hod.finalapp.view.viewmodel.chat.UserChatsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserChatsFragment extends Fragment
{
    private UserChatsViewModel mViewModel;

    private ArrayList<ChatRoom> mChatsList;
    private RecyclerView mChatsListRecyclerView;
    private ChatAdapter mChatAdapter;
    private Button mBackBtn;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_chats, container, false);
        mViewModel = new ViewModelProvider(this).get(UserChatsViewModel.class);

        mChatsList = new ArrayList<>();
        initUi(rootView);
        initObservers();

        mViewModel.initMyChatsListLiveData();


        return rootView;
    }

    private void initUi(View iRootView)
    {
        mBackBtn = iRootView.findViewById(R.id.fragment_user_chats_back_btn);
        mBackBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        mChatsListRecyclerView = iRootView.findViewById(R.id.fragment_user_chats_recyclerView);

        mChatAdapter = new ChatAdapter(mChatsList);
        mChatsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mChatsListRecyclerView.setHasFixedSize(true);
        mChatsListRecyclerView.setAdapter(mChatAdapter);

        mChatAdapter.setListener(new ChatAdapter.ChatListener() {
            @Override
            public void onItemClicked(ChatRoom iChatRoom) {
                Bundle bundle = new Bundle();
                bundle.putString("chat_room_id", iChatRoom.getChatRoomId());
                NavHostFragment.findNavController(UserChatsFragment.this).navigate(R.id.action_to_chatRoomFragment, bundle);
            }
        });

    }

    private void initObservers() {
        mViewModel.getMyChatsListLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatRoom>>() {
            @Override
            public void onChanged(ArrayList<ChatRoom> chatRooms) {
                mChatsList = new ArrayList<>();
                mChatsList.addAll(chatRooms);
                mChatAdapter.setChatRoom(mChatsList);
                mChatAdapter.notifyDataSetChanged();
            }
        });

    }
}
