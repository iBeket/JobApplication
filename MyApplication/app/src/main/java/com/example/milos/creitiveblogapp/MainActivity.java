package com.example.milos.creitiveblogapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private SessionClass session = new SessionClass();
    public static final String TAG = "LoginActivity";
    public static JSONObject credentialsJSONobject;
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


        if (!isNetworkAvailable()) {
            Toast.makeText(getBaseContext(), "There is no internet connection", Toast.LENGTH_LONG).show();
            logButton.setEnabled(false);
        }
        Context context = getApplicationContext();
        String token = session.getToken(getApplicationContext());
        if (!token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, BlogListActivity.class);
            startActivity(intent);
            finish();
        } else {
            logButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        }
    }

    //checking if the validate method is true, receiving information from user
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
        }

        logButton.setEnabled(false);
        Context context = getApplicationContext();
        credentialsJSONobject = new JSONObject();
        try {
            credentialsJSONobject.put("email", email.getText().toString());
            credentialsJSONobject.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray dataObject = new AuthenticationClass(context, "login", "").execute().get();
            setAuthorize(dataObject);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    //check if there is internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    /*
       checks of we obtain token and if we did it will call the next activity
     */
    public void setAuthorize(JSONArray objArray) {
        Log.d(TAG, "setAuthorize function!");
        Log.d(TAG, objArray.toString());
        String token = null;
        String responseCode = null;
        String responseMessage = null;
        try {
            responseCode = objArray.getJSONObject(0).getString("ResponseCode");
            responseMessage = objArray.getJSONObject(0).getString("ResponseMessage");
            token = objArray.getJSONObject(1).getString("token");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Context context = getApplicationContext();
        if (!token.isEmpty()) {
            session.setToken(token, context);
            Intent intent = new Intent(MainActivity.this, BlogListActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Display message to user
            if (responseCode == "403") {
                Toast.makeText(getBaseContext(), "You entered wrong username and password", Toast.LENGTH_LONG).show();
            }
        }
        Log.d(TAG, "Token from session: " + token);
    }
}
