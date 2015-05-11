package com.singularity.archdesignhub.data;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.singularity.archdesignhub.backbone.Backbone;
import com.singularity.archdesignhub.utils.Utils;

import java.io.IOException;


public class SyncIntentService extends IntentService {
    private static final String TAG = SyncIntentService.class.getSimpleName();
    private static final String ACTION_FETCH_NEW_ITEMS = "com.singularity.archdesignhub.data.action.DOWNLOAD";
    /**
     * Content resolver, for performing database operations.
     */
    private ContentResolver mContentResolver;

    public static void startActionFetch(Context context) {
        Intent intent = new Intent(context, SyncIntentService.class);
        intent.setAction(ACTION_FETCH_NEW_ITEMS);
        context.startService(intent);
    }


    public SyncIntentService() {
        super("SyncIntentService");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContentResolver = getContentResolver();

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_NEW_ITEMS.equals(action)) {
                handleActionFetch();
            }
        }
    }

    private void handleActionFetch() {
        int exceptionCount = 0;
        try {
            int num = mContentResolver.bulkInsert(CassiniContract.PropertyEntry.CONTENT_URI, Backbone.getInstance().getPropertyListings());
            Log.i(TAG, "inserted - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }
        try {
            int num = mContentResolver.bulkInsert(CassiniContract.AgentEntry.CONTENT_URI, Backbone.getInstance().getAgents());
            Log.i(TAG, "inserted - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }

        try {
            int num = mContentResolver.bulkInsert(CassiniContract.ImageEntry.CONTENT_URI, Backbone.getInstance().getImages());
            Log.i(TAG, "inserted - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }

        Utils.setDataFetched(getBaseContext(), (exceptionCount == 0));


    }


}
