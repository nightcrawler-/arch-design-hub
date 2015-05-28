package com.singularity.archdesignhub.data;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.singularity.archdesignhub.backbone.Backbone;
import com.singularity.archdesignhub.ui.NewMessageNotification;
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

            ContentValues[] listings = Backbone.getInstance().getPropertyListings();
            int num = 0;
            if (listings != null)
                num = mContentResolver.bulkInsert(CassiniContract.PropertyEntry.CONTENT_URI, listings);
            Log.i(TAG, "inserted listings - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }
        try {
            ContentValues[] agents = Backbone.getInstance().getAgents();
            int num = 0;
            if (agents != null)
                num = mContentResolver.bulkInsert(CassiniContract.AgentEntry.CONTENT_URI, agents);
            Log.i(TAG, "inserted agents - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }

        try {
            ContentValues[] images = Backbone.getInstance().getImages();
            int num = 0;
            if (images != null)
                num = mContentResolver.bulkInsert(CassiniContract.ImageEntry.CONTENT_URI, images);
            Log.i(TAG, "inserted images - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }

        try {
            ContentValues[] events
                    = Backbone.getInstance().getEvents();
            int num = 0;
            if (events != null)
                num = mContentResolver.bulkInsert(CassiniContract.EventEntry.CONTENT_URI, events);
            Log.i(TAG, "inserted events - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;
        }

        try {
            ContentValues[] comments = Backbone.getInstance().getComments();
            int num = 0;
            if (comments != null)
                num = mContentResolver.bulkInsert(CassiniContract.CommentEntry.CONTENT_URI, comments);
            Log.i(TAG, "inserted comments - " + num);

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;

        }

        try {
            ContentValues[] contacts = Backbone.getInstance().getContacts();
            int num = 0;
            if (contacts != null)
                num = mContentResolver.bulkInsert(CassiniContract.ContactEntry.CONTENT_URI, contacts);
            Log.i(TAG, "inserted comments - " + num);
        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;

        }

        try {
            ContentValues[] messages = Backbone.getInstance().getMessages();
            int num = 0;
            if (messages != null)
                num = mContentResolver.bulkInsert(CassiniContract.MessageEntry.CONTENT_URI, messages);
            Log.i(TAG, "inserted messages - " + num);
            if (Utils.isNotify(this)) {
                NewMessageNotification.notify(this, "New message", 0);
                Utils.setNotify(this, false);
            }

        } catch (IOException e) {
            e.printStackTrace();
            exceptionCount++;

        }


        //get rid of later
        Utils.setDataFetched(getBaseContext(), (exceptionCount == 0));


    }


}
