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
import com.hod.finalapp.view.viewmodel.RegisterViewModel;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment
{
    private RegisterViewModel mViewModel;

    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;
    private Button mRegisterBtn;
    private Button mBackBtn;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initUI(rootView);
        initObservers(rootView);

        //TODO Make the input highlight better: https://youtu.be/TwHmrZxiPA8?t=510
        //TODO After register press, hide buttons and display loading gif
        mRegisterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mViewModel.OnRegisterClicked(v);

            }
        });

        mBackBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        return rootView;
    }

    private void initUI(View iRootView)
    {
        mFirstNameEt = iRootView.findViewById(R.id.fragment_register_first_name_et);
        mLastNameEt = iRootView.findViewById(R.id.fragment_register_last_name_et);
        mEmailEt = iRootView.findViewById(R.id.fragment_register_email_et);
        mPasswordEt = iRootView.findViewById(R.id.fragment_register_password_et);
        mConfirmPasswordEt = iRootView.findViewById(R.id.fragment_register_confirm_password_et);

        mRegisterBtn = iRootView.findViewById(R.id.fragment_register_register_btn);
        mBackBtn = iRootView.findViewById(R.id.fragment_register_back_btn);

        mFirstNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setFirstname(s.toString());
            }
        });

        mLastNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setLastname(s.toString());
            }
        });

        mEmailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setUsername(s.toString());
            }
        });

        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setPassword(s.toString());
            }
        });

        mConfirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setConfirmPassword(s.toString());
            }
        });
    }

    private void initObservers(View iRootView)
    {
        mViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty())
                {
                    Snackbar.make(iRootView, s, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mViewModel.getCreateUserResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty())
                {
                    Snackbar.make(iRootView, s, Snackbar.LENGTH_LONG).show();
                }
                else {
                    //TODO navigate to the correct fragment
                }
            }
        });
    }
}
