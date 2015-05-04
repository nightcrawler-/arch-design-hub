package com.singularity.archdesignhub.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Frederick on 4/23/2015.
 */
public class CassiniContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.singularity.archdesignhub";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_PROPERTY = "property";
    public static final String PATH_AGENT = "agent";
    public static final String PATH_USER = "user";


    /* Inner class that defines the table contents of the property table */
    public static final class PropertyEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROPERTY).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_PROPERTY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_PROPERTY;

        // Table name
        public static final String TABLE_NAME = "property";

        //Column names
        public static final String C_ID = "_ID";
        public static final String C_NAME = "name";
        public static final String C_LOCATION = "location";
        public static final String C_TYPE = "type";
        public static final String C_AGENT_ID = "agent_id";
        public static final String C_INTENT = "intent";
        public static final String C_DESCRIPTION = "description";
        public static final String C_WEBSITE = "website";
        public static final String C_MEDIA = "media";
        public static final String C_TEL = "tel";
        public static final String C_EXTRA = "extra";
        public static final String C_EMAIL = "email";
        public static final String C_TIME = "time";
        public static final String C_LATT = "latt";
        public static final String C_LONG = "longi";
        public static final String C_VOLUME = "volume";
        public static final String C_AREA = "area";
        public static final String C_BEDROOMS = "bedrooms";
        public static final String C_BATHROOMS = "bathrooms";
        public static final String C_VALUE = "value";


        public static Uri buildPropertyUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the agent table */
    public static final class AgentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AGENT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_AGENT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_AGENT;

        // Table name
        public static final String TABLE_NAME = "agent";

        //Column names
        public static final String C_ID = "_ID";
        public static final String C_NAME = "name";
        public static final String C_LOCATION = "location";
        public static final String C_DESCRIPTION = "description";
        public static final String C_WEBSITE = "website";
        public static final String C_MEDIA = "media";
        public static final String C_TEL = "tel";
        public static final String C_ADDRESS = "address";
        public static final String C_EMAIL = "email";
        public static final String C_TIME = "time";
        public static final String C_FB = "fb";
        public static final String C_TWITTER = "twitter";
        public static final String C_GOOGLE_PLUS = "gplus";


        public static Uri buildAgentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the user table */
    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        // Table name
        public static final String TABLE_NAME = "user";

        //Column names
        public static final String C_ID = "_ID";
        public static final String C_NAME = "name";
        public static final String C_PIC = "pic";
        public static final String C_TYPE = "type";
        public static final String C_EMAIL = "email";



        public static Uri buildAgentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
