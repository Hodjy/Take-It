package com.hod.finalapp.view.fragments.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hod.finalapp.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;

public class ChangePictureDialogFragment extends DialogFragment
{
    private static final String DIALOG_TAG = "Change picture dialog fragment";
    private IChangePictureDialogListener mCallback;
    private Uri mPictureUri;
    private File m_File;
    private Boolean mIsPhotoUploaded = false ;;
    private ImageView mUserNewPicture;
    private ImageView mTakeScreenshotBtn;
    private ImageView mGetFromStorageBtn;
    private Button mConfirmBtn;
    private Button mBackBtn;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Uri> cameraResultLauncher;
    private ActivityResultLauncher<Intent> pickImageResultLauncher;

    public interface IChangePictureDialogListener
    {
        public void onPictureUriReceived(Uri iImageUri);
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
        initLaunchers();
        setPicSongBtn();
        setChoosePicBtn();


        return rootView;
    }

    public static String getDialogTag(){
        return DIALOG_TAG;
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
                if(mIsPhotoUploaded)
                {
                    mCallback.onPictureUriReceived(mPictureUri);
                    getDialog().dismiss();
                }
            }
        });

        mBackBtn.setOnClickListener(v -> getDialog().dismiss());
    }

    private void setPicSongBtn() {

        mTakeScreenshotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {

                    if(ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    else
                    {
                        takePic();
                    }
                }
                else
                {
                    takePic();
                }

            }
        });

    }



    private void setChoosePicBtn() {
        mGetFromStorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if(ActivityCompat.checkSelfPermission(getActivity().getBaseContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    else
                    {
                        choosePic();
                    }
                }
                else
                {
                    choosePic();
                }
            }
        });
    }




    private void choosePic() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        pickImageResultLauncher.launch(intent);
    }

    private void takePic() {
        //TODO MAY BE A BUG
        m_File = new File(getActivity().getExternalFilesDir(null), "pic.jpg");
        //TODO MAY BE A BUG
        Uri imageUri = FileProvider.getUriForFile(getActivity(),
                "com.hod.finalapp.provider", //(use your app signature + ".provider" )
                m_File);

        mPictureUri = imageUri;
        cameraResultLauncher.launch(imageUri);
    }



    private void initLaunchers() {


        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(!result)
                {
                    Toast.makeText(getActivity(), R.string.no_permissions, Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result)
                {
                    //m_PhotoPath = m_TempImageUri.toString();
                    Glide.with(ChangePictureDialogFragment.this)
                            .load(mPictureUri)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(mUserNewPicture);
                    //Glide.with(AddSongActivity.this).load(m_PhotoPath).into(m_Song_Photo);

                    mIsPhotoUploaded = true;
                }
            }
        });

        pickImageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){

                    //Context context = AddSongActivity.this;
                    mPictureUri = result.getData().getData();
                    if(mPictureUri != null)
                    {
                        Glide.with(ChangePictureDialogFragment.this).load(mPictureUri).into(mUserNewPicture);
                        //TODO CHECK IF NEEDED
//                        context.getContentResolver().takePersistableUriPermission(mPictureUri, result.getData().getFlags()
//                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION + Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
                        mIsPhotoUploaded = true;
                    }

                }
            }
        });
    }

}
