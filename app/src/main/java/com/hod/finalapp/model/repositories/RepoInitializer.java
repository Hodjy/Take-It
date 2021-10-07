package com.hod.finalapp.model.repositories;

public class RepoInitializer
{
    public static void initAllRepo()
    {
        UserRepository.getInstance();
        ItemRepository.getInstance();
        ChatRepository.getInstance();
    }
}
