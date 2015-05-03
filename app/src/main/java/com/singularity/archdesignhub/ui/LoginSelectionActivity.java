package com.singularity.archdesignhub.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;

import java.util.Arrays;


/**
 * Created by Frederick on 5/1/2015.
 */
public class LoginSelectionActivity extends Activity {
    public static final String TAG = LoginSelectionActivity.class.getSimpleName();
    private View facebookLogin, gplusLogin, emailLogin;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
    private static final int FB_LOGIN = 1;
    private static final int GPLUS_LOGIN = 2;
    private static final int EMAIL_LOGIN = 3;
    private static int loginType = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);
        facebookLogin = findViewById(R.id.facebookLogin);
        gplusLogin = findViewById(R.id.gplusLogin);
        emailLogin = findViewById(R.id.emailLogin);

        setUpFbLogin();
        mGoogleApiClient = buildGoogleApiClient();

        gplusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performGpluslogin();
            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFbLogin();
            }
        });

        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performEmailLogin();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (loginType) {
            case FB_LOGIN:
                break;
            case GPLUS_LOGIN:
                if (requestCode == 0) {
                    if (resultCode != RESULT_OK) {
                        mSignInClicked = false;
                    }
                    mIntentInProgress = false;

                    if (!mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.reconnect();
                    }
                }
                break;
            case EMAIL_LOGIN:
                break;
        }

    }

    private void performEmailLogin() {
        loginType = EMAIL_LOGIN;
        startActivityForResult(new Intent(this, LoginActivity.class), EMAIL_LOGIN);
    }

    private GoogleApiClient buildGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        mSignInClicked = false;
                        Toast.makeText(getBaseContext(), "User is connected!", Toast.LENGTH_LONG).show();
                        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                        String currentPersonName = person != null
                                ? person.getDisplayName()
                                : "unkniwn";
                        Log.i(TAG, "cur name - " + currentPersonName);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                });
        builder.addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE);
        return builder.build();
    }

    private void performGpluslogin() {
        loginType = GPLUS_LOGIN;
        mSignInClicked = true;
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            mGoogleApiClient.connect();
        }
    }


    private void performFbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));

    }

    private void setUpFbLogin() {
        loginType = FB_LOGIN;
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        Log.v("facebook - profile", profile2.getFirstName());
                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public static class LoginObject {
        public int type;
        public User user;
    }


}
