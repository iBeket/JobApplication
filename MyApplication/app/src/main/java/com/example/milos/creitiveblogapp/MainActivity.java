package com.example.milos.creitiveblogapp;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    Button logButton;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logButton = (Button) findViewById(R.id.button);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
        }
    }

    public void onLoginSuccess() {
        logButton.setEnabled(true);
        finish();

    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        logButton.setEnabled(true);
    }
    //check email format and password length
    public boolean validate() {
        boolean valid = true;

        String emailTest = email.getText().toString();
        String passwordTest = password.getText().toString();

        if ((emailTest.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailTest).matches())) {
            email.setError("please enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        if (passwordTest.isEmpty() || password.length() < 6) {
            password.setError("password must be at least 6 characters long");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}
