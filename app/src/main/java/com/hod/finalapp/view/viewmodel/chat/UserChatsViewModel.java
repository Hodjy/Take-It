package com.hod.finalapp.view.viewmodel.chat;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.model.database_objects.chatroom.ChatRoom;
import com.hod.finalapp.model.repositories.ChatRepository;
import com.hod.finalapp.view.ApplicationContext;

import org.jetbrains.annotations.NotNull;

public class UserChatsViewModel extends ViewModel
{
    public UserChatsViewModel()
    {

    }

    /*
            ChatRepository.getInstance().createNewChat(new ChatRoom("Hod", "10", "Ofir", "", "Name", null),
                new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task task) {
                        Toast.makeText(ApplicationContext.getAppContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });
     */
}
