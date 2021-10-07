package com.hod.finalapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hod.finalapp.MainActivity;
import com.hod.finalapp.R;
import com.hod.finalapp.model.database_objects.Item;
import com.hod.finalapp.model.database_objects.User;
import com.hod.finalapp.model.repositories.ItemRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServerUploadService extends Service
{
    public static final String SERVICE_NAME = "ItemBroadcast";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();
        String channelID = null;
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            channelID = "1";
            CharSequence channelName = "notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, importance);
            mManager.createNotificationChannel(notificationChannel);
        }

        Notification.Builder builder=new Notification.Builder(getBaseContext(),channelID);
        String contentText = getApplicationContext().getString(R.string.uploading);
        String title = getApplicationContext().getString(R.string.app_name);
        builder.setSmallIcon(android.R.drawable.ic_menu_upload).setContentTitle(title).setContentText(contentText);
        // Intent intent=new Intent(getApplicationContext(), MainActivity.class); if u want to press and get somewhere
        //Notification notification = builder.build();
        //mManager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID,builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<Uri> uris = intent.getParcelableArrayListExtra("uris");
        Item item = intent.getParcelableExtra("item");

        ItemRepository.getInstance().uploadNewItem(item, uris, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                String error = "";

                if(!task.isSuccessful())
                {
                    error = task.getException().getMessage();
                }
                Intent nIntent = new Intent(SERVICE_NAME);
                nIntent.putExtra("error",error);

                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(nIntent);
                stopSelf();
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
