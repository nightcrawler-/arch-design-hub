package com.singularity.archdesignhub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Frederick on 9/22/2014.
 */
public class Utils {

    public static final String TAG = Utils.class.getSimpleName();
    public static final String REGISTERED = "registered";
    public static final String REGISTRATION_ID = "registration_id";
    public static final String GCM_REG = "gcm_reg";
    public static final String LAST_NOTIFICATION_ITEM = "last_notification_item";
    public static final int INTERSTITIAL_DELAY_DAYS = 1;
    public static final int NOTIFICATION_DELAY_DAYS = 1;

    private static boolean activityActive = false;

    private static SharedPreferences prefs = null;


    public static synchronized void applyFonts(View view, Typeface font) {
        if (view instanceof ViewGroup) {

            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                applyFonts(vg.getChildAt(i), font);

        } else if (view instanceof TextView)

            ((TextView) view).setTypeface(font);

    }



    public static synchronized void setGcmRegistered(Context c,
                                                     String registration) {
        SharedPreferences settings = c.getSharedPreferences(GCM_REG,
                0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(REGISTERED, true);
        editor.putString(REGISTRATION_ID, registration);
        editor.commit();
    }

    public static synchronized boolean isGcmRegistered(Context c) {
        return c.getSharedPreferences(GCM_REG, 0).getBoolean(
                REGISTERED, false);
    }

    public static synchronized String getGcmRegId(Context c) {
        return c.getSharedPreferences(GCM_REG, 0).getString(
                REGISTRATION_ID, null);
    }

//    public static synchronized void registerGCM(Context context) {
//        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS && !isGcmRegistered(context))
//            GCMIntentService.register(context);
//        else
//            setGcmRegistered(context, "Play Services Missing");
//
//    }

    public static synchronized boolean is(Context c, Keys what) {
        return getPrefs(c).getBoolean(what.name(), false);
    }

    public static synchronized void set(Context c, Keys what, boolean value) {
        SharedPreferences.Editor editor = getPrefs(c).edit();
        editor.putBoolean(what.name(), value);
        editor.commit();
    }

    public static synchronized long getTime(Context c, Keys what) {
        return getPrefs(c).getLong(what.name(), 0L);
    }

    public static synchronized void setTime(Context c, Keys what, long value) {
        SharedPreferences.Editor editor = getPrefs(c).edit();
        editor.putLong(what.name(), value);
        editor.commit();
    }

    private static synchronized SharedPreferences getPrefs(Context c) {
        if (prefs == null)
            prefs = c.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return prefs;

    }

    public static boolean networkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

//    public static void setNetworkReceiverState(Context context, boolean enable) {
//        ComponentName compName = new ComponentName(context,
//                NetworkReceiver.class);
//
//        context.getPackageManager().setComponentEnabledSetting(
//                compName,
//                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
//                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//        Log.i(TAG, "NetworkReceiver state changed - " + enable);
//
//    }

    public static boolean backgroundSyncIsEnabled(Context context) {
        SharedPreferences sPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String freq = sPrefs.getString("sync_frequency", "180");
        if (Integer.parseInt(freq) > 0)
            return true;
        return false;
    }

    public static long getSyncInterval(Context context) {
        SharedPreferences sPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String freq = sPrefs.getString("sync_frequency", "360");
        long minutes = Long.parseLong(freq);
        return minutes * 60 * 1000;
    }

    public static boolean notificationsEnabled(Context context) {
        SharedPreferences sPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sPrefs.getBoolean("notifications_new_message", true);
    }

    public static boolean interstitialRipe(Context context) {
        if (getTime(context, Keys.INTERSTITIAL_SHOW) <= (System.currentTimeMillis()
                - (INTERSTITIAL_DELAY_DAYS * 24 * 60 * 60 * 1000)))
            return true;
        return false;
    }

    public static boolean isNotificationRipe(Context context) {
        if (getTime(context, Keys.NOTIFICATION_SHOW) <= (System.currentTimeMillis()
                - (NOTIFICATION_DELAY_DAYS * 24 * 60 * 60 * 1000)))
            return true;
        return false;
    }

    public static void setLastNotificationItem(Context c, String guid) {
        SharedPreferences.Editor editor = getPrefs(c).edit();
        editor.putString(LAST_NOTIFICATION_ITEM, guid);
        editor.commit();
    }

    public static synchronized boolean isNotificationValid(Context c, String guid) {
        String old = getPrefs(c).getString(guid, "");
        return old.equals(guid) ? false : true;

    }


    public enum Keys {CACHE_AVAILABLE, UPLOADED_STATS, INTERSTITIAL_SHOW, SWIPE_HINT, RUN, NOTIFICATION_SHOW}

}
