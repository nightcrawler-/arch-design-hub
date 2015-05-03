package com.singularity.archdesignhub.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.singularity.archdesignhub.Constants;
import com.singularity.archdesignhub.R;
import com.singularity.archdesignhub.backend.entities.userApi.model.User;


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
        user.setLoginType(Constants.LoginTypes.EMAIL.name());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
