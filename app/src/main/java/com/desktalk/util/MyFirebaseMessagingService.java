package com.desktalk.util;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Pavan.Chunchula on 22-03-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Toast.makeText( getApplicationContext(),String.valueOf(remoteMessage.getData()),Toast.LENGTH_LONG).show();
    }


}
