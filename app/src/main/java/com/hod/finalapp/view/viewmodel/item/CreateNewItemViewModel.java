package com.hod.finalapp.view.viewmodel.item;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.firebase.StorageManager;
import com.hod.finalapp.model.repositories.ItemRepository;
import com.hod.finalapp.model.repositories.UserRepository;
import com.hod.finalapp.services.ServerUploadService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateNewItemViewModel extends AndroidViewModel
{
    private MutableLiveData<Boolean> mIsCreating;
    private int mItemCategory = 0;
    private String mItemName = "";
    private String mItemDescription = "";
    private double mItemLatitude = 0.0;
    private double mItemLongitude = 0.0;
    private String mItemLocation = "";
    private MutableLiveData<String> mFinishedUploadError;
    private ArrayList<MutableLiveData<Uri>> mPhotoUris;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MutableLiveData<String> mLocationResult;
    private Geocoder mGeocoder;
    private boolean mIsGettingLocation = false;

    public CreateNewItemViewModel(@NonNull @NotNull Application application) {
        super(application);

        mLocationResult = new MutableLiveData<>();
        mFinishedUploadError = new MutableLiveData<>();
        mIsCreating = new MutableLiveData<>(false);
        mPhotoUris = new ArrayList<>();
        for(int i = 0; i < StorageManager.MAX_ITEM_PICTURES; i++)
        {
            mPhotoUris.add(new MutableLiveData<>());
        }
    }

    public void setItemName(String mItemName) {
        this.mItemName = mItemName;
    }

    public void setItemDescription(String mItemDescription) {
        this.mItemDescription = mItemDescription;
    }

    public MutableLiveData<String> getFinishedUpLoadError()
    {
        return mFinishedUploadError;
    }

    public MutableLiveData<Boolean> getIsCreating()
    {
        return mIsCreating;
    }

    public void createItem()
    {
        if(!mIsCreating.getValue())
        {
            boolean isAnyFieldEmpty = mItemName.isEmpty() || mItemDescription.isEmpty() || mItemLocation.isEmpty();
            boolean isUserPickedPicture = (mPhotoUris.get(0).getValue() == null);
            if(!isAnyFieldEmpty)
            {
                if(!isUserPickedPicture)
                {
                    if(!mIsGettingLocation)
                    {
                        mIsCreating.postValue(true);
                        createAndSendItemToUpload();
                    }
                    else
                    {
                        String error = getApplication().getString(R.string.fetching_location);
                        postError(error);
                    }
                }
                else
                {
                    String error = getApplication().getString(R.string.a_picture_is_required);
                    postError(error);
                }
            }
            else
            {
                String error = getApplication().getString(R.string.please_fill_all_the_details);
                postError(error);
            }
        }
        else
        {
            String error = getApplication().getString(R.string.please_wait_for_item_creation);
            postError(error);
        }

    }

    private void postError(String error) {
        mFinishedUploadError.postValue(error);
    }

    private void createAndSendItemToUpload() {
        ArrayList<Uri> uris = new ArrayList<>();
        for (MutableLiveData<Uri> uri : mPhotoUris) {
            if(uri.getValue() == null)
            {
                break;
            }
            uris.add(uri.getValue());
        }

        Item newItem = new Item(
                UserRepository.getInstance().getCurrentUser().getUserId(),
                mItemName, mItemDescription, mItemCategory, GregorianCalendar.getInstance().getTimeInMillis(),
                null, mItemLatitude, mItemLongitude);

        Intent intent = new Intent(getApplication().getBaseContext(),ServerUploadService.class);
        intent.putParcelableArrayListExtra("uris", uris);
        intent.putExtra("item",newItem);

        IntentFilter intentFilter = new IntentFilter(ServerUploadService.SERVICE_NAME);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                postError(intent.getExtras().getString("error"));
            }
        };

        LocalBroadcastManager.getInstance(getApplication().getApplicationContext())
        .registerReceiver(broadcastReceiver, intentFilter);

        getApplication().startService(intent);

        //TODO return if not working
        //ItemRepository.getInstance().uploadNewItem(newItem, uris, getFinishUploadListener());
    }
    /*
    get current time, help with this documentation:
    https://developer.android.com/reference/java/util/GregorianCalendar
     */

    public void setUri(Uri iUri, int iPressedImageIndex)
    {
        int amountOfUri = 0;
        MutableLiveData<Uri> relativeUri = mPhotoUris.get(iPressedImageIndex);

        if(relativeUri.getValue() == null) // if empty, check if empty before
        {
            setAtFirstEmptyUri(iUri, amountOfUri, relativeUri);
        }
        else // set normally.
        {
            relativeUri.postValue(iUri);
        }
    }

    private void setAtFirstEmptyUri(Uri iUri, int amountOfUri, MutableLiveData<Uri> relativeUri) {
        for(MutableLiveData<Uri> mutableUri : mPhotoUris)
        {
            if(mutableUri.getValue() == null)
            {
                mutableUri.postValue(iUri);
                break;
            }

            amountOfUri++;
        }

        if(amountOfUri == StorageManager.MAX_ITEM_PICTURES)
        {
            relativeUri.postValue(iUri);
        }
    }

    public MutableLiveData<Uri> getPhotoUris(int iIndex)
    {
        return mPhotoUris.get(iIndex);
    }

    private OnCompleteListener getFinishUploadListener()
    {
        return new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if(task.isSuccessful())
                {
                    postError("");
                }
                else
                {
                    postError(task.getException().getMessage());
                }

                mIsCreating.postValue(false);
            }
        };
    }

    public void setItemCategory(int iCategoryNumber)
    {
        mItemCategory = iCategoryNumber;
    }

    public MutableLiveData<String> getLocationResult() {
        return mLocationResult;
    }

    public void getLocation(Context iContext){
        mIsGettingLocation = true;
        mGeocoder = new Geocoder(iContext);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(iContext);
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location lastLocation = locationResult.getLastLocation();

                mItemLatitude = lastLocation.getLatitude();
                mItemLongitude = lastLocation.getLongitude();

                try {
                    List<Address> addressList = mGeocoder.getFromLocation(mItemLatitude, mItemLongitude, 1);
                    Address bestAddress = addressList.get(0);
                    mItemLocation = bestAddress.getAdminArea();
                    mLocationResult.postValue(mItemLocation);
                    mIsGettingLocation = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(iContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null);
        else
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, null);
    }


}
