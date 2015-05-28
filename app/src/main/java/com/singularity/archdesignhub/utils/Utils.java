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
    private static SharedPreferences prefs = null;


    public static synchronized void applyFonts(View view, Typeface font) {
        if (view instanceof ViewGroup) {

            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++)
                applyFonts(vg.getChildAt(i), font);

        } else if (view instanceof TextView)

            ((TextView) view).setTypeface(font);

    }

    public static boolean isLoginDone(Context c) {
        return getPrefs(c).getBoolean("login", false);
    }

    public static void setLoginDone(Context c, boolean fetched) {
        SharedPreferences.Editor editor = getPrefs(c).edit();
        editor.putBoolean("login", fetched);
        editor.commit();

    }

    public static boolean isDataFetched(Context c) {
        return getPrefs(c).getBoolean("fetched", false);
    }

    public static void setDataFetched(Context c, boolean fetched) {
        SharedPreferences.Editor editor = getPrefs(c).edit();
        editor.putBoolean("fetched", fetched);
        editor.commit();

    }

    public static boolean isNotify(Context c) {
        return getPrefs(c).getBoolean("notify", false);
    }

    public static void setNotify(Context c, boolean notify) {
        SharedPreferences.Editor editor = getPrefs(c).edit();
        editor.putBoolean("notify", notify);
        editor.commit();
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


}
