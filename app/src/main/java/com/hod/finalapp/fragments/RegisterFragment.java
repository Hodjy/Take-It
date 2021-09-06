package com.hod.finalapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.hod.finalapp.R;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment
{
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        EditText firstNameEt = rootView.findViewById(R.id.fragment_register_first_name_et);
        EditText userNameEt = rootView.findViewById(R.id.fragment_register_user_name_et);
        EditText passwordEt = rootView.findViewById(R.id.fragment_register_password_et);
        EditText confirmPasswordEt = rootView.findViewById(R.id.fragment_register_confirm_password_et);
        Button registerBtn = rootView.findViewById(R.id.fragment_register_register_btn);
        Button backBtn = rootView.findViewById(R.id.fragment_register_back_btn);

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String firstName = firstNameEt.getText().toString();
                String userName = userNameEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirmPassword = confirmPasswordEt.getText().toString();
                boolean detailsNotEmpty = !firstName.isEmpty() && !userName.isEmpty() && !password.isEmpty();

                if(detailsNotEmpty)
                {
                    boolean passwordsMatch = password.equals(confirmPassword);

                    if(passwordsMatch)
                    {
                        //TODO register
                        Toast.makeText(getContext(), "Register!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Snackbar.make(rootView, "Passwords do not match.", Snackbar.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Snackbar.make(rootView,"Please fill all the details.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        backBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        return rootView;
    }
}
