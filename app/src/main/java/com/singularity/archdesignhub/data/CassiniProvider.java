package com.singularity.archdesignhub.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class CassiniProvider extends ContentProvider {
    private static final String TAG = CassiniProvider.class.getSimpleName();
    private static final int AGENTS = 201;
    private static final int PROPERTIES = 202;
    private static final int PROPERTY = 203;
    private static final int IMAGES_ALL = 204;
    private static final int IMAGES_OWNER = 205;
    private static final int EVENTS = 206;
    private static final int COMMENTS = 207;
    private static final int CONTACTS = 208;


    static SQLiteQueryBuilder listingsQueryBuilder, listingDetailsQueryBuilder, agentsQueryBuilder, eventsQueryBuilder, contactsQueryBuilder;

    static {
        listingsQueryBuilder = new SQLiteQueryBuilder();
        listingsQueryBuilder.setTables(CassiniContract.PropertyEntry.TABLE_NAME + " LEFT JOIN " + CassiniContract.ImageEntry.TABLE_NAME + " ON " +
                CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_ID + "=" + CassiniContract.ImageEntry.C_OWNER_ID);
    }

    static {
        listingDetailsQueryBuilder = new SQLiteQueryBuilder();
        listingDetailsQueryBuilder.setTables(CassiniContract.AgentEntry.TABLE_NAME + " , " + CassiniContract.PropertyEntry.TABLE_NAME + " LEFT JOIN " + CassiniContract.ImageEntry.TABLE_NAME + " ON " +
                CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_ID + "=" + CassiniContract.ImageEntry.C_OWNER_ID);
    }

    static {
        agentsQueryBuilder = new SQLiteQueryBuilder();
        agentsQueryBuilder.setTables(CassiniContract.AgentEntry.TABLE_NAME + " LEFT JOIN " + CassiniContract.ImageEntry.TABLE_NAME + " ON " +
                CassiniContract.AgentEntry.TABLE_NAME + "." + CassiniContract.AgentEntry.C_ID + "=" + CassiniContract.ImageEntry.C_OWNER_ID);
    }

    static {
        eventsQueryBuilder = new SQLiteQueryBuilder();
        eventsQueryBuilder.setTables(CassiniContract.EventEntry.TABLE_NAME + " LEFT JOIN " + CassiniContract.ImageEntry.TABLE_NAME + " ON " +
                CassiniContract.EventEntry.TABLE_NAME + "." + CassiniContract.EventEntry.C_ID + "=" + CassiniContract.ImageEntry.TABLE_NAME + "." + CassiniContract.ImageEntry.C_OWNER_ID);
    }

    static {
        contactsQueryBuilder = new SQLiteQueryBuilder();
        contactsQueryBuilder.setTables(CassiniContract.ContactEntry.TABLE_NAME + " LEFT JOIN " + CassiniContract.ImageEntry.TABLE_NAME + " ON " +
                CassiniContract.ContactEntry.TABLE_NAME + "." + CassiniContract.ContactEntry.C_ID + "=" + CassiniContract.ImageEntry.C_OWNER_ID);
    }

    private CassiniDbHelper dbHelper;

    public CassiniProvider() {
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_AGENT, AGENTS);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_PROPERTY, PROPERTIES);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_EVENT, EVENTS);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_IMAGE, IMAGES_ALL);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_COMMENT, COMMENTS);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_CONTACT, CONTACTS);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_PROPERTY + "/#", PROPERTY);
        sUriMatcher.addURI(CassiniContract.CONTENT_AUTHORITY, CassiniContract.PATH_IMAGE + "/#", IMAGES_OWNER);
        return sUriMatcher;
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
        int match = buildUriMatcher().match(uri);
        Log.i(TAG, "matched - " + match);

        Cursor retCursor;

        switch (match) {
            case AGENTS:
                retCursor = agentsQueryBuilder.query(dbHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case PROPERTIES:
                retCursor = listingsQueryBuilder.query(dbHelper.getReadableDatabase(), projection, selection,
                        selectionArgs, CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_ID, null, sortOrder);
                break;
            case PROPERTY:
                retCursor = listingDetailsQueryBuilder.query(dbHelper.getReadableDatabase(), projection,
                        CassiniContract.PropertyEntry.TABLE_NAME + "." + CassiniContract.PropertyEntry.C_ID + "=" + ContentUris.parseId(uri),
                        selectionArgs, CassiniContract.ImageEntry.C_URL, null, sortOrder);
                break;
            case IMAGES_OWNER:
                retCursor = dbHelper.getReadableDatabase().query(CassiniContract.ImageEntry.TABLE_NAME, projection,
                        CassiniContract.ImageEntry.C_OWNER_ID + "=" + ContentUris.parseId(uri), selectionArgs, null, null, sortOrder);
                break;
            case EVENTS:
                retCursor = eventsQueryBuilder.query(dbHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, CassiniContract.EventEntry.TABLE_NAME + "." + CassiniContract.EventEntry.C_ID, null, sortOrder);
                break;
            case COMMENTS:
                retCursor = dbHelper.getReadableDatabase().query(CassiniContract.CommentEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CONTACTS:
                retCursor = contactsQueryBuilder.query(dbHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, CassiniContract.ContactEntry.TABLE_NAME + "." + CassiniContract.ContactEntry.C_ID, null, sortOrder);
                break;
            default:
                retCursor = null;
        }

        //Registers the content observer to recive any updates
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = buildUriMatcher().match(uri);
        int result = -1;

        switch (match) {
            case PROPERTIES:
                result = dbHelper.getWritableDatabase().update(CassiniContract.PropertyEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case AGENTS:
                result = dbHelper.getWritableDatabase().update(CassiniContract.AgentEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            default:
                result = -1;
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int match = buildUriMatcher().match(uri);
        Log.i(TAG, "matched - " + match);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        long _id = -1;
        String table = null;
        switch (match) {
            case AGENTS:
                table = CassiniContract.AgentEntry.TABLE_NAME;
                break;
            case PROPERTIES:
                table = CassiniContract.PropertyEntry.TABLE_NAME;
                break;
            case IMAGES_ALL:
                table = CassiniContract.ImageEntry.TABLE_NAME;
                break;
            case EVENTS:
                table = CassiniContract.EventEntry.TABLE_NAME;
                break;
            case COMMENTS:
                table = CassiniContract.CommentEntry.TABLE_NAME;
                break;
            case CONTACTS:
                table = CassiniContract.ContactEntry.TABLE_NAME;
                break;
        }

        if (table != null) {
            try {
                for (ContentValues value : values) {
                    _id = db.insertWithOnConflict(table, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                    if (_id != -1)
                        returnCount++;
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnCount;

    }
}
