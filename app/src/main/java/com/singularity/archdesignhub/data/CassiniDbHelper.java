package com.singularity.archdesignhub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Frederick on 4/23/2015.
 */
public class CassiniDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "archdesign2.db";

    // Create a table to hold properties.
    final String SQL_CREATE_PROPERTIES_TABLE = "CREATE TABLE " + CassiniContract.PropertyEntry.TABLE_NAME + " (" +
            CassiniContract.PropertyEntry.C_ID + " INTEGER PRIMARY KEY," +
            CassiniContract.PropertyEntry.C_NAME + " TEXT UNIQUE, " +
            CassiniContract.PropertyEntry.C_DESCRIPTION + " TEXT, " +
            CassiniContract.PropertyEntry.C_LOCATION + " TEXT, " +
            CassiniContract.PropertyEntry.C_TYPE + " TEXT, " +
            CassiniContract.PropertyEntry.C_AGENT_ID + " REAL, " +
            CassiniContract.PropertyEntry.C_INTENT + " TEXT, " +
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
            CassiniContract.PropertyEntry.C_TIME + " REAL);";

    // Create a table to hold agents.
    final String SQL_CREATE_AGENTS_TABLE = "CREATE TABLE " + CassiniContract.AgentEntry.TABLE_NAME + " (" +
            CassiniContract.AgentEntry.C_ID + " INTEGER PRIMARY KEY, " +
            CassiniContract.AgentEntry.C_NAME + " TEXT UNIQUE, " +
            CassiniContract.AgentEntry.C_DESCRIPTION + " TEXT, " +
            CassiniContract.AgentEntry.C_LOCATION + " TEXT, " +
            CassiniContract.AgentEntry.C_ADDRESS + " TEXT, " +
            CassiniContract.AgentEntry.C_GOOGLE_PLUS + " TEXT, " +
            CassiniContract.AgentEntry.C_TWITTER + " TEXT, " +
            CassiniContract.AgentEntry.C_FB + " TEXT, " +
            CassiniContract.AgentEntry.C_WEBSITE + " TEXT, " +
            CassiniContract.AgentEntry.C_MEDIA + " TEXT, " +
            CassiniContract.AgentEntry.C_TEL + " TEXT, " +
            CassiniContract.AgentEntry.C_EMAIL + " TEXT, " +
            CassiniContract.AgentEntry.C_TIME + " REAL);";

    // Create a table to hold images.
    final String SQL_CREATE_IMAGES_TABLE = "CREATE TABLE " + CassiniContract.ImageEntry.TABLE_NAME + " (" +
            CassiniContract.ImageEntry.C_ID + " TEXT PRIMARY KEY," +
            CassiniContract.ImageEntry.C_NAME + " TEXT, " +
            CassiniContract.ImageEntry.C_URL + " TEXT, " +
            CassiniContract.ImageEntry.C_OWNER_ID + " INTEGER);";

    // Create a table to hold comments.
    final String SQL_CREATE_COMMENTS_TABLE = "CREATE TABLE " + CassiniContract.CommentEntry.TABLE_NAME + " (" +
            CassiniContract.CommentEntry.C_ID + " INTEGER PRIMARY KEY," +
            CassiniContract.CommentEntry.C_COMMENT + " TEXT, " +
            CassiniContract.CommentEntry.C_OWNER_NAME + " TEXT, " +
            CassiniContract.CommentEntry.C_OWNER_PIC + " TEXT, " +
            CassiniContract.CommentEntry.C_RESPONSE + " TEXT, " +
            CassiniContract.CommentEntry.C_RESPONSE_OWNER + " TEXT, " +
            CassiniContract.CommentEntry.C_RESPONSE_TIME + " INTEGER, " +
            CassiniContract.CommentEntry.C_TIME + " INTEGER, " +
            CassiniContract.CommentEntry.C_OWNER_ID + " INTEGER);";


    //create event table
    final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + CassiniContract.EventEntry.TABLE_NAME + " (" +
            CassiniContract.EventEntry.C_ID + " INTEGER PRIMARY KEY," +
            CassiniContract.EventEntry.C_TITLE + " TEXT, " +
            CassiniContract.EventEntry.C_DESCRIPTION + " TEXT, " +
            CassiniContract.EventEntry.C_LOCATION + " TEXT, " +
            CassiniContract.EventEntry.C_TIME + " INTEGER, " +
            CassiniContract.EventEntry.C_LIKES + " INTEGER, " +
            CassiniContract.EventEntry.C_LATT + " INTEGER, " +
            CassiniContract.EventEntry.C_LONG + " INTEGER, " +
            CassiniContract.EventEntry.C_OWNER_ID + " INTEGER);";

    //create contacts table
    final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE " + CassiniContract.ContactEntry.TABLE_NAME + " (" +
            CassiniContract.ContactEntry.C_ID + " INTEGER PRIMARY KEY," +
            CassiniContract.ContactEntry.C_TITLE + " TEXT, " +
            CassiniContract.ContactEntry.C_PHONE + " TEXT, " +
            CassiniContract.ContactEntry.C_EMAIL + " TEXT, " +
            CassiniContract.ContactEntry.C_ADDRESS + " INTEGER, " +
            CassiniContract.ContactEntry.C_WEBSITE + " INTEGER, " +
            CassiniContract.ContactEntry.C_LATT + " INTEGER, " +
            CassiniContract.ContactEntry.C_LONG + " INTEGER);";

    //create messages table
    final String SQL_CREATE_MESSAGES_TABLE = "CREATE TABLE " + CassiniContract.MessageEntry.TABLE_NAME + " (" +
            CassiniContract.MessageEntry.C_ID + " INTEGER PRIMARY KEY," +
            CassiniContract.MessageEntry.C_EXTRA + " TEXT, " +
            CassiniContract.MessageEntry.C_MESSAGE + " TEXT, " +
            CassiniContract.MessageEntry.C_TIME + " INTEGER, " +
            CassiniContract.MessageEntry.C_VIEWS + " INTEGER);";


    public CassiniDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(SQL_CREATE_AGENTS_TABLE);
        db.execSQL(SQL_CREATE_PROPERTIES_TABLE);
        db.execSQL(SQL_CREATE_IMAGES_TABLE);
        db.execSQL(SQL_CREATE_COMMENTS_TABLE);
        db.execSQL(SQL_CREATE_EVENTS_TABLE);
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
        db.execSQL(SQL_CREATE_MESSAGES_TABLE);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
//        db.execSQL("DROP TABLE IF EXISTS " + CassiniContract.AgentEntry.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CassiniContract.PropertyEntry.TABLE_NAME);
//        onCreate(db);

        if (newVersion == 2) ;
        //db.execSQL(SQL_CREATE_IMAGES_TABLE);


    }

}



