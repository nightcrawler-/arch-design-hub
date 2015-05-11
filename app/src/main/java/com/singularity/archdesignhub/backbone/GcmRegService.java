package com.singularity.archdesignhub.backbone;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.singularity.archdesignhub.backend.registration.Registration;
import com.singularity.archdesignhub.utils.Utils;

import java.io.IOException;


public class GcmRegService extends IntentService {
    private static final String TAG = GcmRegService.class.getSimpleName();
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;

    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "460866140621";


    public GcmRegService() {
        super("GCMRegService");
    }

    public static void register(Context context) {
        context.startService(new Intent(context, GcmRegService.class));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            registerGcm();
        }
    }

    private String registerGcm() {
        if (regService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            Log.i(TAG, "rootUrl - " + builder.getRootUrl());
            regService = builder.build();
        }

        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(this);
            }
            String regId = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regId;

            //persist reg status locally
            Utils.setGcmRegistered(this, regId);
            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            regService.register(regId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
        return msg;
    }


}
