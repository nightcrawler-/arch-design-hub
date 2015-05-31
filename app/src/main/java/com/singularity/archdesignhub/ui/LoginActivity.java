package com.singularity.archdesignhub.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.singularity.archdesignhub.Constants;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.auth.LoginManager;
import com.singularity.archdesignhub.backbone.Backbone;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity {
    private EditText nameEdit, emailEdit, pwdEdit, pwdVerEdit;
    private TextView register, reset;
    private Button action;
    private boolean modeLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameEdit = (EditText) findViewById(R.id.editText3);
        emailEdit = (EditText) findViewById(R.id.editText);
        pwdEdit = (EditText) findViewById(R.id.editText2);
        pwdVerEdit = (EditText) findViewById(R.id.editText4);
        register = (TextView) findViewById(R.id.textView19);
        reset = (TextView) findViewById(R.id.textView4);
        action = (Button) findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeLogin)
                    setupRegisterMode();
                else setupLoginMode();
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeLogin)
                    //as required
                    ;
                else
                    //hivo hivo tu
                    ;
            }
        });


    }

    private void setupRegisterMode() {
        nameEdit.setVisibility(View.VISIBLE);
        pwdVerEdit.setVisibility(View.VISIBLE);
        register.setText("Already Registered");
        reset.setVisibility(View.GONE);
        modeLogin = false;
    }

    private void setupLoginMode() {
        nameEdit.setVisibility(View.GONE);
        pwdVerEdit.setVisibility(View.GONE);
        register.setText("Register");
        reset.setVisibility(View.VISIBLE);
        modeLogin = true;
    }

    private void getLoginData() {
        String emailString = emailEdit.getText().toString().trim();
        if (!(emailString.length() > 3)) {
            emailEdit.setError("Required");
            return;
        }

        String pwdString = pwdEdit.getText().toString().trim();
        if (pwdString.length() == 0) {
            pwdEdit.setError("Required");
            return;
        }

        User user = new User();
        user.setEmail(emailString);
        user.setId(emailString);
        user.setPassword(pwdString);
        user.setLoginType(Constants.LoginTypes.EMAIL.name());


    }

    private void getRegData() {
        String nameString = nameEdit.getText().toString().trim();
        if (nameString.length() == 0) {
            nameEdit.setError("Required");
            return;
        }

        String emailString = emailEdit.getText().toString().trim();
        if (!(emailString.length() > 3)) {
            emailEdit.setError("Required");
            return;
        }

        String pwdString = pwdEdit.getText().toString().trim();
        if (pwdString.length() == 0) {
            pwdEdit.setError("Required");
            return;
        }

        String pwdVerString = pwdVerEdit.getText().toString().trim();
        if (pwdVerString.length() == 0) {
            pwdVerEdit.setError("Required");
            return;
        }

        if (!pwdString.equals(pwdVerString)) {
            pwdVerEdit.setError("They don't match");
            return;
        }
        User user = new User();
        user.setEmail(emailString);
        user.setName(nameString);
        user.setId(emailString);
        user.setPassword(pwdString);
        user.setLoginType(Constants.LoginTypes.EMAIL.name());


    }

    private class LoginTask extends AsyncTask<User, Object, User> {
        private boolean modeLogin;

        public LoginTask(boolean modeLogin) {
            this.modeLogin = modeLogin;
        }

        @Override
        protected User doInBackground(User... params) {
            if (modeLogin) {
                User user = null;
                try {
                    user = Backbone.getInstance().getUser(params[0]);
                    if (user == null || (user.getPassword() != params[0].getPassword())) {
                        //is not there, do something, two birds, one brick
                    }else{
                        //we good
                        LoginManager.getInstance(getBaseContext()).cacheUser(user);
                        return user;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                //do sign up
                //
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (modeLogin) {

            } else {
                //sign up stuff here
            }
        }
    }


}
