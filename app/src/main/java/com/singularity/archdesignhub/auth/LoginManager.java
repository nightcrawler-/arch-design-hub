package com.singularity.archdesignhub.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.singularity.archdesignhub.backbone.BackboneBase;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;
import com.singularity.archdesignhub.data.CassiniContract;

/**
 * Created by Frederick on 5/3/2015.
 */
public class LoginManager extends BackboneBase {
    private static final String TAG = LoginManager.class.getSimpleName();
    Context context;
    SharedPreferences prefs;

    public LoginManager(Context context) {
        super();
        this.context = context;
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

    public void validateSocialLogin() {

    }

    public void validateEmailLogin() {

    }

    private SharedPreferences getPrefs() {
        if (prefs == null)
            prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return prefs;
    }


}
