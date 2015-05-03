package com.singularity.archdesignhub.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CassiniProvider extends ContentProvider {
    private CassiniDbHelper dbHelper;

    public CassiniProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = -1;
        if (uri.equals(CassiniContract.PropertyEntry.CONTENT_URI))
            result = dbHelper.getWritableDatabase().delete(CassiniContract.PropertyEntry.TABLE_NAME, selection, selectionArgs);
        else if (uri.equals(CassiniContract.AgentEntry.CONTENT_URI))
            result = dbHelper.getWritableDatabase().delete(CassiniContract.AgentEntry.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = null;
        if (uri.equals(CassiniContract.PropertyEntry.CONTENT_URI))
            result = CassiniContract.PropertyEntry.buildPropertyUri(dbHelper.getWritableDatabase().insertWithOnConflict(CassiniContract.PropertyEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE));

        else if (uri.equals(CassiniContract.AgentEntry.CONTENT_URI))
            result = CassiniContract.AgentEntry.buildAgentUri(dbHelper.getWritableDatabase().insertWithOnConflict(CassiniContract.AgentEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE));
        getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CassiniDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (uri.equals(CassiniContract.PropertyEntry.CONTENT_URI))
            return dbHelper.getReadableDatabase().query(CassiniContract.PropertyEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        else if (uri.equals(CassiniContract.AgentEntry.CONTENT_URI))
            return dbHelper.getReadableDatabase().query(CassiniContract.AgentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int result = -1;
        if (uri.equals(CassiniContract.PropertyEntry.CONTENT_URI))
            result = dbHelper.getWritableDatabase().update(CassiniContract.PropertyEntry.TABLE_NAME, values, selection, selectionArgs);
        else if (uri.equals(CassiniContract.AgentEntry.CONTENT_URI))
            result = dbHelper.getWritableDatabase().update(CassiniContract.AgentEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();


        db.beginTransaction();
        int returnCount = 0;
        long _id = -1;

        try {
            for (ContentValues value : values) {
                if (uri.equals(CassiniContract.PropertyEntry.CONTENT_URI))
                    _id = db.insert(CassiniContract.PropertyEntry.TABLE_NAME, null, value);
                else if (uri.equals(CassiniContract.AgentEntry.CONTENT_URI))
                    _id = db.insert(CassiniContract.AgentEntry.TABLE_NAME, null, value);

                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

    }
}
