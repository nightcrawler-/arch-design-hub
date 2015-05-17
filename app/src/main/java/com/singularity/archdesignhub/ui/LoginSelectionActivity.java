package com.singularity.archdesignhub.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.singularity.archdesignhub.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;


/**
 * Created by Frederick on 5/1/2015.
 */
public class LoginSelectionActivity extends Activity {
    public static final String TAG = LoginSelectionActivity.class.getSimpleName();
    private View facebookLogin, gplusLogin, emailLogin, noLogin;
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
        noLogin = findViewById(R.id.textView20);

        setUpFbLogin();
        mGoogleApiClient = buildGoogleApiClient();
        noLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDone();
            }
        });

        gplusLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getBaseContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();

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


    public void loginDone() {
        Utils.setLoginDone(this, true);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

    }

    private void setUpFbLogin() {
        loginType = FB_LOGIN;
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            private GraphRequest request;
            private User user = new User();


            @Override
            public void onSuccess(LoginResult loginResult) {
                //one of two, hackity hack for getting both email and profile pic url - both of which apparently are accessed in only one of these two ways
                request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    handleRepsponse(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //the next async for getting profile pic only proceeds if email is non-empty, so a value has to be set
                                    user.setEmail("unavailable");
                                }
                            }

                            private void handleRepsponse(GraphResponse response) throws JSONException {
                                JSONObject obj = response.getJSONObject();
                                user.setEmail(obj.getString("email"));
                                //ensures we call the logintask if the other user fields have been populated, this call is returned async
                                if (user.getId() != null)
                                    new LoginTask(com.singularity.archdesignhub.auth.LoginManager.getInstance(getBaseContext())).execute(user);


                            }
                        });
                request.executeAsync();


                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        mProfileTracker.stopTracking();
                        user.setId(profile2.getId());
                        user.setName(profile2.getName());
                        user.setImage(profile2.getProfilePictureUri(100, 100).toString());
                        user.setExtra(profile2.getLinkUri().toString());
                        user.setLoginType(String.valueOf(FB_LOGIN));
                        //ensures we call the logintask if the other user fields(email) have been populated, this call is returned async
                        if (user.getEmail() != null)
                            new LoginTask(com.singularity.archdesignhub.auth.LoginManager.getInstance(getBaseContext())).execute(user);
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


    public class LoginTask extends AsyncTask<User, Object, User> {
        com.singularity.archdesignhub.auth.LoginManager loginManager;


        public LoginTask(com.singularity.archdesignhub.auth.LoginManager loginManager) {
            this.loginManager = loginManager;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(User... params) {
            User user = null;
            if (Integer.parseInt(params[0].getLoginType()) == FB_LOGIN || Integer.parseInt(params[0].getLoginType()) == GPLUS_LOGIN) {
                loginManager.cacheUser(params[0]);
                try {
                    user = loginManager.validateSocialLogin(params[0]);
                    loginManager.setSocialAuthed(true);
                    loginManager.setUserSignedIn(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    user = loginManager.validateEmailLogin(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            loginDone();
        }
    }


}
