package com.singularity.archdesignhub.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.singularity.archdesignhub.backbone.Backbone;
import com.singularity.archdesignhub.backbone.BackboneBase;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;
import com.singularity.archdesignhub.data.CassiniContract;

import java.io.IOException;

/**
 * Created by Frederick on 5/3/2015.
 */
public class LoginManager extends BackboneBase {
    private static final String TAG = LoginManager.class.getSimpleName();
    private static final String PREF_USER_SIGNED_IN = "user_signed_in";
    private static final String PREF_SOCIAL_AUTHED = "social_authed";
    private static final String PREF_GCM_REG_ID = "gcm_reg_id";


    Context context;
    SharedPreferences prefs;
    public static LoginManager me;

    private LoginManager(Context context) {
        super();
        this.context = context;
    }

    public static LoginManager getInstance(Context c) {
        if (me == null)
            me = new LoginManager(c);
        return me;

    }

    public boolean userSignedIn() {
        return getPrefs().getBoolean(PREF_USER_SIGNED_IN, false);
    }

    public boolean socialAuthed() {
        return getPrefs().getBoolean(PREF_SOCIAL_AUTHED, false);
    }


    public void setUserSignedIn(boolean signedIn) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean(PREF_GCM_REG_ID, signedIn);
        editor.commit();

    }

    public void setSocialAuthed(boolean socialAuthed) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean(PREF_SOCIAL_AUTHED, socialAuthed);
        editor.commit();

    }

    public void cacheUser(User user) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(CassiniContract.UserEntry.C_NAME, user.getName());
        editor.putString(CassiniContract.UserEntry.C_PIC, user.getImage());
        editor.putString(CassiniContract.UserEntry.C_EMAIL, user.getEmail());
        editor.putString(CassiniContract.UserEntry.C_TYPE, user.getLoginType());
        editor.commit();

    }

    public User getCachedUser() {
        User user = new User();
        user.setLoginType(getPrefs().getString(CassiniContract.UserEntry.C_TYPE, null));
        user.setName(getPrefs().getString(CassiniContract.UserEntry.C_NAME, null));
        user.setEmail(getPrefs().getString(CassiniContract.UserEntry.C_EMAIL, null));
        user.setImage(getPrefs().getString(CassiniContract.UserEntry.C_PIC, null));
        return user;
    }

    public User validateSocialLogin(User user) throws IOException {
        return Backbone.getInstance().insert(user);

    }

    @Deprecated
    public User validateEmailLogin(User user) throws IOException {
        User savedUser = Backbone.getInstance().getUser(user);
        if (savedUser == null)
            savedUser = Backbone.getInstance().insert(user);
        return savedUser;

    }

    private SharedPreferences getPrefs() {
        if (prefs == null)
            prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return prefs;
    }


}
