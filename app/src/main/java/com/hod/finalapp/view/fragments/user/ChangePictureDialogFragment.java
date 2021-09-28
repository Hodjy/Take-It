package com.hod.finalapp.view.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hod.finalapp.R;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public class ChangePictureDialogFragment extends DialogFragment
{
    private IChangePictureDialogListener mCallback;
    private URI mPictureURI;
    private ImageView mUserNewPicture;
    private Button mTakeScreenshotBtn;
    private Button mGetFromStorageBtn;
    private Button mConfirmBtn;
    private Button mBackBtn;

    public interface IChangePictureDialogListener
    {
        public void onPictureUriReceived(URI iImageUri);
    }

    public ChangePictureDialogFragment(IChangePictureDialogListener iCallback)
    {
        mCallback = iCallback;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_change_picture, container, false);

        initUI(rootView);

        return rootView;
    }

    private void initUI(View iRootView)
    {
        mUserNewPicture = iRootView.findViewById(R.id.dialog_fragment_change_picture_user_picture_iv);
        mTakeScreenshotBtn = iRootView.findViewById(R.id.dialog_fragment_change_picture_camera_iv);
        mGetFromStorageBtn = iRootView.findViewById(R.id.dialog_fragment_change_picture_file_iv);
        mConfirmBtn = iRootView.findViewById(R.id.dialog_fragment_change_picture_confirm_btn);
        mBackBtn = iRootView.findViewById(R.id.dialog_fragment_change_picture_back_btn);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPictureURI != null)
                {
                    mCallback.onPictureUriReceived(mPictureURI);
                    getDialog().dismiss();
                }
            }
        });

        mBackBtn.setOnClickListener(v -> getDialog().dismiss());
    }

}
