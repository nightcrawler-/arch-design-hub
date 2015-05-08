package com.singularity.archdesignhub.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.singularity.archdesignhub.backbone.Backbone;

import java.io.IOException;

/**
 * Created by Frederick on 5/6/2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();


    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            mContentResolver.bulkInsert(CassiniContract.PropertyEntry.CONTENT_URI, Backbone.getInstance().getPropertyListings());
        } catch (IOException e) {
            syncResult.hasError();
            syncResult.stats.numIoExceptions++;
            e.printStackTrace();
        }
        try {
            mContentResolver.bulkInsert(CassiniContract.AgentEntry.CONTENT_URI, Backbone.getInstance().getAgents());
        } catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            syncResult.hasError();

            e.printStackTrace();
        }
    }
}
