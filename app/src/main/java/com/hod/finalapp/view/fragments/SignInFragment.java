package com.hod.finalapp.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.hod.finalapp.R;
import com.hod.finalapp.model.repositories.RepoInitializer;
import com.hod.finalapp.view.viewmodel.SignInViewModel;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment
{
    private SignInViewModel mViewmodel;
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mSignInBtn;
    private Button mBackBtn;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mViewmodel = new ViewModelProvider(this).get(SignInViewModel.class);

        initUI(rootView);
        setObservers(rootView, this);

        mSignInBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mViewmodel.signIn();
            }
        });

        mBackBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());



        return rootView;
    }

    private void initUI(View iRootView)
    {
        mUsernameET = iRootView.findViewById(R.id.fragment_sign_in_email_et);
        mPasswordET = iRootView.findViewById(R.id.fragment_sign_in_password_et);

        mSignInBtn = iRootView.findViewById(R.id.fragment_sign_in_sign_in_btn);
        mBackBtn = iRootView.findViewById(R.id.fragment_sign_in_back_btn);

        mUsernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewmodel.setUsername(s.toString());
            }
        });

        mPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewmodel.setPassword(s.toString());
            }
        });
    }

    private void setObservers(View iRootView, Fragment iThisFragment)
    {
        mViewmodel.getSignInError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("")) // empty = success, else print the error.
                {

                    NavHostFragment.findNavController(iThisFragment).navigate(R.id.action_to_userMainScreenFragment);

                    RepoInitializer.initAllRepo();
                }
                else
                {
                    Snackbar.make(iRootView, s, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
