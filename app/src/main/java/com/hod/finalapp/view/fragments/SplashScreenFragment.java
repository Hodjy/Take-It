package com.hod.finalapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.RepoInitializer;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.view.viewmodel.SplashScreenViewModel;

import org.jetbrains.annotations.NotNull;

public class SplashScreenFragment extends Fragment
{
    private SplashScreenViewModel mViewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        mViewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);

        initObservers(this);
        mViewModel.initStartup();

        //
        return rootView;
    }

    private void initObservers(Fragment iThisFragment)
    {
        mViewModel.getIsUserLoggedIn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)// if user is logged in, and finished loading the User class data.
                {
                    NavHostFragment.findNavController(iThisFragment).navigate(R.id.action_splashScreenFragment_to_userMainScreenFragment);
                    RepoInitializer.initAllRepo();
                }
                else // if user is not logged in.
                {
                    NavHostFragment.findNavController(iThisFragment).navigate(R.id.action_splashScreenFragment_to_welcomeScreenFragment);
                }
            }
        });
    }
}
