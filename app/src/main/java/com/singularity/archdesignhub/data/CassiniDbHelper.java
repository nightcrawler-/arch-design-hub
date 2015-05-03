package com.singularity.archdesignhub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Frederick on 4/23/2015.
 */
public class CassiniDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "cassini.db";

    public CassiniDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold properties.
        final String SQL_CREATE_PROPERTY_TABLE = "CREATE TABLE " + CassiniContract.PropertyEntry.TABLE_NAME + " (" +
                CassiniContract.PropertyEntry.C_ID + " INTEGER PRIMARY KEY," +
                CassiniContract.PropertyEntry.C_NAME + " TEXT UNIQUE NOT NULL, " +
                CassiniContract.PropertyEntry.C_DESCRIPTION + " TEXT, " +
                CassiniContract.PropertyEntry.C_LOCATION + " TEXT NOT NULL, " +
                CassiniContract.PropertyEntry.C_TYPE + " TEXT NOT NULL, " +
                CassiniContract.PropertyEntry.C_AGENT_ID + " REAL NOT NULL, " +
                CassiniContract.PropertyEntry.C_INTENT + " TEXT NOT NULL, " +
                CassiniContract.PropertyEntry.C_WEBSITE + " TEXT, " +
                CassiniContract.PropertyEntry.C_MEDIA + " TEXT, " +
                CassiniContract.PropertyEntry.C_TEL + " TEXT, " +
                CassiniContract.PropertyEntry.C_EXTRA + " TEXT, " +
                CassiniContract.PropertyEntry.C_EMAIL + " TEXT, " +
                CassiniContract.PropertyEntry.C_LATT + " REAL, " +
                CassiniContract.PropertyEntry.C_LONG + " REAL, " +
                CassiniContract.PropertyEntry.C_AREA + " REAL, " +
                CassiniContract.PropertyEntry.C_VOLUME + " REAL, " +
                CassiniContract.PropertyEntry.C_BEDROOMS + " REAL, " +
                CassiniContract.PropertyEntry.C_BATHROOMS + " REAL, " +
                CassiniContract.PropertyEntry.C_VALUE + " REAL, " +
                CassiniContract.PropertyEntry.C_TIME + " REAL NOT NULL);";

        // Create a table to hold agents.
        final String SQL_CREATE_AGENT_TABLE = "CREATE TABLE " + CassiniContract.PropertyEntry.TABLE_NAME + " (" +
                CassiniContract.AgentEntry.C_ID + " INTEGER PRIMARY KEY," +
                CassiniContract.AgentEntry.C_NAME + " TEXT UNIQUE NOT NULL, " +
                CassiniContract.AgentEntry.C_DESCRIPTION + " TEXT, " +
                CassiniContract.AgentEntry.C_LOCATION + " TEXT NOT NULL, " +
                CassiniContract.AgentEntry.C_ADDRESS + " TEXT, " +
                CassiniContract.AgentEntry.C_GOOGLE_PLUS + " TEXT, " +
                CassiniContract.AgentEntry.C_TWITTER + " TEXT, " +
                CassiniContract.AgentEntry.C_FB + " TEXT, " +
                CassiniContract.AgentEntry.C_WEBSITE + " TEXT, " +
                CassiniContract.AgentEntry.C_MEDIA + " TEXT, " +
                CassiniContract.AgentEntry.C_TEL + " TEXT, " +
                CassiniContract.AgentEntry.C_EMAIL + " TEXT, " +
                CassiniContract.AgentEntry.C_TIME + " REAL NOT NULL);";


        db.execSQL(SQL_CREATE_AGENT_TABLE);
        db.execSQL(SQL_CREATE_PROPERTY_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + CassiniContract.AgentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CassiniContract.PropertyEntry.TABLE_NAME);
        onCreate(db);
    }

}



