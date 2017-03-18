package com.example.milos.creitiveblogapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.milos.creitiveblogapp.MainActivity.credentialsJSONobject;

/*
this call represent the main logic of the app, here we check url connection
as well as the request method we are getting depending on if we have a token or not.
In addition, we are reading information that that we are getting from the server which
we are using to create the content of the app
 */
public class AuthenticationClass extends AsyncTask<String, Void, JSONArray> {
    SessionClass session = new SessionClass();

    String TAG = "Auth LOG class: ";

    private Context context;
    private String processType;
    private String pageNumber;

    public AuthenticationClass(Context context, String processType, String pageNumber) {
        this.context = context;
        this.processType = processType;
        this.pageNumber = pageNumber;
    }

    @Override
    protected JSONArray doInBackground(String... params) {

        String userCredentials = null;
        if (this.processType == "login") {
            userCredentials = credentialsJSONobject.toString();
            Log.d(TAG, userCredentials);
        }
        JSONArray sessionData = new JSONArray();

        try {

            //Make a connection
            String urlString = null;
            switch (this.processType) {
                case "login":
                    urlString = "http://blogsdemo.creitiveapps.com:80/login";
                    break;
                case "blogList":
                    urlString = "http://blogsdemo.creitiveapps.com:80/blogs";
                    break;
                case "blogPage":
                    urlString = "http://blogsdemo.creitiveapps.com:80/blogs/" + this.pageNumber;
                    break;
                default:
                    urlString = null;
                    break;
            }
            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (this.processType == "login") {
                urlConnection.setDoOutput(true);
            } else {
                urlConnection.setDoOutput(false);
            }
            urlConnection.setDoInput(true);
            String reqMethod;
            //check if we are connecting for the first time
            if (this.processType == "login") {
                reqMethod = "POST";
            } else {
                reqMethod = "GET";
            }
            urlConnection.setRequestMethod(reqMethod);
            urlConnection.setRequestProperty("X-Client-Platform", "Android");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            if (this.processType != "login") {
                urlConnection.setRequestProperty("X-Authorize", session.getToken(this.context));
            }
            urlConnection.setChunkedStreamingMode(0);//connection established

            //Write information
            //Sending user credentials
            if (this.processType == "login") {
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                if (this.processType == "login") {
                    writer.write(userCredentials);
                }
                writer.close();
                outputStream.close();
            }

            //Read information
            InputStream serverResponse = urlConnection.getInputStream();
            JSONObject responseInfo = new JSONObject();
            try {
                responseInfo.put("ResponseCode", urlConnection.getResponseCode());
                responseInfo.put("ResponseMessage", urlConnection.getResponseMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sessionData.put(responseInfo);

            InputStreamReader response = new InputStreamReader(serverResponse);

            //reading token
            BufferedReader bufferData = null;
            StringBuilder responseData = new StringBuilder();
            String line;
            try {

                bufferData = new BufferedReader(response);
                while ((line = bufferData.readLine()) != null) {
                    responseData.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferData != null) {
                    try {
                        bufferData.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                if (this.processType == "login" || this.processType == "blogPage") {
                    sessionData.put(new JSONObject(responseData.toString()));
                } else {
                    sessionData.put(new JSONArray(responseData.toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionData;
    }

    @Override
    protected void onPostExecute(JSONArray sessionData) {
        super.onPostExecute(sessionData);
        // Dismiss the progress dialog
    }
}
