package com.singularity.archdesignhub.backbone;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.singularity.archdesignhub.data.SyncIntentService;
import com.singularity.archdesignhub.utils.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;


public class GcmIntentService extends IntentService {


    public GcmIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                showToast(extras.getString("message"));
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        try {
            if (Integer.parseInt(message) == 1) {
                Utils.setNotify(this, true);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        SyncIntentService.startActionFetch(this);


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
               // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


}
